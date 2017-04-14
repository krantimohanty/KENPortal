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
public class AlertLateExitAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<AlertAbsentModel> exitModelArrayList;

    public AlertLateExitAdapter(Context context, ArrayList<AlertAbsentModel> exitModelArrayList) {
        this.context=context;
        this.exitModelArrayList=exitModelArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_lateexit_data,parent,false);
        vh=new LateExitHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LateExitHolder lateExitHolder = (LateExitHolder) holder;

        lateExitHolder.text_Date.setText(exitModelArrayList.get(position).getDate());
        lateExitHolder.text_inDate.setText(exitModelArrayList.get(position).getDayTime());

    }

    @Override
    public int getItemCount() {
        return exitModelArrayList.size();
    }


    private class LateExitHolder extends RecyclerView.ViewHolder {

        public TextView text_Date;
        public TextView text_inDate;

        public LateExitHolder(View itemView) {
            super(itemView);
            text_Date= (TextView) itemView.findViewById(R.id.text_Date);
            text_inDate= (TextView) itemView.findViewById(R.id.text_inDate);

        }

    }
}
