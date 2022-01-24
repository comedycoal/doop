package com.tranhulovu.doop.todocardsystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tranhulovu.doop.MainActivity;

public class NotificationRequestResponder extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String type = intent.getStringExtra("type");
        String title = intent.getStringExtra("title");
        String id = intent.getStringExtra("id");
        int intId = Integer.parseInt(id);

        if (type == "NOTIFICATION")
        {
            Intent appIntent = new Intent(context, NotificationRequestResponder.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.getInstance(), 0, intent, 0);


            // TODO: Make Notification here
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                    MainActivity.getInstance().getAppNotificationChannelId())
                    .setContentTitle("Task " + title)
                    .setContentText("It's time finish your task!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(context).notify(intId, builder.build());
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
        intent.putExtra("id", notification.getToDoCardId());

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
