package com.tranhulovu.doop.localdatabase;

import android.net.Uri;

public class SettingAccessor {
    private Uri mSettingPath;

    public SettingAccessor(Uri dataPath) {
        this.mSettingPath = dataPath;
    }

    public void initialize() {

    }

    public void write(String name, Object value) {

    }

    public Object read(String name) {

        return null;
    }
}
