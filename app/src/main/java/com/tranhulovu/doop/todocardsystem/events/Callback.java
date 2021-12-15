package com.tranhulovu.doop.todocardsystem.events;

public interface Callback<T>
{
    public void execute(T data);
}
