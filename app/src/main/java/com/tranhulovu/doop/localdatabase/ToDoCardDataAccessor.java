package com.tranhulovu.doop.localdatabase;

import android.net.Uri;

import com.tranhulovu.doop.todocardsystem.ToDoCard;

import java.util.Map;

public class ToDoCardDataAccessor {
    public Uri mDataFolderPath;
    public Map<String, Uri> mCardFileMap;

    public ToDoCardDataAccessor(Uri dataPath) {
        this.mDataFolderPath = dataPath;
    }

    public void initialize() {

    }

    public void write(ToDoCard card) {

    }

    public void erase(String cardId) {

    }

    public void erase(ToDoCard card) {

    }

    public ToDoCard read(String cardId) {

        return null;
    }
}
