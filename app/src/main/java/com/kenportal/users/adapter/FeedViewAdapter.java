package com.kenportal.users.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.kenportal.users.R;
import com.kenportal.users.app_interface.OnLoadMoreListener;
import com.kenportal.users.datamodels.FeedModel;
/**
 * Created by kranti on 11/26/2016.
 */
public class FeedViewAdapter extends RecyclerView.Adapter {
    private ArrayList<FeedModel> mDataset;
    Context c;
    RecyclerView rv;
    private OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private int visibleThreshold = 5; //number of items remain to the recycler before reaching the end
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    final Animation animScale;

    public FeedViewAdapter(Context con, ArrayList<FeedModel> myDataset, RecyclerView recyclerView) {
        mDataset = myDataset;
        rv = recyclerView;
        c = con;
        animScale = AnimationUtils.loadAnimation(c, R.anim.anim_scale);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_feed, parent, false);
        vh = new RecentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecentViewHolder) {
            try {
                String stry = mDataset.get(position).getStory();
                if (stry == null) {
                    ((RecentViewHolder) holder).txtCompUser.setText("Swash Convergence Technologies Pvt. Ltd.");
                } else {
                    ((RecentViewHolder) holder).txtCompUser.setText(stry);
                }
                //((RecentViewHolder) holder).txtCompUser.setText(stry);
                //getting time and formatting it
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                Date date = dateFormat.parse(mDataset.get(position).getCreated_time());
                dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String formatedDate = dateFormat.format(date);
                ((RecentViewHolder) holder).txtTime.setText(formatedDate);

                ((RecentViewHolder) holder).txtCompDetail.setText(mDataset.get(position).getMessage());

                Picasso.with(c)
                        .load(mDataset.get(position).getFull_picture())
                        .placeholder(R.drawable.strips)
                        .error(R.drawable.strips)
                        .into(((RecentViewHolder) holder).imgCompImage);

//                ((RecentViewHolder) holder).card_view.setOnClickListener(cardClickListener(mDataset.get(position).getLink()));

                ((RecentViewHolder) holder).layoutLike.setOnClickListener(likeClickListener(((RecentViewHolder) holder).txtLike,mDataset.get(position).getId()));

                ((RecentViewHolder) holder).layoutComment.setOnClickListener(commentClickListener());


            } catch (Exception e) {
            }
        }

    }

    /*Like Click Listener*/

    private View.OnClickListener likeClickListener(final TextView viw,final String ob_id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://android-er.blogspot.in/2012/02/apply-animation-on-button.html
                viw.startAnimation(animScale);
                /* make the API call */
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/"+ob_id+"/likes",
                        null,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                /* handle the result */
                            }
                        }
                ).executeAsync();

            }
        };
    }
    /*Like Click Listener*/

    private View.OnClickListener commentClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://android-er.blogspot.in/2012/02/apply-animation-on-button.html
//                v.startAnimation(animScale);
            }
        };
    }


    /*Card Click Listener*/
    private View.OnClickListener cardClickListener(final String link) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    ((Activity) c).startActivity(intent);

                } catch (Exception e) {
                }
            }
        };
    }

//    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
//        this.onLoadMoreListener = onLoadMoreListener;
//    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        int retInt = mDataset.get(position) != null ? VIEW_ITEM : VIEW_PROG;
//        //   Log.i("RTAG", mDataset.get(position) + "-"+position);
//        return retInt;
//    }
//
//    public void setLoaded() {
//        loading = false;
//    }

    public static class RecentViewHolder extends RecyclerView.ViewHolder {
        TextView txtCompDetail;
        TextView txtCompUser;
        TextView txtTime;
        TextView txtCompArea;
        TextView txtLikeCount;
        TextView txt_comment_count;
        ImageView imgUserimage;
        ImageView imgCompImage;
        LinearLayout layoutShare;
        LinearLayout layoutLike;
        LinearLayout layoutComment;
        LinearLayout layoutSupportShare;
        TextView txtClapIcon;
        TextView txtClap;
        LinearLayout layoutClap;
        LinearLayout layoutResolveAction;
        TextView txtUpdatedBy;
        TextView txtUpdatedOn;
        TextView txtResolvedRemark;
        //        TextView txtideaCatagory;
        LinearLayout layout_comment;
        LinearLayout ideaImgLayout;
        FrameLayout card_view;
//        Button btnScale;
        TextView textComment;
        TextView txtLike;
        //LinearLayout cardLayout;

        public RecentViewHolder(View itemView) {
            super(itemView);
            txtCompUser = (TextView) itemView.findViewById(R.id.txtCompUser);
            txtCompDetail = (TextView) itemView.findViewById(R.id.txtCompDetail);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
//            txtCompArea = (TextView) itemView.findViewById(R.id.txtCompArea);
//            txtLikeCount = (TextView) itemView.findViewById(R.id.txtLikeCount);
//            txt_comment_count = (TextView) itemView.findViewById(R.id.txt_comment_count);
            layoutShare = (LinearLayout) itemView.findViewById(R.id.layoutShare);
            layoutLike = (LinearLayout) itemView.findViewById(R.id.layoutLike);
            layoutComment = (LinearLayout) itemView.findViewById(R.id.layoutComment);
//            layoutSupportShare = (LinearLayout) itemView.findViewById(R.id.layout_support_share_bar);
//            layoutClap = (LinearLayout) itemView.findViewById(R.id.layout_clap);
//            txtClapIcon = (TextView) itemView.findViewById(R.id.txt_clap_icon);
//            txtClap = (TextView) itemView.findViewById(R.id.txt_clap);
           /* layoutResolveAction = (LinearLayout) itemView.findViewById(R.id.layout_resolve_action);
            txtUpdatedBy = (TextView) itemView.findViewById(R.id.txt_updated_by);
            txtUpdatedOn = (TextView) itemView.findViewById(R.id.txt_updated_on);
            txtResolvedRemark = (TextView) itemView.findViewById(R.id.txt_resolve_remark);*/
//            imgUserimage = (ImageView) itemView.findViewById(R.id.imgUserimage);
            imgCompImage = (ImageView) itemView.findViewById(R.id.imgCompImage);
//            txtLike = (IconTextView) itemView.findViewById(R.id.txtLike);
////            txtideaCatagory = (TextView) itemView.findViewById(R.id.txtideaCatagory);
//            layout_comment = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            ideaImgLayout = (LinearLayout) itemView.findViewById(R.id.ideaImgLayout);
            card_view = (FrameLayout) itemView.findViewById(R.id.card_view);
            //cardLayout = (LinearLayout) itemView.findViewById(R.id.cardLayout);
            textComment=(TextView)itemView.findViewById(R.id.textComment);

            txtLike = (TextView) itemView.findViewById(R.id.txtLike);
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



