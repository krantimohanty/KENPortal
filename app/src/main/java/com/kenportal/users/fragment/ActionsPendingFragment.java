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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.R;
import com.kenportal.users.adapter.ActionAdapter;
import com.kenportal.users.datamodels.ActionModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.iconify.widget.IconTextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ActionsPendingFragment extends Fragment {

    View rootView;
    private RecyclerView mRecyclerView;
    private ActionAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<ActionModel> actionList;
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
        rootView = inflater.inflate(R.layout.fragment_actions_pending, container, false);

        batchIcon=(IconTextView)rootView.findViewById(R.id.batchIcon);
        heading1=(TextView)rootView.findViewById(R.id.heading1);
        heading2=(TextView)rootView.findViewById(R.id.heading2);

        actionList = new ArrayList<>();

        progress_connect=(ProgressBar) rootView.findViewById(R.id.progress_connect);
        progress_connect.setVisibility(View.VISIBLE);

        ViewSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.ViewSwipe);
        layoutSwipe=(SwipeRefreshLayout)rootView.findViewById(R.id.layoutSwipe);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_actions);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        callingPendingActions();

        ViewSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callingPendingActions();
            }
        });

        layoutSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callingPendingActions();
            }
        });

        return rootView;
    }

    private void callingPendingActions(){

        layoutSwipe.setVisibility(View.GONE);
        //getUpcoming wish List
        if(ConnectionDetector.staticisConnectingToInternet(rootView.getContext())){
            batchIcon.setVisibility(View.GONE);
            heading1.setVisibility(View.GONE);
            heading2.setVisibility(View.GONE);
            new ActionThread().execute();
        }else {
            actionList.clear();
            mAdapter = new ActionAdapter(getActivity(), actionList, mRecyclerView);
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

    public class ActionThread extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            getActions();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    //get pending actions
    public void getActions() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ServerLinks.ServerUrl + "/get_actions/" + CustomPreference.with(getActivity()).getString("id", ""),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jr = response.getJSONObject("get_actionsResult").getJSONArray("get_actions");
                            Gson converter = new Gson();
                            Type type = new TypeToken<List<ActionModel>>() {}.getType();
                            List<ActionModel> tempArrayList = converter.fromJson(String.valueOf(jr), type);
                            actionList.clear();
                            for (int i = 0; i < tempArrayList.size(); i++) {
                                actionList.add(tempArrayList.get(i));
                            }

                            mAdapter = new ActionAdapter(getActivity(), actionList, mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);
                            progress_connect.setVisibility(View.GONE);

                            if (actionList.isEmpty()) {
                                actionList.clear();
                                mAdapter = new ActionAdapter(getActivity(), actionList, mRecyclerView);
                                mRecyclerView.setAdapter(mAdapter);
                                batchIcon.setText("{ic-sad 70dp #8C8989}");
                                heading2.setText("No pending actions.");
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
                            actionList.clear();
                            mAdapter = new ActionAdapter(getActivity(), actionList, mRecyclerView);
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
                        actionList.clear();
                        mAdapter = new ActionAdapter(getActivity(), actionList, mRecyclerView);
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
                        5000,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
             }
}