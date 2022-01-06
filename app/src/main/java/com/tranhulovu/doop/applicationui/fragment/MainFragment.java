package com.tranhulovu.doop.applicationui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationui.ViewPagerAdapter;

import java.util.Calendar;

public class MainFragment extends ManagerFragment implements View.OnClickListener {
    private BottomNavigationView mTopNavigation;
    private FloatingActionButton mFloatingActionButton;
    private BottomAppBar mBottomAppBar;
    private ViewPager2 mViewpager2;
    private NavController navController;
    private ExtendedFloatingActionButton mFABAdd;
    private ExtendedFloatingActionButton mFABSmartAdd;
    private ExtendedFloatingActionButton mFABChangeView;
    private ExtendedFloatingActionButton mFABFilter;
    private Dialog dialog;
    private boolean mFABclicked = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTopNavigation = view.findViewById(R.id.bottom_nav);
        mBottomAppBar = view.findViewById(R.id.bottomAppBar);
        mViewpager2 = view.findViewById(R.id.viewpaper);
        mFloatingActionButton = view.findViewById(R.id.floatingAction);
        mFABAdd = view.findViewById(R.id.FABAdd);
        mFABSmartAdd = view.findViewById(R.id.FABSmartAdd);
        mFABChangeView = view.findViewById(R.id.FABChangeView);
        mFABFilter = view.findViewById(R.id.FABFilter);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        mViewpager2.setAdapter(viewPagerAdapter);

        mTopNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_tasks) {
                    mViewpager2.setCurrentItem(0);
                } else if (id == R.id.action_statistics) {
                    mViewpager2.setCurrentItem(1);
                } else if (id == R.id.action_settings) {
                    mViewpager2.setCurrentItem(2);
                }
                return true;
            }
        });
        mViewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mTopNavigation.getMenu().findItem(R.id.action_tasks).setChecked(true);
                        break;
                    case 1:
                        mTopNavigation.getMenu().findItem(R.id.action_statistics).setChecked(true);
                        break;
                    case 2:
                        mTopNavigation.getMenu().findItem(R.id.action_settings).setChecked(true);
                        break;
                }
            }
        });
        mFloatingActionButton.setOnClickListener(this);
        mFABSmartAdd.setOnClickListener(this);
        mFABAdd.setOnClickListener(this);
        mFABFilter.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingAction:
                if (mFABclicked) {
                    mFABAdd.hide();
                    mFABSmartAdd.hide();
                    mFABChangeView.hide();
                    mFABFilter.hide();
                } else {
                    mFABAdd.show();
                    mFABSmartAdd.show();
                    mFABChangeView.show();
                    mFABFilter.show();
                }
                mFABclicked = !mFABclicked;
                break;
            case R.id.FABSmartAdd:

                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_smart_add);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            case R.id.FABAdd:
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_add);
                //dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.findViewById(R.id.Date_start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = dialog.findViewById(R.id.Date_start);
                        showDatePickerDialog(d);
                    }
                });
                dialog.findViewById(R.id.Date_end).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = dialog.findViewById(R.id.Date_end);
                        showDatePickerDialog(d);
                    }
                });
                dialog.findViewById(R.id.Time_start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = dialog.findViewById(R.id.Time_start);
                        showTimePickerDialog(d);
                    }
                });
                dialog.findViewById(R.id.Time_end).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = dialog.findViewById(R.id.Time_end);
                        showTimePickerDialog(d);
                    }
                });

                dialog.show();
                break;
            case R.id.FABFilter:
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_filter);
                //dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
        }
    }

    public void showDatePickerDialog(TextView d) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = " " + i + "/" + i1 + "/" + i2;
                        d.setText(date);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void showTimePickerDialog(TextView d) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String date = " " + i + ":" + i1;
                        d.setText(date);
                    }

                },
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }
};




