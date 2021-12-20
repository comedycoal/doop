package com.tranhulovu.doop.todocardsystem;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;

public class Notification
{
    public static enum Type
    {
        SILENT,
        NOTIFICATION,
        ALARM
    }

    public static class Modifier
    {
        Notification mAssociatedNotification;
        private Type mType;
        private ToDoCard.DateTime mAlarmDeadline;
        private long mMinutesPrior;

        private Modifier() { }

        public void build()
        {
            mAssociatedNotification.mAlarmDeadline = mAlarmDeadline;
            mMinutesPrior = mMinutesPrior;
            mType = mType;

            // TODO: Re-register notification, or something
        }

        public Modifier setType(Type type)
        {
            this.mType = type;
            return this;
        }

        public Modifier setAlarmDeadline(ToDoCard.DateTime alarmDeadline)
        {
            this.mAlarmDeadline = alarmDeadline;
            return this;
        }

        public Modifier setMinutesPrior(long minutesPrior)
        {
            this.mMinutesPrior = minutesPrior;
            return this;
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private int mId;
    private String mToDoCardId;
    private String mCardName;
    private Type mType;
    private ToDoCard.DateTime mAlarmDeadline;
    private long mMinutesPrior;

    // Statuses
    private boolean mIsRegistered;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    Notification(String toDoCardId, String name)
    {
        mToDoCardId = toDoCardId;
        mCardName = name;
        mType = Type.NOTIFICATION;
        mMinutesPrior = 12 * 60;
        mAlarmDeadline = null;  // TODO: Set time at tomorrow, maybe.

        mIsRegistered = false;
    }

    Notification(String toDoCardId,
                 Type type,
                 ToDoCard.DateTime alarmDeadline,
                 long minutesPrior)
    {
        this.mToDoCardId = toDoCardId;
        this.mType = type;
        this.mAlarmDeadline = alarmDeadline;
        this.mMinutesPrior = minutesPrior;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Methods

    public void registerNotification(NotificationRequestResponder responder)
    {
        if (!mIsRegistered)
        {
            // TODO: Add to notification service.

            mIsRegistered = true;
        }
    }

    public void cancelNotification(NotificationRequestResponder responder)
    {
        if (mIsRegistered)
        {
            // TODO: Remove from notification service.


            mIsRegistered = false;
        }
    }

    public String getValueOf(String field) throws InvalidParameterException
    {
        switch (field)
        {
            case "toDoCardId": return mToDoCardId;
            case "type": return mType.name();
            case "deadlineTime": return mAlarmDeadline.toString();
            case "minutesPrior": return String.valueOf(mMinutesPrior);
            default:
            {
                throw new InvalidParameterException(
                        field + " does not correspond to any getter of ToDoCard");
            }
        }
    }

    public Modifier createModifier()
    {
        Modifier m = new Modifier();
        m.mAssociatedNotification = this;
        m.mType = mType;
        m.mAlarmDeadline = mAlarmDeadline;
        m.mMinutesPrior = mMinutesPrior;
        return m;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Getters
    public int getManagedId()
    {
        return mId;
    }

    public String getCardName()
    {
        return mCardName;
    }

    public String getToDoCardId()
    {
        return mToDoCardId;
    }

    public Type getType()
    {
        return mType;
    }

    public ToDoCard.DateTime getAlarmDeadline()
    {
        return mAlarmDeadline;
    }

    public long getMinutesPrior()
    {
        return mMinutesPrior;
    }

    public long getCorrectNotificationTimeInMillis()
    {
        // TODO;
        return 99;
    }
}
