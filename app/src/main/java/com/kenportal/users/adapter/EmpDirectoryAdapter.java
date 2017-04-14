package com.kenportal.users.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kenportal.users.MessangerActivity;
import com.kenportal.users.utils.ConnectionDetector;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.kenportal.users.R;
import com.kenportal.users.ViewEmployeeDetail;
import com.kenportal.users.app_interface.OnLoadMoreListener;
import com.kenportal.users.local_db.DbModel;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.widgets.RecyclerViewFastScroller;

/**
 * Created by kranti on 11/26/2016.
 */
public class EmpDirectoryAdapter extends RecyclerView.Adapter implements RecyclerViewFastScroller.BubbleTextGetter{
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5; //number of items remain to the recycler before reaching the end
    private List<DbModel> empListModels;
    private List<DbModel> orig;
    Animation myAnimation;
    // Connection detector class
    List<String> idList = new ArrayList<>();
    List<String> statusList = new ArrayList<>();
    List<String> idList1 = new ArrayList<>();
    List<String> statusList1 = new ArrayList<>();
    String userStatus, userId;
    Map empStatusModels = new HashMap();
    // Map<String,String> empStatusModels = new HashMap<>();
    // The minimum amount of items to have below your current scroll position
    private int lastVisibleItem, totalItemCount, firstVisibleInListview;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    public static Context context;

    public EmpDirectoryAdapter(final Map<String, String> empStatusModels, Context ctx) {
        this.empStatusModels = empStatusModels;
        this.context = ctx;
    }

    public EmpDirectoryAdapter(final List<DbModel> empListModels, RecyclerView recyclerView) {
        this.empListModels = empListModels;

//        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//
//                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                        // End has been reached
//                        // Do something
//                        if (onLoadMoreListener != null) {
//                            onLoadMoreListener.onLoadMore();
//                        }
//                        loading = true;
//                    }
//
//                    firstVisibleInListview = linearLayoutManager.findFirstVisibleItemPosition();
//                    int currentFirstVisible = linearLayoutManager.findFirstVisibleItemPosition();
//                    if (currentFirstVisible > firstVisibleInListview) {
//                        for (int i = currentFirstVisible; i <= lastVisibleItem; i++) {
//                            //new GetEmployeeStatusThread(empListModels.get(i).getEmp_id()).execute();
//                        }
//                    }
//                    firstVisibleInListview = currentFirstVisible;
//                }
//            });
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return empListModels.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        context = parent.getContext();
        myAnimation = AnimationUtils.loadAnimation(context, R.anim.popup_in);
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.emp_list_row, parent, false);
            vh = new EmpListViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof EmpListViewHolder) {

            String emp_img = empListModels.get(position).getEmp_photo().replace(" ", "%20");
            //load image
            Picasso.with(context)
                    .load(ServerLinks.smallPhoto + emp_img)
                    .placeholder(R.drawable.strips)
                    .error(R.drawable.strips)
                    .into(((EmpListViewHolder) holder).profile_pic);

            //ArrayList<String> empNameList = new ArrayList<>();
            ((EmpListViewHolder) holder).emp_title.setText(empListModels.get(position).getEmp_name());
            ((EmpListViewHolder) holder).emp_designation.setText(empListModels.get(position).getEmp_designation());
            //((EmpListViewHolder) holder).emp_status.setVisibility(View.GONE);

       /*     if (empListModels.get(position).getEmp_status().equalsIgnoreCase("")) {
                ((EmpListViewHolder) holder).emp_status.setVisibility(View.GONE);
            } else {*/
            ((EmpListViewHolder) holder).emp_status.setText(empListModels.get(position).getEmp_status());

            if (((EmpListViewHolder) holder).emp_status.getText().toString().equalsIgnoreCase("ABSENT")) {
                ((EmpListViewHolder) holder).emp_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_dot, 0, 0, 0);
            } else if (((EmpListViewHolder) holder).emp_status.getText().toString().equalsIgnoreCase("PRESENT")) {
                ((EmpListViewHolder) holder).emp_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_dot, 0, 0, 0);
            } else if (((EmpListViewHolder) holder).emp_status.getText().toString().equalsIgnoreCase("TOUR")) {
                ((EmpListViewHolder) holder).emp_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_dot, 0, 0, 0);
            } else if (((EmpListViewHolder) holder).emp_status.getText().toString().equalsIgnoreCase("MEETING")) {
                ((EmpListViewHolder) holder).emp_status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.pink_dot, 0, 0, 0);
            }else if (((EmpListViewHolder) holder).emp_status.getText().toString().equalsIgnoreCase("LOCAL VISIT")) {
                ((EmpListViewHolder) holder).emp_status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.gray_dot, 0, 0, 0);
            }else if (((EmpListViewHolder) holder).emp_status.getText().toString().equalsIgnoreCase("LEAVE")) {
                ((EmpListViewHolder) holder).emp_status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.blue_dot, 0, 0, 0);
            }
            ((EmpListViewHolder) holder).emp_status.setAnimation(myAnimation);


            // }

            if (ConnectionDetector.staticisConnectingToInternet(context)) {

                if (empStatusModels.size() > 0){
                    Set keys = empStatusModels.keySet();
                Iterator itr = keys.iterator();
                while (itr.hasNext()) {
                    if ((String) itr.next() == empListModels.get(position).getEmp_id()) {
                        ((EmpListViewHolder) holder).emp_status.setText((String) empStatusModels.get((String) itr.next()).toString());
                        ((EmpListViewHolder) holder).emp_status.setAnimation(myAnimation);
                        notifyItemChanged(position);
                    } else {
                        ((EmpListViewHolder) holder).emp_status.setVisibility(View.GONE);
                        notifyItemChanged(position);
                    }
                }
            }

            }
            else {
                ((EmpListViewHolder) holder).emp_status.setVisibility(View.GONE);
            }

            ((EmpListViewHolder) holder).layoutClick.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(context, ((EmpListViewHolder) holder).emp_more);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            // Toast.makeText(context, "You Clicked : " + item.getItemId(), Toast.LENGTH_SHORT).show();
                            switch (item.getItemId()) {
                                case R.id.action_view_detail:
                                    Intent i = new Intent(context, ViewEmployeeDetail.class);
                                    i.putExtra("flag", "emp_dir");
                                    i.putExtra("id", empListModels.get(position).getEmp_id());
                                    context.startActivity(i);
                                    break;
                                case R.id.action_call:
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empListModels.get(position).getMobile()));
                                    ((Activity) context).startActivity(intent);
                                    break;
                                case R.id.action_call_office:
                                    Intent inte = new Intent(Intent.ACTION_DIAL);
                                    inte.setData(Uri.parse("tel:" + empListModels.get(position).getEmp_pre_tel_o()));
                                    ((Activity) context).startActivity(inte);
                                    break;
                                case R.id.action_sms:
//                                    sendSMS(empListModels.get(position).getMobile(), "");
//                                    Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
//                                    intentsms.putExtra( "sms_body", "" );
//                                    ((Activity) context).startActivity(intentsms);
                                    ((Activity) context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", empListModels.get(position).getMobile(), null)));
                                    break;
                                case R.id.action_email:
                                    Log.i("atag","email");
                                    Intent emailIntent = new Intent(
                                            android.content.Intent.ACTION_SEND);
                                    emailIntent.setAction(Intent.ACTION_SEND);
                                    emailIntent.setType("message/rfc822");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                            new String[]{empListModels.get(position).getEmail()});
                                    emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_BCC, "");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                            "");
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                    emailIntent.setType("text/html");

                                    // FOLLOWING STATEMENT CHECKS WHETHER THERE IS ANY APP THAT CAN HANDLE OUR EMAIL INTENT
                                    ((Activity) context).startActivity(Intent.createChooser(emailIntent,
                                            "Send Email Using: "));

                                    break;
                                case R.id.action_chat:
                                    ((Activity)context).startActivity(new Intent(context, MessangerActivity.class));
                                    break;
                                case R.id.action_send_request_for_location:
                                    break;
                                case R.id.action_add_to_phn_contct:
                                    break;
                            }
                            return true;
                        }
                    });
                    try {
                        Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
                        mFieldPopup.setAccessible(true);
                        MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                        mPopup.setForceShowIcon(true);
                    } catch (Exception e) {
                    }
                    popup.show();

                    return false;
                }
            });

            ((EmpListViewHolder) holder).layoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ViewEmployeeDetail.class);
                    i.putExtra("flag", "emp_dir");
                    i.putExtra("id", empListModels.get(position).getEmp_id());
                    context.startActivity(i);
                }
            });


            ((EmpListViewHolder) holder).emp_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, ((EmpListViewHolder) holder).emp_more);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            // Toast.makeText(context, "You Clicked : " + item.getItemId(), Toast.LENGTH_SHORT).show();
                            switch (item.getItemId()) {
                                case R.id.action_view_detail:
                                    Intent i = new Intent(context, ViewEmployeeDetail.class);
                                    i.putExtra("flag", "emp_dir");
                                    i.putExtra("id", empListModels.get(position).getEmp_id());
                                    context.startActivity(i);
                                    break;
                                case R.id.action_call:
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + empListModels.get(position).getMobile()));
                                    ((Activity) context).startActivity(intent);
                                    break;
                                case R.id.action_call_office:
                                    Intent inte = new Intent(Intent.ACTION_DIAL);
                                    inte.setData(Uri.parse("tel:" + empListModels.get(position).getEmp_pre_tel_o()));
                                    ((Activity) context).startActivity(inte);
                                    break;
                                case R.id.action_sms:
//                                    sendSMS(empListModels.get(position).getMobile(), "");
//                                    Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
//                                    intentsms.putExtra( "sms_body", "" );
//                                    ((Activity) context).startActivity(intentsms);

                                    ((Activity) context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", empListModels.get(position).getMobile(), null)));
                                    break;
                                case R.id.action_email:
                                    Intent emailIntent = new Intent(
                                            android.content.Intent.ACTION_SEND);
                                    emailIntent.setAction(Intent.ACTION_SEND);
                                    emailIntent.setType("message/rfc822");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                            new String[]{empListModels.get(position).getEmail()});
                                    emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_BCC, "");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                            "");
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                    emailIntent.setType("text/html");

                                    // FOLLOWING STATEMENT CHECKS WHETHER THERE IS ANY APP THAT CAN HANDLE OUR EMAIL INTENT
                                    ((Activity) context).startActivity(Intent.createChooser(emailIntent,
                                            "Send Email Using: "));

                                    break;
                                case R.id.action_chat:
                                    ((Activity)context).startActivity(new Intent(context, MessangerActivity.class));
                                    break;
                                case R.id.action_send_request_for_location:
                                    break;
                                case R.id.action_add_to_phn_contct:
                                    break;
                            }
                            return true;
                        }
                    });
                    try {
                        Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
                        mFieldPopup.setAccessible(true);
                        MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                        mPopup.setForceShowIcon(true);
                    } catch (Exception e) {
                    }
                    popup.show();
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public String getTextToShowInBubble(final int pos) {
        String s = "";
        try {
            s = Character.toString(empListModels.get(pos).getEmp_name().charAt(0));
        } catch (Exception e) {

        }
        return s;
    }

    @Override
    public int getItemCount() {
        return empListModels.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class EmpListViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_pic;
        public TextView emp_title;
        public TextView emp_designation;
        public TextView emp_status;
        public IconTextView emp_more;
        public DbModel model;
        public LinearLayout layoutClick;

        public EmpListViewHolder(View v) {
            super(v);
            profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
            emp_title = (TextView) v.findViewById(R.id.emp_title);
            emp_designation = (TextView) v.findViewById(R.id.emp_designation);
            emp_status = (TextView) v.findViewById(R.id.emp_status);
            emp_more = (IconTextView) v.findViewById(R.id.emp_more);
            layoutClick = (LinearLayout) v.findViewById(R.id.layoutClick);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public void setModels(List<DbModel> models) {
        empListModels = null;
        empListModels = new ArrayList<>(models);
    }

    public void animateTo(List<DbModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<DbModel> newModels) {
        for (int i = empListModels.size() - 1; i >= 0; i--) {
            final DbModel model = empListModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<DbModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final DbModel model = newModels.get(i);
            if (!empListModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<DbModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final DbModel model = newModels.get(toPosition);
            final int fromPosition = empListModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public DbModel removeItem(int position) {
        final DbModel model = empListModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, DbModel model) {
        empListModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final DbModel model = empListModels.remove(fromPosition);
        empListModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    //get employee status


    public void getStatusList(List<String> i, List<String> s) {
        idList1 = i;
        statusList1 = s;
    }

//    private void sendSMS(String phoneNumber, String message) {
//        String SENT = "SMS_SENT";
//        String DELIVERED = "SMS_DELIVERED";
//
//        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
//                new Intent(SENT), 0);
//
//        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
//                new Intent(DELIVERED), 0);
//
//        //---when the SMS has been sent---
//        context.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(context, "SMS sent",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(context, "Generic failure",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(context, "No service",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(context, "Null PDU",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Toast.makeText(context, "Radio off",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(SENT));
//
//        //---when the SMS has been delivered---
//        context.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(context, "SMS delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(context, "SMS not delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(DELIVERED));
//
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
//    }

}