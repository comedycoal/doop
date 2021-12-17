package com.tranhulovu.doop.todocardsystem.filter;

import com.tranhulovu.doop.todocardsystem.ToDoCard;

import java.util.Comparator;

public class FilterOption
{
    public final String Field;
    public final String Value;

    private FilterOption(String field, String value)
    {
        this.Field = field;
        this.Value = value;
    }
}
