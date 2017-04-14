package com.kenportal.users.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenportal.users.R;
import com.kenportal.users.UpdatesDetailActivity;
import com.kenportal.users.app_interface.OnLoadMoreListener;
import com.kenportal.users.datamodels.UpdatesModel;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.utils.DateUtil;

import java.util.List;

/**
 * Created by kranti on 11/26/2016.
 */
public class UpdatesAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<UpdatesModel> updatesModels;
    private static Context context;
    public static String s, s1;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    DbHelper dbHelper;




    public UpdatesAdapter(Context context, List<UpdatesModel> updatesModels, RecyclerView recyclerView) {
        this.updatesModels = updatesModels;
        this.context = context;
        dbHelper = new DbHelper(context);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.updates_list_row, parent, false);
            vh = new UpdatesViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UpdatesViewHolder) {

            if (updatesModels.get(position).getRead_status().equalsIgnoreCase("TRUE")) {
                ((UpdatesViewHolder) holder).row_view.setBackgroundColor(Color.parseColor("#FFFFFF"));

            } else if (updatesModels.get(position).getRead_status().equalsIgnoreCase("FALSE")) {
                ((UpdatesViewHolder) holder).row_view.setBackgroundColor(Color.parseColor("#FDF8D7"));
            }
            ((UpdatesViewHolder) holder).ActionName.setText(updatesModels.get(position).getUpdate_subject());
            ((UpdatesViewHolder) holder).ActionEmpName.setText(dbHelper.getEmpName(updatesModels.get(position).getUser_id()));

            if ((Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) >= 1) && (Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) <= 30)) {
                ((UpdatesViewHolder) holder).Actiondays.setText(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim() + " days ago");
            } else if ((Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) >= 31) && (Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) <= 365)) {
                int mnth = Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) / 31;
                if (mnth == 1) {
                    ((UpdatesViewHolder) holder).Actiondays.setText(String.valueOf(mnth) + " month ago");
                } else {
                    ((UpdatesViewHolder) holder).Actiondays.setText(String.valueOf(mnth) + " months ago");
                }

            } else if ((Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) >= 366)) {
                int yr = Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) / 366;
                if (yr == 1) {
                    ((UpdatesViewHolder) holder).Actiondays.setText(String.valueOf(yr) + " year ago");
                } else {
                    ((UpdatesViewHolder) holder).Actiondays.setText(String.valueOf(yr) + " years ago");
                }
            } else if ((Integer.valueOf(DateUtil.getNoDays(updatesModels.get(position).getDate()).replace("-", "").trim()) < 1)) {
                ((UpdatesViewHolder) holder).Actiondays.setText("Today");
            }

            ((UpdatesViewHolder) holder).row_view.setOnClickListener(gotoUpdateDetail(updatesModels.get(position).getUpdate_subject(),updatesModels.get(position).getUpdate_detail(),((UpdatesViewHolder) holder).row_view,updatesModels.get(position).getUpdate_id()));
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return updatesModels.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return updatesModels.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private View.OnClickListener gotoUpdateDetail(final String subject,final String details,final RelativeLayout vw,final String updt_id){
        return  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vw.setBackgroundColor(Color.parseColor("#FFFFFF"));
                context.startActivity(new Intent(context, UpdatesDetailActivity.class).putExtra("topic", subject).putExtra("detail", details).putExtra("updt_id",updt_id));
            }
        };
    }

    public static class UpdatesViewHolder extends RecyclerView.ViewHolder {

        public TextView ActionName;
        public TextView ActionEmpName;
        public TextView Actiondays;
        public UpdatesModel model;
        public RelativeLayout row_view;

        public UpdatesViewHolder(View v) {
            super(v);
            ActionName = (TextView) v.findViewById(R.id.ActionName);
            ActionEmpName = (TextView) v.findViewById(R.id.ActionEmpName);
            Actiondays = (TextView) v.findViewById(R.id.Actiondays);
            row_view=(RelativeLayout) v.findViewById(R.id.row_view);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}