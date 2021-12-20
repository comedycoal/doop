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
    public static final String TAG_ID = "tag_id";
    public static final String TAG_NAME = "tag_name";
    public static final String TAG_TODO_CARD_ID = "card_id";

    // Group table with id column, group name column
    public static final String TABLE_GROUP_NAME = "groups_name";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_TODO_CARD_ID = "card_id";

    // ToDo table with card id column, card name column, TimeStart column, TimeEnd column,
    // Status column, Description column, Note column
    public static final String TABLE_TODO_CARD_NAME = "todocard_name";
    public static final String TODO_CARD_ID = "card_id";
    public static final String TODO_CARD_NAME = "card_name";
    public static final String TIME_START = "time_start";
    public static final String TIME_END = "time_end";
    public static final String CARD_STATUS = "card_status";
    public static final String CARD_DESCRIPTION = "card_description";
    public static final String CARD_NOTE = "card_note";
    public static final String CARD_NOTIFICATION = "card_notification";

    // Notification table with Notification id column, associated card id column ,time column,
    // status column, type column and frequency column
    public static final String TABLE_NOTIFICATION_NAME = "notification_name";
    public static final String NOTIFICATION_ID = "notification_id";
//    public static final String ASSOCIATED_CARD_ID = "card_id";
    public static final String NOTIFICATION_TIME = "time";
    public static final String NOTIFICATION_STATUS = "status";
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_FREQUENCY = "frequency";

    //forcing foreign key
    public static final String FORCE_FOREIGN_KEY="PRAGMA foreign_keys=ON";

    //creating tags table query
    private static final String CREATE_TAG_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
            TABLE_TAG_NAME + "(" + TAG_ID + " TEXT NOT NULL,"
            + TAG_NAME + " TEXT NOT NULL UNIQUE,"
            + " FOREIGN KEY(" + TAG_TODO_CARD_ID
            + ") REFERENCES " + TABLE_TODO_CARD_NAME + "(" + TODO_CARD_ID + "), "
            + "PRIMARY KEY(" + TAG_ID + "," + TAG_TODO_CARD_ID + ")"
            + ")"
            ;

    //creating group table query
    private static final String CREATE_GROUP_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
            TABLE_GROUP_NAME + "(" + GROUP_ID + " TEXT NOT NULL,"
            + GROUP_NAME + " TEXT NOT NULL UNIQUE,"
            + " FOREIGN KEY(" + GROUP_TODO_CARD_ID
            + ") REFERENCES " + TABLE_TODO_CARD_NAME + "(" + TODO_CARD_ID + "), "
            + "PRIMARY KEY(" + GROUP_ID + "," + TAG_TODO_CARD_ID + ")"
            + ")"
            ;

    //creating todo table query

    private static final String CREATE_TODO_CARD_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_TODO_CARD_NAME +
            "(" + TODO_CARD_ID + " TEXT NOT NULL PRIMARY KEY AUTOINCREMENT," +
            TODO_CARD_NAME + " TEXT NOT NULL," + TIME_START +
            " TEXT NOT NULL," + TIME_END + " TEXT NOT NULL," +
            CARD_STATUS + " INTEGER NOT NULL," + CARD_DESCRIPTION +
            " TEXT NOT NULL," + CARD_NOTE + " TEXT NOT NULL," +
            CARD_NOTIFICATION + " TEXT NOT NULL REFERENCES " +
            TABLE_NOTIFICATION_NAME + "(" + NOTIFICATION_ID + "))"
            ;

    //creating notification table query
    private static final String CREATE_NOTIFICATION_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTIFICATION_NAME + "(" + NOTIFICATION_ID +
            " TEXT NOT NULL PRIMARY KEY AUTOINCREMENT," + NOTIFICATION_TIME +
            " TEXT NOT NULL," + NOTIFICATION_STATUS + " TEXT NOT NULL," +
            NOTIFICATION_TYPE + " TEXT NOT NULL," + NOTIFICATION_FREQUENCY +
            " TEXT NOT NULL)"
            ;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TAG_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_GROUP_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_TODO_CARD_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_NOTIFICATION_TABLE_QUERY);
        sqLiteDatabase.execSQL(FORCE_FOREIGN_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
