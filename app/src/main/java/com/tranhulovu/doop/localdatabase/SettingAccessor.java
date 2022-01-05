package com.tranhulovu.doop.localdatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SettingAccessor {
//    private Uri mSettingPath;
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;

    public SettingAccessor(Context context) {
        this.mContext = context;
        mDatabaseHandler = new DatabaseHandler(context);
    }

    public void initialize() {
        SQLiteDatabase sqLiteDatabase = mDatabaseHandler.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.AUTO_ARCHIVE_CARD_SETTING, "ON");
        contentValues.put(DatabaseHandler.DATE_SETTING, "26 Dec, 2021 ");
        contentValues.put(DatabaseHandler.NOTIFICATION_SETTING, "ON");
        contentValues.put(DatabaseHandler.TIME_FORMAT_SETTING, "24 Hour");
        sqLiteDatabase.update(DatabaseHandler.TABLE_SETTING_NAME,
                contentValues,
                null,
                null);
    }

    public void write(String name, Object value) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHandler.getWritableDatabase();
        String query = "SELECT ? FROM " + DatabaseHandler.TABLE_SETTING_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[] {name});
        if (cursor == null) {
            initialize();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(name, value.toString());
        sqLiteDatabase.update(DatabaseHandler.TABLE_SETTING_NAME,
                contentValues,
                null,
                null);

        cursor.close();
        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public Object read(String name) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHandler.getReadableDatabase();
        String query = "SELECT ? FROM " + DatabaseHandler.TABLE_SETTING_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[] {name});
        String settingData = cursor.getString(cursor.getColumnIndex(name));

        cursor.close();
        sqLiteDatabase.close();
        return settingData;
    }
}
