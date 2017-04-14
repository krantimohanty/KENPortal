package com.kenportal.users.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.kenportal.users.MapsActivity;
import com.kenportal.users.R;
import com.kenportal.users.local_db.DbModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.progress_dialog.AppProgressDialog;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;

/**
 * Created by kranti on 11/26/2016.
 */
public class EmployeeListAdapter extends RecyclerView.Adapter {
    private List<DbModel> mDataset;
    private ImageLoader mImageLoader;
    ArrayList<String> arr_emp_ids=new ArrayList<String>();
    LinearLayout buttonLayout;
    Button ok_btn;
    AppProgressDialog appProgressDialog;
    String id;
    String employee_ids = "";
    Context c;
    SharedPreferences user_prefs;
    String flg;

    public EmployeeListAdapter(List<DbModel> myDataset, RecyclerView recyclerView,LinearLayout lyt,Button btn,Context context) {
        mDataset=myDataset;
        buttonLayout=lyt;
        ok_btn=btn;
        c=context;
        user_prefs = c.getSharedPreferences("MAP_USER_FLAG", Context.MODE_PRIVATE);
        flg = user_prefs.getString("user_check_flsg", "");
        if(!flg.equals("")){
            String[] user_ids= flg.split(",");
            for(int i=0;i<user_ids.length;i++){
                arr_emp_ids.add(user_ids[i]);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_listitem, parent, false);
        vh = new UserListViewHolder(v);
        appProgressDialog = new AppProgressDialog(parent.getContext(),"Sharing...");
        id = CustomPreference.with(parent.getContext()).getString("id", "");
        mImageLoader = VolleySingleton.getInstance().getImageLoader();
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserListViewHolder) {
            ((UserListViewHolder)holder).txtName.setText(ServerLinks.getSentencecaseString(mDataset.get(position).getEmp_name()));
            ((UserListViewHolder)holder).txtDesig.setText(mDataset.get(position).getEmp_designation());
            ((UserListViewHolder)holder).checkBox.setTag(mDataset.get(position).getEmp_id());
            if(arr_emp_ids.size()>0){
                for(int i=0;i<arr_emp_ids.size();i++){
                    if(arr_emp_ids.contains(((UserListViewHolder)holder).checkBox.getTag().toString())){
                        ((UserListViewHolder)holder).checkBox.setChecked(true);
                    }else{
                        ((UserListViewHolder)holder).checkBox.setChecked(false);
                    }
                }

            }
            String emp_img = (mDataset.get(position).getEmp_photo()).replace(" ", "%20");
            String pic_url=ServerLinks.smallPhoto+emp_img;
            //load image
            Picasso.with(c)
                    .load(pic_url)
                    .placeholder(R.drawable.strips)
                    .error(R.drawable.strips)
                    .into(((UserListViewHolder) holder).imgUserImage);

            ((UserListViewHolder)holder).checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    employee_ids = "";
                    CheckBox cb = (CheckBox) v;
                    String emp_ids = cb.getTag().toString();
                    if(cb.isChecked()) {
                        if(!arr_emp_ids.contains(emp_ids)) {
                            arr_emp_ids.add(emp_ids);
                        }
                        for (String ids : arr_emp_ids) {
                            employee_ids = employee_ids + "," + ids;
                        }
                        employee_ids = employee_ids.substring(1);
                        SharedPreferences.Editor editor = user_prefs.edit();
                        editor.putString("user_check_flsg", employee_ids);
                        editor.commit();
                    }else{
                        employee_ids = "";
                        arr_emp_ids.remove(emp_ids);
                        for (String ids : arr_emp_ids) {
                            employee_ids = employee_ids + "," + ids;
                        }
                       try {
                           employee_ids = employee_ids.substring(1);
                       }catch (IndexOutOfBoundsException e){
                           employee_ids = "";
                       }

                        SharedPreferences.Editor editor = user_prefs.edit();
                        editor.putString("user_check_flsg", employee_ids);
                        editor.commit();
                    }
                    if(arr_emp_ids.size()>0){
                        buttonLayout.setVisibility(View.VISIBLE);
                    }else{
                        buttonLayout.setVisibility(View.GONE);
                    }
                }
            });
            ok_btn.setOnClickListener(shareSpecificUser());
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private View.OnClickListener shareSpecificUser(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (String ids : arr_emp_ids) {
                    employee_ids = employee_ids + "," + ids;
                }
                employee_ids = employee_ids.substring(1);
                appProgressDialog.initializeProgress();
                String url = ServerLinks.ServerUrl + "/share_employee_location";
                JSONObject params = new JSONObject();
                try {
                    params.put("user_id", id);
                    params.put("user_geolocation", ServerLinks.latLong);
                    params.put("to_user_id", employee_ids);
                } catch (Exception e) {
                }
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                appProgressDialog.hideProgressDialog();
                                try {
                                    String res = response.getString("result");
                                    if (res.equals("success")) {
//                                        SharedPreferences.Editor editor = user_prefs.edit();
//                                        editor.putString("user_check_flsg", employee_ids);
//                                        editor.commit();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.AppCompatAlertDialogStyle);
//                                        builder.setTitle("Logout");
                                        builder.setMessage("You are successfully shared your location to selected employees.");
//                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ((Activity)c).finish();

                                            }
                                        });

                                        builder.show();

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.AppCompatAlertDialogStyle);
//                                        builder.setTitle("Logout");
                                        builder.setMessage("Something went wrong.Please try after sometimes.");
//                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ((Activity)c).finish();
                                            }
                                        });
                                        builder.show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                      AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.AppCompatAlertDialogStyle);
//                                        builder.setTitle("Logout");
                                    builder.setMessage("Something went wrong.Please try after sometimes.");
//                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((Activity)c).finish();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appProgressDialog.hideProgressDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.AppCompatAlertDialogStyle);
//                                        builder.setTitle("Logout");
                        builder.setMessage("Something went wrong.Please try after sometimes.");
//                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent=new Intent(((Activity)c),MapsActivity.class);
                                ((Activity)c).finish();
                                ((Activity)c).startActivity(intent);

                            }
                        });
                        builder.show();
                    }
                });
                //Retry policy of request queue
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "share_specific_location");
            }
        };
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgUserImage;
        TextView txtName;
        TextView txtDesig;
        CheckBox checkBox;
        public UserListViewHolder(View itemView) {
            super(itemView);
            imgUserImage=(ImageView)itemView.findViewById(R.id.imgUserImage);
            txtName=(TextView)itemView.findViewById(R.id.txtName);
            txtDesig=(TextView)itemView.findViewById(R.id.txtDesig);
            checkBox=(CheckBox)itemView.findViewById(R.id.checkBox);
        }
    }

    public void setModels(List<DbModel> models) {
        mDataset=null;
        mDataset = new ArrayList<>(models);
    }

    public DbModel removeItem(int position) {
        final DbModel model = mDataset.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, DbModel model) {
        mDataset.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final DbModel model = mDataset.remove(fromPosition);
        mDataset.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<DbModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<DbModel> newModels) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final DbModel model = mDataset.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<DbModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final DbModel model = newModels.get(i);
            if (!mDataset.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<DbModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final DbModel model = newModels.get(toPosition);
            final int fromPosition = mDataset.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


}




