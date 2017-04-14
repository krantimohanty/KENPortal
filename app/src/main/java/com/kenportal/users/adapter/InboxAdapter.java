package com.kenportal.users.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.R;
import com.kenportal.users.datamodels.InboxModel;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * Created by kranti on 11/26/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    Dialog dialog;
    private List<InboxModel> inboxModels;
    static Context context;
    DbHelper dbHelper;

    public InboxAdapter(Context context, List<InboxModel> inboxModels, RecyclerView recyclerView) {
        this.context = context;
        this.inboxModels = inboxModels;
        dbHelper = new DbHelper(context);
    }

    @Override
    public int getItemViewType(int position) {
        return inboxModels.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        // if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_list_row, parent, false);
        vh = new InboxHolder(v);
       /* } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }*/
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof InboxHolder) {

            //load wish image
            Picasso.with(context)
                    .load(ServerLinks.greetng_img_url + inboxModels.get(position).getPhoto())
                    .placeholder(R.drawable.complaint_image_null)
                    .error(R.drawable.complaint_image_null)
                    .into(((InboxHolder) holder).greeting_card);
            //Load profile image
            Picasso.with(context)
                    .load(ServerLinks.smallPhoto + (dbHelper.getEmpPic(inboxModels.get(position).getSend_by_id())).replace(" ", "%20"))
                    .placeholder(R.drawable.userpic)
                    .error(R.drawable.userpic)
                    .into(((InboxHolder) holder).profile_pic);

            ((InboxHolder) holder).emp_title.setText(dbHelper.getEmpName(inboxModels.get(position).getSend_by_id()));
            ((InboxHolder) holder).emp_designation.setText(dbHelper.getEmpDesignation(inboxModels.get(position).getSend_by_id()));
            ((InboxHolder) holder).emp_location.setText(dbHelper.getEmpLocation(inboxModels.get(position).getSend_by_id()));
            ((InboxHolder) holder).message.setText(inboxModels.get(position).getMessage());

            ((InboxHolder) holder).send_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((InboxHolder) holder).reply.getText().toString().equals("")) {
//                            ((InboxHolder) holder).reply.setError("error");
                        Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Please enter some text.", Snackbar.LENGTH_LONG).show();
                    } else {
                        sendReply(inboxModels.get(position).getGreeting_id(),
                                CustomPreference.with(context).getString("id", ""),
                                inboxModels.get(position).getSend_by_id(),
                                inboxModels.get(position).getWish_type(),
                                ((InboxHolder) holder).reply.getText().toString(),
                                ((InboxHolder) holder).sendReply,
                                ((InboxHolder) holder).replied);
                    }
                }
            });

            if (inboxModels.get(position).getReply_status().equals("0")) {
                ((InboxHolder) holder).sendReply.setVisibility(View.VISIBLE);
                ((InboxHolder) holder).replied.setVisibility(View.GONE);
            } else {
                ((InboxHolder) holder).sendReply.setVisibility(View.GONE);
                ((InboxHolder) holder).replied.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return inboxModels.size();
    }


    public static class InboxHolder extends RecyclerView.ViewHolder {

        public ImageView profile_pic;
        public TextView emp_title;
        public TextView emp_designation;
        public TextView emp_location;
        public TextView message;
        public ImageView greeting_card;
        RelativeLayout send_reply;
        EditText reply;
        TextView status;
        LinearLayout sendReply;
        LinearLayout replied;

        public InboxHolder(View v) {
            super(v);
            profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
            emp_title = (TextView) v.findViewById(R.id.emp_title);
            emp_designation = (TextView) v.findViewById(R.id.emp_designation);
            emp_location = (TextView) v.findViewById(R.id.emp_location);
            message = (TextView) v.findViewById(R.id.text_wish);
            greeting_card = (ImageView) v.findViewById(R.id.greetings);
            send_reply = (RelativeLayout) v.findViewById(R.id.send_reply);
            reply = (EditText) v.findViewById(R.id.reply);
            status = (TextView) v.findViewById(R.id.reply_status);
            sendReply = (LinearLayout) v.findViewById(R.id.sendReply);
            replied = (LinearLayout) v.findViewById(R.id.replied);
        }
    }

    public void sendReply(String greeting_id,
                          String user_id,
                          String to_user_id,
                          String wish_type,
                          String reply_message,
                          final LinearLayout sendRepy,
                          final LinearLayout repld) {

        dialog = new Dialog(context, R.style.Theme_D1NoTitleDim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setTitle("Loading data...");
        dialog.setContentView(R.layout.custom_progress_view);
        dialog.show();

        JSONObject params = new JSONObject();
        try {
            params.put("greeting_id", greeting_id);
            params.put("user_id", user_id);
            params.put("To_user_id", to_user_id);
            params.put("wishType", "Re:"+wish_type);
            params.put("reply_message", reply_message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServerLinks.ServerUrl + "/reply_greetings", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if ((response.getString("result")).equals("success")) {
                                dialog.hide();
                                sendRepy.setVisibility(View.GONE);
                                repld.setVisibility(View.VISIBLE);

                                Snackbar snack = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Message sent successfully", Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                snack.setActionTextColor(Color.WHITE).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.hide();
                    Snackbar snack = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Something went wrong.Please try after sometimes. ", Snackbar.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) snack.getView();
                    group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    snack.setActionTextColor(Color.WHITE).show();
                }
            });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }


}