package com.tranhulovu.doop.todocardsystem;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * FOJO object for a Notifcation
 */
public class Notification
{
    public static enum Type
    {
        SILENT,
        NOTIFICATION,
        ALARM
    }

    public static class Builder
    {
        private String toDoCardId = null;
        private String name = "Name of Card";
        private Type type = Type.NOTIFICATION;
        private ZonedDateTime deadline = ZonedDateTime.now().plusDays(7);
        private long minutesPrior = 60 * 12;

        public Builder setAssociatedCard(String toDoCardId)
        {
            this.toDoCardId = toDoCardId;
            return this;
        }

        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder setType(Type type)
        {
            this.type = type;
            return this;
        }

        public Builder setDeadline(ZonedDateTime deadline)
        {
            this.deadline = deadline;
            return this;
        }

        public Builder setMinutesPrior(long minutesPrior)
        {
            this.minutesPrior = minutesPrior;
            return this;
        }

        public Notification create()
        {
            if (name == null) return null;
            return new Notification(toDoCardId, name, type, deadline, minutesPrior);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private String mToDoCardId;
    private String mName;                       // For quick access
    private Type mType;
    private ZonedDateTime mAlarmDeadline;
    private long mMinutesPrior;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    private Notification(String toDoCardId, String name, Type type,
                         ZonedDateTime deadline, long minutesPrior) { }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Methods
    public Map<String, Object> toMap()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", this.getName());
        map.put("type", this.getType());
        map.put("deadline", this.getAlarmDeadline());
        map.put("min_prior", this.getMinutesPrior());
        return map;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Getters
    public String getToDoCardId()
    {
        return mToDoCardId;
    }

    public int getIntId()
    {
        return Integer.parseInt(mToDoCardId);
    }

    public String getName()
    {
        return mName;
    }

    public Type getType()
    {
        return mType;
    }

    public ZonedDateTime getAlarmDeadline()
    {
        return mAlarmDeadline;
    }

    public long getMinutesPrior()
    {
        return mMinutesPrior;
    }

    public ZonedDateTime getExactNotificationTime()
    {
        return mAlarmDeadline.minusMinutes(mMinutesPrior);
    }
}
