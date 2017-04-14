package com.kenportal.users;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kenportal.users.adapter.FeedViewAdapter;
import com.kenportal.users.datamodels.FeedModel;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FacebookActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<FeedModel> feedArrayList;
    private static FeedViewAdapter mAdapter;
    private String nextUrl = "";
    protected Handler loadMorehandler;
    LinearLayout networkUnavailable;
    AppCompatButton connect_to_fb;
    TextView message;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Toolbar customization
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        networkUnavailable = (LinearLayout) findViewById(R.id.networkUnavailable);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_feed);
        message = (TextView) findViewById(R.id.message);
        feedArrayList = new ArrayList<FeedModel>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FeedViewAdapter(this, feedArrayList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        if (ConnectionDetector.staticisConnectingToInternet(this) && ServerLinks.accessToken != null) {
            networkUnavailable.setVisibility(View.GONE);
            GraphRequest request1 = GraphRequest.newGraphPathRequest(
                    ServerLinks.accessToken,
                    "/kenportal/newsfeed",
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                JSONObject data = new JSONObject(response.getRawResponse());
                                JSONArray feedData = data.getJSONArray("data");
                                nextUrl = data.getJSONObject("paging").getString("next");
                                bindDataToAdapter(feedData);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });
            Bundle parameters1 = new Bundle();
            parameters1.putString("fields", "message,id,created_time,full_picture,link,story");
            parameters1.putString("limit", "100");
            request1.setParameters(parameters1);
            request1.executeAsync();
        } else {
            networkUnavailable.setVisibility(View.VISIBLE);
        }




                      /* Load More Items on Infinite Scroll*/
//        loadMorehandler=new Handler();
//        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                //add null , so the adapter will check view_type and show progress bar at bottom
//                feedArrayList.add(null);
//                mAdapter.notifyItemInserted(feedArrayList.size() - 1);
//
//                loadMorehandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //   remove progress item
//                        // compArrayList.remove(compArrayList.size() - 1);
//                        feedArrayList.remove(null);
//                        mAdapter.notifyItemRemoved(feedArrayList.size());
//                        //add items one by one
//                        int start = feedArrayList.size();
//                        // int end = start + 10;
//                        // callServiceMethd("allComplaint/T/0/" + start + "/0/0","LOAD_MORE");
//
//                        Log.i("atag","on load more tyyyyy");
//                        getMoreFeed();
////                        callServiceMethd("getIdeas/R/" + catagory_id + "/" + strCitizenID + "/" + start, "LOAD_MORE");
//
//                        mAdapter.setLoaded();
//                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                    }
//                }, 2000);
//
//            }
//        });


    }


//    public void getMoreFeed() {
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,nextUrl , null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("atag","next data   "+response);
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("atag", " next error  " + error);
//
//                    }
//                });
//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // Adding request to request queue
//        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "nextFeed_json_obj");
//
//    }

    private void bindDataToAdapter(JSONArray feedData) {


        Gson converter = new Gson();
        Type type = new TypeToken<ArrayList<FeedModel>>() {
        }.getType();
        ArrayList<FeedModel> tempArrayList = converter.fromJson(String.valueOf(feedData), type);
        // compArrayList = converter.fromJson(String.valueOf(response), type);

//        switch(load_type){
//            case "LOAD_TOP":
//                recentArrayList.clear();
//                for(int i=0; i<tempArrayList.size(); i++) {
//                    recentArrayList.add(tempArrayList.get(i));
//                }
//                mAdapter.notifyDataSetChanged();
//                break;
//            case "LOAD_DEFAULT":
//                feedArrayList.clear();
        for (int i = 0; i < tempArrayList.size(); i++) {
            feedArrayList.add(tempArrayList.get(i));
        }
        mAdapter.notifyDataSetChanged();
//                progress_connect.animate()
//                        .translationY(0)
//                        .alpha(0.0f)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                progress_connect.setVisibility(View.GONE);
//                            }
//                        });
//                break;
//            case "LOAD_MORE":
//                for(int i=0; i<tempArrayList.size(); i++) {
//                    recentArrayList.add(tempArrayList.get(i));
//                }
//                mAdapter.notifyItemInserted(recentArrayList.size());
//                //mAdapter.notifyDataSetChanged();
//                mAdapter.setLoaded();
//                break;
//        }

//        if(mSwipeRefreshLayout!=null) {
//            mSwipeRefreshLayout.setRefreshing(false);
//            progress_connect.setVisibility(View.GONE);
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
