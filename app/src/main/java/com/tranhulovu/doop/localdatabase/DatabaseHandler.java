package com.tranhulovu.doop.localdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "doop_database";

    // Tag table with id column, tag name column
    public static final String TABLE_TAG_NAME = "tags_table";
    public static final String TAG_NAME = "tag_name";
    public static final String TAG_TODO_CARD_ID = "card_id";

    // ToDo table with card id column, card name column, TimeStart column, TimeEnd column,
    // Status column, Description column, Note column
    public static final String TABLE_TODO_CARD_NAME = "todocard_table";
    public static final String TODO_CARD_ID = "card_id";
    public static final String TODO_CARD_NAME = "card_name";
    public static final String TIME_START = "time_start";
    public static final String TIME_END = "time_end";
    public static final String CARD_STATUS = "card_status";
    public static final String CARD_DESCRIPTION = "card_description";
    public static final String CARD_NOTE = "card_note";
    public static final String CARD_NOTIFICATION = "card_notification";
    public static final String CARD_GROUP = "card_group";
    public static final String CARD_PRIORITY = "card_priority";

    // Notification table with Notification id column, time column,
    // status column, type column and frequency column
    public static final String TABLE_NOTIFICATION_NAME = "notification_table";
//    public static final String NOTIFICATION_ID = "notification_id";
    public static final String ASSOCIATED_CARD_ID = "card_id";
    public static final String NOTIFICATION_DEALINE = "time";
    public static final String NOTIFICATION_MINUTESPRIOR = "minutes_prior";
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_NAME = "name";
//    public static final String NOTIFICATION_FREQUENCY = "frequency";

    // Setting table with NotificationSetting column, AutoArchieveCard column, TimeFormat column
    // and DateSetting column
    public static final String TABLE_SETTING_NAME = "setting_table";
    public static final String NOTIFICATION_SETTING = "notification_setting";
    public static final String AUTO_ARCHIEVE_CARD_SETTING = "auto_archieve_card_setting";
    public static final String TIME_FORMAT_SETTING = "time_format";
    public static final String DATE_SETTING = "date_setting";

    //forcing foreign key
    public static final String FORCE_FOREIGN_KEY="PRAGMA foreign_keys=ON";

    //creating tags table query
    private static final String CREATE_TAG_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
            TABLE_TAG_NAME + "(" + TAG_NAME + " TEXT NOT NULL UNIQUE,"
            + " FOREIGN KEY(" + TAG_TODO_CARD_ID + ") REFERENCES "
            + TABLE_TODO_CARD_NAME + "(" + TODO_CARD_ID + ") ON DELETE CASCADE, "
            + "PRIMARY KEY(" + TAG_NAME + "," + TAG_TODO_CARD_ID + ")"
            + ")"
            ;

    //creating todo table query

    private static final String CREATE_TODO_CARD_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_TODO_CARD_NAME +
            "(" + TODO_CARD_ID + " TEXT NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + TODO_CARD_NAME + " TEXT NOT NULL,"
            + TIME_START + " TEXT NOT NULL,"
            + TIME_END + " TEXT NOT NULL,"
            + CARD_STATUS + " TEXT NOT NULL,"
            + CARD_DESCRIPTION + " TEXT NOT NULL,"
            + CARD_NOTE + " TEXT NOT NULL,"
            + CARD_GROUP + " TEXT NOT NULL,"
            + CARD_PRIORITY + " INTEGER NOT NULL,"
            + CARD_NOTIFICATION + " TEXT NOT NULL)"
            ;

    //creating notification table query
    private static final String CREATE_NOTIFICATION_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTIFICATION_NAME + "(" + NOTIFICATION_NAME + " TEXT NOT NULL,"
            + NOTIFICATION_DEALINE + " TEXT NOT NULL,"
            + NOTIFICATION_MINUTESPRIOR + " INTEGER NOT NULL,"
            + NOTIFICATION_TYPE + " TEXT NOT NULL,"
            + "FOREIGN KEY(" + NOTIFICATION_TYPE + ") REFERENCES "
            + TABLE_TODO_CARD_NAME + "(" + CARD_NOTIFICATION + ") ON DELETE CASCADE, "
            + "FOREIGN KEY(" + ASSOCIATED_CARD_ID + ") REFERENCES "
            + TABLE_TODO_CARD_NAME + "(" + TODO_CARD_ID + ") ON DELETE CASCADE, "
            + "PRIMARY KEY(" + NOTIFICATION_TYPE + ", " + ASSOCIATED_CARD_ID + ")"
            +")"
            ;

    private static final String CREATE_SETTING_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SETTING_NAME + "(" + NOTIFICATION_SETTING + " TEXT NOT NULL,"
            + AUTO_ARCHIEVE_CARD_SETTING + " TEXT NOT NULL,"
            + TIME_FORMAT_SETTING + " TEXT NOT NULL,"
            + DATE_SETTING + " TEXT NOT NULL,"
            + "PRIMARY KEY(" + NOTIFICATION_SETTING + "," + AUTO_ARCHIEVE_CARD_SETTING
            + "," + TIME_FORMAT_SETTING + "," + DATE_SETTING + "))";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TAG_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_TODO_CARD_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_NOTIFICATION_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_SETTING_TABLE_QUERY);
        sqLiteDatabase.execSQL(FORCE_FOREIGN_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
