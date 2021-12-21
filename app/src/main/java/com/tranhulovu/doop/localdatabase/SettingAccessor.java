package com.tranhulovu.doop.localdatabase;

import android.content.Context;
import android.net.Uri;

public class SettingAccessor {
//    private Uri mSettingPath;
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;

    public SettingAccessor(Context context) {
        this.mContext = context;
        mDatabaseHandler = new DatabaseHandler(context);
    }

    public void initialize() {

    }

    public void write(String name, Object value) {

    }

    public Object read(String name) {

        return null;
    }
}
