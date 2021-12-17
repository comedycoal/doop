package com.tranhulovu.doop.todocardsystem;

import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.todocardsystem.events.Callback;
import com.tranhulovu.doop.todocardsystem.filter.FilterOption;
import com.tranhulovu.doop.todocardsystem.filter.SortOption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardManager
{
    public enum Status
    {
        READY,
        NOT_READY
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private DynamicStatistics mDynamicStatistics;
    private Map<String, ToDoCard> mActiveCards;
    private Map<String, ToDoCard> mArchivedCards;

    private Collection<String> mModifiedCards;
    private Collection<String> mDeletedCards;

    private NotificationManager mNotifManager;
    private LocalAccessorFacade mLocalAccessor;

    private Status mCardLiveStatus = Status.NOT_READY;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    public CardManager(NotificationManager notifManagerRef,
                       LocalAccessorFacade facade)
    {
        mNotifManager = notifManagerRef;
        mLocalAccessor = facade;

        mDeletedCards = new HashSet<>();
        mModifiedCards = new HashSet<>();

        onCreate();
    }

    private void onCreate()
    {
        // TODO: Get list of card ids

        // TODO: Spawn a thread to fetch all cards data, maybe?
        //       This is such a demo app so fetch all cards sounds about right.

    }

    /**
     * Call this method when the program terminates.
     */
    public void finalize()
    {
        actualizeModification();
        actualizeDeletion();
    }


    //////////////////////////////////////////////////
    //---// Methods

    /**
     * Performs a write to disk for all cards modified
     * prior to when this method is called.
     */
    public void actualizeModification()
    {
        // TODO: Write cards using LocalAccessor
    }

    /**
     * Performs a deletion from disk for all cards marked as deleted
     * prior to when this method is called.
     */
    public void actualizeDeletion()
    {
        // TODO: Delete cards using LocalAccessor
    }


    private String generateId()
    {
        // TODO: Maybe generate id base on time created instead
        return String.valueOf(mActiveCards.size());
    }

    private Collection<ToDoCard> getAllCards()
    {
        return mActiveCards.values();
    }

    private ToDoCard checkAndFetch(String id)
    {
        ToDoCard card = null;
        if (mActiveCards.containsKey(id))
        {
            card = mActiveCards.get(id);
            if (card == null)
            {
                // TODO: Call LocalFacade to load card
            }
        }
        else if (mArchivedCards.containsKey(id))
        {
            card = mArchivedCards.get(id);
            if (card == null)
            {
                // TODO: Call LocalFacade to load card
            }
        }

        return card;
    }


    /**
     * Create a new card, and in effect register it to the local database.
     * @param onCardRegisteredCallback {@code Callback} to be invoked with the card's string
     *                                 when the card is created, and {@code null} otherwise.
     */
    public void createNewCard(Callback<String> onCardRegisteredCallback)
    {
        Runnable task = () ->
        {
            String id = generateId();
            ToDoCard newC = new ToDoCard(id);


            // TODO: Add new ToDoCard reference

            // TODO: Add card to local database


            if (onCardRegisteredCallback != null)
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
     * @throws InvalidParameterException when card with ID {@code id} does not exist
     */
    public void getCardModifier(String id, Callback<ToDoCard.Modifier> onCardFetchedCallback)
                                        throws InvalidParameterException
    {
        CardManager m = this;
        Runnable task = () ->
        {
            ToDoCard card = checkAndFetch(id);
            if (card == null)
            {
                throw new InvalidParameterException(
                    "CardManager instance does not recognize card with id " + id);
            }

            if (onCardFetchedCallback != null)
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
     *
     * @throws InvalidParameterException when id is not valid.
     */
    public void getCardInfo(String id, Callback<Map<String, Object>> onCardInfoFetchedCallback)
                                throws InvalidParameterException
    {
        Runnable task = () ->
        {
            ToDoCard card = checkAndFetch(id);

            if (card == null)
            {
                throw new InvalidParameterException(
                        "CardManager instance does not recognize card with id " + id);
            }

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

            if (onCardInfoFetchedCallback != null)
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
            if (mArchivedCards.containsKey(id) || mActiveCards.containsKey(id))
            {
                // TODO: Ask LocalAccessor to delete the thing

                // TODO: If delete successfully do this:
                if (mArchivedCards.containsKey(id))
                {

                }

            }

            if (onCardDeletionResultCallback != null)
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

            if (onFetchedCallback != null)
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

            if (onFetchedCallback != null)
                onFetchedCallback.execute(list);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    /**
     * Get a list of cards filtered and sorted by a set of criteria.
     * {@code options} is not guaranteed to retain when {@code onFetchedCallback} is called.
     * @param filterOptions A {@code List} of {@code FilterOption}.
     * @param sortOptions A {@code List} of {@code SortOption}.
     *                Options are considered from top to bottom (index 0 to the last).
     * @param onFetchedCallback {@code Callback} to be invoked with the fetched ID list.
     */
    public void filterAndSort(List<FilterOption> filterOptions, List<SortOption> sortOptions,
                              Callback<List<String>> onFetchedCallback)
    {
        Runnable task = () ->
        {
            List<ToDoCard> list = new ArrayList<ToDoCard>(getAllCards());

            Predicate<ToDoCard> filterPredicate = p -> true;

            if (filterOptions != null && sortOptions.size() > 0)
            {
                for (FilterOption o : filterOptions)
                {
                    Predicate<ToDoCard> p = new Predicate<ToDoCard>()
                    {
                        @Override
                        public boolean test(ToDoCard toDoCard)
                        {
                            return toDoCard.getValueOf(o.Field).equals(o.Value);
                        }
                    };
                    filterPredicate.and(p);
                }
            }

            Comparator<ToDoCard> comparator = null;
            if (sortOptions != null && sortOptions.size() > 0)
            {
                SortOption first = sortOptions.remove(0);
                Function<ToDoCard, String> a = ToDoCard.getGetterOf(first.Field);
                if (first.Ascending)
                    comparator = Comparator.comparing(a);
                else
                    comparator = Comparator.comparing(a).reversed();

                if (sortOptions.size() > 0)
                    for (SortOption o : sortOptions)
                    {
                        Function<ToDoCard, String> b = ToDoCard.getGetterOf(o.Field);
                        if (o.Ascending)
                            comparator.thenComparing(b);
                        else
                            comparator.thenComparing(b).reversed();
                    }
            }

            if (comparator == null) comparator = Comparator.comparing(ToDoCard::getName);

            List<String> idList = list.stream()
                    .filter(filterPredicate)
                    .sorted(comparator)
                    .map(p -> p.getId())
                    .collect(Collectors.toList());

            if (onFetchedCallback != null)
                onFetchedCallback.execute(idList);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
