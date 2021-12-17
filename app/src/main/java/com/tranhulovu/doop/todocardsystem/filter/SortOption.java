package com.tranhulovu.doop.todocardsystem.filter;

public class SortOption
{
    public final String Field;
    public final boolean Ascending;

    public SortOption(String field, boolean sortAscending)
    {
        Field = field;
        Ascending = sortAscending;
    }
}
