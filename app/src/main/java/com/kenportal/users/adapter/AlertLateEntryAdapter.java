package com.kenportal.users.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenportal.users.R;
import com.kenportal.users.datamodels.AlertAbsentModel;

import java.util.ArrayList;

/**
 * Created by kranti on 11/26/2016.
 */
public class AlertLateEntryAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<AlertAbsentModel> lateEntryModelArrayList;

    public AlertLateEntryAdapter(Context context, ArrayList<AlertAbsentModel> lateEntryModelArrayList) {
        this.context=context;
        this.lateEntryModelArrayList=lateEntryModelArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_lateentry_data,parent,false);
        vh=new LateEntryHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LateEntryHolder absentHolder = (LateEntryHolder) holder;

        absentHolder.text_Date.setText(lateEntryModelArrayList.get(position).getDate());
        absentHolder.text_inDate.setText(lateEntryModelArrayList.get(position).getDayTime());

    }

    @Override
    public int getItemCount() {
        return lateEntryModelArrayList.size();
    }


    private class LateEntryHolder extends RecyclerView.ViewHolder {

        public TextView text_Date;
        public TextView text_inDate;

        public LateEntryHolder(View itemView) {
            super(itemView);
            text_Date= (TextView) itemView.findViewById(R.id.text_Date);
            text_inDate= (TextView) itemView.findViewById(R.id.text_inDate);

        }

    }
}
