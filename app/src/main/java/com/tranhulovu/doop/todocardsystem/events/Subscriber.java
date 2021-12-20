package com.tranhulovu.doop.todocardsystem.events;

public interface Subscriber<T>
{
    public boolean isPersistent();

    public void update(T data);
}
