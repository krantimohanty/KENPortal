package com.kenportal.users;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.adapter.EmpDirectoryAdapter;
import com.kenportal.users.custom_searchview.SearchBox;
import com.kenportal.users.custom_searchview.SearchResult;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.local_db.DbModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.progress_dialog.AppProgressDialog;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.widgets.RecyclerViewFastScroller;
import com.joanzapata.iconify.widget.IconTextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmployeeDirectoryActivity extends BaseActivity {
    //Progress Dialog
    AppProgressDialog appProgressDialog;
    //DbHelper
    DbHelper dbHelper;
    List<DbModel> empDirectoryModelList;
    List<DbModel> allEmpDirectoryModelList;

    private SearchBox search ;
    Toolbar toolbar;
    TextView noRecord;
    private RecyclerView mRecyclerView;
    private EmpDirectoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String page_flag;
    LinearLayout bubbleLayout;
    TextView serchData;
    IconTextView close_bubble;
    Map<String, String> statusMap = new HashMap<>();
    String userStatus, userId;
    String dept_id="",dept_name="",id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_directory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent i=new Intent();
//        i.setComponentName(new ComponentName(getApplicationContext(),Activity2.class));
//        startActivity(i);


        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(EmployeeDirectoryActivity.this);
        noRecord = (TextView) findViewById(R.id.noRecord);

        dept_id = CustomPreference.with(this).getString("dept_id", "");
        dept_name = CustomPreference.with(this).getString("dept_Name", "");

        bubbleLayout = (LinearLayout) findViewById(R.id.bubbleLayout);
        serchData = (TextView) findViewById(R.id.serchData);
        close_bubble = (IconTextView) findViewById(R.id.close_bubble);
        //Toolbar customization
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbHelper = new DbHelper(EmployeeDirectoryActivity.this);
        //Initialize pg
        appProgressDialog = new AppProgressDialog(EmployeeDirectoryActivity.this, "Loading directory...");


        empDirectoryModelList = new ArrayList<DbModel>();
        allEmpDirectoryModelList = new ArrayList<DbModel>();
        //loading list
        mRecyclerView = (RecyclerView) findViewById(R.id.employee_recycle_list);
        mRecyclerView.setHasFixedSize(true);
         mLayoutManager = new LinearLayoutManager(EmployeeDirectoryActivity.this);
        // use a linear layout manager
         mRecyclerView.setLayoutManager(mLayoutManager);
        // create an Object for Adapter

        final RecyclerViewFastScroller fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(EmployeeDirectoryActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(mAdapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(mRecyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
        mAdapter = new EmpDirectoryAdapter(empDirectoryModelList, mRecyclerView);
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        page_flag = getIntent().getStringExtra("filter_flag");
        if (page_flag.equals("location")) {
            id=getIntent().getStringExtra("loc_id");
//            new GetEmployeeThread().execute();
            bindEmployee();
            bubbleLayout.setVisibility(View.VISIBLE);
            serchData.setText(getIntent().getStringExtra("loc_name"));
        } else if (page_flag.equals("department")) {
            id=getIntent().getStringExtra("dept_id");
//            new GetEmployeeThread().execute();
            bindEmployee();
            bubbleLayout.setVisibility(View.VISIBLE);
            serchData.setText(getIntent().getStringExtra("dept_name"));
        } else if(page_flag.equals("dashbrd")) {
//            new GetEmployeeThread().execute();
            bindEmployee();
            bubbleLayout.setVisibility(View.VISIBLE);
            serchData.setText(dept_name);
        }else{
//            new GetEmployeeThread().execute();
            bindEmployee();
            bubbleLayout.setVisibility(View.VISIBLE);
            serchData.setText(dept_name);
        }

        close_bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_flag="";
//                new GetEmployeeThread().execute();
                bindEmployee();
                bubbleLayout.setVisibility(View.VISIBLE);
                bubbleLayout.setBackgroundColor(EmployeeDirectoryActivity.this.getResources().getColor(R.color.colorPrimary));
                serchData.setText("All employees");
                serchData.setTextColor(EmployeeDirectoryActivity.this.getResources().getColor(R.color.white));
                serchData.setTextSize(EmployeeDirectoryActivity.this.getResources().getDimension(R.dimen.textSize));
//                serchData.setTypeface(Typeface.DEFAULT_BOLD);
                close_bubble.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (search.getsearchStatus()) {
            search.setsearchStatus(false);
            closeSearch();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.employee_directory, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        else if (id == R.id.action_filter) {
            Intent i = new Intent(EmployeeDirectoryActivity.this, EmployeeDirectoryAdavanced.class);
            i.putExtra("flag", "directory");
            finish();
            startActivity(i);
            return true;
        } else if (id == R.id.action_search) {
            openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    /*Search operation*/
    public void openSearch() {
        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);

        search.setMenuListener(new SearchBox.MenuListener() {
            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                // Toast.makeText(EmployeeDirectoryActivity.this, "Menu click", Toast.LENGTH_LONG).show();
            }
        });

        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
                toolbar.setTitle("Employee Directory");
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
                final List<DbModel> filteredModelList = filter(empDirectoryModelList, term);

                if (filteredModelList.isEmpty()) {
                    noRecord.setVisibility(View.VISIBLE);
                } else {
                    noRecord.setVisibility(View.GONE);
                }
                mAdapter.setModels(empDirectoryModelList);
                mAdapter.animateTo(filteredModelList);
                mRecyclerView.scrollToPosition(0);
            }

            @Override
            public void onSearch(String searchTerm) {
//                Toast.makeText(EmployeeDirectoryActivity.this, searchTerm + " Searched",
//                        Toast.LENGTH_LONG).show();
//                toolbar.setTitle(searchTerm);
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to result being clicked
            }

            @Override
            public void onSearchCleared() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if (search.getSearchText().isEmpty()) toolbar.setTitle("");

        final List<DbModel> filteredModelList = filter(empDirectoryModelList, "");
        mAdapter.setModels(empDirectoryModelList);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        search.setSearchText();
    }

    private List<DbModel> filter(List<DbModel> models, String query) {
        query = query.toLowerCase();
        final List<DbModel> filteredModelList = new ArrayList<>();
        try {
            for (DbModel model : models) {
                final String text = model.getEmp_name().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
        } catch (Exception e) {
        }
        return filteredModelList;
    }

    private void bindEmployee(){

        try {
            new GetEmployeeStatusThread("1").execute();

            if (page_flag.equals("location")) {
                empDirectoryModelList.clear();
                List<DbModel> tempArrayList = dbHelper.getPaggingEmpByLoc(id, 1);
                for (int i = 0; i < tempArrayList.size(); i++) {
                    empDirectoryModelList.add(tempArrayList.get(i));
                }
                mAdapter = new EmpDirectoryAdapter(empDirectoryModelList, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
                appProgressDialog.hideProgressDialog();
            } else if (page_flag.equals("department")) {
                empDirectoryModelList.clear();
                List<DbModel> tempArrayList = dbHelper.getPaggingEmpByDept(id, 1);
                for (int i = 0; i < tempArrayList.size(); i++) {
                    empDirectoryModelList.add(tempArrayList.get(i));
                }
                mAdapter = new EmpDirectoryAdapter(empDirectoryModelList, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
                appProgressDialog.hideProgressDialog();
            } else if(page_flag.equals("dashbrd")) {
                empDirectoryModelList.clear();
                List<DbModel> tempArrayList = dbHelper.getPaggingEmpByDept(dept_id, 1);
                for (int i = 0; i < tempArrayList.size(); i++) {
                    empDirectoryModelList.add(tempArrayList.get(i));
                }
                mAdapter = new EmpDirectoryAdapter(empDirectoryModelList, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
                appProgressDialog.hideProgressDialog();
            } else  {
                empDirectoryModelList.clear();
                List<DbModel> tempArrayList = dbHelper.getAllEmp();
                for (int i = 0; i < tempArrayList.size(); i++) {
                    empDirectoryModelList.add(tempArrayList.get(i));
                }
                mAdapter = new EmpDirectoryAdapter(empDirectoryModelList, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
                appProgressDialog.hideProgressDialog();
            }

        }catch (Exception e){}

    }

    //getemployee thread
//    public class GetEmployeeThread extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            appProgressDialog.initializeProgress();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//        try {
//            if (page_flag.equals("location")) {
//                empDirectoryModelList.clear();
//                List<DbModel> tempArrayList = dbHelper.getPaggingEmpByLoc(id, 1);
//                for (int i = 0; i < tempArrayList.size(); i++) {
//                    empDirectoryModelList.add(tempArrayList.get(i));
//                }
//                mAdapter.notifyDataSetChanged();
//                appProgressDialog.hideProgressDialog();
////                bubbleLayout.setVisibility(View.VISIBLE);
////                serchData.setText(getIntent().getStringExtra("loc_name"));
//            } else if (page_flag.equals("department")) {
//                empDirectoryModelList.clear();
//                List<DbModel> tempArrayList = dbHelper.getPaggingEmpByDept(id, 1);
//                for (int i = 0; i < tempArrayList.size(); i++) {
//                    empDirectoryModelList.add(tempArrayList.get(i));
//                }
//                mAdapter.notifyDataSetChanged();
//                appProgressDialog.hideProgressDialog();
////                bubbleLayout.setVisibility(View.VISIBLE);
////                serchData.setText(getIntent().getStringExtra("dept_name"));
//            } else if(page_flag.equals("dashbrd")) {
//                empDirectoryModelList.clear();
//                List<DbModel> tempArrayList = dbHelper.getPaggingEmpByDept(dept_id, 1);
////                List<DbModel> tempArrayList = dbHelper.getAllEmp();
//                for (int i = 0; i < tempArrayList.size(); i++) {
//                    empDirectoryModelList.add(tempArrayList.get(i));
//                }
//                mAdapter.notifyDataSetChanged();
//                appProgressDialog.hideProgressDialog();
//            } else  {
//                empDirectoryModelList.clear();
////                List<DbModel> tempArrayList = dbHelper.getPaggingEmpByDept(dept_id, 1);
//                List<DbModel> tempArrayList = dbHelper.getAllEmp();
//                for (int i = 0; i < tempArrayList.size(); i++) {
//                    empDirectoryModelList.add(tempArrayList.get(i));
//                }
//                new EmpDirectoryAdapter(empDirectoryModelList, mRecyclerView);
//                mAdapter.notifyDataSetChanged();
//                appProgressDialog.hideProgressDialog();
//            }
//
//        }catch (Exception e){}
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            new GetEmployeeStatusThread("1").execute();
//            super.onPostExecute(aVoid);
//        }
//    }

    public class GetEmployeeStatusThread extends AsyncTask<Void, Void, Void> {
        String s;

        public GetEmployeeStatusThread(String s) {
            super();
            this.s = s;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            if (ConnectionDetector.staticisConnectingToInternet(EmployeeDirectoryActivity.this)) {
                getEmployeeStatus(s);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    //get employee status
    public void getEmployeeStatus(String emp_ids) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", emp_ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServerLinks.ServerUrl + "/get_employee_status", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jr = response.getJSONArray("get_emp_status");
                            for (int i = 0; i < jr.length(); i++) {
                                JSONObject jsonObjectProfile = (JSONObject) jr.get(i);
                                userId = jsonObjectProfile.getString("user_id");
                                userStatus = jsonObjectProfile.getString("user_status");
                                statusMap.put(userId, userStatus);
                            }
                            for (int i = 0; i < empDirectoryModelList.size(); i++) {
                                if (statusMap.size() > 0) {
                                    Set keys = statusMap.keySet();
                                    Iterator itr = keys.iterator();
                                    while (itr.hasNext()) {
                                        String key = (String) itr.next();
                                        String listValue = empDirectoryModelList.get(i).getEmp_id().toString();
                                        if (key.equalsIgnoreCase(listValue)) {
                                            empDirectoryModelList.get(i).setEmp_status((String) statusMap.get(key));
                                        }
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
         });
//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }
}

