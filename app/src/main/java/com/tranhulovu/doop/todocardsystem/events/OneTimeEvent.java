package com.tranhulovu.doop.todocardsystem.events;

import java.util.Collections;
import java.util.HashSet;

public class OneTimeEvent<T> extends Event<T>
{
    @Override
    public void invoke(T data)
    {
        for (Subscriber<T> s : mSubscribers)
        {
            s.update(data);
        }
        mSubscribers.clear();
    }

    public OneTimeEvent()
    {
        mSubscribers = new HashSet<>();
        mSubscribers = Collections.synchronizedSet(mSubscribers);
    }
}
