package com.tranhulovu.doop.todocardsystem;

import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.todocardsystem.events.Callback;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class NotificationManager
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private Map<String, Notification> mNotifications;

    private Collection<String> mDeletedNotifications;
    private Collection<String> mModifiedNotification;

    private CardManager mCardManager;
    private LocalAccessorFacade mLocalAccessor;

    // TODO: Android background notification service



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    public NotificationManager(CardManager cardManager,
                        LocalAccessorFacade facade)
    {
        mCardManager = cardManager;
        mLocalAccessor = facade;

        mDeletedNotifications = new HashSet<>();
        mModifiedNotification = new HashSet<>();

        onCreate();
    }

    private void onCreate()
    {
        // TODO: Get list of notification ids

        // TODO: Spawn a thread to fetch all notification data, maybe?
        //       This is such a demo app so fetch all notification sounds about right.

    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Methods

    /**
     * Performs a write to disk for all notifications modified
     * prior to when this method is called.
     */
    public void actualizeModification()
    {
        // TODO: Write notification data using LocalAccessor
    }

    /**
     * Performs a deletion from disk for all notifications marked as deleted
     * prior to when this method is called.
     */
    public void actualizeDeletion()
    {
        // TODO: Delete notification data using LocalAccessor
    }


    public void modifyNotification(String associatedCardId,
                                   Callback<Notification.Modifier> onCreationCallback)
    {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void getNotificationInfo(String id,
                                    Callback<Map<String, Object>> onFetchCallback)
    {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void requestDeleteNotification(String id,
                                          Callback<Map<String, Object>> onDeletionCallback)
    {
        // TODO
        throw new UnsupportedOperationException();
    }
}
