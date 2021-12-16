package com.tranhulovu.doop.todocardsystem;

import com.tranhulovu.doop.todocardsystem.events.Event;
import com.tranhulovu.doop.todocardsystem.events.PersistentEvent;
import com.tranhulovu.doop.todocardsystem.events.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class encapsulating a ToDoCard
 * It has specific getters, but they are only used internally.
 * Any exposure of a ToDoCard instance's properties are unintentional and should be regarded as a
 * bug.
 */
public class ToDoCard
{
    /**
     * Enumeration for status of a {@code ToDoCard}
     */
    public enum CheckStatus
    {
        NOT_STARTED,
        DONE,
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
     * Facade for objects representing a Time of a Date.
     * WARNING: PLACEHOLDER
     */
    public static class DateTime
    {
        public static DateTime NOT_SPECIFIED;
    }

    /**
     * "Builder" for a {@code ToDoCard}.
     * Allow for modifying an existing {@code ToDoCard} via setters, rather than helping creating one.
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
        private DateTime mStart;
        private DateTime mEnd;
        private ArchivalStatus mArchivalStatus;
        private String mNotificationId;

        private Modifier() { }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //---// Methods

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
            mAssociatedCard.mNotificationId = mNotificationId;
            mAssociatedCard.impliesCheckStatus();
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
        public Modifier setTimeRange(DateTime start, DateTime end)
        {
            this.mStart = start;
            this.mEnd = end;
            return this;
        }

        /**
         * Mark the associated as Done
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier markDone() { mDone = true; return this; }

        /**
         * Remove the Done status, if marked.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier markNotDone() { mDone = false; return this; }

        /**
         * Mark the card as archived.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier archive()
        {
            this.mArchivalStatus = ArchivalStatus.ARCHIVED;
            // TODO: What happens when a card is archived?
            // TODO: Probably nothing, except that the UI needs to reload or something,
            // TODO: which I totally don't have to care about.
            return this;
        }

        /**
         * Remove the card's archival status, if marked.
         * Probably used to implement UI's "Undo action", if that happens.
         * @return The current {@code ToDoCard.Modifier}
         */
        public Modifier unarchive()
        {
            this.mArchivalStatus = ArchivalStatus.NOT_ARCHIVED;
            return this;
        }



        // This one is subject to change, also pretty complicated
        public Modifier setNotification(String id)
        {
            //TODO
            this.mNotificationId = mNotificationId;
            return this;
        }



        // Explanatory setters //
        public Modifier setName(String name) { this.mName = name; return this; }

        public Modifier setDescription(String description)
        {
            this.mDescription = description;
            return this;
        }

        public Modifier setNote(String note) { this.mNote = note; return this; }

        public Modifier setGroup(String group) { this.mGroup = group; return this; }

        public Modifier setTags(List<String> tags) { this.mTags = tags; return this; }

        public Modifier addTag(String tag) { this.mTags.add(tag); return this; }

        public Modifier removeTag(String tag)
        {
            this.mTags.removeIf(x -> x.equals(tag));
            return this;
        }

        public Modifier setPriority(int priority) { this.mPriority = priority; return this; }


        // These are event subscribing methods.
        /**
         * Subscribe to the "OnCheck" event (invoked when a card is marked as DONE).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnCheck(Subscriber<Void> subscriber)
        {
            mAssociatedCard.getOnCheckEvent().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "OnCheckStatusChanged" event
         * (invoked when a card is marked as DONE, or unmarked).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnCheckStatusChanged(Subscriber<CheckStatus> subscriber)
        {
            mAssociatedCard.getOnCheckStatusChanged().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "sOnArchivalStatusChanged" event
         * (invoked when a card is marked as ARCHIVED, or unmarked).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnArchivalStatusChanged( Subscriber<ArchivalStatus> subscriber)
        {
            mAssociatedCard.getOnArchivalStatusChanged().addSubscriber(subscriber);
            return this;
        }

        /**
         * Subscribe to the "OnModified" event (invoked when the card's properties change,
         * notification changes that are not ID change also count).
         * The subscription is done immediately, without having to call {@code build()}.
         * @param subscriber the subscriber.
         * @return the current {@code ToDoCard.Modifier}
         */
        public Modifier subscribeOnModified(Subscriber<Void> subscriber)
        {
            mAssociatedCard.getOnModifiedEvent().addSubscriber(subscriber);
            return this;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private String mId;         // Id for referential purposes

    // All explanatory fields
    private String mName;
    private String mDescription;
    private String mNote;
    private String mGroup;
    private List<String> mTags;
    private int mPriority;

    // DateTime
    private DateTime mStart;
    private DateTime mEnd;

    // Statuses
    private boolean mDone;
    private ArchivalStatus mArchivalStatus;

    // Notification
    private String mNotificationId;
    private NotificationInfo mOfflineNotifInfo;

    // Events
    private Event<Void> mOnCheck;
    private Event<CheckStatus> mOnCheckStatusChanged;
    private Event<ArchivalStatus> mOnArchivalStatusChanged;
    private Event<Void> mOnModified;


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
        mStart = DateTime.NOT_SPECIFIED;
        mEnd = DateTime.NOT_SPECIFIED;
        mArchivalStatus = ArchivalStatus.NOT_ARCHIVED;
        mNotificationId = null;
        mOfflineNotifInfo = null;

        mOnCheck = new PersistentEvent<>();
        mOnArchivalStatusChanged = new PersistentEvent<>();
        mOnCheckStatusChanged = new PersistentEvent<>();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Methods
    public void setEventTriggers()
    {

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
        b.mPriority = mPriority;
        b.mStart = mStart;
        b.mEnd = mEnd;
        b.mArchivalStatus = mArchivalStatus;
        b.mNotificationId = mNotificationId;
        return b;
    }

    /**
     * Derive Check status of the card based on its due and start time,
     * its Done and Archived status.
     * Usually called when card's info is changed or a time check happens.
     */
    private void impliesCheckStatus()
    {

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

    public DateTime getStart() { return mStart; }

    public DateTime getEnd() { return mEnd; }

    public ArchivalStatus getArchivalStatus() { return mArchivalStatus; }

    public String getNotificationId() { return mNotificationId; }

    /**
     * Retrieve the cached notification info in the card, without querying the manager.
     * This is mostly used to quickly return the notification id.
     * @return a {@code NotificationInfo} object.
     */
    public NotificationInfo getOfflineNotifInfo() { return mOfflineNotifInfo; }

    public CheckStatus getCheckStatus()
    {
        //TODO
        return CheckStatus.NOT_STARTED;
    }

    public Event<Void> getOnCheckEvent() {return mOnCheck; }
    public Event<CheckStatus> getOnCheckStatusChanged() {return mOnCheckStatusChanged; }
    public Event<ArchivalStatus> getOnArchivalStatusChanged() {return mOnArchivalStatusChanged; }
    public Event<Void> getOnModifiedEvent() {return mOnModified; }
}
