package com.tranhulovu.doop.localdatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tranhulovu.doop.todocardsystem.ToDoCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ToDoCardDataAccessor {
//    public Uri mDataFolderPath;
//    public Map<String, Uri> mCardFileMap;
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;

    public ToDoCardDataAccessor(Context context) {
        this.mContext = context;
        mDatabaseHandler = new DatabaseHandler(context);
    }

    public void initialize() {

    }

    public void write(ToDoCard card) {
        SQLiteDatabase sqLiteDatabase = this.mDatabaseHandler.getWritableDatabase();
        // create query to check if card exists in database
        // if card doesn't exist -> insert
        // else -> update database
        String findCardQuery = "SELECT * FROM " + this.mDatabaseHandler.TABLE_TODO_CARD_NAME
                + " WHERE " + this.mDatabaseHandler.TODO_CARD_ID + " = ?"
                ;

        // prepare values to insert/update database
        ContentValues contentCardValues = new ContentValues();
        contentCardValues.put(DatabaseHandler.TODO_CARD_ID, card.getId());
        contentCardValues.put(DatabaseHandler.TODO_CARD_NAME, card.getName());
        contentCardValues.put(DatabaseHandler.TIME_START, card.getValueOf("start"));
        contentCardValues.put(DatabaseHandler.TIME_END, card.getValueOf("end"));
        contentCardValues.put(DatabaseHandler.CARD_STATUS, card.getValueOf("archivalStatus"));
        contentCardValues.put(DatabaseHandler.CARD_DESCRIPTION, card.getDescription());
        contentCardValues.put(DatabaseHandler.CARD_NOTIFICATION, card.getValueOf("notificationType"));
        contentCardValues.put(DatabaseHandler.CARD_GROUP, card.getGroup());
        contentCardValues.put(DatabaseHandler.CARD_PRIORITY, card.getValueOf("priority"));

        ContentValues contentTagValues = new ContentValues();
        List<String> tagList = card.getTags();
        for (int i = 0; i < card.getTags().size(); i++) {
            contentTagValues.put(DatabaseHandler.TAG_NAME, tagList.get(i));
            contentTagValues.put(DatabaseHandler.TAG_TODO_CARD_ID, card.getId());
        }

        Cursor cursor = sqLiteDatabase.rawQuery(findCardQuery, new String[] {card.getId()});
        if (cursor == null) {
            // insert card to database
            sqLiteDatabase.insert(DatabaseHandler.TABLE_TODO_CARD_NAME, null, contentCardValues);
            sqLiteDatabase.insert(DatabaseHandler.TABLE_TAG_NAME, null, contentTagValues);
        }
        else {
            sqLiteDatabase.update(DatabaseHandler.TABLE_TODO_CARD_NAME,
                    contentCardValues,
                    DatabaseHandler.TODO_CARD_ID + "= ?",
                    new String[] {card.getId()});
            sqLiteDatabase.update(DatabaseHandler.TABLE_TAG_NAME,
                    contentTagValues,
                    DatabaseHandler.TAG_TODO_CARD_ID + "= ?,",
                    new String[] {card.getId()});
            cursor.close();
        }

        sqLiteDatabase.close();
    }

    public void erase(String cardId) {
        SQLiteDatabase sqLiteDatabase = this.mDatabaseHandler.getWritableDatabase();

        sqLiteDatabase.delete(this.mDatabaseHandler.TABLE_TODO_CARD_NAME,
                DatabaseHandler.TODO_CARD_ID + "= ?",
                new String[] {cardId});

        sqLiteDatabase.close();
    }

    public void erase(ToDoCard card) {
        SQLiteDatabase sqLiteDatabase = this.mDatabaseHandler.getWritableDatabase();

        sqLiteDatabase.delete(this.mDatabaseHandler.TABLE_TODO_CARD_NAME,
                DatabaseHandler.TODO_CARD_ID + "= ?",
                new String[] {card.getId()});

        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public Map<String, Object> read(String cardId) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHandler.getReadableDatabase();
        Map<String, Object> data = new HashMap<String, Object>();

        String findCardQuery = "SELECT * FROM " + DatabaseHandler.TABLE_TODO_CARD_NAME
                + " INNER JOIN " + DatabaseHandler.TABLE_TAG_NAME + " ON "
                + DatabaseHandler.TODO_CARD_ID + " = " + DatabaseHandler.TAG_TODO_CARD_ID
                + " INNER JOIN " + DatabaseHandler.TABLE_NOTIFICATION_NAME + " ON "
                + DatabaseHandler.TODO_CARD_ID + " = " + DatabaseHandler.ASSOCIATED_CARD_ID
                + " WHERE " + DatabaseHandler.TODO_CARD_ID + " = ?"
                ;

        Cursor cursor = sqLiteDatabase.rawQuery(findCardQuery, new String[] {cardId});
        if (cursor.moveToFirst()) {
            // Get card ID
            data.put("id",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.TODO_CARD_ID)));
            // Get card name
            data.put("name",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.TODO_CARD_NAME)));
            // Get start time
            data.put("start",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.TIME_START)));
            // Get end time
            data.put("end",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.TIME_END)));
            // Get card status
            data.put("status",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.CARD_STATUS)));
            // Get card description
            data.put("description",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.CARD_DESCRIPTION)));
            // Get card note
            data.put("note",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.CARD_NOTE)));
            // Get card group
            data.put("group",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.CARD_GROUP)));
            // Get card priority
            data.put("priority",
                    cursor.getInt(cursor.getColumnIndex(DatabaseHandler.CARD_PRIORITY)));
            // Get card notification type
            data.put("notification",
                    cursor.getString(cursor.getColumnIndex(DatabaseHandler.ASSOCIATED_CARD_ID)));
            // Get list of tag
            List<String> tagList = new ArrayList<String>();
            for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()) {
                tagList.add(cursor.getString(cursor.getColumnIndex(
                        DatabaseHandler.TAG_NAME)));
            }
            data.put("Tags",
                    tagList);
        }

        cursor.close();
        sqLiteDatabase.close();
        return data;
    }
}
