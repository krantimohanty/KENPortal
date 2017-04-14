package com.kenportal.users.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.kenportal.users.utils.ConnectionDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.kenportal.users.R;
import com.kenportal.users.adapter.UpdatesAdapter;
import com.kenportal.users.app_interface.OnLoadMoreListener;
import com.kenportal.users.datamodels.UpdatesModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;
import com.joanzapata.iconify.widget.IconTextView;

public class UpdatesFragment extends android.support.v4.app.Fragment {

    View rootView;
    private RecyclerView mRecyclerView;
    private UpdatesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<UpdatesModel> updateList;

    IconTextView batchIcon;
    TextView heading1;
    TextView heading2;

    ProgressBar progress_connect;
    SwipeRefreshLayout ViewSwipe;
    SwipeRefreshLayout layoutSwipe;

    int frequency = 10;
    int lastId = 1;
    protected Handler loadMorehandler;
    private String LoadStatus="DEFAULT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_updates, container, false);

        batchIcon=(IconTextView)rootView.findViewById(R.id.batchIcon);
        heading1=(TextView)rootView.findViewById(R.id.heading1);
        heading2=(TextView)rootView.findViewById(R.id.heading2);

        progress_connect=(ProgressBar) rootView.findViewById(R.id.progress_connect);
        progress_connect.setVisibility(View.VISIBLE);

        ViewSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.ViewSwipe);
        layoutSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.layoutSwipe);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_updates);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateList = new ArrayList<UpdatesModel>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UpdatesAdapter(getActivity(), updateList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        callingUpdates();

        ViewSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastId=1;
                LoadStatus="DEFAULT";
                callingUpdates();
            }
        });

        layoutSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastId=1;
                LoadStatus="DEFAULT";
                callingUpdates();
            }
        });

   /* Load More Items on Infinite Scroll*/
        loadMorehandler=new Handler();
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                updateList.add(null);
                mAdapter.notifyItemInserted(updateList.size() - 1);

                loadMorehandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateList.remove(null);
                        mAdapter.notifyItemRemoved(updateList.size());
                        callingMoreUpdates();
                        mAdapter.setLoaded();
                    }
                }, 2000);
            }
        });
    }

    private void callingUpdates(){

        layoutSwipe.setVisibility(View.GONE);
        //getUpcoming wish List
        if(ConnectionDetector.staticisConnectingToInternet(rootView.getContext())){
            batchIcon.setVisibility(View.GONE);
            heading1.setVisibility(View.GONE);
            heading2.setVisibility(View.GONE);
            new GetUpdates(lastId).execute();
        }else {
            updateList.clear();
            mAdapter.notifyDataSetChanged();
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

    private void callingMoreUpdates(){
        lastId=lastId+frequency;
        LoadStatus="MORE";
        callingUpdates();
    }

    //Thread
    public class GetUpdates extends AsyncTask<Void, Void, Void> {

        int lastId;

        public GetUpdates(int lastId) {
            this.lastId = lastId;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            getUpdates(lastId);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    //get updates
    public void getUpdates(final int i) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ServerLinks.ServerUrl + "/get_updates/" + Integer.parseInt(CustomPreference.with(getActivity()).getString("id", "")) + "/" + i + "/" + frequency,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jr = response.getJSONObject("get_updatesResult").getJSONArray("get_updates");
                            Gson converter = new Gson();
                            Type type = new TypeToken<List<UpdatesModel>>() {}.getType();
                            List<UpdatesModel> tempArrayList = converter.fromJson(String.valueOf(jr), type);

                            if(LoadStatus.equals("DEFAULT")){
                                updateList.clear();
                            }

                            for (int i = 0; i < tempArrayList.size(); i++) {
                                updateList.add(tempArrayList.get(i));
                            }
                            mAdapter.notifyDataSetChanged();
                            progress_connect.setVisibility(View.GONE);

                            if (updateList.isEmpty()) {
                                updateList.clear();
                                mAdapter.notifyDataSetChanged();
                                batchIcon.setText("{ic-sad 70dp #8C8989}");
                                heading2.setText("No updates.");
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
                            updateList.clear();
                            mAdapter.notifyDataSetChanged();
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
                        updateList.clear();
                        mAdapter.notifyDataSetChanged();
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

                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }
}