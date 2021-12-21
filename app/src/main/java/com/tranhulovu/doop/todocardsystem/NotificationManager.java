package com.tranhulovu.doop.todocardsystem;

import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.todocardsystem.events.Callback;
import com.tranhulovu.doop.todocardsystem.events.Subscriber;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class NotificationManager implements Subscriber<Notification>
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private Map<String, Notification> mNotifications;

    private Collection<String> mDeletedNotifications;
    private Collection<String> mModifiedNotification;

    private NotificationRequestResponder mResponder;

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

        mResponder = new NotificationRequestResponder();

        onCreate();
    }

    private void onCreate()
    {
        // TODO: Get list of notification ids

        // TODO: Spawn a thread to fetch all notification data, maybe?
        //       This is such a demo app so fetch all notification sounds about right.


        // TODO: Do an exhaustive search in AlarmManager for unregistered notifications.

    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Methods

    @Override
    public boolean isPersistent()
    {
        return true;
    }

    @Override
    public void update(Notification data)
    {
        String id = data.getToDoCardId();
        
    }

    public void addNotificationToWatch(ToDoCard card)
    {

    }

    public void addNotificationToWatch(String cardId)
    {

    }

    public void removeFromWatch(ToDoCard card)
    {

    }

    public void removeFromWatch(Notification notification)
    {

    }

    public void removeFromWatch(String cardId)
    {

    }

}
