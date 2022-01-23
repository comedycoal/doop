package com.tranhulovu.doop.todocardsystem;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.todocardsystem.events.Subscriber;

public class NotificationManager
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private NotificationRequestResponder mResponder;

    private LocalAccessorFacade mLocalAccessor;

    private Subscriber<Notification> mNotificationChangeResponder;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    public NotificationManager(LocalAccessorFacade facade,
                        Context applicationContext)
    {
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

    public void removeWatch(@NonNull ToDoCard card)
    {
        card.getNotificationChangeEvent().removeSubscriber(mNotificationChangeResponder);
    }

}
