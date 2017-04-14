package com.kenportal.users.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenportal.users.R;
import com.kenportal.users.datamodels.MessangerModel;

import java.util.ArrayList;

/**
 * Created by kranti on 11/26/2016.
 */
public class MessengerAdapter extends RecyclerView.Adapter {

    private ArrayList<MessangerModel> dataSet;
    Context context;


    public MessengerAdapter(Context c, ArrayList<MessangerModel> data, RecyclerView rv) {
        dataSet = data;
        context = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messenger_list, parent, false);
        vh = new MessengerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessengerViewHolder) {
            ((MessengerViewHolder)holder).messageTxt.setText(dataSet.get(position).getMessage());
            ((MessengerViewHolder)holder).time.setText(dataSet.get(position).getDate());
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MessengerViewHolder extends RecyclerView.ViewHolder {
        TextView messageTxt;
        TextView time;


        public MessengerViewHolder(View itemView) {
            super(itemView);
            messageTxt = (TextView) itemView.findViewById(R.id.messageTxt);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
