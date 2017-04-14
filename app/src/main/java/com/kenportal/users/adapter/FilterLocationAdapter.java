package com.kenportal.users.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kenportal.users.EmployeeDirectoryActivity;
import com.kenportal.users.R;
import com.kenportal.users.datamodels.LocationModel;
import com.kenportal.users.local_db.DbHelper;
import java.util.ArrayList;

/**
 * Created by kranti on 11/26/2016.
 */
public class FilterLocationAdapter extends RecyclerView.Adapter {

    private ArrayList<LocationModel> locationtModels;
    static Context context;
    DbHelper dbHelper;

    public FilterLocationAdapter(Context c, ArrayList<LocationModel> locData, RecyclerView recyclerView) {
        this.locationtModels = locData;
        context = c;
        dbHelper = new DbHelper(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_wise_list_row, parent, false);
        vh = new LocationHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LocationHolder) {

            ((LocationHolder) holder).locationName.setText(locationtModels.get(position).getName());
            ((LocationHolder) holder).empNo.setText(locationtModels.get(position).getCount());
            ((LocationHolder) holder).fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((locationtModels.get(position).getCount()).equals("0")) {
                        Snackbar snack = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "No employees in this location.", Snackbar.LENGTH_LONG);
                        ViewGroup group = (ViewGroup) snack.getView();
                        group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        snack.setActionTextColor(Color.WHITE).show();
                    } else {
                        Intent i = new Intent(context, EmployeeDirectoryActivity.class);
                        i.putExtra("loc_id", locationtModels.get(position).getId());
                        i.putExtra("loc_name", locationtModels.get(position).getName());
                        i.putExtra("filter_flag", "location");
                        ((Activity) context).finish();
                        context.startActivity(i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return locationtModels.size();
    }

    public static class LocationHolder extends RecyclerView.ViewHolder {

        public TextView locationName;
        public TextView empNo;
        public LocationModel model;
        public RelativeLayout fullLayout;

        public LocationHolder(View v) {
            super(v);
            locationName = (TextView) v.findViewById(R.id.locationName);
            empNo = (TextView) v.findViewById(R.id.locationEmpNo);
            fullLayout = (RelativeLayout) v.findViewById(R.id.fullLayout);
        }
    }
}