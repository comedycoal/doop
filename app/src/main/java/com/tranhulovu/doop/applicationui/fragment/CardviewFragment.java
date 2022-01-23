package com.tranhulovu.doop.applicationui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.todocardsystem.AutogenerationConfirmer;
import com.tranhulovu.doop.todocardsystem.Notification;
import com.tranhulovu.doop.todocardsystem.ToDoCard;
import com.tranhulovu.doop.todocardsystem.events.Callback;

import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CardviewFragment extends ManagerFragment {
    private static CardviewFragment instance = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardviewfragment, container, false);
        instance = this;
        return view;
    }

    public static CardviewFragment getInstance() {
        return instance;
    }


    /**
     * Change view card from listview to weekview and weekview to listview
     */
    public void changeView() {

    }

    public void loadCards() {
        MainActivity.getInstance().getCardManager()
                .getActiveCards(new Callback<List<String>>() {
                    @Override
                    public void execute(List<String> data) {
                        // Show cards with this
                    }
                });
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
    public void actionAddCard(String name,
                              String descrip,
                              String datestart,
                              String timestart,
                              String dateend,
                              String timeend,
                              int noti,
                              int type,
                              int time,
                              int till) {
        Context context = MainActivity.getInstance();
        MainActivity.getInstance().getCardManager().createNewCard(new Callback<String>() {
            @Override
            public void execute(String data) {
                MainActivity.getInstance().getCardManager().getCardModifier(data, new Callback<ToDoCard.Modifier>() {
                    @Override
                    public void execute(ToDoCard.Modifier data) {
                        Toast.makeText(context, datestart, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, dateend, Toast.LENGTH_SHORT).show();
                        data.setName(name);
                        data.setDescription(descrip);
                        data.setTimeRange(ZonedDateTime.parse(datestart, ToDoCard.DefaultFormatter),
                                ZonedDateTime.parse(dateend, ToDoCard.DefaultFormatter));
                        Notification.Builder b = new Notification.Builder();

                        data.build();

                        loadCards();
                    }
                });
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
     *
     * @param field
     * @param sort  0 is ASC, 1 is DES
     */
    public void actionFilter(String field, String sort) {

    }
}
