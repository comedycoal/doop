package com.tranhulovu.doop.localdatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tranhulovu.doop.todocardsystem.Notification;
import com.tranhulovu.doop.todocardsystem.ToDoCard;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalAccessorFacade {
    private ToDoCardDataAccessor mCardAccessor;
    private NotificationDataAccessor mNotifAccessor;
    private SettingAccessor mSettingAccessor;

    public void writeCard(ToDoCard card) {
        mCardAccessor = new ToDoCardDataAccessor(null);
        mCardAccessor.write(card);
    }

    public void writeBulkCards(List<ToDoCard> cards) {
        mCardAccessor = new ToDoCardDataAccessor(null);
        for (int i = 0; i < cards.size(); i++) {
            mCardAccessor.write(cards.get(i));
        }
    }

    public Map<String, Object> readCard(String id) {
        mCardAccessor = new ToDoCardDataAccessor(null);
        return mCardAccessor.read(id);
    }

    public Map<String, Map<String, Object>> readBulkCards(List<String> ids) {
        mCardAccessor = new ToDoCardDataAccessor(null);
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), mCardAccessor.read(ids.get(i)));
        }
        return map;
    }

    public void writeNotification(Notification notif) {
        mNotifAccessor = new NotificationDataAccessor(null);
        mNotifAccessor.write(notif);
    }

    public void writeBulkNotification(List<Notification> notifs) {
        mNotifAccessor = new NotificationDataAccessor(null);
        for (int i = 0; i < notifs.size(); i++) {
            mNotifAccessor.write(notifs.get(i));
        }
    }

    public Notification readNotification(String id) {
        mNotifAccessor = new NotificationDataAccessor(null);
        return mNotifAccessor.read(id);
    }

    public Map<String, Notification> readBulkNotification(List<String> ids) {
        mNotifAccessor = new NotificationDataAccessor(null);
        Map<String, Notification> map = new HashMap<String, Notification>();
        for (int i = 0; i < ids.size(); i++) {
            Notification notif = mNotifAccessor.read(ids.get(i));
            map.put(ids.get(i), notif);
        }
        return map;
    }

    public void writeSettings(Map<String, Object> settingMap) {

    }

    public Map<String, Object> readSettings() {
        return null;
    }


}
