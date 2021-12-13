package com.tranhulovu.doop.todocardsystem;

public class ToDoCardBuilder
{//---// FIELDS //-----------------------------------//
    //
    String mCardId;
    String mName = "New Card";
    ToDoCard.DateTime mStartDateTime;
    ToDoCard.DateTime mEndDateTime;
    int mPriority;
    String mGroup;
    String[] mTags;
    String mDescription;
    String mNote;
    Notification mNotification;

    public ToDoCardBuilder(String id)
    {
        mCardId = id;
    }

    static ToDoCardBuilder fromToDoCard(ToDoCard card)
    {
        ToDoCardBuilder b = new ToDoCardBuilder(card.mCardId);
        b.mName = card.mName;
        b.mStartDateTime = card.mStartDateTime;
        b.mEndDateTime = card.mEndDateTime;
        b.mPriority = card.mPriority;
        b.mGroup = card.mGroup;
        b.mDescription = card.mDescription;
        b.mTags = card.mTags;
        b.mNote = card.mNote;
        b.mNotification = card.mNotification;

        return b;
    }

    public ToDoCard build()
    {
        return new ToDoCard(
                mCardId,
                mName,
                mStartDateTime,
                mEndDateTime,
                mPriority,
                mGroup,
                mTags,
                mDescription,
                mNote,
                mNotification);
    }


    //---// FIELDS //-----------------------------------//
    // Setters

    public void setCardId(String cardId)
    {
        this.mCardId = cardId;
    }

    public void setName(String name)
    {
        this.mName = name;
    }

    public void setTimeRange(ToDoCard.DateTime start, ToDoCard.DateTime end)
    {
        this.mStartDateTime = start;
        this.mEndDateTime = end;
    }

    public void setPriority(int priority)
    {
        this.mPriority = priority;
    }

    public void setGroup(String group)
    {
        this.mGroup = group;
    }

    public void setTags(String... tags)
    {
        this.mTags = new String[tags.length];
        System.arraycopy(tags, 0, this.mTags, 0, tags.length);
    }

    public void setDescription(String description)
    {
        this.mDescription = description;
    }

    public void setNote(String note)
    {
        this.mNote = mNote;
    }

    // PLEASE CHANGE THE NOTIFICATION WHEN NOTIFICATION MANAGER IS IMPLEMENTED
    public void setNotification(Notification notification)
    {
        this.mNotification = notification;
    }
}
