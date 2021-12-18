package com.tranhulovu.doop.todocardsystem.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for event. An event has a list of subscribers and
 * @param <T>
 */
public class Event<T>
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

    public void invoke(T data)
    {
        Set<Subscriber<T>> newL = new HashSet<>(mSubscribers);

        for (Subscriber<T> s : mSubscribers)
        {
            s.update(data);
            if (s.isPersistent())
                newL.remove(s);
        }

        mSubscribers = newL;
    }
}
