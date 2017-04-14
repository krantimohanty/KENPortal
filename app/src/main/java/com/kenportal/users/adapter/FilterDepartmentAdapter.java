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
import java.util.List;
import com.kenportal.users.EmployeeDirectoryActivity;
import com.kenportal.users.R;
import com.kenportal.users.datamodels.DepartmentModel;
import com.kenportal.users.local_db.DbHelper;

/**
 * Created by kranti on 11/26/2016.
 */
public class FilterDepartmentAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<DepartmentModel> departmentModels;
    static Context context;
    static String dept;
    DbHelper dbHelper;

    public FilterDepartmentAdapter(Context c, List<DepartmentModel> departmentModels, RecyclerView recyclerView) {
        context = c;
        this.departmentModels = departmentModels;
        dbHelper = new DbHelper(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_wise_list_row, parent, false);
        vh = new DepartmentHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DepartmentHolder) {
            ((DepartmentHolder) holder).locationName.setText(departmentModels.get(position).getName()+" ("+departmentModels.get(position).getLocation()+")");
            ((DepartmentHolder) holder).empNo.setText(departmentModels.get(position).getCount());
            ((DepartmentHolder) holder).fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((departmentModels.get(position).getCount()).equals("0")) {
                        Snackbar snack = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "No employees in this department.", Snackbar.LENGTH_LONG);
                        ViewGroup group = (ViewGroup) snack.getView();
                        group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        snack.setActionTextColor(Color.WHITE).show();
                    } else {
                        Intent i = new Intent(context, EmployeeDirectoryActivity.class);
                        i.putExtra("dept_id", departmentModels.get(position).getId());
                        i.putExtra("dept_name", departmentModels.get(position).getName());
                        i.putExtra("filter_flag", "department");
                        ((Activity)context).finish();
                        context.startActivity(i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return departmentModels.size();
    }

    public static class DepartmentHolder extends RecyclerView.ViewHolder {
        public TextView locationName;
        public TextView empNo;
        public RelativeLayout fullLayout;
        public DepartmentHolder(View v) {
            super(v);
            locationName = (TextView) v.findViewById(R.id.locationName);
            empNo = (TextView) v.findViewById(R.id.locationEmpNo);
            fullLayout = (RelativeLayout) v.findViewById(R.id.fullLayout);
        }
    }
}