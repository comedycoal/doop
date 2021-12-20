package com.tranhulovu.doop.applicationcontrol;

import java.util.Map;

public class SettingManager {
    private Map<String, Object> mSettingMap;

    public void loadSettingsFromSources() {
        // TODO: Load settings from local database
    }

    public void saveSettingsToSources() {
        // TODO: Save settings to local databse
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
