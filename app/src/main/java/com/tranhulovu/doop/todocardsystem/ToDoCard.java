package com.tranhulovu.doop.todocardsystem;

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
        OVERDUED
    }

    /**
     * Enumeration for archival status of a {@code ToDoCard}
     */
    public enum ArchivalStatus
    {
        ARCHIVED,
        NOT_ARCHIVED
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
        private boolean mDone;
        private ZonedDateTime mStart;
        private ZonedDateTime mEnd;
        private ArchivalStatus mArchivalStatus;
        private Notification mNotification;

        private boolean mAnyChanged;
        private boolean mNotificationChanged;
        private boolean mCheckStatusChanged;
        private boolean mArchivalStatusChanged;

        private Modifier() { }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //---// Methods
        private void resetFlags()
        {
            mAnyChanged = false;
            mNotificationChanged = false;
            mCheckStatusChanged = false;
            mArchivalStatusChanged = false;
        }

        /**
         *  Commit all changes made using setters into the associated {@code ToDoCard}
         */
        public void build()
        {
            mAssociatedCard.mName = mName;
            mAssociatedCard.mDescription = mDescription;
            mAssociatedCard.mNote = mNote;
            mAssociatedCard.mGroup = mGroup;
            mAssociatedCard.mTags = mTags;
            mAssociatedCard.mPriority = mPriority;
            mAssociatedCard.mStart = mStart;
            mAssociatedCard.mEnd = mEnd;
            mAssociatedCard.mArchivalStatus = mArchivalStatus;
            mAssociatedCard.mNotification = mNotification;

            // Check modification flags here
            if (mAnyChanged)
                mAssociatedCard.mOnModified.invoke(mAssociatedCard.toMap());

            if (mNotificationChanged)
                mAssociatedCard.mOnNotificationChanged.invoke(mAssociatedCard.mNotification);

            if (mArchivalStatusChanged)
                mAssociatedCard.mOnArchivalStatusChanged.invoke(mAssociatedCard.mArchivalStatus);

            if (mCheckStatusChanged)
                mAssociatedCard.mOnCheckStatusChanged.invoke(mAssociatedCard.getCheckStatus());

            resetFlags();
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

                mAnyChanged = true;
                mCheckStatusChanged = true;
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
                mCheckStatusChanged = true;
            }

            return this;
        }

        /**
         * Mark the card as archived.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier archive()
        {
            if (mArchivalStatus == ArchivalStatus.NOT_ARCHIVED)
            {
                this.mArchivalStatus = ArchivalStatus.ARCHIVED;

                mAnyChanged = true;
                mArchivalStatusChanged = true;
            }
            return this;
        }

        /**
         * Remove the card's archival status, if marked.
         * Probably used to implement UI's "Undo action", if that happens.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier unarchive()
        {
            if (mArchivalStatus == ArchivalStatus.ARCHIVED)
            {
                this.mArchivalStatus = ArchivalStatus.NOT_ARCHIVED;

                mAnyChanged = true;
                mArchivalStatusChanged = true;
            }
            return this;
        }


        public Modifier setNotification(Notification notification)
        {
            this.mNotification = notification;

            mAnyChanged = true;
            mNotificationChanged = true;

            return this;
        }


        // Explanatory setters //
        public Modifier setName(String name)
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
         * Subscribe to the "OnCheckStatusChanged" event
         * (invoked when a card is marked as DONE, or unmarked).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnCheckStatusChanged(Subscriber<CheckStatus> subscriber)
        {
            mAssociatedCard.getCheckStatusEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "sOnArchivalStatusChanged" event
         * (invoked when a card is marked as ARCHIVED, or unmarked).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnArchivalStatusChanged(Subscriber<ArchivalStatus> subscriber)
        {
            mAssociatedCard.getArchivalStatusEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "OnModified" event (invoked when the card's properties change,
         * notification changes that are not ID change also count).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnModified(Subscriber<Map<String, Object>> subscriber)
        {
            mAssociatedCard.getModificationEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "On" event (invoked when the card's notification info changed,
         * NOT when the notification is registered in Android system).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnNotificationChanged(Subscriber<Notification> subscriber)
        {
            mAssociatedCard.getNotificationEvent().addSubscriber(subscriber);
            return this;
        }

        // These are event unsubscribing methods.
        /**
         * Unsubscribe to the "OnCheckStatusChanged" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unSubscribeOnCheckStatusChanged(Subscriber<CheckStatus> subscriber)
        {
            mAssociatedCard.getCheckStatusEvent().removeSubscriber(subscriber);
            return this;
        }

        /**
         * Unsubscribe to the "OnArchivalStatusChanged" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unSubscribeOnArchivalStatusChanged(Subscriber<ArchivalStatus> subscriber)
        {
            mAssociatedCard.getArchivalStatusEvent().removeSubscriber(subscriber);
            return this;
        }

        /**
         * Unsubscribe to the "On" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unSubscribeOnNotificationChanged(Subscriber<Notification> subscriber)
        {
            mAssociatedCard.getNotificationEvent().removeSubscriber(subscriber);
            return this;
        }

        /**
         * Unsubscribe to the "OnModified" event.
         * The operation is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier unSubscribeOnModified(Subscriber<Map<String, Object>> subscriber)
        {
            mAssociatedCard.getModificationEvent().removeSubscriber(subscriber);
            return this;
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    public static DateTimeFormatter DefaultFormatter
            = DateTimeFormatter.ofPattern("MM-dd-yyyy");

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
    private ArchivalStatus mArchivalStatus;

    // Notification
    private Notification mNotification;
    private Map<String, Object> mOfflineNotifInfo;

    // Events
    private Event<CheckStatus> mOnCheckStatusChanged;
    private Event<ArchivalStatus> mOnArchivalStatusChanged;
    private Event<Notification> mOnNotificationChanged;
    private Event<Map<String, Object>> mOnModified;



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
        mDone = false;
        mStart = ZonedDateTime.now();
        mEnd = ZonedDateTime.now().plusDays(7);
        mArchivalStatus = ArchivalStatus.NOT_ARCHIVED;
        mNotification = null;
        mOfflineNotifInfo = null;

        mOnArchivalStatusChanged = new Event<>();
        mOnCheckStatusChanged = new Event<>();
        mOnNotificationChanged = new Event<>();
        mOnModified = new Event<>();
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
            case "archivalStatus": return mArchivalStatus.name();
            case "notificationType":
                return (mNotification != null ? mNotification.getType().name() : "NONE");
            case "status": return getCheckStatus().name();
            default:
            {
                String[] f = field.split("_");
                if (f.length == 2 && f[0].equals("note"))
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
        map.put("archivalStatus", this.getArchivalStatus());
        map.put("notificationId", this.getNotificationId());
        return map;
    }

    /**
     * Create a modifier for this {@code ToDoCard}.
     * Multiple modifiers can co-exist, though highly advised against.
     * @return a new {@code ToDoCard.Modifier} object.
     */
    Modifier makeModifier()
    {
        Modifier b = new Modifier();
        b.mAssociatedCard = this;
        b.mName = mName;
        b.mDescription = mDescription;
        b.mNote = mNote;
        b.mGroup = mGroup;
        b.mTags = mTags;
        b.mDone = mDone;
        b.mPriority = mPriority;
        b.mStart = mStart;
        b.mEnd = mEnd;
        b.mArchivalStatus = mArchivalStatus;
        b.mNotification = mNotification;
        return b;
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

    public ArchivalStatus getArchivalStatus() { return mArchivalStatus; }

    public Notification getNotificationId() { return mNotification; }

    /**
     * Retrieve the cached notification info in the card, without querying the manager.
     * This is mostly used to quickly return the notification id.
     * @return a {@code NotificationInfo} object.
     */
    public Map<String, Object> getOfflineNotifInfo() { return mOfflineNotifInfo; }

    public CheckStatus getCheckStatus()
    {
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

    public Event<CheckStatus> getCheckStatusEvent() { return mOnCheckStatusChanged; }
    public Event<ArchivalStatus> getArchivalStatusEvent() { return mOnArchivalStatusChanged; }
    public Event<Map<String, Object>> getModificationEvent() { return mOnModified; }
    public Event<Notification> getNotificationEvent() { return mOnNotificationChanged; }
}
