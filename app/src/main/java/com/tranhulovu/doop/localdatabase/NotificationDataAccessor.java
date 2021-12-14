package com.tranhulovu.doop.localdatabase;

import android.net.Uri;

import com.tranhulovu.doop.todocardsystem.Notification;

import java.util.Map;

public class NotificationDataAccessor {
    public Uri mDataFolderPath;
    public Map<String, Uri> mNotificationMap;

    public NotificationDataAccessor(Uri dataPath) {
        this.mDataFolderPath = dataPath;
    }

    public void initialize() {

    }

    public void write(Notification notif) {

    }

    public void erase(String notifId) {

    }

    public void erase(Notification notif) {

    }

    public Notification read(String notifId) {
        return null;
    }
}
