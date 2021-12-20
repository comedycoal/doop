package com.tranhulovu.doop.localdatabase;

import com.tranhulovu.doop.todocardsystem.Notification;
import com.tranhulovu.doop.todocardsystem.ToDoCard;

import java.util.List;
import java.util.Map;

public class LocalAccessorFacade {
    private ToDoCardDataAccessor mCardAccessor;
    private NotificationDataAccessor mNotifAccessor;
    private SettingAccessor mSettingAccessor;

    public void writeCard(ToDoCard card) {
        this.mCardAccessor = new ToDoCardDataAccessor(null);
    }

    public void writeBulkCards(List<ToDoCard> cards) {

    }

    public ToDoCard readCard(String id) {

        return null;
    }

    public Map<String, ToDoCard> readBulkCards(List<String> ids) {
        return null;
    }

    public void writeNotification(Notification notif) {

    }

    public void writeBulkNotification(List<Notification> notifs) {

    }

    public Notification readNotification(String id) {
        return null;
    }

    public Map<String, ToDoCard> readBulkNotification(List<String> ids) {
        return null;
    }

    public void writeSettings(Map<String, Object> settingMap) {

    }

    public Map<String, Object> readSettings() {
        return null;
    }


}
