package com.tranhulovu.doop.todocardsystem;

import android.util.Pair;

import com.tranhulovu.doop.localdatabase.NotificationDataAccessor;
import com.tranhulovu.doop.localdatabase.ToDoCardDataAccessor;
import com.tranhulovu.doop.todocardsystem.events.Callback;
import com.tranhulovu.doop.todocardsystem.events.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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


    //////////////////////////////////////////////////
    //---// Fields
    private DynamicStatistics mDynamicStatistics;
    private Map<String, ToDoCard> mCards;

    private NotificationManager mNotifManager;
    private ToDoCardDataAccessor mCardAccessor;
    private NotificationDataAccessor mNotifAccessor;

    private Status mCardLiveStatus = Status.NOT_READY;


    //////////////////////////////////////////////////
    //---// Constructors
    public CardManager(NotificationManager notifManagerRef,
                       ToDoCardDataAccessor cardAccessorRef,
                       NotificationDataAccessor notifAccessorRef)
    {
        mNotifManager = notifManagerRef;
        mCardAccessor = cardAccessorRef;
        mNotifAccessor = notifAccessorRef;
        onCreate();
    }

    private void onCreate() { }


    //////////////////////////////////////////////////
    //---// Methods
    private String generateId()
    {
        return String.valueOf(mCards.size());
    }

    /**
     * Create a new card,
     * @param onCardRegisteredCallback
     */
    public void createNewCard(Callback<String> onCardRegisteredCallback)
    {
        String id = generateId();
        ToDoCard newC = new ToDoCard(id);

        Runnable task = () ->
        {
            // TODO: Add new ToDoCard reference
            // TODO: Add card to local database

            onCardRegisteredCallback.execute(id);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void getCardModifier(String id, Callback<ToDoCard.Modifier> onCardFetchedCallback)
    {
        Runnable task = () ->
        {
            // TODO: Find card

            ToDoCard card = mCards.get(id);

            onCardFetchedCallback.execute(card.makeBuilder());
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public Map<String, Object> getCardInfo(String id)
    {
        ToDoCard mAssociatedCard = mCards.get(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", mAssociatedCard.getName());
        map.put("description", mAssociatedCard.getDescription());
        map.put("note", mAssociatedCard.getNote());
        map.put("group", mAssociatedCard.getGroup());
        map.put("tags", mAssociatedCard.getTags());
        map.put("priority", mAssociatedCard.getPriority());
        map.put("start", mAssociatedCard.getStart());
        map.put("end", mAssociatedCard.getEnd());
        map.put("archivalStatus", mAssociatedCard.getArchivalStatus());
        map.put("notificationId", mAssociatedCard.getNotificationId());
        return map;
    }

}
