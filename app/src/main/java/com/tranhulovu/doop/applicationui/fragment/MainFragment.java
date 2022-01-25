package com.tranhulovu.doop.applicationui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationcontrol.Authenticator;
import com.tranhulovu.doop.applicationui.ViewPagerAdapter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    private Authenticator mAuthenticator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mAuthenticator = MainActivity.getAuthenticator();

        if (mAuthenticator.getSignInState() == Authenticator.SignInState.NOT_SIGNED_IN) {
            navController.navigate(R.id.action_mainFragment_to_authenticatorFragment);
        }
        else {
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
            mFABChangeView.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.FABChangeView) {
            if (CardviewFragment.getInstance() != null)
                CardviewFragment.getInstance().changeView();
        } else if (view.getId() == R.id.floatingAction) {
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
        }
        else if (view.getId() == R.id.FABSmartAdd) {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_smart_add);
            EditText textInput = dialog.findViewById(R.id.smartAddinput);
            String code = textInput.getText().toString();
            dialog.findViewById(R.id.buttonDoneAutoAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CardviewFragment.getInstance() != null)
                        CardviewFragment.getInstance().actionAutoAddCard(code);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else if (view.getId() == R.id.FABAdd) {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_add);
            Button bt;
            TextView tt;

            TextView timeCtrl;
            ZonedDateTime now = ZonedDateTime.now();

            timeCtrl = dialog.findViewById(R.id.Date_start);
            timeCtrl.setText(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            timeCtrl = dialog.findViewById(R.id.Time_start);
            timeCtrl.setText(now.withMinute(now.getMinute() / 10 * 10).format(DateTimeFormatter.ofPattern("HH:mm")));

            timeCtrl = dialog.findViewById(R.id.Date_end);
            timeCtrl.setText(now.plusMinutes(30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            timeCtrl = dialog.findViewById(R.id.Time_end);
            timeCtrl.setText(now.plusMinutes(30).withMinute(now.plusMinutes(30).getMinute() / 10 * 10).format(DateTimeFormatter.ofPattern("HH:mm")));

            bt=dialog.findViewById(R.id.buttonOnOffNoti);
            bt.setText("ON");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b=dialog.findViewById(R.id.buttonOnOffNoti);
                    if (b.getText().toString()=="ON"){
                        b.setText("OFF");
                        b.setTextColor(Color.parseColor("#DA6363"));
                        dialog.findViewById(R.id.setUpNotification).setVisibility(View.INVISIBLE);
                    }
                    else{
                        b.setText("ON");
                        b.setTextColor(Color.parseColor("#38817A"));
                        dialog.findViewById(R.id.setUpNotification).setVisibility(View.VISIBLE);
                    }
                }
            });
            bt=dialog.findViewById(R.id.notiOrAlarm);
            bt.setText("Noti");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b=dialog.findViewById(R.id.notiOrAlarm);
                    if (b.getText().toString()=="Noti"){
                        Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_alarm_24);
                        b.setText("Alarm");
                        b.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    }
                    else{
                        Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_notifications_24);
                        b.setText("Noti");
                        b.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    }
                }
            });
            tt=dialog.findViewById(R.id.timeNoti);
            tt.setText("1 hour");
            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView t=dialog.findViewById(R.id.timeNoti);
                    if (t.getText().toString()=="1 hour"){
                        t.setText("30 min");
                    }
                    else{
                        t.setText("1 hour");
                    }
                }
            });
            tt=dialog.findViewById(R.id.timelineNoti);
            tt.setText("Start time");
            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView t=dialog.findViewById(R.id.timelineNoti);
                    if (t.getText().toString()=="Start time"){
                        t.setText("End time");
                    }
                    else{
                        t.setText("Start time");
                    }
                }
            });
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
            dialog.findViewById(R.id.buttonDoneAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextInputLayout textInputLayout = dialog.findViewById(R.id.addCardName);
                    String name = textInputLayout.getEditText().getText().toString();
                    textInputLayout = dialog.findViewById(R.id.addDescription);
                    String description = textInputLayout.getEditText().getText().toString();
                    TextView inf = dialog.findViewById(R.id.Date_start);
                    String datestart = inf.getText().toString();
                    inf = dialog.findViewById(R.id.Time_start);
                    String timestart = inf.getText().toString();
                    inf = dialog.findViewById(R.id.Date_end);
                    String dateend = inf.getText().toString();
                    inf = dialog.findViewById(R.id.Time_end);
                    String timeend = inf.getText().toString();
                    Button b;
                    TextView t;
                    int noti=0;
                    b= dialog.findViewById(R.id.buttonOnOffNoti);
                    if(b.getText().toString()=="ON")
                        noti=1;
                    int type = 2;
                    b= dialog.findViewById(R.id.notiOrAlarm);
                    if(b.getText().toString()=="Noti")
                        type=1;
                    int time = 1;
                    t= dialog.findViewById(R.id.timeNoti);
                    if(t.getText().toString()=="30 min")
                        time=0;
                    int till = 0;
                    t= dialog.findViewById(R.id.timelineNoti);
                    if(t.getText().toString()=="Start time")
                        till=1;
//                    String s=name+description+datestart+datestart+timestart;
//                    Toast.makeText(MainActivity.getInstance(), s, Toast.LENGTH_SHORT).show();
                    if (CardviewFragment.getInstance() != null)
                        CardviewFragment.getInstance().actionAddCard(dialog, name, description, datestart, timestart, dateend, timeend, noti, type, time, till);
                }
            });
            dialog.show();
        }
        else if (view.getId() == R.id.FABFilter) {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_filter);
            Spinner spinnerFilter = dialog.findViewById(R.id.spinnerFilter);
            String[] items1 = new String[]{"Name", "TStart", "TEnd"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items1);
            spinnerFilter.setAdapter(adapter1);
            Spinner spinnerSort = dialog.findViewById(R.id.spinnerSort);
            String[] items2 = new String[]{"ASC", "DES"};
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items2);
            spinnerSort.setAdapter(adapter2);
            dialog.findViewById(R.id.buttonDoneFilter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Spinner spinFilter = dialog.findViewById(R.id.spinnerFilter);
                    Spinner spinSort = dialog.findViewById(R.id.spinnerSort);
                    EditText e= dialog.findViewById(R.id.editText);
                    //Toast.makeText(MainActivity.getInstance(), e.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (CardviewFragment.getInstance() != null)
                        CardviewFragment.getInstance().actionFilter(spinFilter.getSelectedItem().toString(), spinSort.getSelectedItem().toString(),e.getText().toString());
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void showDatePickerDialog(TextView d) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = " " + i + "/" + (i1 + 1) + "/" + i2;
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




