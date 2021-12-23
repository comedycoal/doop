package com.tranhulovu.doop.todocardsystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.todocardsystem.events.Subscriber;

import java.util.Map;

public class NotificationManager
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private Map<String, Notification> mNotifications;

    private NotificationRequestResponder mResponder;

    private CardManager mCardManager;
    private LocalAccessorFacade mLocalAccessor;

    private Subscriber<Notification> mNotificationChangeResponder;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    public NotificationManager(CardManager cardManager,
                        LocalAccessorFacade facade)
    {
        mCardManager = cardManager;
        mLocalAccessor = facade;

        mResponder = new NotificationRequestResponder();

        mNotificationChangeResponder = new Subscriber<Notification>()
        {
            @Override
            public boolean isPersistent()
            {
                return true;
            }

            @Override
            public void update(Notification data)
            {
                // TODO: WARNING: CHANGE CONTEXT FROM NULL
                if (data.getType() == Notification.Type.SILENT)
                {
                    mResponder.cancelNotification(null, data);
                }
                else
                    mResponder.setNotification(null, data);
            }
        };
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
    public Notification getNotification(String cardId)
    {
        return mNotifications.get(cardId);
    }

    public void saveNotification(Notification notification)
    {
        mNotifications.put(notification.getToDoCardId(), notification);

        // TODO: Save notification to disk
    }

    public void deleteNotification(@NonNull Notification notification)
    {
        mNotifications.remove(notification.getToDoCardId());

        // TODO: Delete notification to disk
    }

    public void watch(@NonNull ToDoCard card)
    {
        card.getNotificationChangeEvent().addSubscriber(mNotificationChangeResponder);
    }

    public void removeWtach(@NonNull ToDoCard card)
    {
        card.getNotificationChangeEvent().removeSubscriber(mNotificationChangeResponder);
    }

}
