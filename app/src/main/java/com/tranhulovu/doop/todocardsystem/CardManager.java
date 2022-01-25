package com.tranhulovu.doop.todocardsystem;

import android.app.Activity;

import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.todocardsystem.events.Callback;
import com.tranhulovu.doop.todocardsystem.filter.FilterOption;
import com.tranhulovu.doop.todocardsystem.filter.SortOption;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public static class DebugModeActivatedException extends Exception
    {
        public DebugModeActivatedException(String message)
        {
            super(message);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Fields
    private DynamicStatistics mDynamicStatistics;
    private Map<String, ToDoCard> mCards = new HashMap<>();

    private Collection<String> mNeedsWritingCards;

    private NotificationManager mNotifManager;
    private LocalAccessorFacade mLocalAccessor;

    private Status mCardLiveStatus = Status.NOT_READY;

    private boolean debugMode = false;

    private final String[] mDebugCardNames = {"Do homeword", "Workout", "Submit", "Wake up"};
    private final String[] mDebugCardDescriptions = {"Do homeword", "Workout", "to the Overlord", "!"};


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //---// Constructors
    public CardManager(NotificationManager notifManagerRef,
                       LocalAccessorFacade facade)
    {
        mNotifManager = notifManagerRef;
        mLocalAccessor = facade;

        mNeedsWritingCards = new HashSet<>();

        debugMode = true;

        // Create a bunch of cards
        for (int i = 0; i < 4; ++i)
        {
            ToDoCard card = makeDebugCard(i);
            mCards.put(card.getId(), card);
        }

        onCreate();
    }

    private void onCreate()
    {
        if (debugMode)
            return;

        // Get list of card ids
        List<String> ids = mLocalAccessor.getAddCardIDs();

        // Spawn a thread to fetch all cards data, maybe?
        // This is such a demo app so fetch all cards sounds about right.
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                Map<String, Map<String, Object>> all = mLocalAccessor.readBulkCards(ids);
                for (Map.Entry<String, Map<String, Object>> entry : all.entrySet())
                {
                    String id = entry.getKey();
                    ToDoCard card = ToDoCard.fromMapWithoutNotif(entry.getValue());
                    Notification notif = mLocalAccessor.readNotification(id);

                    mNotifManager.watch(card);

                    mCards.put(id, card);
                }
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    /**
     * Call this method when the program terminates.
     */
    public void finalize()
    {
        actualizeModification();
        actualizeDeletion();
    }

    private ToDoCard makeDebugCard(int number)
    {
        String id = generateId();
        ToDoCard newC = new ToDoCard(id);

        ToDoCard.Modifier m = newC.makeModifier();
        m.setName(mDebugCardNames[number]);
        m.setDescription(mDebugCardDescriptions[number]);
        m.setTimeRange(ZonedDateTime.now().plusHours(number).withMinute(0).withSecond(0),
                    ZonedDateTime.now().plusDays(number+1).withMinute(0).withSecond(0));
        m.setGroup("Testing");
        m.addTag("test" + number);
        m.setNote("Card's note");
        m.setPriority(1);

        m.build();
        return newC;
    }

    /**
     * Make a CardManager in debug mode (to work without wrting anything to disk).
     * For now, DO NOT create notification for it. (though it shouldn't break anything).
     * Pass {@code null} for {@code notifManagerRef} and {@code facade} if not available
     * @param notifManagerRef A {@code NotificationManager}, probably get from {@code MainActivity}
     * @param facade A {@code LocalDatabaseAccessor}, probably get from {@code MainActivity}
     * @param howmany how many cards are already in the manager when created.
     * @return a debug-mode {@code CardManager} object
     */
    public static CardManager getManagerForTesting(NotificationManager notifManagerRef,
                                                   LocalAccessorFacade facade,
                                                   int howmany)
    {
        CardManager manager = new CardManager(notifManagerRef, facade);
        manager.debugMode = true;

        // Create a bunch of cards
        for (int i = 0; i < howmany; ++i)
        {
            ToDoCard card = manager.makeDebugCard(i);
            manager.mCards.put(card.getId(), card);
        }

        return manager;
    }


    //////////////////////////////////////////////////
    //---// Methods

    /**
     * Returns whether the cards are active and queries can be used without errors.
     * @return the status.
     */
    public Status getStatus()
    {
        return mCardLiveStatus;
    }


    /**
     * Performs a write to disk for all cards modified
     * prior to when this method is called.
     */
    public void actualizeModification()
    {
        if (debugMode) return;
        for (String id : mNeedsWritingCards)
        {
            ToDoCard card = mCards.get(id);
            // Write cards using LocalAccessor

            if (card != null)
            {
                mLocalAccessor.writeCard(card);
                mNotifManager.saveNotification(card.getNotification());
            }
        }

        mNeedsWritingCards = new HashSet<>();
    }

    /**
     * Performs a deletion from disk for all cards marked as deleted
     * prior to when this method is called.
     */
    public void actualizeDeletion()
    {
        if (debugMode) return;
        // Filter deleted cards
        List<String> list
                = mCards.entrySet().stream()
                    .filter(p -> p.getValue().getStatus() == ToDoCard.CheckStatus.DELETED)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

        for (String id : list)
        {
            // Delete cards using LocalAccessor
            ToDoCard card = mCards.get(id);

            if (card != null)
            {
                mLocalAccessor.deleteCard(card);
                mNotifManager.deleteNotification(card.getNotification());
            }

            mNotifManager.deleteNotification(mCards.get(id).getNotification());
        }
    }


    private String generateId()
    {
        //Generate id base on time created
        return String.valueOf(ZonedDateTime.now().toInstant().toEpochMilli());
    }

    private Collection<ToDoCard> getAllCards()
    {
        return mCards.values();
    }

    ToDoCard checkAndFetch(String id)
            throws DebugModeActivatedException
    {
        ToDoCard card = null;
        if (mCards.containsKey(id))
        {
            card = mCards.get(id);
            if (card == null)
            {
                if (debugMode)
                    throw new DebugModeActivatedException("Debug mode is on," +
                            "cannot query to database for card with id " + id);
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

            // Add new ToDoCard reference
            mCards.put(id, newC);

            // Add card to local database
            mNeedsWritingCards.add(id);

            if (onCardRegisteredCallback != null)
                onCardRegisteredCallback.execute(id);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void autoCreateCards(String textToParse, Callback<AutogenerationConfirmer> onCreation)
    {
        Runnable task = () ->
        {
            Set<ToDoCard> pendingCards = new HashSet<>();

            // DONE: Parse string and create the cards
            List<String> lines = ParserUtilities.splitLines(textToParse);
            List<TaskFitter.Task> tasks = new ArrayList<>();

            String firstLine = lines.get(0);
            lines = new ArrayList<>(lines);
            lines.remove(0);
            TaskFitter.Task dummy = ParserUtilities.getTask
                    (ParserUtilities.splitIntoTokens(firstLine), null);

            TaskFitter fitter = new TaskFitter(dummy.start, dummy.end);

            TaskFitter.Task prev = null;
            boolean erroreous = false;
            int firstErrorIndex = -1;
            int index = 1;
            for (String line : lines)
            {
                TaskFitter.Task t = ParserUtilities.getTask(
                        ParserUtilities.splitIntoTokens(firstLine), prev);
                boolean res = fitter.fit(t);
                if (res)
                {
                    tasks.add(t);
                }
                else
                {
                    if (firstErrorIndex == -1)
                        firstErrorIndex = index;
                    erroreous = true;
                }
                prev = t;
                index++;
            }

            for (TaskFitter.Task t : tasks)
            {
                String id = generateId();
                ToDoCard newC = new ToDoCard(id);

                ToDoCard.Modifier m = newC.makeModifier();
                m.setName(t.name);
                m.setTimeRange(ZonedDateTime.ofInstant(Instant.ofEpochSecond(t.start), ZoneId.systemDefault()),
                                ZonedDateTime.ofInstant(Instant.ofEpochSecond(t.end), ZoneId.systemDefault()));
                m.build();

                pendingCards.add(newC);
            }

            String error = null;
            if (erroreous)
                error = "Cannot fit task described at line " + firstErrorIndex + ".";

            AutogenerationConfirmer confirmer =
                    new AutogenerationConfirmer(pendingCards,
                            error,
                            new Callback<Collection<ToDoCard>>()
                            {
                                @Override
                                public void execute(Collection<ToDoCard> data)
                                {
                                    for (ToDoCard card : data)
                                    {
                                        mCards.put(card.getId(), card);
                                        mNeedsWritingCards.add(card.getId());
                                    }
                                }
                            });

            if (onCreation != null)
                onCreation.execute(confirmer);
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
    public void getCardModifier(String id, Callback<ToDoCard.Modifier> onCardFetchedCallback, Activity activity)
                                        throws InvalidParameterException
    {
        CardManager m = this;
        Runnable task = () ->
        {
            ToDoCard card = null;
            try
            {
                card = checkAndFetch(id);
            } catch (DebugModeActivatedException e) {}
            if (card == null)
            {
                throw new InvalidParameterException(
                    "CardManager instance does not recognize card with id " + id);
            }

            mNeedsWritingCards.add(id);

            if (onCardFetchedCallback != null)
                onCardFetchedCallback.execute(card.makeModifier());
        };

        activity.runOnUiThread(task);
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
            ToDoCard card = null;
            try
            {
                card = checkAndFetch(id);
            } catch (DebugModeActivatedException e) {}

            if (card == null)
            {
                throw new InvalidParameterException(
                        "CardManager instance does not recognize card with id " + id);
            }

            Map<String, Object> map = card.toMap();

            if (onCardInfoFetchedCallback != null)
                onCardInfoFetchedCallback.execute(map);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void getCardInfos(List<String> ids, Callback<List<Map<String, Object>>> onCardInfosFetchedCallback, Activity activity)
            throws InvalidParameterException
    {
        Runnable task = () ->
        {
            List<Map<String, Object>> infos = new ArrayList<>();

            for (String id : ids)
            {
                infos.add(mCards.get(id).toMap());
            }

            if (onCardInfosFetchedCallback != null)
                onCardInfosFetchedCallback.execute(infos);
        };

        activity.runOnUiThread(task);
    }

    /**
     * Get a list of active card's IDs (an active card is not marked as archived),
     * sorted by default sorting method (by Due time).
     * @param onFetchedCallback {@code Callback} to be invoked with the fetched ID list.
     */
    public void getActiveCards(Callback<List<Map<String, Object>>> onFetchedCallback, Activity activity)
    {
        Runnable task = () ->
        {
            // Get and sort list or something
            List<String> list = mCards.entrySet().stream()
                    .filter(p -> (p.getValue().getStatus() != ToDoCard.CheckStatus.DELETED
                              && p.getValue().getStatus() != ToDoCard.CheckStatus.ARCHIVED))
                    .sorted(new Comparator<Map.Entry<String, ToDoCard>>()
                    {
                        @Override
                        public int compare(Map.Entry<String, ToDoCard> o1, Map.Entry<String, ToDoCard> o2)
                        {
                            return o2.getValue().getEnd().compareTo(o1.getValue().getEnd());
                        }
                    })
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            List<Map<String, Object>> infos = new ArrayList<>();

            for (String id : list)
            {
                infos.add(mCards.get(id).toMap());
            }

            if (onFetchedCallback != null)
                onFetchedCallback.execute(infos);
        };

        activity.runOnUiThread(task);
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
            // Get and sort list or something
            List<String> list = mCards.entrySet().stream()
                    .filter(p -> p.getValue().getStatus() == ToDoCard.CheckStatus.ARCHIVED)
                    .sorted(new Comparator<Map.Entry<String, ToDoCard>>()
                    {
                        @Override
                        public int compare(Map.Entry<String, ToDoCard> o1, Map.Entry<String, ToDoCard> o2)
                        {
                            return o2.getValue().getEnd().compareTo(o1.getValue().getEnd());
                        }
                    })
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (onFetchedCallback != null)
                onFetchedCallback.execute(list);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public int getUrgentCount()
    {
        List<Map.Entry<String, ToDoCard>> list = mCards.entrySet().stream()
                .filter(p -> p.getValue().getStatus() == ToDoCard.CheckStatus.URGENT)
                .collect(Collectors.toList());

        return list.size();
    }

    public int getOverdueCount()
    {
        List<Map.Entry<String, ToDoCard>> list = mCards.entrySet().stream()
                .filter(p -> p.getValue().getStatus() == ToDoCard.CheckStatus.OVERDUED)
                .collect(Collectors.toList());

        return list.size();
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
                    if (o.Value.isEmpty())
                        continue;

                    Predicate<ToDoCard> p = new Predicate<ToDoCard>()
                    {
                        @Override
                        public boolean test(ToDoCard toDoCard)
                        {
                            boolean res = toDoCard.getValueOf(o.Field).equals(o.Value);
                            return res;
                        }
                    };
                    filterPredicate = filterPredicate.and(p);
                }
            }

            Comparator<ToDoCard> comparator = null;
            if (sortOptions != null && sortOptions.size() > 0)
            {
                SortOption first = sortOptions.remove(0);
                Function<ToDoCard, String> a = ToDoCard.getGetterOf(first.Field);
                if (!first.Ascending)
                    comparator = Comparator.comparing(a);
                else
                    comparator = Comparator.comparing(a).reversed();

                if (sortOptions.size() > 0)
                    for (SortOption o : sortOptions)
                    {
                        Function<ToDoCard, String> b = ToDoCard.getGetterOf(o.Field);
                        if (!o.Ascending)
                            comparator = comparator.thenComparing(b);
                        else
                            comparator = comparator.thenComparing(b).reversed();
                    }
            }

            if (comparator == null) comparator = Comparator.comparing(ToDoCard::getName);

            List<String> idList = list.stream()
                    .filter(filterPredicate)
                    .sorted(comparator)
                    .map(ToDoCard::getId)
                    .collect(Collectors.toList());

            if (onFetchedCallback != null)
                onFetchedCallback.execute(idList);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
