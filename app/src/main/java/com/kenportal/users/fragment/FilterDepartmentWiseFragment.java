package com.kenportal.users.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import com.kenportal.users.adapter.FilterDepartmentAdapter;
import com.kenportal.users.datamodels.DepartmentModel;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.progress_dialog.AppProgressDialog;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;

public class FilterDepartmentWiseFragment extends Fragment {

    //Progress Dialog
    AppProgressDialog appProgressDialog;
    private RecyclerView mRecyclerView;
    private FilterDepartmentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DepartmentModel> departList;
    LinearLayout networkUnavailable;
    DbHelper dbHelper;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_filter_department_wise, container, false);
        networkUnavailable = (LinearLayout) rootView.findViewById(R.id.networkUnavailable);
        dbHelper = new DbHelper(getContext());
        //calling progress dailog
        appProgressDialog = new AppProgressDialog(getActivity(), "Loading...");


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.department_filter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        // use a linear layout manager
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        departList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FilterDepartmentAdapter(getContext(), departList, mRecyclerView);

        callServiceMethod();

    }

    private void callServiceMethod() {
        networkUnavailable.setVisibility(View.GONE);
        if (ConnectionDetector.staticisConnectingToInternet(getActivity().getApplicationContext())) {
            loadOnlineLocationData(ServerLinks.ServerUrl + "/get_department");
        } else {
            loadOfflineLocationData(ServerLinks.ServerUrl + "/get_department");
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Network connection is not available.", Snackbar.LENGTH_LONG).show();
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jr = response.getJSONObject("get_DepartmentResult").getJSONArray("department");
                            JSONArray jr1 = new JSONArray();
                            ArrayList<Integer> str = new ArrayList<Integer>();
                            for (int i = 0; i < jr.length(); i++) {
                                String id = jr.getJSONObject(i).getString("id");
                                String numOfEmp = dbHelper.getDepartmentCount(id);
                                str.add(Integer.valueOf(numOfEmp));
                                JSONObject js = new JSONObject();
                                js.put("id", id);
                                js.put("name", jr.getJSONObject(i).getString("name"));
                                js.put("short_name", jr.getJSONObject(i).getString("short_name"));
                                js.put("location", jr.getJSONObject(i).getString("location"));
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
                                .setActionTextColor(Color.RED)
                                .show();
                    }
                });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "department_object");
    }


    private void loadOfflineLocationData(String url) {
        Cache cache = VolleySingleton.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
//        cache.invalidate(url, true);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    JSONArray locDt = new JSONObject(data).getJSONObject("get_DepartmentResult").getJSONArray("department");
                    JSONArray jr1 = new JSONArray();
                    ArrayList<Integer> str = new ArrayList<Integer>();
                    for (int i = 0; i < locDt.length(); i++) {
                        String id = locDt.getJSONObject(i).getString("id");
                        String numOfEmp = dbHelper.getDepartmentCount(id);
                        str.add(Integer.valueOf(numOfEmp));
                        JSONObject js = new JSONObject();
                        js.put("id", id);
                        js.put("name", locDt.getJSONObject(i).getString("name"));
                        js.put("short_name", locDt.getJSONObject(i).getString("short_name"));
                        js.put("location", locDt.getJSONObject(i).getString("location"));
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
        Type type = new TypeToken<List<DepartmentModel>>() {
        }.getType();
        List<DepartmentModel> tempArrayList = converter.fromJson(String.valueOf(locationData), type);
        departList.clear();
        for (int i = 0; i < tempArrayList.size(); i++) {
            departList.add(tempArrayList.get(i));
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    public class GetEmployeeDepartmentWise extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getEmployeeDeparment();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getEmployeeDeparment() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/get_department", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                // Parsing json object response
                // response will be a json object
                try {
                    JSONArray jr = response.getJSONObject("get_DepartmentResult").getJSONArray("department");
                    Gson converter = new Gson();
                    Type type = new TypeToken<List<DepartmentModel>>() {
                    }.getType();
                    List<DepartmentModel> tempArrayList = converter.fromJson(String.valueOf(jr), type);
                    for (int i = 0; i < tempArrayList.size(); i++) {
                        departList.add(tempArrayList.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter = new FilterDepartmentAdapter(getActivity(), departList, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
                if (departList.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong, try again!!", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }
}
