package com.kenportal.users.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.R;
import com.kenportal.users.adapter.UpcomingWishAdapter;
import com.kenportal.users.datamodels.WishModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UpcomingWishFragment extends Fragment {

    View rootView;
    //Progress Dialog
    String tag_json_obj = "json_obj_req";
    //Recycle view
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<WishModel> wishList;


    IconTextView batchIcon;
    TextView heading1;
    TextView heading2;


    ProgressBar progress_connect;
    SwipeRefreshLayout ViewSwipe;
    SwipeRefreshLayout layoutSwipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upcoming_wish, container, false);

        batchIcon=(IconTextView)rootView.findViewById(R.id.batchIcon);
        heading1=(TextView)rootView.findViewById(R.id.heading1);
        heading2=(TextView)rootView.findViewById(R.id.heading2);

        wishList = new ArrayList<WishModel>();

        progress_connect=(ProgressBar) rootView.findViewById(R.id.progress_connect);
        progress_connect.setVisibility(View.VISIBLE);

        ViewSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.ViewSwipe);
        layoutSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.layoutSwipe);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_upcoming_bday);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //getUpcoming wish List
        callingUpcomingWish();




        /*RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);*/

        ViewSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callingUpcomingWish();
            }
        });

        layoutSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callingUpcomingWish();
            }
        });
        return rootView;
    }

    private void callingUpcomingWish(){
        layoutSwipe.setVisibility(View.GONE);
        //getUpcoming wish List
        if(ConnectionDetector.staticisConnectingToInternet(rootView.getContext())){
            batchIcon.setVisibility(View.GONE);
            heading1.setVisibility(View.GONE);
            heading2.setVisibility(View.GONE);
            new GetUpcomingWish().execute();
        }else {

            Log.i("atag", "hello");
            wishList.clear();
            mAdapter = new UpcomingWishAdapter(getActivity(), wishList, mRecyclerView);
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
    public class GetUpcomingWish extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getUpcomingWishList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getUpcomingWishList() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/get_wish_lists/upcoming", new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            wishList.clear();
                            JSONObject empObject = response.getJSONObject("get_wish_listsResult");
                            JSONArray jr = empObject.getJSONArray("wish_list");
                            Gson converter = new Gson();
                            Type type = new TypeToken<List<WishModel>>() {
                            }.getType();
                            List<WishModel> tempArrayList = converter.fromJson(String.valueOf(jr), type);

                            for (int i = 0; i < tempArrayList.size(); i++) {
                                wishList.add(tempArrayList.get(i));
                            }

                        mAdapter = new UpcomingWishAdapter(getActivity(), wishList, mRecyclerView);

                        // set the adapter object to the Recyclerview
                        mRecyclerView.setAdapter(mAdapter);
                        progress_connect.setVisibility(View.GONE);

                        if (wishList.isEmpty()) {
                            wishList.clear();
                            mAdapter = new UpcomingWishAdapter(getActivity(), wishList, mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);
//                            mRecyclerView.setVisibility(View.GONE);
//                            empty_view.setVisibility(View.VISIBLE);
                            batchIcon.setText("{ic-sad 70dp #8C8989}");
//                            heading1.setText("Can't Connect");
                            heading2.setText("No wish in your inbox.");
                            batchIcon.setVisibility(View.VISIBLE);
                            heading1.setVisibility(View.GONE);
                            heading2.setVisibility(View.VISIBLE);
                            layoutSwipe.setRefreshing(false);
                            layoutSwipe.setVisibility(View.VISIBLE);

                        } else {
                            batchIcon.setVisibility(View.GONE);
                            heading1.setVisibility(View.GONE);
                            heading2.setVisibility(View.GONE);
                            layoutSwipe.setRefreshing(false);
                            layoutSwipe.setVisibility(View.GONE);
                        }
                        } catch (JSONException e) {
                            wishList.clear();
                            mAdapter = new UpcomingWishAdapter(getActivity(), wishList, mRecyclerView);
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
                mAdapter = new UpcomingWishAdapter(getActivity(), wishList, mRecyclerView);
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