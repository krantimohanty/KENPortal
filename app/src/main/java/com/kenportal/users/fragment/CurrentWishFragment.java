package com.kenportal.users.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.BestWishActivity;
import com.kenportal.users.R;
import com.kenportal.users.adapter.CurrentWishAdapter;
import com.kenportal.users.datamodels.TodayWishModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.utils.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.iconify.widget.IconTextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWishFragment extends Fragment {

    View rootView;
    String tag_json_obj = "json_obj_req";
    //Recycle view
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TodayWishModel> wishList;
    IconTextView batchIcon;
    TextView heading1;
    TextView heading2;
    ProgressBar progress_connect;
    String own_id="";
    SwipeRefreshLayout ViewSwipe;
    SwipeRefreshLayout layoutSwipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_current_wish, container, false);
        //Data unavailable message layout
        batchIcon=(IconTextView)rootView.findViewById(R.id.batchIcon);
        heading1=(TextView)rootView.findViewById(R.id.heading1);
        heading2=(TextView)rootView.findViewById(R.id.heading2);

        wishList = new ArrayList<TodayWishModel>();

        progress_connect=(ProgressBar) rootView.findViewById(R.id.progress_connect);
        progress_connect.setVisibility(View.VISIBLE);
        own_id= CustomPreference.with(rootView.getContext()).getString("id", "");

        ViewSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.ViewSwipe);
        layoutSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.layoutSwipe);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        callingTodayWish();

        ViewSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callingTodayWish();
            }
        });

        layoutSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callingTodayWish();
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        // TODO Handle item click
                        LinearLayout cam = (LinearLayout) view.findViewById(R.id.send_pic_from_camera);
                        final EditText edit_wish = (EditText) view.findViewById(R.id.edit_wish);
                        LinearLayout send = (LinearLayout) view.findViewById(R.id.Send);

                        cam.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((BestWishActivity) getActivity()).getImageFromCamera((ImageView) view.findViewById(R.id.imageview_detail));
                            }
                        });

                        LinearLayout gallery = (LinearLayout) view.findViewById(R.id.send_pic_from_gallery);
                        gallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((BestWishActivity) getActivity()).getImageFromGallery((ImageView) view.findViewById(R.id.imageview_detail));
                            }
                        });
                    }
                })
        );
        return rootView;
    }

   private void callingTodayWish(){
       layoutSwipe.setVisibility(View.GONE);
       //getUpcoming wish List
       if(ConnectionDetector.staticisConnectingToInternet(rootView.getContext())){
           batchIcon.setVisibility(View.GONE);
           heading1.setVisibility(View.GONE);
           heading2.setVisibility(View.GONE);
           new GetTodayWish().execute();
       }else {
           wishList.clear();
           mAdapter = new CurrentWishAdapter(getActivity(), wishList, mRecyclerView);
           mRecyclerView.setAdapter(mAdapter);
           batchIcon.setText("{ic-noconnection 70dp #8C8989}");
           heading1.setText("Can't Connect");
           heading2.setText("Please swipe top to refresh");
           batchIcon.setVisibility(View.VISIBLE);
           heading1.setVisibility(View.VISIBLE);
           heading2.setVisibility(View.VISIBLE);
           progress_connect.setVisibility(View.GONE);
           ViewSwipe.setRefreshing(false);
           layoutSwipe.setVisibility(View.VISIBLE);
           layoutSwipe.setRefreshing(false);
       }

   }
    //Thread
    public class GetTodayWish extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            getTodayWishList();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getTodayWishList() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/get_wish_lists/today", new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jr = response.getJSONObject("get_wish_listsResult").getJSONArray("wish_list");
                            Gson converter = new Gson();
                            Type type = new TypeToken<List<TodayWishModel>>() {}.getType();
                            List<TodayWishModel> tempArrayList = converter.fromJson(String.valueOf(jr), type);
                            wishList.clear();
                            for (int i = 0; i < tempArrayList.size(); i++) {
                                if(!(tempArrayList.get(i).getUser_id()).equals(own_id))
                                        wishList.add(tempArrayList.get(i));
                            }

                        mAdapter = new CurrentWishAdapter(getActivity(), wishList, mRecyclerView);
                        // set the adapter object to the Recyclerview
                        mRecyclerView.setAdapter(mAdapter);
                        progress_connect.setVisibility(View.GONE);
                        //loadList
                        if (wishList.isEmpty()) {
                            wishList.clear();
                            mAdapter = new CurrentWishAdapter(getActivity(), wishList, mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);
//                            mRecyclerView.setVisibility(View.GONE);
//                            empty_view.setVisibility(View.VISIBLE);
                            batchIcon.setText("{ic-sad 70dp #8C8989}");
//                            heading1.setText("Can't Connect");
                            heading2.setText("No current wish.");
                            batchIcon.setVisibility(View.VISIBLE);
                            heading1.setVisibility(View.GONE);
                            heading2.setVisibility(View.VISIBLE);
                            layoutSwipe.setRefreshing(false);
                            layoutSwipe.setVisibility(View.VISIBLE);


                        } else {
//                            mRecyclerView.setVisibility(View.VISIBLE);
//                            empty_view.setVisibility(View.GONE);
                            batchIcon.setVisibility(View.GONE);
                            heading1.setVisibility(View.GONE);
                            heading2.setVisibility(View.GONE);
                            layoutSwipe.setRefreshing(false);
                            layoutSwipe.setVisibility(View.GONE);
                        }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            wishList.clear();
                            mAdapter = new CurrentWishAdapter(getActivity(), wishList, mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);
                            batchIcon.setText("{ic-warn 70dp #8C8989}");
                            heading1.setText("Something Went wrong");
                            heading2.setText("Please try after sometimes.");
                            batchIcon.setVisibility(View.VISIBLE);
                            heading1.setVisibility(View.VISIBLE);
                            heading2.setVisibility(View.VISIBLE);
                            progress_connect.setVisibility(View.GONE);
                            layoutSwipe.setRefreshing(false);
                            layoutSwipe.setVisibility(View.VISIBLE);
                        }
                        ViewSwipe.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        wishList.clear();
                        mAdapter = new CurrentWishAdapter(getActivity(), wishList, mRecyclerView);
                        mRecyclerView.setAdapter(mAdapter);
                        batchIcon.setText("{ic-warn 70dp #8C8989}");
                        heading1.setText("Something Went wrong");
                        heading2.setText("Please try after sometimes.");
                        batchIcon.setVisibility(View.VISIBLE);
                        heading1.setVisibility(View.VISIBLE);
                        heading2.setVisibility(View.VISIBLE);
                        progress_connect.setVisibility(View.GONE);
                        ViewSwipe.setRefreshing(false);
                        layoutSwipe.setVisibility(View.VISIBLE);
                        layoutSwipe.setRefreshing(false);
                    }
                });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}