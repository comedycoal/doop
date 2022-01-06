package com.tranhulovu.doop.todocardsystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationRequestResponder extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String type = intent.getStringExtra("type");
        intent.getStringExtra("title");

        if (type == "NOTIFICATION")
        {
            // TODO: Make Notification here
        }
        else
        {
            // TODO: Make Alarm here
        }
    }

    private PendingIntent makePendingIntent(Context context, Notification notification)
    {
        int id = (int) notification.getAlarmDeadline().toEpochSecond();
        Intent intent = new Intent(context, NotificationRequestResponder.class);

        intent.putExtra("title", notification.getName());
        intent.putExtra("type", notification.getType().name());

        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        return pi;
    }

    public void setNotification(Context context, Notification notification)
    {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = makePendingIntent(context, notification);
        am.setExact(AlarmManager.RTC_WAKEUP,
                notification.getExactNotificationTime().toInstant().toEpochMilli(),
                pi);
    }

    public void cancelNotification(Context context, Notification notification)
    {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = makePendingIntent(context, notification);
        am.cancel(pi);
    }
}
