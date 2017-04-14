package com.kenportal.users;

/*

* Created Date :-18-Nov-2016
* Modified By :-
* Modified Date :-26-May-2016
* Description :- TourActivity is used to show the list of employees in tour with their past and current position of scheduled duration
* */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.adapter.TourAdapter;
import com.kenportal.users.datamodels.TourModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TourActivity extends AppCompatActivity {

    static Context context;
    private RecyclerView mRecycleView;  //Declaration of recycle view to show tour list
    private LinearLayoutManager mLayoutManager; //Declaration of layout manager for the adapter
    private TourAdapter mAdapter;  //Declaration of adapter cor recycle view
    private ArrayList<TourModel> tourModelArrayList;   //Declaration of tour model arraylist
    private ArrayList<String> userArrList;
    String userId,userName,startDate,endDate;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        //Getting reference of appcompact tool bar through id.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Setting title to the toolbar
        toolbar.setTitle("Tour");
        //Setting title text color
        toolbar.setTitleTextColor(Color.WHITE);
        //setting toolbar to the action bar
        setSupportActionBar(toolbar);
        //to display back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Getting reference of the recycle view through id.
        mRecycleView = (RecyclerView) findViewById(R.id.mRecycleView);
        //Add decoration to the recycle view
        mRecycleView.addItemDecoration(new SimpleDividerItemDecoration(this));
        //Setting recycle view size
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        //Initializing the ArrayList
        tourModelArrayList = new ArrayList<>();
        //Initializing the ArrayList
         userArrList =new ArrayList<>();



       /* mAdapter=new TourAdapter(this,tourModelArrayList,mRecycleView);
        mRecycleView.setAdapter(mAdapter);*/

        //get tour data
        getTourData();
    }

    private void getTourData() {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.dashboardServerUrl + "/EmpTourDetails" + "/1092", null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Output", response.toString());
                        JSONObject jsonObject=null;

                        try {
                            JSONArray jsonArray = response.getJSONArray("EmpTourDetailsResult");

                            Log.d("Result", jsonArray.toString());

                            Gson converter = new Gson();
                            Type type = new TypeToken<ArrayList<TourModel>>() {
                            }.getType();

                            tourModelArrayList.clear();

                            ArrayList<TourModel> tempArrayList = converter.fromJson(String.valueOf(jsonArray), type);

                            Log.i("atag",tempArrayList.toString());

                            //userArrList.add(String.valueOf(tempArrayList.get(0).getINT_USERID()));
                            //userArrList.add(String.valueOf(tempArrayList));
/*
                                 if(userArrList.contains(tempArrayList.get(0).getINT_USERID())){
                                     for(int i=0;i<userArrList.size();i++){
                                         Log.i("r",userArrList.get(i));
                                     }
                                 }*/

                               for(int i=0;i<tempArrayList.size();i++) {

                                   userId = tempArrayList.get(i).getINT_USERID();
                                   userName = tempArrayList.get(i).getNAME();
                                   startDate = tempArrayList.get(i).getSTARTDATE();
                                   endDate = tempArrayList.get(i).getENDDATE();

                                   //jsonObject = (JSONObject) jsonArray.get(i);

                                   //userId = jsonObject.getString("INT_USERID");
                                  /* userName = jsonObject.getString("NAME");
                                   startDate = jsonObject.getString("STARTDATE");
                                   endDate = jsonObject.getString("ENDDATE");*/
                                   //Log.i("r",userId);


                                   if (userId.contains(tempArrayList.get(i).getINT_USERID())) {

                                       for ( i = 0; i < userId.length(); i++) {
                                           //Log.i("rr", userId);
                                           userArrList.add(userId);
                                           userArrList.add(userName);
                                           userArrList.add(startDate);
                                           userArrList.add(endDate);

                                           Log.i("r", userArrList.get(i));
                                       }
                                   }
                               }

                            for (int i = 0; i < tempArrayList.size(); i++) {
                                tourModelArrayList.add(tempArrayList.get(i));
                            }
                            mAdapter = new TourAdapter(context, tourModelArrayList, userArrList);
                            mRecycleView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            tourModelArrayList.clear();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq);
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
