package com.tranhulovu.doop.applicationui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.type.DateTime;
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationui.cardViewAdapter;
import com.tranhulovu.doop.todocardsystem.AutogenerationConfirmer;
import com.tranhulovu.doop.todocardsystem.Notification;
import com.tranhulovu.doop.todocardsystem.ToDoCard;
import com.tranhulovu.doop.todocardsystem.events.Callback;
import com.tranhulovu.doop.todocardsystem.filter.FilterOption;
import com.tranhulovu.doop.todocardsystem.filter.SortOption;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CardviewFragment extends ManagerFragment {
    private static CardviewFragment instance = null;
    private RecyclerView mrecyclerView;

    private Callback<List<Map<String, Object>>> mFetchCallback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardviewfragment, container, false);
        instance = this;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mrecyclerView=view.findViewById(R.id.rcv_card);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        mrecyclerView.setLayoutManager(gridLayoutManager);

        // Temporarily set to empty, wait for CardManager to return the card list
        cardViewAdapter adapter = new cardViewAdapter(this, new ArrayList<>(),getActivity());

        mFetchCallback = new Callback<List<Map<String, Object>>>()
        {
            @Override
            public void execute(List<Map<String, Object>> data)
            {
                List<dataCardView> l = data.stream().map(e -> new dataCardView(
                        (String)e.get("id"),
                        (String)e.get("name"),
                        (String)e.get("description"),
                        (ZonedDateTime)e.get("start"),
                        (ZonedDateTime)e.get("end"),
                        ((ToDoCard.CheckStatus)e.get("status")).name(),
                        ((Notification)e.get("notification")).getStringMessage(),
                        ((Notification)e.get("notification")).getType().name()))
                        .collect(Collectors.toList());

                adapter.setData(l);
            }
        };

        mrecyclerView.setAdapter(adapter);

        loadCards();
    }

    public static CardviewFragment getInstance() {
        return instance;
    }


    /**
     * Change view card from listview to weekview and weekview to listview
     */
    public void changeView()
    {

    }

    public void loadCards()
    {
        MainActivity.getInstance().getCardManager().getActiveCards(mFetchCallback, MainActivity.getInstance());
    }

    public void loadCards(List<String> ids)
    {
        MainActivity.getInstance().getCardManager().getCardInfos(ids, mFetchCallback, MainActivity.getInstance());
    }


    /**
     * Mainfragment call when add card
     *
     * @param name      name of card.
     * @param descrip   description of card
     * @param datestart date start task
     * @param timestart time start task
     * @param dateend   date end task
     * @param timeend   time start task
     * @param noti      0 if turn off noti and 1 if turn on noti
     * @param type      type noti when {@param noti} is 1, 1 is notification, 2 is alarm
     * @param time      time noti before {@param till}, 0 is 30 min, 2 is 1 hour
     * @param till      0 is {@param timeend}, 1 is {@param timestart}
     * @throws InvalidParameterException when card with ID {@code id} does not exist
     */
    public void actionAddCard(Dialog addDialog,
                              String name,
                              String descrip,
                              String datestart,
                              String timestart,
                              String dateend,
                              String timeend,
                              int noti,
                              int type,
                              int time,
                              int till)
    {
        Context context = MainActivity.getInstance();

        DateTimeFormatter wholeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m");

        ZonedDateTime stime = ZonedDateTime.now();
        ZonedDateTime etime = stime;

        String error = "";
        boolean success = true;
        if (name.isEmpty())
        {
            success = false;
            error = "Please enter card's name";
        }
        else
        {
            try
            {
                String startWholeString = datestart.trim() + " " + timestart.trim();
                LocalDateTime local = LocalDateTime.parse(startWholeString, wholeFormatter);
                stime = local.atZone(ZoneOffset.systemDefault());
            }
            catch (Exception e)
            {
                error = "Invalid start time";
                success = false;
                stime = ZonedDateTime.now().withMinute(ZonedDateTime.now().getMinute() / 10 * 10);
                throw e;
            }

            try
            {
                String endWholeString = dateend.trim() + " " + timeend.trim();
                LocalDateTime local = LocalDateTime.parse(endWholeString, wholeFormatter);
                etime = local.atZone(ZoneOffset.systemDefault());
            }
            catch (Exception e)
            {
                error = "Invalid end time";
                success = false;
            }

            if (stime.compareTo(etime) > 0)
            {
                error = "Error: end time is after start time.";
                success = false;
            }
        }

        if (!success)
        {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            return;
        }

        addDialog.dismiss();

        final ZonedDateTime finalStime = stime;
        final ZonedDateTime finalEtime = etime;
        MainActivity.getInstance().getCardManager().createNewCard(new Callback<String>()
        {
            @Override
            public void execute(String data)
            {
                String id = data;
                MainActivity.getInstance().getCardManager().getCardModifier(data, new Callback<ToDoCard.Modifier>()
                {
                    @Override
                    public void execute(ToDoCard.Modifier data)
                    {

                        data.setName(name);
                        data.setDescription(descrip);

                        data.setTimeRange(finalStime, finalEtime);


                        Notification.Builder b = new Notification.Builder();

                        b.setName(name);
                        b.setAssociatedCard(id);
                        b.setType(noti == 0 ? Notification.Type.SILENT :
                                (type == 1 ? Notification.Type.NOTIFICATION
                                    : Notification.Type.ALARM));
                        b.setDeadlineType(till == 1 ? Notification.DeadlineType.START : Notification.DeadlineType.END);
                        b.setDeadline(till == 1 ? finalStime : finalEtime);
                        b.setMinutesPrior(time == 0 ? 30 : 60);
                        Notification notif = b.build();

                        data.setNotification(notif);
                        data.build();

                        loadCards();
                    }
                }, MainActivity.getInstance());

            }
        });
    }

    /**
     * Mainfragment call when auto add card
     *
     * @param code code for auto add card
     */
    public void actionAutoAddCard(String code) {
        MainActivity.getInstance().getCardManager().autoCreateCards(code, new Callback<AutogenerationConfirmer>() {
            @Override
            public void execute(AutogenerationConfirmer data) {
                Set<String> ids = data.getAllIds();
                for (String id : ids) {
                    Map<String, Object> cardInfo = data.getPendingCard(id);
                }

                // Show new cards
            }
        });
    }

    /**
     * Mainfragment call when filter card
     *  @param field is String "Name", "TStart" or "Tend"
     * @param sort  is "ASC" or "DES"
     * @param s is string input
     */
    public void actionFilter(String field, String sort, String s)
    {
        field = field.equals("Name") ? "name" : (field.equals("TStart") ? "start" : "end");

        List<FilterOption> a = new ArrayList<>();
        a.add(new FilterOption(field, s));

        List<SortOption> b = new ArrayList<>();
        b.add(new SortOption(field, sort.equals("ASC") ? true : false));

        MainActivity.getInstance().getCardManager().filterAndSort(a, b, new Callback<List<String>>()
        {
            @Override
            public void execute(List<String> data)
            {
                loadCards(data);
            }
        });
    }

    public void actionDone(String cardId)
    {
        MainActivity.getInstance().getCardManager().getCardModifier(cardId, new Callback<ToDoCard.Modifier>()
        {
            @Override
            public void execute(ToDoCard.Modifier data)
            {
                if (data.isDone())
                    data.markNotDone();
                else
                    data.markDone();
                data.build();

                loadCards();
            }
        }, MainActivity.getInstance());
    }

    public void actionArchive(String cardId)
    {
        MainActivity.getInstance().getCardManager().getCardModifier(cardId, new Callback<ToDoCard.Modifier>()
        {
            @Override
            public void execute(ToDoCard.Modifier data)
            {
                data.archive();
                data.build();

                Toast.makeText(MainActivity.getInstance(), "Card archived", Toast.LENGTH_SHORT).show();
                loadCards();
            }
        }, MainActivity.getInstance());
    }

    public void actionNotificationChange(String cardId)
    {
        MainActivity.getInstance().getCardManager().getCardModifier(cardId, new Callback<ToDoCard.Modifier>()
        {
            @Override
            public void execute(ToDoCard.Modifier data)
            {
                Notification.Builder b = new Notification.Builder(data.getNotification());
                b.setType(data.getNotification().getNextType());
                data.setNotification(b.build());
                data.build();

                loadCards();
            }
        }, MainActivity.getInstance());
    }

    public void actionEdit(String cardId)
    {

    }
}
