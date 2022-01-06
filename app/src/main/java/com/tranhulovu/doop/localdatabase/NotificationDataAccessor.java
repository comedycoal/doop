package com.tranhulovu.doop.localdatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tranhulovu.doop.todocardsystem.Notification;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class NotificationDataAccessor {
//    public Uri mDataFolderPath;
//    public Map<String, Uri> mNotificationMap;
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;

    public NotificationDataAccessor(Context context) {
        this.mContext = context;
        this.mDatabaseHandler = new DatabaseHandler(context);
    }

    public void initialize() {

    }

    public void write(Notification notif) {
        DateTimeFormatter DefaultFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        SQLiteDatabase sqLiteDatabase = this.mDatabaseHandler.getWritableDatabase();
        // create query to check if notif exists in database
        // if notif doesn't exist -> insert
        // else -> update database
        String findNotifQuery = "SELECT * FROM " + this.mDatabaseHandler.TABLE_NOTIFICATION_NAME
                + " WHERE " + this.mDatabaseHandler.ASSOCIATED_CARD_ID + " = ?"
                ;

        // prepare values to insert/update database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.NOTIFICATION_TYPE, notif.getType().toString());
        contentValues.put(DatabaseHandler.ASSOCIATED_CARD_ID, notif.getToDoCardId());
        contentValues.put(DatabaseHandler.NOTIFICATION_DEALINE, notif.getAlarmDeadline()
                .format(DefaultFormatter));
        contentValues.put(DatabaseHandler.NOTIFICATION_MINUTESPRIOR, notif.getMinutesPrior());
        contentValues.put(DatabaseHandler.NOTIFICATION_NAME, notif.getName());

        Cursor cursor = sqLiteDatabase.rawQuery(findNotifQuery, new String[]
                {notif.getToDoCardId()});

        if (cursor == null) {
            // insert notif to database
            sqLiteDatabase.insert(DatabaseHandler.TABLE_TODO_CARD_NAME, null, contentValues);
        }
        else {
            // update database
            sqLiteDatabase.update(DatabaseHandler.TABLE_NOTIFICATION_NAME,
                    contentValues,
                    DatabaseHandler.ASSOCIATED_CARD_ID + "= ?",
                    new String[] {notif.getToDoCardId()});
            cursor.close();
        }
        //close database
        sqLiteDatabase.close();
    }

    public void erase(String notifId) {
        SQLiteDatabase sqLiteDatabase = this.mDatabaseHandler.getWritableDatabase();

        sqLiteDatabase.delete(DatabaseHandler.TABLE_NOTIFICATION_NAME,
                DatabaseHandler.ASSOCIATED_CARD_ID + " = ?",
                new String[] {notifId});

        // close database
        sqLiteDatabase.close();
    }

    public void erase(Notification notif) {
        SQLiteDatabase sqLiteDatabase = this.mDatabaseHandler.getWritableDatabase();

        sqLiteDatabase.delete(DatabaseHandler.TABLE_NOTIFICATION_NAME,
                DatabaseHandler.ASSOCIATED_CARD_ID + " = ?",
                new String[] {notif.getToDoCardId()});

        // close database
        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public Notification read(String notifId) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHandler.getReadableDatabase();
        DateTimeFormatter DefaultFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        String findCardQuery = "SELECT * FROM " + mDatabaseHandler.TABLE_NOTIFICATION_NAME
                + " WHERE " + mDatabaseHandler.ASSOCIATED_CARD_ID + " = ?"
                ;

        Notification.Builder notif = new Notification.Builder();
        Cursor cursor = sqLiteDatabase.rawQuery(findCardQuery, new String[] {notifId});
        if (cursor.moveToFirst()) {
            // set AssociatedCard for Notification
            notif.setAssociatedCard(cursor.getString(
                    cursor.getColumnIndex(DatabaseHandler.ASSOCIATED_CARD_ID)));
            // set Deadline for Notification
            String Deadline = cursor.getString(
                    cursor.getColumnIndex(DatabaseHandler.NOTIFICATION_DEALINE));
            ZonedDateTime time = ZonedDateTime.parse(Deadline, DefaultFormatter);
            notif.setDeadline(time);
            // set Name for Notification
            notif.setName(cursor.getString(
                    cursor.getColumnIndex(DatabaseHandler.NOTIFICATION_NAME)));
            // set Type for Notification
            notif.setType(Notification.Type.valueOf(cursor.getString(
                    cursor.getColumnIndex(DatabaseHandler.NOTIFICATION_TYPE))));
            // set
            notif.setMinutesPrior(cursor.getLong(
                    cursor.getColumnIndex(DatabaseHandler.NOTIFICATION_MINUTESPRIOR)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return notif.build();
    }
}
