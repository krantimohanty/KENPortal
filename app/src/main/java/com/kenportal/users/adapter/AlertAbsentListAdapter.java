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
public class AlertAbsentListAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<AlertAbsentModel> absentModelArrayList;

    public AlertAbsentListAdapter(Context context, ArrayList<AlertAbsentModel> absentModelArrayList) {
        this.context=context;
        this.absentModelArrayList=absentModelArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_absent_data,parent,false);
        vh=new AbsentHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbsentHolder absentHolder = (AbsentHolder) holder;

        absentHolder.text_Date.setText(absentModelArrayList.get(position).getDate());
        absentHolder.text_inDate.setText(absentModelArrayList.get(position).getDayTime());

    }

    @Override
    public int getItemCount() {
        return absentModelArrayList.size();
    }


    private class AbsentHolder extends RecyclerView.ViewHolder {

        public TextView text_Date;
        public TextView text_inDate;

        public AbsentHolder(View itemView) {
            super(itemView);
            text_Date= (TextView) itemView.findViewById(R.id.text_Date);
            text_inDate= (TextView) itemView.findViewById(R.id.text_inDate);

        }

    }
}
