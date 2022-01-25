package com.tranhulovu.doop.todocardsystem;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.tranhulovu.doop.todocardsystem.events.Event;
import com.tranhulovu.doop.todocardsystem.events.Subscriber;
import com.tranhulovu.doop.todocardsystem.filter.StringFieldGetter;

import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Class encapsulating a ToDoCard
 * It has specific getters, but they are only used internally.
 * Any exposure of a ToDoCard instance's properties are unintentional and should be regarded as a
 * bug.
 */
public class ToDoCard implements StringFieldGetter
{
    /**
     * Enumeration for status of a {@code ToDoCard}
     */
    public enum CheckStatus
    {
        NOT_STARTED,
        DONE,
        NOT_DONE,
        URGENT,
        OVERDUED,
        ARCHIVED,
        DELETED
    }

    /**
     * "Builder" for a {@code ToDoCard}.
     * Allow for modifying an existing {@code ToDoCard} via setters,
     * rather than helping creating one.
     * Calling {@code build()} method commit all modifications.
     */
    public static class Modifier
    {
        ToDoCard mAssociatedCard;

        private String mName;
        private String mDescription;
        private String mNote;
        private String mGroup;
        private List<String> mTags;
        private int mPriority;

        private ZonedDateTime mStart;
        private ZonedDateTime mEnd;

        private boolean mDone;
        private boolean mArchived;
        private boolean mDeleted;

        private Notification mNotification;

        private boolean mAnyChanged;
        private boolean mNotificationChanged;
        private boolean mDoneChanged;
        private boolean mArchivalChanged;
        private boolean mDeletionChanged;

        private Modifier() { }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //---// Methods
        private void resetFlags()
        {
            mAnyChanged = false;
            mNotificationChanged = false;
            mDoneChanged = false;
            mArchivalChanged = false;
            mDeletionChanged = false;
        }

        /**
         *  Commit all changes made using setters into the associated {@code ToDoCard}
         */
        public void build()
        {
            ToDoCard old = new ToDoCard(mAssociatedCard);

            mAssociatedCard.mName = mName;
            mAssociatedCard.mDescription = mDescription;
            mAssociatedCard.mNote = mNote;
            mAssociatedCard.mGroup = mGroup;
            mAssociatedCard.mTags = mTags;
            mAssociatedCard.mPriority = mPriority;

            mAssociatedCard.mStart = mStart;
            mAssociatedCard.mEnd = mEnd;

            mAssociatedCard.mDone = mDone;
            mAssociatedCard.mDeleted = mDeleted;
            mAssociatedCard.mArchived = mArchived;
            mAssociatedCard.mBackup = old;

            mAssociatedCard.mNotification = mNotification;

            // Check modification flags here
            if (mAnyChanged)
                mAssociatedCard.mModifiedEvent.invoke(mAssociatedCard.toMap());

            if (mNotificationChanged)
                mAssociatedCard.mNotificationChangedEvent.invoke(mAssociatedCard.mNotification);

            if (mDoneChanged)
                mAssociatedCard.mDoneChangeEvent.invoke(mAssociatedCard.mDone);
            if (mArchivalChanged)
                mAssociatedCard.mArchivalChangeEvent.invoke(mAssociatedCard.mArchived);
            if (mDeletionChanged)
                mAssociatedCard.mDeletionChangeEvent.invoke(mAssociatedCard.mDeleted);

            resetFlags();
        }

        public void revertChanges()
        {
            mAssociatedCard.revertBackup();
            mAssociatedCard.useModifier(this);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //---// Setters

        // These needs explaining?
        /**
         * Set starting and ending timestamp of a card
         * @param start Starting timestamp of a card
         * @param end Ending timestamp of a card
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier setTimeRange(ZonedDateTime start, ZonedDateTime end)
        {
            this.mStart = start;
            this.mEnd = end;

            Notification.Builder b = new Notification.Builder(mNotification);

            mNotification = mNotification.getDeadlineType() == Notification.DeadlineType.START
                    ? b.setDeadline(start).build() : b.setDeadline(end).build();

            mAnyChanged = true;
            mNotificationChanged = true;

            return this;
        }

        /**
         * Mark the associated as Done
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier markDone()
        {
            if (!mDone)
            {
                mDone = true;

                mNotification = new Notification.Builder(mNotification)
                        .setType(Notification.Type.SILENT).build();

                mAnyChanged = true;
                mDoneChanged = true;
                mNotificationChanged = true;
            }

            return this;
        }

        /**
         * Remove the Done status, if marked.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier markNotDone()
        {
            if (mDone)
            {
                mDone = false;

                mAnyChanged = true;
                mDoneChanged = true;
            }

            return this;
        }

        /**
         * Mark the card as archived.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier archive()
        {
            if (!mArchived)
            {
                mArchived = true;

                mNotification = new Notification.Builder(mNotification)
                    .setType(Notification.Type.SILENT).build();

                mAnyChanged = true;
                mArchivalChanged = true;
                mNotificationChanged = true;
            }
            return this;
        }

        /**
         * Remove the card's archival status, if marked.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier unarchive()
        {
            if (mArchived)
            {
                mArchived = false;

                mAnyChanged = true;
                mArchivalChanged = true;
            }
            return this;
        }

        /**
         * Mark the card as deleted.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier delete()
        {
            if (!mDeleted)
            {
                mDeleted = true;

                mNotification = new Notification.Builder(mNotification)
                        .setType(Notification.Type.SILENT).build();

                mAnyChanged = true;
                mDeletionChanged = true;
            }
            return this;
        }

        /**
         * Remove the card's deletion status.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier undoDelete()
        {
            if (mDeleted)
            {
                mDeleted = false;

                mAnyChanged = true;
                mDeletionChanged = true;
            }
            return this;
        }


        public Modifier setNotification(@NonNull Notification notification)
                throws AssertionError
        {
            assert notification.getToDoCardId().equals(this.mAssociatedCard.getId());
            this.mNotification = notification;

            mAnyChanged = true;
            mNotificationChanged = true;

            return this;
        }


        // Explanatory setters //
        public Modifier setName(@NonNull String name)
        {
            this.mName = name;

            mAnyChanged = true;

            return this; }

        public Modifier setDescription(String description)
        {
            this.mDescription = description;

            mAnyChanged = true;

            return this;
        }

        public Modifier setNote(String note)
        {
            this.mNote = note;

            mAnyChanged = true;

            return this;
        }

        public Modifier setGroup(String group)
        {
            this.mGroup = group;

            mAnyChanged = true;

            return this;
        }

        public Modifier setTags(List<String> tags)
        {
            this.mTags = tags;

            mAnyChanged = true;

            return this;
        }

        public Modifier addTag(String tag)
        {
            this.mTags.add(tag);

            mAnyChanged = true;

            return this;
        }

        public Modifier removeTag(String tag)
        {
            boolean res = this.mTags.removeIf(x -> x.equals(tag));

            if (res)
                mAnyChanged = true;

            return this;
        }

        public Modifier setPriority(int priority)
        {
            this.mPriority = priority;

            mAnyChanged = true;

            return this;
        }


        // These are event subscribing methods.
        /**
         * Subscribe to the "DoneChange" event
         * (invoked when a card is marked as DONE, or unmarked).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeDoneChangeEvent(Subscriber<Boolean> subscriber)
        {
            mAssociatedCard.getDoneChangeEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "ArchivalChange" event
         * (invoked when a card is marked as ARCHIVED, or unmarked).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeArchivalChangeEvent(Subscriber<Boolean> subscriber)
        {
            mAssociatedCard.getArchivalChangeEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "DeletionChange" event
         * (invoked when a card is marked as DELETED, or unmarked).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeDeletionChangeEvent(Subscriber<Boolean> subscriber)
        {
            mAssociatedCard.getDeletionChangeEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "Modified" event (invoked when the card's properties change,
         * notification changes that are not ID change also count).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeModifiedEvent(Subscriber<Map<String, Object>> subscriber)
        {
            mAssociatedCard.getModifiedEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "NotificationChanged" event (invoked when the card's notification info changed,
         * NOT when the notification is registered in Android system).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeNotificationChangeEvent(Subscriber<Notification> subscriber)
        {
            mAssociatedCard.getNotificationChangeEvent().addSubscriber(subscriber);
            return this;
        }

        // These are event unsubscribing methods.
        /**
         * Unsubscribe to the "DoneChange" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unsubscribeDoneChangeEvent(Subscriber<Boolean> subscriber)
        {
            mAssociatedCard.getDoneChangeEvent().removeSubscriber(subscriber);
            return this;
        }

        /**
         * Unsubscribe to the "ArchivalChange" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unsubscribeArchivalChangeEvent(Subscriber<Boolean> subscriber)
        {
            mAssociatedCard.getArchivalChangeEvent().removeSubscriber(subscriber);
            return this;
        }

        /**
         * Unsubscribe to the "DeletionChange" event
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unsubscribeDeletionChangeEvent(Subscriber<Boolean> subscriber)
        {
            mAssociatedCard.getDeletionChangeEvent().removeSubscriber(subscriber);
            return this;
        }

        /**
         * Unsubscribe to the "NotificationChange" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unsubscribeNotificationChangeEvent(Subscriber<Notification> subscriber)
        {
            mAssociatedCard.getNotificationChangeEvent().removeSubscriber(subscriber);
            return this;
        }

        /**
         * Unsubscribe to the "Modified" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unSubscribeModifiedEvent(Subscriber<Map<String, Object>> subscriber)
        {
            mAssociatedCard.getModifiedEvent().removeSubscriber(subscriber);
            return this;
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    public static DateTimeFormatter DefaultFormatter
            = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public static long DefaultUrgentHours = 12;

    private String mId;         // Id for referential purposes

    // All explanatory fields
    private String mName;
    private String mDescription;
    private String mNote;
    private String mGroup;
    private List<String> mTags;
    private int mPriority;

    // DateTime
    private ZonedDateTime mStart;
    private ZonedDateTime mEnd;

    // Statuses
    private boolean mDone;
    private boolean mArchived;
    private boolean mDeleted;
    private ToDoCard mBackup;

    // Notification
    private Notification mNotification;

    // Events
    private Event<Boolean> mDoneChangeEvent;
    private Event<Boolean> mArchivalChangeEvent;
    private Event<Boolean> mDeletionChangeEvent;
    private Event<Notification> mNotificationChangedEvent;
    private Event<Map<String, Object>> mModifiedEvent;

    private Event<ToDoCard> mManagerToDoCardChangeEvent;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors

    /**
     * Construct a {@code ToDoCard} object
     * @param id Id for the Card, should be managed by a manager.
     */
    ToDoCard(String id)
    {
        mId = id;

        mName = "New Card";
        mDescription = "Description.";
        mNote = "";
        mGroup = "Default";
        mTags = new ArrayList<>();
        mPriority = 1;

        mStart = ZonedDateTime.now();
        mEnd = ZonedDateTime.now().plusDays(7);

        mDone = false;
        mArchived = false;
        mDeleted = false;
        mBackup = null;

        mNotification = new Notification.Builder()
            .setAssociatedCard(getId())
            .setDeadline(mEnd)
            .setName(mName)
            .setType(Notification.Type.SILENT)
            .build();

        mDoneChangeEvent = new Event<>();
        mArchivalChangeEvent = new Event<>();
        mDeletionChangeEvent = new Event<>();
        mNotificationChangedEvent = new Event<>();
        mModifiedEvent = new Event<>();
    }

    ToDoCard(ToDoCard other)
    {
        mId = other.mId;

        mName = other.mName;
        mDescription = other.mDescription;
        mNote = other.mNote;
        mGroup = other.mGroup;
        mTags = new ArrayList<>(other.mTags);
        mPriority = other.mPriority;

        mStart = other.mStart;
        mEnd = other.mEnd;

        mDone = other.mDone;
        mArchived = other.mArchived;
        mDeleted = other.mDeleted;
        mBackup = null;

        mNotification = other.mNotification;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Methods

    /**
     * Retrieve a {@code String} value of this {@code ToDoCard}'s properties.
     * @param field property to retrieve. Can be {@code "name"}, {@code "description"},
     * {@code "note"}, {@code "group"}, {@code "priority"}, {@code "start"}, {@code "end"},
     * {@code "start"}, {@code "archivalStatus"}, {@code "notificationType"}, {@code "status"},
     * or {@code "tag_<number of the tag>"}
     * @return the corresponding string value.
     *
     * @throws InvalidParameterException when {@code field} is not one of those listed above.
     * @throws NumberFormatException when {@code <number of the tag>} is not parsable to int.
     * @throws IndexOutOfBoundsException when {@code <number of the tag>} is out of bounds.
     */
    public String getValueOf(String field) throws InvalidParameterException,
                                                    NumberFormatException,
                                                    IndexOutOfBoundsException
    {
        switch (field)
        {
            case "name": return mName;
            case "description": return mDescription;
            case "note": return mNote;
            case "group": return mGroup;
            case "priority": return String.valueOf(mPriority);
            case "start": return mStart.format(DefaultFormatter);
            case "end": return mEnd.format(DefaultFormatter);
            case "notificationType":
                return (mNotification != null ? mNotification.getType().name() : "NONE");
            case "status": return getStatus().name();
            default:
            {
                String[] f = field.split("_");
                if (f.length == 2 && f[0].equals("tag"))
                {
                    int idx = Integer.parseInt(f[1]);
                    return mTags.get(idx);
                }
                else
                    throw new InvalidParameterException(
                            field + " does not correspond to any getter of ToDoCard");
            }
        }
    }

    /**
     * Helper function to create a String-value getter for a field.
     * It probably works, I don't know.
     * @param field property to retrieve. Can be {@code "name"}, {@code "description"},
     * {@code "note"}, {@code "group"}, {@code "priority"}, {@code "start"}, {@code "end"},
     * {@code "start"}, {@code "archivalStatus"}, {@code "notificationId"}, {@code "status"},
     * or {@code "tag_<number of the tag>"}
     * @return A getter function, in form of {@code Function<ToDoCard, String>}
     */
    public static Function<ToDoCard, String> getGetterOf(String field)
    {
        return toDoCard -> toDoCard.getValueOf(field);
    }

    /**
     * Retrieve a {@code ToDoCard}'s properties as a Mapping of {@code String} to {@code Object}.
     * Each entry needs casting to its appropriate type.
     */
    public Map<String, Object> toMap()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", this.getName());
        map.put("description", this.getDescription());
        map.put("note", this.getNote());
        map.put("group", this.getGroup());
        map.put("tags", this.getTags());
        map.put("priority", this.getPriority());
        map.put("start", this.getStart());
        map.put("end", this.getEnd());
        map.put("status", this.getStatus());
        map.put("notification", this.getNotification());
        return map;
    }

    /**
     * Create a ToDoCard from data gotten from a {@code Map},
     * Notification is not included, and should be set again.
     * @param map object of type {@code Map<String, Object>}
     * @return a ToDoCard object
     * @throws InvalidParameterException if {@code map} is invalid
     */
    public static ToDoCard fromMapWithoutNotif(Map<String, Object> map)
            throws InvalidParameterException
    {
        try
        {
            String id = (String)map.get("id");
            ToDoCard a = new ToDoCard(id);
            a.mName = (String)map.get("name");
            a.mDescription = (String)map.get("description");
            a.mNote = (String)map.get("note");
            a.mGroup = (String)map.get("group");
            a.mTags = (List<String>)map.get("tags");
            a.mPriority = (int)map.get("priority");
            a.mDone = (boolean)map.get("done");
            a.mArchived = (boolean)map.get("archived");
            a.mStart = ZonedDateTime.parse((String)map.get("start"), DefaultFormatter);
            a.mEnd = ZonedDateTime.parse((String)map.get("end"), DefaultFormatter);

            return a;
        }
        catch (Exception e)
        {
            throw new InvalidParameterException("Cannot infer all data to recreate ToDoCard from map");
        }
    }

    /**
     * Create a modifier for this {@code ToDoCard}.
     * Multiple modifiers can co-exist, though highly advised against.
     * @return a new {@code ToDoCard.Modifier} object.
     */
    Modifier makeModifier()
    {
        Modifier b = new Modifier();
        useModifier(b);
        return b;
    }

    public void revertBackup()
    {
        ToDoCard backup = mBackup;
        if (backup != null)
        {
            // Replace the card with backup
            mName = backup.mName;
            mDescription = backup.mDescription;
            mNote = backup.mNote;
            mGroup = backup.mGroup;
            mTags = backup.mTags;
            mPriority = backup.mPriority;

            mStart = backup.mStart;
            mEnd = backup.mEnd;

            boolean oldDone = mDone;
            boolean oldDeleted = mDeleted;
            boolean oldArchived = mArchived;
            Notification oldNotif = mNotification;

            mDone = backup.mDone;
            mDeleted = backup.mDeleted;
            mArchived = backup.mArchived;
            mBackup = null;

            mNotification = backup.mNotification;

            if (oldDone != mDone)
                mDoneChangeEvent.invoke(mDone);
            if (oldDeleted != mDeleted)
                mDoneChangeEvent.invoke(mDeleted);
            if (oldArchived != mArchived)
                mDoneChangeEvent.invoke(mArchived);

            if (!oldNotif.equals(mNotification))
                mNotificationChangedEvent.invoke(mNotification);

            mModifiedEvent.invoke(toMap());

        }
    }

    public void useModifier(Modifier modifier)
    {
        modifier.mAssociatedCard = this;

        modifier.mName = mName;
        modifier.mDescription = mDescription;
        modifier.mNote = mNote;
        modifier.mGroup = mGroup;
        modifier.mTags = mTags;
        modifier.mPriority = mPriority;

        modifier.mStart = mStart;
        modifier.mEnd = mEnd;

        modifier.mDone = mDone;
        modifier.mArchived = mArchived;
        modifier.mDeleted = mDeleted;

        modifier.mNotification = mNotification;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Getters

    // Self-explanatory //
    public String getId() { return mId; }

    public String getName() { return mName; }

    public String getDescription() { return mDescription; }

    public String getNote() { return mNote; }

    public String getGroup() { return mGroup; }

    /**
     * Return a list of Tags for the card.
     * The tag list is not modifiable.
     * @return An unmodifiable {@code List<String>}
     */
    public List<String> getTags() { return Collections.unmodifiableList(mTags);}

    public int getPriority() { return mPriority; }

    public ZonedDateTime getStart() { return mStart; }

    public ZonedDateTime getEnd() { return mEnd; }

    public Notification getNotification() { return mNotification; }

    public boolean isDone()
    {
        return mDone;
    }

    public boolean isArchived()
    {
        return mArchived;
    }

    public boolean isDeleted()
    {
        return mDeleted;
    }

    public CheckStatus getStatus()
    {
        if (mDeleted)
            return CheckStatus.DELETED;

        if (mArchived)
            return CheckStatus.ARCHIVED;

        if (mDone)
            return CheckStatus.DONE;

        ZonedDateTime now = ZonedDateTime.now();

        if (now.isBefore(mStart))
            return CheckStatus.NOT_STARTED;

        ZonedDateTime urgentTime = mEnd.minusHours(DefaultUrgentHours);

        if (now.isAfter(urgentTime) && now.isBefore(mEnd))
            return CheckStatus.URGENT;
        else if (now.isAfter(mEnd))
            return CheckStatus.OVERDUED;

        return CheckStatus.NOT_DONE;
    }


    public Event<Boolean> getDoneChangeEvent()
    {
        return mDoneChangeEvent;
    }

    public Event<Boolean> getArchivalChangeEvent()
    {
        return mArchivalChangeEvent;
    }

    public Event<Boolean> getDeletionChangeEvent()
    {
        return mDeletionChangeEvent;
    }

    public Event<Notification> getNotificationChangeEvent()
    {
        return mNotificationChangedEvent;
    }

    public Event<Map<String, Object>> getModifiedEvent()
    {
        return mModifiedEvent;
    }

    Event<ToDoCard> getManagerToDoCardChangeEvent()
    {
        return mManagerToDoCardChangeEvent;
    }
}
