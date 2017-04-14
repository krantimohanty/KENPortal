package com.kenportal.users.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kenportal.users.R;
import com.kenportal.users.WebViewActivity;
import com.kenportal.users.datamodels.ActionModel;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.DateUtil;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by kranti on 11/26/2016.
 */
public class ActionAdapter extends RecyclerView.Adapter {
    private List<ActionModel> actionModels;
    private Context context;
    DbHelper dbHelper;
    String s;


    public ActionAdapter(Context context, List<ActionModel> actionModels, RecyclerView recyclerView) {
        this.context = context;
        this.actionModels = actionModels;
        dbHelper = new DbHelper(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.actions_pending_list, parent, false);
        vh = new ActionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActionViewHolder) {
            //load image
            Picasso.with(context)
                    .load(ServerLinks.smallPhoto + dbHelper.getEmpPic(actionModels.get(position).getUser_id().replace(" ", "%20")))
                    .placeholder(R.drawable.strips)
                    .error(R.drawable.strips)
                    .into(((ActionViewHolder) holder).profile_pic);

            ((ActionViewHolder) holder).setUrl(actionModels.get(position).getAction_url());
            ((ActionViewHolder) holder).setSubject(actionModels.get(position).getAction_subject());
            ((ActionViewHolder) holder).actionName.setText(actionModels.get(position).getAction_subject());
            ((ActionViewHolder) holder).actionEmpName.setText("By " + dbHelper.getEmpName(actionModels.get(position).getUser_id()));

            //setting action duration
            if ((Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) >= 1) && (Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) <= 30)) {
                ((ActionViewHolder) holder).actionDays.setText(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim() + " days ago");
            } else if ((Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) >= 31) && (Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) <= 365)) {
                int mnth = Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) / 31;
                if (mnth == 1) {
                    ((ActionViewHolder) holder).actionDays.setText(String.valueOf(mnth) + " month ago");
                } else {
                    ((ActionViewHolder) holder).actionDays.setText(String.valueOf(mnth) + " months ago");
                }
            } else if ((Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) >= 366)) {
                int yr = Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) / 366;
                if (yr == 1) {
                    ((ActionViewHolder) holder).actionDays.setText(String.valueOf(yr) + " year ago");
                } else {
                    ((ActionViewHolder) holder).actionDays.setText(String.valueOf(yr) + " years ago");
                }
            } else if ((Integer.valueOf(DateUtil.getNoDays(actionModels.get(position).getDate()).replace("-", "").trim()) < 1)) {
                ((ActionViewHolder) holder).actionDays.setText("Today");
            }

            if (actionModels.get(position).getAction_subject().contains("Late")) {
                ((ActionViewHolder) holder).indicator.setBackgroundColor(context.getResources().getColor(R.color.late));
            } else if (actionModels.get(position).getAction_subject().contains("Meeting")) {
                ((ActionViewHolder) holder).indicator.setBackgroundColor(context.getResources().getColor(R.color.meeting));
            } else if (actionModels.get(position).getAction_subject().contains("Tour")) {
                ((ActionViewHolder) holder).indicator.setBackgroundColor(context.getResources().getColor(R.color.tour));
            } else if (actionModels.get(position).getAction_subject().contains("Leave")) {
                ((ActionViewHolder) holder).indicator.setBackgroundColor(context.getResources().getColor(R.color.leave));
            }else if (actionModels.get(position).getAction_subject().contains("Training")) {
                ((ActionViewHolder) holder).indicator.setBackgroundColor(context.getResources().getColor(R.color.training));
            }else{
                ((ActionViewHolder) holder).indicator.setBackgroundColor(context.getResources().getColor(R.color.other));
            }
        }
    }

    @Override
    public int getItemCount() {
        return actionModels.size();
    }


    public class ActionViewHolder extends RecyclerView.ViewHolder {

        public TextView actionName;
        public TextView actionEmpName;
        public TextView actionDays;
        View indicator;
        String url;
        String subject;
        ImageView profile_pic;
        public ActionModel model;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ActionViewHolder(View v) {
            super(v);


            actionName = (TextView) v.findViewById(R.id.ActionName);
            actionEmpName = (TextView) v.findViewById(R.id.ActionEmpName);
            actionDays = (TextView) v.findViewById(R.id.Actiondays);
            profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
            indicator = v.findViewById(R.id.indicator);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, WebViewActivity.class).putExtra("url", getUrl()).putExtra("sub", getSubject()));
                    ((Activity)context).finish();
                }
            });
        }
    }
}