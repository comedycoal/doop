package com.tranhulovu.doop.todocardsystem;

public class ToDoCard
{
    public enum Status
    {
        NOT_STARTED,
        DONE,
        URGENT,
        OVERDUED
    }

    public static class DateTime
    {
        public static DateTime NOT_SPECIFIED;
    }
    
    //
    //---// FIELDS //--------------------------//
    //
    String mCardId;
    String mName;
    DateTime mStartDateTime;
    DateTime mEndDateTime;
    Status mStatus;
    int mPriority;
    String mGroup;
    String[] mTags;
    String mDescription;
    String mNote;
    Notification mNotification;

    ToDoCardBuilder mBuilder = null;

    public ToDoCard(String cardId,
                    String name,
                    DateTime startDateTime,
                    DateTime endDateTime,
                    int priority,
                    String group,
                    String[] tags,
                    String description,
                    String note,
                    Notification notification)
    {
        this.mCardId = cardId;
        this.mName = name;
        this.mStartDateTime = startDateTime;
        this.mEndDateTime = endDateTime;
        this.mPriority = priority;
        this.mGroup = group;
        this.mTags = tags;
        this.mDescription = description;
        this.mNote = note;
        this.mNotification = notification;
        impliesStatus();
    }

    public ToDoCardBuilder getBuilder()
    {
        if (mBuilder == null)
            mBuilder = ToDoCardBuilder.fromToDoCard(this);
        return mBuilder;
    }

    private void impliesStatus()
    {
        if (mEndDateTime == null)
            return;

        // TODO
    }

    //
    //---// GETTERS //--------------------------//
    //
    public String getCardId() { return mCardId; }

    public String getName() { return mName; }

    public DateTime getStartDateTime() { return mStartDateTime; }

    public DateTime getEndDateTime() { return mEndDateTime; }

    public int getPriority() { return mPriority; }

    public String getGroup() { return mGroup; }

    public String[] getTags() { return mTags; }

    public String getDescription() { return mDescription; }

    public String getNote() { return mNote; }

    public Notification getNotification() { return mNotification; }
}
