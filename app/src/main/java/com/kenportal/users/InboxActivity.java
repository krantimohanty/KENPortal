package com.kenportal.users;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.adapter.InboxAdapter;
import com.kenportal.users.datamodels.InboxModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.utils.CustomPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity {

    //Recycle view
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    LinearLayout networkUnavailable;
    LinearLayout empty_view;

    private List<InboxModel> wishList = new ArrayList<InboxModel>();
    ProgressBar prgs_cnnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        networkUnavailable = (LinearLayout) findViewById(R.id.networkUnavailable);
        empty_view = (LinearLayout) findViewById(R.id.empty_view);
        prgs_cnnt=(ProgressBar)findViewById(R.id.prgs_cnnt);
        prgs_cnnt.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(InboxActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(ConnectionDetector.staticisConnectingToInternet(this)){
            new GetUpcomingWish().execute();
        }else{
            networkUnavailable.setVisibility(View.VISIBLE);
        }
    }

    //Thread for wish list
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
        networkUnavailable.setVisibility(View.GONE);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ServerLinks.ServerUrl + "/get_greetings/" + CustomPreference.with(InboxActivity.this).getString("id", ""),
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            wishList.clear();
                            JSONObject empObject = response.getJSONObject("get_greetingsResult");
                            JSONArray jr = empObject.getJSONArray("wish_list");
                            Gson converter = new Gson();
                            Type type = new TypeToken<List<InboxModel>>() {
                            }.getType();
                            List<InboxModel> tempArrayList = converter.fromJson(String.valueOf(jr), type);
                            for (int i = 0; i < tempArrayList.size(); i++) {
                                wishList.add(tempArrayList.get(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter = new InboxAdapter(InboxActivity.this, wishList, mRecyclerView);
                        // set the adapter object to the Recyclerview
                        mRecyclerView.setAdapter(mAdapter);
                        prgs_cnnt.setVisibility(View.GONE);
                        if (wishList.isEmpty()) {
                            mRecyclerView.setVisibility(View.GONE);
                            empty_view.setVisibility(View.VISIBLE);

                        } else {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            empty_view.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                networkUnavailable.setVisibility(View.VISIBLE);
                Snackbar snack = Snackbar.make(InboxActivity.this.findViewById(android.R.id.content), "Something went wrong.", Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snack.getView();
                group.setBackgroundColor(ContextCompat.getColor(InboxActivity.this, R.color.colorPrimary));
                snack.setActionTextColor(Color.WHITE).show();

            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }

    public void sendReply(String greetingId, String userId) {
        networkUnavailable.setVisibility(View.GONE);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServerLinks.ServerUrl + "/reply_greetings/" + greetingId + "/" + userId, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject empObject = response.getJSONObject("get_greetingsResult");
                            JSONArray jr = empObject.getJSONArray("wish_list");
                            Gson converter = new Gson();
                            Type type = new TypeToken<List<InboxModel>>() {
                            }.getType();
                            List<InboxModel> tempArrayList = converter.fromJson(String.valueOf(jr), type);
                            for (int i = 0; i < tempArrayList.size(); i++) {
                                wishList.add(tempArrayList.get(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter = new InboxAdapter(InboxActivity.this, wishList, mRecyclerView);
                        // set the adapter object to the Recyclerview
                        mRecyclerView.setAdapter(mAdapter);
                        if (wishList.isEmpty()) {
                            mRecyclerView.setVisibility(View.GONE);
                            empty_view.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            empty_view.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                networkUnavailable.setVisibility(View.VISIBLE);
                Snackbar snac=Snackbar.make(InboxActivity.this.findViewById(android.R.id.content), "Something went wrong.", Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snac.getView();
                group.setBackgroundColor(ContextCompat.getColor(InboxActivity.this, R.color.colorPrimary));
                snac.setActionTextColor(Color.WHITE).show();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
