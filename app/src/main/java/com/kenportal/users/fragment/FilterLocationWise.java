package com.kenportal.users.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kenportal.users.R;
import com.kenportal.users.adapter.FilterLocationAdapter;
import com.kenportal.users.datamodels.LocationModel;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.progress_dialog.AppProgressDialog;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;


public class FilterLocationWise extends Fragment {

    //Progress Dialog
    AppProgressDialog appProgressDialog;
    private RecyclerView mRecyclerView;
    private FilterLocationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<LocationModel> locList;
    LinearLayout networkUnavailable;
    DbHelper dbHelper;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_filter_location_wise, container, false);
        networkUnavailable = (LinearLayout) rootView.findViewById(R.id.networkUnavailable);
        //calling progress dailog
        dbHelper = new DbHelper(getContext());
        appProgressDialog = new AppProgressDialog(getActivity(), "Loading...");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.location_filter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FilterLocationAdapter(getContext(), locList, mRecyclerView);

        callServiceMethod();

    }

    private void callServiceMethod() {
        networkUnavailable.setVisibility(View.GONE);
        if (ConnectionDetector.staticisConnectingToInternet(getActivity().getApplicationContext())) {
            loadOnlineLocationData(ServerLinks.ServerUrl + "/get_location");
        } else {
            loadOfflineLocationData(ServerLinks.ServerUrl + "/get_location");

            Snackbar.make(getActivity().findViewById(android.R.id.content), "Network connection is not available.", Snackbar.LENGTH_LONG)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
        }

    }

    private void loadOnlineLocationData(String url) {
        dialog = new Dialog(getActivity(), R.style.Theme_D1NoTitleDim);
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
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,  null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jr = response.getJSONObject("get_locationResult").getJSONArray("location");
                            JSONArray jr1 = new JSONArray();
                            ArrayList<Integer> str = new ArrayList<Integer>();
                            for (int i = 0; i < jr.length(); i++) {
                                String id = jr.getJSONObject(i).getString("id");
                                String numOfEmp = dbHelper.getLocationCount(id);
                                str.add(Integer.valueOf(numOfEmp));
                                JSONObject js = new JSONObject();
                                js.put("id", id);
                                js.put("name", jr.getJSONObject(i).getString("name"));
                                js.put("count", numOfEmp);
                                jr1.put(js);
                            }
                            List<Integer> jsonValues = new ArrayList<Integer>();
                            ArrayList<String> id_array = new ArrayList<String>();
                            for (int i = 0; i < jr1.length(); i++) {
                                jsonValues.add(jr1.getJSONObject(i).getInt("count"));
                            }
                            Collections.sort(jsonValues);
                            jsonValues = jsonValues;
                            JSONArray jr3 = new JSONArray();
                            String id = "";
                            for (int i = jsonValues.size() - 1; i >= 0; i--) {
                                for (int j = 0; j < jr1.length(); j++) {
                                    id = jr1.getJSONObject(j).getString("id");
                                    if (jsonValues.get(i).equals(jr1.getJSONObject(j).getInt("count")) && !id_array.contains(id)) {
                                        id_array.add(jr1.getJSONObject(j).getString("id"));
                                        jr3.put(jr1.getJSONObject(j));
                                    }
                                }
                            }
                            bindDataToAdapter(jr3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong!! Please try after sometime", Snackbar.LENGTH_LONG)
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .setActionTextColor(Color.RED)
                        .show();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "location_object");
    }

    private void loadOfflineLocationData(String url) {
        Cache cache = VolleySingleton.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    JSONArray locDt = new JSONObject(data).getJSONObject("get_locationResult").getJSONArray("location");
                    JSONArray jr1 = new JSONArray();
                    ArrayList<Integer> str = new ArrayList<Integer>();
                    for (int i = 0; i < locDt.length(); i++) {
                        String id = locDt.getJSONObject(i).getString("id");
                        String numOfEmp = dbHelper.getLocationCount(id);
                        str.add(Integer.valueOf(numOfEmp));
                        JSONObject js = new JSONObject();
                        js.put("id", id);
                        js.put("name", locDt.getJSONObject(i).getString("name"));
                        js.put("count", numOfEmp);
                        jr1.put(js);
                    }
                    List<Integer> jsonValues = new ArrayList<Integer>();
                    ArrayList<String> id_array = new ArrayList<String>();
                    for (int i = 0; i < jr1.length(); i++) {
                        jsonValues.add(jr1.getJSONObject(i).getInt("count"));
                    }
                    Collections.sort(jsonValues);
                    jsonValues = jsonValues;
                    JSONArray jr3 = new JSONArray();
                    String id = "";
                    for (int i = jsonValues.size() - 1; i >= 0; i--) {
                        for (int j = 0; j < jr1.length(); j++) {
                            id = jr1.getJSONObject(j).getString("id");
                            if (jsonValues.get(i).equals(jr1.getJSONObject(j).getInt("count")) && !id_array.contains(id)) {
                                id_array.add(jr1.getJSONObject(j).getString("id"));
                                jr3.put(jr1.getJSONObject(j));
                            }
                        }
                    }
                    bindDataToAdapter(jr3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            networkUnavailable.setVisibility(View.VISIBLE);
        }
    }

    public void bindDataToAdapter(JSONArray locationData) {
        Gson converter = new Gson();
        Type type = new TypeToken<List<LocationModel>>() {
        }.getType();
        ArrayList<LocationModel> tempArrayList = converter.fromJson(String.valueOf(locationData), type);
        locList.clear();
        for (int i = 0; i < tempArrayList.size(); i++) {
            locList.add(tempArrayList.get(i));
        }
        mRecyclerView.setAdapter(mAdapter);
    }

}
