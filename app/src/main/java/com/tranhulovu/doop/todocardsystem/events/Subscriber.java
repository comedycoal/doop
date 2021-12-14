package com.tranhulovu.doop.todocardsystem.events;

public interface Subscriber<T>
{
    public void update(T data);
}
