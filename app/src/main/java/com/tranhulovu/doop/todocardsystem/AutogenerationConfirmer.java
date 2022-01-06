package com.tranhulovu.doop.todocardsystem;

import com.tranhulovu.doop.todocardsystem.events.Callback;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutogenerationConfirmer
{
    private Map<String, ToDoCard> mPendingCards;
    private String mErrorMessage = null;
    private Callback<Collection<ToDoCard>> mConfirmCallback;

    public AutogenerationConfirmer(Collection<ToDoCard> cards,
                                   String errorMessage,
                                   Callback<Collection<ToDoCard>> confirmCallback)
    {
        mPendingCards = new HashMap<>();
        for (ToDoCard card : cards)
        {
            mPendingCards.put(card.getId(), card);
        }
        mErrorMessage = errorMessage;
        mConfirmCallback = confirmCallback;
    }

    public void confirm()
    {
        if (mConfirmCallback != null)
            mConfirmCallback.execute(null);
    }

    public void discard()
    {
        mConfirmCallback = null;
    }

    public Set<String> getAllIds()
    {
        return mPendingCards.keySet();
    }

    public Map<String, Object> getPendingCard(String id)
    {
        return mPendingCards.containsKey(id)
                ? mPendingCards.get(id).toMap() : null;
    }

    public ToDoCard.Modifier getModifier(String id)
    {
        return mPendingCards.containsKey(id) ?
                mPendingCards.get(id).makeModifier() : null;
    }
}
