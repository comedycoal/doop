package com.tranhulovu.doop.applicationui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationui.fragment.dataCardView;

import java.util.List;

public class cardViewAdapter extends RecyclerView.Adapter<cardViewAdapter.cardViewHolder>{

    private List<dataCardView> mListCard;
    public cardViewAdapter(List<dataCardView> mListCard){
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
        holder.start.setText(mDatacardview.start);
        holder.end.setText(mDatacardview.end);
        holder.descrip.setText(mDatacardview.description);
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
        mListCard = newData;
        notifyDataSetChanged();
    }


    public class cardViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView descrip;
        private TextView end;
        private TextView start;


        public cardViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.todocard_label_name);
            descrip=itemView.findViewById(R.id.todocard_description1);
            start=itemView.findViewById(R.id.todocard_start);
            end=itemView.findViewById(R.id.todocard_due);
        }
    }
}
