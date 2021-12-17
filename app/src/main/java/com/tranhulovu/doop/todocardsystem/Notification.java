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
    private String mToDoCardId;
    private Type mType;
    private ToDoCard.DateTime mAlarmDeadline;
    private long mMinutesPrior;



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    Notification(String toDoCardId)
    {
        mToDoCardId = toDoCardId;
        mType = Type.NOTIFICATION;
        mMinutesPrior = 12 * 60;
        mAlarmDeadline = null;  // TODO: Set time at tomorrow, maybe.
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

    private void registerNotification()
    {
        // TODO: Add to notification service.
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
}
