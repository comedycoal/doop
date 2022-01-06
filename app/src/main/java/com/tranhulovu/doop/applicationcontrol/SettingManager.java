package com.tranhulovu.doop.applicationcontrol;

import android.content.Context;

import com.tranhulovu.doop.localdatabase.DatabaseHandler;
import com.tranhulovu.doop.localdatabase.SettingAccessor;

import java.util.HashMap;
import java.util.Map;

public class SettingManager {
    private final Map<String, Object> mSettingMap;
    private final SettingAccessor mSettingAccessor;

    public SettingManager(Context context) {
        mSettingMap = new HashMap<>();
        mSettingAccessor = new SettingAccessor(context);
    }

    public void loadSettingsFromSources() {
        mSettingMap.put(DatabaseHandler.NOTIFICATION_SETTING, mSettingAccessor.read(DatabaseHandler.NOTIFICATION_SETTING));
        mSettingMap.put(DatabaseHandler.AUTO_ARCHIVE_CARD_SETTING, mSettingAccessor.read(DatabaseHandler.AUTO_ARCHIVE_CARD_SETTING));
        mSettingMap.put(DatabaseHandler.TIME_FORMAT_SETTING, mSettingAccessor.read(DatabaseHandler.TIME_FORMAT_SETTING));
        mSettingMap.put(DatabaseHandler.DATE_SETTING, mSettingAccessor.read((DatabaseHandler.DATE_SETTING)));
    }

    public void saveSettingsToSources() {
        for (Map.Entry<String, Object> entry: mSettingMap.entrySet()) {
            mSettingAccessor.write(entry.getKey(), entry.getValue());
        }
    }

    public void setSetting(String name, String value) {
        mSettingMap.put(name, value);
        saveSettingsToSources();
    }

    public Object getSetting(String name) {
        return mSettingMap.get(name);
    }

    public Map<String, Object> getSettings() {
        return mSettingMap;
    }
}
