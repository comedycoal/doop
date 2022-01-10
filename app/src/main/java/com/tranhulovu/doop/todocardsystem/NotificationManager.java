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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationManager
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private NotificationRequestResponder mResponder;

    private CardManager mCardManager;
    private LocalAccessorFacade mLocalAccessor;

    private Subscriber<Notification> mNotificationChangeResponder;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    public NotificationManager(CardManager cardManager,
                        LocalAccessorFacade facade,
                        Context applicationContext)
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
                    mResponder.cancelNotification(applicationContext, data);
                }
                else
                    mResponder.setNotification(applicationContext, data);
            }
        };
    }

    private void onCreate()
    {

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Methods
    public void saveNotification(Notification notification)
    {
        //Save notification to disk
        mLocalAccessor.writeNotification(notification);
    }

    public void deleteNotification(@NonNull Notification notification)
    {
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
