package com.tranhulovu.doop.todocardsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tranhulovu.doop.todocardsystem.events.Event;

import java.util.List;

public class ToDoCard
{
    public enum CheckStatus
    {
        NOT_STARTED,
        DONE,
        URGENT,
        OVERDUED
    }

    public enum ArchivalStatus
    {
        ARCHIVED,
        NOT_ARCHIVED
    }

    public static class DateTime
    {
        public static DateTime NOT_SPECIFIED;
    }

    public static class Builder
    {

    }


    //////////////////////////////////////////////////
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
    private ArchivalStatus mArchivalStatus;

    // Notification
    private String mNotificationId;
    private NotificationInfo mOfflineNotifInfo;

    // Events
    private Event<Void> mOnCheck;
    private Event<CheckStatus> mOnCheckStatusChanged;
    private Event<ArchivalStatus> mOnArchivalStatusChanged;

}
