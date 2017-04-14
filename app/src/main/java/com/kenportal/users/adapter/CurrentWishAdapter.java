package com.kenportal.users.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.BestWishActivity;
import com.kenportal.users.R;
import com.kenportal.users.datamodels.TodayWishModel;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.widgets.AutoFitEditText;
import com.kenportal.users.widgets.AutofitEditTextUtil;
import com.kenportal.users.widgets.FoldableLayout;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by kranti on 11/26/2016.
 */
public class CurrentWishAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private Map<Integer, Boolean> mFoldStates = new HashMap<>();
    private List<TodayWishModel> wishModels;
    static Context context;
    DbHelper dbHelper;

    public CurrentWishAdapter(Context context, List<TodayWishModel> wishModels, RecyclerView recyclerView) {
        this.context = context;
        this.wishModels = wishModels;
        dbHelper = new DbHelper(context);
    }

    @Override
    public int getItemViewType(int position) {
        return wishModels.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WishHolder(new FoldableLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof WishHolder ) {
            //load image
            Picasso.with(context)
                    .load(ServerLinks.smallPhoto + dbHelper.getEmpPic(wishModels.get(position).getUser_id().replace(" ", "%20")))
                    .placeholder(R.drawable.userpic)
                    .error(R.drawable.userpic)
                    .into(((WishHolder) holder).profile_pic);

            ((WishHolder) holder).emp_title.setText(dbHelper.getEmpName(wishModels.get(position).getUser_id()));
            ((WishHolder) holder).emp_designation.setText(dbHelper.getEmpDesignation(wishModels.get(position).getUser_id()) + ", " + dbHelper.getEmpLocation(wishModels.get(position).getUser_id()));
            ((WishHolder) holder).emp_location.setText(wishModels.get(position).getWish_type());
            ((WishHolder) holder).emp_title1.setText(dbHelper.getEmpName(wishModels.get(position).getUser_id()));

            if (CustomPreference.with(context).getString("uid", "").equals(wishModels.get(position).getUser_id())) {

                String stringArray = CustomPreference.with(context).getString("pic", null);
                if (stringArray != null) {
                    String[] split = stringArray.substring(1, stringArray.length() - 1).split(", ");
                    byte[] array = new byte[split.length];
                    for (int i = 0; i < split.length; i++) {
                        array[i] = Byte.parseByte(split[i]);
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
                    ((WishHolder) holder).img.setImageBitmap(bitmap);
                    ((WishHolder) holder).img_detail.setImageBitmap(bitmap);
                }

                ((WishHolder) holder).edit_wish.setText(CustomPreference.with(context).getString("wish_msg", ""));
                ((WishHolder) holder).edit_wish.setEnabled(false);
                ((WishHolder) holder).status.setVisibility(View.VISIBLE);
                ((WishHolder) holder).send_text.setText("{fa-check-circle} Greetings already sent.");
                ((WishHolder) holder).send.setEnabled(false);
                ((WishHolder) holder).sendPicFromCam.setVisibility(View.GONE);
                ((WishHolder) holder).sendPicFromCam.setEnabled(false);
                ((WishHolder) holder).sendPicFromGallery.setVisibility(View.GONE);
                ((WishHolder) holder).sendPicFromGallery.setEnabled(false);

            } else {
                ((WishHolder) holder).status.setVisibility(View.GONE);
                if (wishModels.get(position).getWish_type().contains("HAPPY BIRTH DAY")) {
                    ((WishHolder) holder).img.setImageResource(R.drawable.birthday);
                    ((WishHolder) holder).img_detail.setImageResource(R.drawable.birthday);
                    ((WishHolder) holder).edit_wish.setText("Happy Birthday");
                } else if (wishModels.get(position).getWish_type().contains("ANNIVERSARY")) {
                    ((WishHolder) holder).img.setImageResource(R.drawable.anniversary);
                    ((WishHolder) holder).img_detail.setImageResource(R.drawable.anniversary);
                    ((WishHolder) holder).edit_wish.setText("Happy Anniversery");
                } else {
                    ((WishHolder) holder).img.setImageResource(R.drawable.other_wish);
                    ((WishHolder) holder).img_detail.setImageResource(R.drawable.other_wish);
                    ((WishHolder) holder).edit_wish.setText("Best Wishes");
                }
            }

            ((WishHolder) holder).send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((WishHolder) holder).send_text.setText("Sending...");
                        Bitmap bitmap = ((BitmapDrawable) ((WishHolder) holder).img_detail.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                        byte[] b = stream.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        sendGreeting(((WishHolder) holder).mFoldableLayout, CustomPreference.with(context).getString("id", ""), wishModels.get(position).getUser_id(), ((WishHolder) holder).edit_wish.getText().toString(), encodedImage, ((WishHolder) holder).send_text, ((WishHolder) holder).status, ((WishHolder) holder).send, ((WishHolder) holder).sendPicFromCam, ((WishHolder) holder).sendPicFromGallery, wishModels.get(position).getWish_type(),b);
                    } catch (Exception e) {
                    }
                }
            });

            // Bind state
            if (mFoldStates.containsKey(position)) {
                if (mFoldStates.get(position) == Boolean.TRUE) {
                    if (!((WishHolder) holder).mFoldableLayout.isFolded()) {
                        ((WishHolder) holder).mFoldableLayout.foldWithoutAnimation();
                    }
                } else if (mFoldStates.get(position) == Boolean.FALSE) {
                    if (((WishHolder) holder).mFoldableLayout.isFolded()) {
                        ((WishHolder) holder).mFoldableLayout.unfoldWithoutAnimation();
                    }
                }
            } else {
                ((WishHolder) holder).mFoldableLayout.foldWithoutAnimation();
            }

            ((WishHolder) holder).mFoldableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((WishHolder) holder).mFoldableLayout.isFolded()) {
                        ((WishHolder) holder).mFoldableLayout.unfoldWithAnimation();
                    } else {
                        ((WishHolder) holder).mFoldableLayout.foldWithAnimation();
                    }
                }
            });
            ((WishHolder) holder).mFoldableLayout.setFoldListener(new FoldableLayout.FoldListener() {
                @Override
                public void onUnFoldStart() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((WishHolder) holder).mFoldableLayout.setElevation(5);
                    }
                }

                @Override
                public void onUnFoldEnd() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((WishHolder) holder).mFoldableLayout.setElevation(0);
                    }
                    mFoldStates.put(holder.getAdapterPosition(), false);
                }

                @Override
                public void onFoldStart() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((WishHolder) holder).mFoldableLayout.setElevation(5);
                    }
                }

                @Override
                public void onFoldEnd() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((WishHolder) holder).mFoldableLayout.setElevation(0);
                    }
                    mFoldStates.put(holder.getAdapterPosition(), true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return wishModels.size();
    }

    public static class WishHolder extends RecyclerView.ViewHolder {
        FoldableLayout mFoldableLayout;
        public ImageView profile_pic;
        public ImageView img_detail;
        public TextView emp_title;
        public TextView emp_designation;
        public TextView emp_location;
        public TextView emp_title1;
        public ImageView img;
        LinearLayout sendPicFromCam;
        LinearLayout sendPicFromGallery;
        AutoFitEditText edit_wish;
        LinearLayout send;
        TextInputLayout mRootView;
        IconTextView send_text;
        IconTextView status;
        String s;
        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public TodayWishModel model;

        public WishHolder(FoldableLayout foldableLayout) {
            super(foldableLayout);
            mFoldableLayout = foldableLayout;
            foldableLayout.setupViews(R.layout.list_item_cover, R.layout.list_item_detail, R.dimen.card_cover_height, itemView.getContext());
            img = (ImageView) foldableLayout.findViewById(R.id.imagebg);
            img_detail = (ImageView) foldableLayout.findViewById(R.id.imageview_detail);
            profile_pic = (ImageView) foldableLayout.findViewById(R.id.profile_pic);
            emp_title = (TextView) foldableLayout.findViewById(R.id.emp_title);
            emp_designation = (TextView) foldableLayout.findViewById(R.id.emp_designation);
            emp_location = (TextView) foldableLayout.findViewById(R.id.emp_location);
            emp_title1 = (TextView) foldableLayout.findViewById(R.id.emp_title1);
            sendPicFromCam = (LinearLayout) foldableLayout.findViewById(R.id.send_pic_from_camera);
            sendPicFromGallery = (LinearLayout) foldableLayout.findViewById(R.id.send_pic_from_gallery);
            send_text = (IconTextView) foldableLayout.findViewById(R.id.send_text);
            status = (IconTextView) foldableLayout.findViewById(R.id.status);
            edit_wish = (AutoFitEditText) foldableLayout.findViewById(R.id.edit_wish);
            edit_wish.setEnabled(true);
            edit_wish.setFocusableInTouchMode(true);
            edit_wish.setFocusable(true);
            edit_wish.setEnableSizeCache(false);
            //might cause crash on some devices
            edit_wish.setMovementMethod(null);
            // can be added after layout inflation;
            edit_wish.setMaxHeight(100);
            //don't forget to add min text size programmatically
            edit_wish.setMinTextSize(50f);
            edit_wish.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View v, final MotionEvent motionEvent) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });
            mRootView = (TextInputLayout) foldableLayout.findViewById(R.id.wishLayout);
            AutofitEditTextUtil.setNormalization(((BestWishActivity) context), mRootView, edit_wish);
            send = (LinearLayout) foldableLayout.findViewById(R.id.Send);
            setS(edit_wish.getText().toString());
        }
    }

    public void sendGreeting(final FoldableLayout foldableLayout,
                             final String user_id_from,
                             final String user_id_to,
                             final String message,
                             final String photo,
                             final IconTextView send_text,
                             final IconTextView status,
                             final LinearLayout send,
                             final LinearLayout cam,
                             final LinearLayout gal,
                             final String wish_type,
                             final byte[] imgBM) {

        JSONObject params = new JSONObject();
        try {
            params.put("user_id_from", user_id_from);
            params.put("user_id_to", user_id_to);
            params.put("message", message);
            params.put("photo", photo);
            params.put("wishType", wish_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServerLinks.ServerUrl + "/send_greetings", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if ((response.getString("result")).equals("success")) {
                                send_text.setText("{fa-check-circle} Sent");
                                CustomPreference.with(context).save("uid", user_id_to);
                                CustomPreference.with(context).save("wish_msg", message);
                                CustomPreference.with(context).save("pic", Arrays.toString(imgBM));
                                Snackbar snack = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Message sent successfully", Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                snack.setActionTextColor(Color.WHITE).show();
                                        foldableLayout.foldWithAnimation();
                                        send.setEnabled(false);
                                        cam.setVisibility(View.GONE);
                                        cam.setEnabled(false);
                                        gal.setVisibility(View.GONE);
                                        gal.setEnabled(false);
                                        status.setVisibility(View.VISIBLE);
                                        send_text.setText("{fa-check-circle} Greetings already sent.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snack = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Something went wrong. Please try after sometimes. ", Snackbar.LENGTH_LONG);
                        ViewGroup group = (ViewGroup) snack.getView();
                        group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        snack.setActionTextColor(Color.WHITE).show();
                        send_text.setText("Send");
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

