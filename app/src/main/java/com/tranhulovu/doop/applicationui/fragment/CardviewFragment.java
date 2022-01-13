package com.tranhulovu.doop.applicationui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.tranhulovu.doop.R;

import java.security.InvalidParameterException;

public class CardviewFragment extends ManagerFragment {
    private static CardviewFragment instance = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statisticsfragment, container, false);
        instance = this;
        return view;
    }
    public static CardviewFragment getInstance() {
        return instance;
    }



    /**
     * Change view card from listview to weekview and weekview to listview

     */
    public void changeView(){

    }


    /**
     * Mainfragment call when add card
     * @param name name of card.
     * @param descrip description of card
     * @param datestart date start task
     * @param timestart time start task
     * @param dateend date end task
     * @param timeend time start task
     * @param noti 0 if turn off noti and 1 if turn on noti
     * @param type type noti when {@param noti} is 1, 1 is notification, 2 is alarm
     * @param time time noti before {@param till}
     * @param till 0 is {@param timeend}, 1 is {@param timestart}
     * @throws InvalidParameterException when card with ID {@code id} does not exist
     */
    public void actionAddCard(String name,String descrip, String datestart, String timestart, String dateend, String timeend, boolean noti, int type, String time, String till){

    }

    /**
     * Mainfragment call when auto add card
     * @param code code for auto add card
     */
    public void actionAutoAddCard(String code){

    }
    /**
     * Mainfragment call when filter card
     * @param field
     * @param sort 0 is ASC, 1 is DES
     */
    public void actionFilter(String field,String sort){

    }
}
