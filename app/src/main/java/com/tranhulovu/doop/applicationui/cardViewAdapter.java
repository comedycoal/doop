package com.tranhulovu.doop.applicationui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationui.fragment.CardviewFragment;
import com.tranhulovu.doop.applicationui.fragment.dataCardView;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class cardViewAdapter extends RecyclerView.Adapter<cardViewAdapter.cardViewHolder>{

    private List<dataCardView> mListCard;
    private CardviewFragment mCardViewFragment;

    public cardViewAdapter(CardviewFragment fragment, List<dataCardView> mListCard)
    {
        mCardViewFragment = fragment;
        this.mListCard=mListCard;
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

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);

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
