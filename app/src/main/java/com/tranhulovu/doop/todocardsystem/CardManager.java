package com.tranhulovu.doop.todocardsystem;

import android.util.Pair;

import java.util.List;
import java.util.Map;

public class CardManager
{
    public enum Status
    {
        READY,
        NOT_READY
    }

    public static class FilterOption
    {

    }

    public static class SortOption
    {

    }


    private DynamicStatistics mDynamicStatistics;
    private Map<String, ToDoCard> mCards;

    private Status mCardLiveStatus = Status.NOT_READY;

    public CardManager()
    {
        onCreate();
    }

    private void onCreate()
    {
        mCardLiveStatus = Status.READY;
    }

    private String generateId()
    {
        String generated = "";
        while (generated.equals("") || mCards.containsKey(generated))
            generated = String.valueOf(mCards.size() + 1);

        return generated;
    }

    public ToDoCard.Builder createNewCard()
    {
        String id = this.generateId();
        // TODO

        throw new UnsupportedOperationException();
    }

    public List<String> getAllCardIds()
    {
        throw new UnsupportedOperationException();
    }

    public List<String> filterCards(List<Pair<FilterOption, SortOption>> filterRequest)
    {
        throw new UnsupportedOperationException();
    }

    public ToDoCard.Builder getCardBuilder(String id)
    {
        throw new UnsupportedOperationException();
    }

    public void deleteCard(ToDoCard.Builder builder)
    {
        throw new UnsupportedOperationException();
    }

    public void deleteCard(String id)
    {

    }
}
