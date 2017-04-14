package com.kenportal.users.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenportal.users.R;
import com.kenportal.users.datamodels.AbsentModel;

import java.util.ArrayList;

/**
 *  AbsentAdapter Class.
 */
public class AbsentAdapter extends RecyclerView.Adapter{

    //List<String> data;
    private Context context;                       //context -  The current context.
    private ArrayList<AbsentModel> absentMoArList;
    private LayoutInflater vi;
    private final int VIEW_ITEM = 1;
    RecyclerView mRecyclerView;

    public AbsentAdapter(Context context, ArrayList<AbsentModel> absentMoArList) {
        this.context=context;
        this.absentMoArList=absentMoArList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_absent,parent,false);
        vh=new AbsentHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        AbsentModel item=absentMoArList.get(position);

        AbsentHolder absentHolder = (AbsentHolder) holder;

        absentHolder.text_Name.setText(absentMoArList.get(position).getEmpname());
        absentHolder.text_status.setText(absentMoArList.get(position).getStatus());
        absentHolder.text_slno.setText(absentMoArList.get(position).getSlno());
        //sectionViewHolder.text_Name.setText();

    }

    @Override
    public int getItemCount() {
        return absentMoArList.size();
    }


    private class AbsentHolder extends RecyclerView.ViewHolder {

        public TextView text_Name;
        public TextView text_status;
        public TextView text_slno;

        public AbsentHolder(View itemView) {
            super(itemView);
            text_Name= (TextView) itemView.findViewById(R.id.text_empname);
            text_status= (TextView) itemView.findViewById(R.id.text_status);
            text_slno= (TextView) itemView.findViewById(R.id.text_slno);
        }

    }
}
