package com.kenportal.users.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenportal.users.R;
import com.kenportal.users.datamodels.WishModel;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.urls.ServerLinks;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kranti on 11/26/2016.
 */
public class UpcomingWishAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<WishModel> wishModels;

    static Context context;
    static String dept;
    DbHelper dbHelper;


    public UpcomingWishAdapter(Context context, List<WishModel> wishModels, RecyclerView recyclerView) {
        this.context = context;
        this.wishModels = wishModels;

        dbHelper = new DbHelper(context);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });*/
        }
    }

    @Override
    public int getItemViewType(int position) {
        return wishModels.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        // if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.upcoming_list_row, parent, false);

        vh = new WishHolder(v);
       /* } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }*/
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WishHolder) {


            //load image
            Picasso.with(context)
                    .load(ServerLinks.smallPhoto + dbHelper.getEmpPic(wishModels.get(position).getUser_id().replace(" ", "%20")))
                    .placeholder(R.drawable.strips)
                    .error(R.drawable.strips)
                    .into(((WishHolder) holder).profile_pic);

            ((WishHolder) holder).emp_title.setText(dbHelper.getEmpName(wishModels.get(position).getUser_id()));
            ((WishHolder) holder).emp_designation.setText(dbHelper.getEmpDesignation(wishModels.get(position).getUser_id()).trim() + ", " + dbHelper.getEmpLocation(wishModels.get(position).getUser_id()));
            ((WishHolder) holder).emp_location.setText(wishModels.get(position).getWish_type());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                Date parsedDate = sdf.parse(wishModels.get(position).getWish_date());
                SimpleDateFormat print = new SimpleDateFormat("dd/MMM");
                System.out.println(print.format(parsedDate));//previous date

                ((WishHolder) holder).date.setText(print.format(parsedDate));
//                dayDifference = get_count_of_days(formattedDate, print.format(parsedDate));
                //Log.d("Difference:-->", formattedDate + " - " + print.format(parsedDate) + " = " + dayDifference);
            } catch (ParseException exception) {
            }


            //((WishHolder) holder).wish_img.setText(wishModels.get(position).getWish_type());

        }

    }


    @Override
    public int getItemCount() {
        return wishModels.size();
    }


    public static class WishHolder extends RecyclerView.ViewHolder {

        public ImageView profile_pic;
        public TextView emp_title;
        public TextView emp_designation;
        public TextView emp_location;
        public TextView date;
        public IconTextView wish_img;

        public WishModel model;

        public WishHolder(View v) {
            super(v);
            profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
            emp_title = (TextView) v.findViewById(R.id.emp_title);
            emp_designation = (TextView) v.findViewById(R.id.emp_designation);
            emp_location = (TextView) v.findViewById(R.id.emp_location);
            date = (TextView) v.findViewById(R.id.emp_upcoming_wish_date);
//            wish_img = (IconTextView) v.findViewById(R.id.wish_img);
        }
    }
}