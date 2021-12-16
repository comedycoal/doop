package com.tranhulovu.doop.todocardsystem;

import android.util.Pair;

import com.tranhulovu.doop.localdatabase.NotificationDataAccessor;
import com.tranhulovu.doop.localdatabase.ToDoCardDataAccessor;
import com.tranhulovu.doop.todocardsystem.events.Callback;
import com.tranhulovu.doop.todocardsystem.filter.FilterOption;
import com.tranhulovu.doop.todocardsystem.filter.SortOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardManager
{
    public enum Status
    {
        READY,
        NOT_READY
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
     * Create a new card, and in effect register it to the local database.
     * @param onCardRegisteredCallback {@code Callback} to be invoked with the card's string
     *                                 when the card is created.
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

    /**
     * Retrieve an interface to modify a card's information.
     * @param id Id of the requested card.
     * @param onCardFetchedCallback {@code Callback} to be invoked with the modifier object
     *                              when it is created.
     */
    public void getCardModifier(String id, Callback<ToDoCard.Modifier> onCardFetchedCallback)
    {
        Runnable task = () ->
        {
            // TODO: Find card

            ToDoCard card = mCards.get(id);

            onCardFetchedCallback.execute(card.makeModifier());
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    /**
     * Retrieve a {@code ToDoCard}'s properties as a Mapping of {@code String} to {@code Object}.
     * Each entry needs casting to its appropriate type.
     * @param id Id of the requested card.
     * @param onCardInfoFetchedCallback {@code Callback} to be invoked with the mapping object
     *                                  when it is fetched.
     */
    public void getCardInfo(String id, Callback<Map<String, Object>> onCardInfoFetchedCallback)
    {
        Runnable task = () ->
        {
            ToDoCard card = mCards.get(id);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", card.getName());
            map.put("description", card.getDescription());
            map.put("note", card.getNote());
            map.put("group", card.getGroup());
            map.put("tags", card.getTags());
            map.put("priority", card.getPriority());
            map.put("start", card.getStart());
            map.put("end", card.getEnd());
            map.put("archivalStatus", card.getArchivalStatus());
            map.put("notificationId", card.getNotificationId());

            onCardInfoFetchedCallback.execute(map);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    /**
     * Request a deletion of a card from the manager and the database.
     * Most often, a user will just archive a card instead.
     * Use this method with serious discretion.
     * @param id Id of the requested card.
     * @param onCardDeletionResultCallback {@code Callback} to be invoked with the deletion result
     *                                     (deleted, failed to delete).
     */
    public void requestDeleteCard(String id, Callback<Boolean> onCardDeletionResultCallback)
    {
        Runnable task = () ->
        {
            // TODO: Delete a card and stuff
            onCardDeletionResultCallback.execute(true);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    /**
     * Get a list of active card's IDs (an active card is not marked as archived),
     * sorted by default sorting method (by Due time).
     * @param onFetchedCallback {@code Callback} to be invoked with the fetched ID list.
     */
    public void getActiveCards(Callback<List<String>> onFetchedCallback)
    {
        Runnable task = () ->
        {
            List<String> list = new ArrayList<>();
            // TODO: Get and sort list or something
            onFetchedCallback.execute(list);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    /**
     * Get a list of archived card's IDs.
     * sorted by default sorting method (by Due time).
     * @param onFetchedCallback {@code Callback} to be invoked with the fetched ID list.
     */
    public void getArchivedCards(Callback<List<String>> onFetchedCallback)
    {
        Runnable task = () ->
        {
            List<String> list = new ArrayList<>();
            // TODO: Get and sort list or something

            onFetchedCallback.execute(list);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    /**
     * Get a list of cards filtered and sorted by a set of criteria.
     * @param options A {@code List} of {@code FilterOption} and {@code SortOption} in pairs.
     *                Options are considered from top to bottom (index 0 to the last).
     * @param onFetchedCallback {@code Callback} to be invoked with the fetched ID list.
     */
    public void filterAndSort(List<Pair<FilterOption, SortOption>> options,
                              Callback<List<String>> onFetchedCallback)
    {
        Runnable task = () ->
        {
            List<String> list = new ArrayList<>();
            // TODO: Get and sort list or something

            onFetchedCallback.execute(list);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
