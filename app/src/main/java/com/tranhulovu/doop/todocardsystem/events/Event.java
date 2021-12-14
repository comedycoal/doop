package com.tranhulovu.doop.todocardsystem.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Event<T>
{
    protected Set<Subscriber<T>> mSubscribers;

    public void addSubscriber(Subscriber<T> s)
    {
        mSubscribers.add(s);
    }

    public void removeSubscriber(Subscriber<T> s)
    {
        mSubscribers.remove(s);
    }

    public abstract void invoke(T data);
}
