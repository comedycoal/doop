package com.tranhulovu.doop.applicationui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationui.fragment.CardviewFragment;
import com.tranhulovu.doop.applicationui.fragment.ManagerFragment;
import com.tranhulovu.doop.applicationui.fragment.dataCardView;

import java.sql.Array;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class cardViewAdapter extends RecyclerView.Adapter<cardViewAdapter.cardViewHolder> {

    private List<dataCardView> mListCard;
    private CardviewFragment mCardViewFragment;
    public Context mContext;
    public cardViewAdapter(CardviewFragment fragment, List<dataCardView> mListCard, Context ct)
    {
        mCardViewFragment = fragment;
        this.mListCard=mListCard;
        this.mContext=ct;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.todocard,parent,false);
        return new cardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        dataCardView mDatacardview= mListCard.get(position);
        if (mDatacardview==null){
            return;
        }
        holder.name.setText(mDatacardview.name);
        holder.start.setText("Start " + mDatacardview.start.format(DateTimeFormatter.ofPattern("h:mm a MMM dd")));
        holder.end.setText("Due " + mDatacardview.end.format(DateTimeFormatter.ofPattern("h:mm a MMM dd")));
        holder.descrip.setText(mDatacardview.description);
        if (mDatacardview.status == "DONE")
            holder.mCheckButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check_on, 0, 0 , 0);
        else
            holder.mCheckButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0 , 0);

        holder.mNotification.setText(mDatacardview.notification);
        if (mDatacardview.notificationType.equals("SILENT"))
            holder.mNotification.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_alarm, 0, 0 , 0);
        else if (mDatacardview.notificationType.equals("NOTIFICATION"))
            holder.mNotification.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_noti, 0, 0 , 0);
        else
            holder.mNotification.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_alarm, 0, 0 , 0);

        holder.mCheckButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCardViewFragment.actionDone(mDatacardview.id);
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCardViewFragment.actionArchive(mDatacardview.id);
            }
        });

        holder.mNotification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCardViewFragment.actionNotificationChange(mDatacardview.id);
            }
        });

        holder.mTodoCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.dialog = new Dialog(mContext);
                holder.dialog.setContentView(R.layout.dialog_add);
                TextInputLayout textInputLayout = holder.dialog.findViewById(R.id.addCardName);
                textInputLayout.getEditText().setText(mDatacardview.name);
                textInputLayout = holder.dialog.findViewById(R.id.addDescription);
                textInputLayout.getEditText().setText(mDatacardview.description);
                TextView inf;

                inf=holder.dialog.findViewById(R.id.addOrEdit);
                inf.setText("Edit card");


                Button bt;
                TextView tt;

                TextView timeCtrl;
                ZonedDateTime now = ZonedDateTime.now();

                timeCtrl = holder.dialog.findViewById(R.id.Date_start);
                timeCtrl.setText(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                timeCtrl = holder.dialog.findViewById(R.id.Time_start);
                timeCtrl.setText(now.withMinute(now.getMinute() / 10 * 10).format(DateTimeFormatter.ofPattern("HH:mm")));

                timeCtrl = holder.dialog.findViewById(R.id.Date_end);
                timeCtrl.setText(now.plusMinutes(30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                timeCtrl = holder.dialog.findViewById(R.id.Time_end);
                timeCtrl.setText(now.plusMinutes(30).withMinute(now.plusMinutes(30).getMinute() / 10 * 10).format(DateTimeFormatter.ofPattern("HH:mm")));

                bt=holder.dialog.findViewById(R.id.buttonOnOffNoti);
                bt.setText("ON");
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button b=holder.dialog.findViewById(R.id.buttonOnOffNoti);
                        if (b.getText().toString()=="ON"){
                            b.setText("OFF");
                            b.setTextColor(Color.parseColor("#DA6363"));
                            holder.dialog.findViewById(R.id.setUpNotification).setVisibility(View.INVISIBLE);
                        }
                        else{
                            b.setText("ON");
                            b.setTextColor(Color.parseColor("#38817A"));
                            holder.dialog.findViewById(R.id.setUpNotification).setVisibility(View.VISIBLE);
                        }
                    }
                });
                bt=holder.dialog.findViewById(R.id.notiOrAlarm);
                bt.setText("Noti");
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button b=holder.dialog.findViewById(R.id.notiOrAlarm);
                        if (b.getText().toString()=="Noti"){
                            Drawable img = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_alarm_24);
                            b.setText("Alarm");
                            b.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        }
                        else{
                            Drawable img = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_notifications_24);
                            b.setText("Noti");
                            b.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        }
                    }
                });
                tt=holder.dialog.findViewById(R.id.timeNoti);
                tt.setText("1 hour");
                tt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView t=holder.dialog.findViewById(R.id.timeNoti);
                        if (t.getText().toString()=="1 hour"){
                            t.setText("30 min");
                        }
                        else{
                            t.setText("1 hour");
                        }
                    }
                });
                tt=holder.dialog.findViewById(R.id.timelineNoti);
                tt.setText("Start time");
                tt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView t=holder.dialog.findViewById(R.id.timelineNoti);
                        if (t.getText().toString()=="Start time"){
                            t.setText("End time");
                        }
                        else{
                            t.setText("Start time");
                        }
                    }
                });
                holder.dialog.findViewById(R.id.Date_start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = holder.dialog.findViewById(R.id.Date_start);
                        showDatePickerDialog(d);
                    }
                });
                holder.dialog.findViewById(R.id.Date_end).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = holder.dialog.findViewById(R.id.Date_end);
                        showDatePickerDialog(d);
                    }
                });
                holder.dialog.findViewById(R.id.Time_start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = holder.dialog.findViewById(R.id.Time_start);
                        showTimePickerDialog(d);
                    }
                });
                holder.dialog.findViewById(R.id.Time_end).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView d = holder.dialog.findViewById(R.id.Time_end);
                        showTimePickerDialog(d);
                    }
                });
                holder.dialog.findViewById(R.id.buttonDoneAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextInputLayout textInputLayout = holder.dialog.findViewById(R.id.addCardName);
                        String name = textInputLayout.getEditText().getText().toString();
                        textInputLayout = holder.dialog.findViewById(R.id.addDescription);
                        String description = textInputLayout.getEditText().getText().toString();
                        TextView inf = holder.dialog.findViewById(R.id.Date_start);
                        String datestart = inf.getText().toString();
                        inf = holder.dialog.findViewById(R.id.Time_start);
                        String timestart = inf.getText().toString();
                        inf = holder.dialog.findViewById(R.id.Date_end);
                        String dateend = inf.getText().toString();
                        inf = holder.dialog.findViewById(R.id.Time_end);
                        String timeend = inf.getText().toString();
                        Button b;
                        TextView t;
                        int noti=0;
                        b= holder.dialog.findViewById(R.id.buttonOnOffNoti);
                        if(b.getText().toString()=="ON")
                            noti=1;
                        int type = 2;
                        b= holder.dialog.findViewById(R.id.notiOrAlarm);
                        if(b.getText().toString()=="Noti")
                            type=1;
                        int time = 1;
                        t= holder.dialog.findViewById(R.id.timeNoti);
                        if(t.getText().toString()=="30 min")
                            time=0;
                        int till = 0;
                        t= holder.dialog.findViewById(R.id.timelineNoti);
                        if(t.getText().toString()=="Start time")
                            till=1;
                        if (CardviewFragment.getInstance() != null){
                            // Code here id = mDatacardview.id data follow below ----v
                        }
                        //    CardviewFragment.getInstance().actionAddCard(holder.dialog, name, description, datestart, timestart, dateend, timeend, noti, type, time, till);
                    }
                });
                inf = holder.dialog.findViewById(R.id.Date_start);
                //Toast.makeText(MainActivity.getInstance(), mDatacardview.start, Toast.LENGTH_SHORT).show();
                inf.setText(mDatacardview.getStartDate());
                inf = holder.dialog.findViewById(R.id.Time_start);
                inf.setText(mDatacardview.getStartTime());
                inf = holder.dialog.findViewById(R.id.Date_end);
                inf.setText(mDatacardview.getEndDate());
                inf = holder.dialog.findViewById(R.id.Time_end);
                inf.setText(mDatacardview.getEndTime());
                holder.dialog.show();
                return true;
            }
        });
    }

    public void showDatePickerDialog(TextView d) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                mContext,
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
                mContext,
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

    @Override
    public int getItemCount() {
        if (mListCard != null){
            return mListCard.size();
        }
        return 0;
    }

    public void setData(List<dataCardView> newData)
    {
        Collections.reverse(newData);
        mListCard = newData;
        notifyDataSetChanged();
    }


    public class cardViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView descrip;
        private TextView end;
        private TextView start;
        private TextView mDeleteButton;
        private TextView mCheckButton;
        private TextView mNotification;
        private MaterialCardView mTodoCard;
        private Dialog dialog;

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            mTodoCard=itemView.findViewById(R.id.todocard);
            name=itemView.findViewById(R.id.todocard_label_name);
            descrip=itemView.findViewById(R.id.todocard_description1);
            start=itemView.findViewById(R.id.todocard_start);
            end=itemView.findViewById(R.id.todocard_due);

            mDeleteButton = itemView.findViewById(R.id.todocard_delete);
            mCheckButton = itemView.findViewById(R.id.todocard_label_check);
            mNotification = itemView.findViewById(R.id.todocard_notification);
        }
    }
}
