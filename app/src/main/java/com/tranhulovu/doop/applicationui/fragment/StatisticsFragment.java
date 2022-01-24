package com.tranhulovu.doop.applicationui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tranhulovu.doop.R;

import java.util.ArrayList;

public class StatisticsFragment extends ManagerFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statisticsfragment, container, false);

        PieChart pieChart_total = view.findViewById(R.id.staticstic_total_chart);
        PieChart pieChart_thismonth = view.findViewById(R.id.staticstic_thismonth_chart);
        PieChart pieChart_lastmonth = view.findViewById(R.id.staticstic_lastmonth_chart);

        ArrayList<PieEntry> numcards = new ArrayList<>();
        numcards.add(new PieEntry(10, "Missed"));
        numcards.add(new PieEntry(15, "Done"));
        numcards.add(new PieEntry(25, "Ongoing"));

        PieDataSet pieDataSet = new PieDataSet(numcards, "Cards");
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart_total.setData(pieData);
        pieChart_total.getDescription().setEnabled(false);
        pieChart_total.setCenterTextSize(24f);
        pieChart_total.setCenterText("50\nCards");
        pieChart_total.animate();

        pieChart_thismonth.setData(pieData);
        pieChart_thismonth.getDescription().setEnabled(false);
        pieChart_thismonth.setCenterTextSize(24f);
        pieChart_thismonth.setCenterText("50\nCards");
        pieChart_thismonth.animate();

        pieChart_lastmonth.setData(pieData);
        pieChart_lastmonth.getDescription().setEnabled(false);
        pieChart_lastmonth.setCenterTextSize(24f);
        pieChart_lastmonth.setCenterText("50\nCards");
        pieChart_lastmonth.animate();

        return view;
    }
}
