package com.tranhulovu.doop.applicationui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        // set id
    }

    @Override
    public int getItemCount() {
        if (mListCard != null){
            return mListCard.size();
        }
        return 0;
    }

    public class cardViewHolder extends RecyclerView.ViewHolder{

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);

            //Set id
        }
    }
}
