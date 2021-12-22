package com.tranhulovu.doop.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tranhulovu.doop.todocardsystem.ToDoCard;

import java.util.List;
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

    public ToDoCard read(String cardId) {
        SQLiteDatabase sqLiteDatabase = this.mDatabaseHandler.getReadableDatabase();

        String findCardQuery = "SELECT * FROM " + this.mDatabaseHandler.TABLE_TODO_CARD_NAME
                + " WHERE " + this.mDatabaseHandler.TODO_CARD_ID + " = ?"
                ;

        Cursor cursor = sqLiteDatabase.rawQuery(findCardQuery, new String[] {cardId});
        if (cursor.moveToFirst()) {
            // implement
        }
        return null;
    }
}
