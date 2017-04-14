package com.kenportal.users;
/*
* Created By :- Kranti Mohanty
* Created Date :-26-July-2016
* Modified By :- Kranti Mohanty
* Modified Date :-28-July-2016
* Description :- AttendanceNewActivity is used to show attendance detail of an employee for a month
* */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.kenportal.users.adapter.AlertAbsentListAdapter;
import com.kenportal.users.adapter.AlertLateEntryAdapter;
import com.kenportal.users.adapter.AlertLateExitAdapter;
import com.kenportal.users.datamodels.AlertAbsentModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceNewActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    AppCompatTextView text_absent;         //Declaration of variable for absent
    AppCompatTextView text_late_entry;      //Declaration of variable for late entry
    AppCompatTextView text_late_exit;       //Declaration of variable for late exit
    //AppCompatButton selectDate;
    AppCompatEditText selectDate; //Declaration of variable for date picker
    Context context;
    AlertDialog.Builder alertDialog;                           //Declaration of alertdialog box to show data
    ArrayList<AlertAbsentModel> absentModelArrayList;         //Model object list for AlertAbsentModel used to store data
    ArrayList<AlertAbsentModel> lateEntryModelArrayList;      //Model object list for AlertAbsentModel used to store data
    ArrayList<AlertAbsentModel> exitModelArrayList;            //Model object list for AlertAbsentModel used to store data
    RecyclerView mRecycleView;
    AlertAbsentListAdapter absentListAdapter;
    AlertLateExitAdapter lateExitAdapter;
    AlertLateEntryAdapter lateEntryAdapter;

    private Calendar myCalendar = Calendar.getInstance(); // Declaration and initialization of calander object
    //Declaring an Spinner
    private AppCompatSpinner nameSpinner;
    ArrayAdapter<String> userNameAdapter;                       // reference  of userNameAdapter
    private LinearLayoutManager mLayoutManager;                 //reference of layoutmanager
    //An ArrayList for Spinner Items
    private ArrayList<String> username;



   /* int year=myCalendar.get(Calendar.YEAR);
    int month=myCalendar.get(Calendar.MONTH);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_new);
        //Getting reference of appcompact tool bar through id.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Setting title to the toolbar
        toolbar.setTitle("Attendance Report");
        //Setting title text color
        toolbar.setTitleTextColor(Color.WHITE);
        //setting toolbar to the action bar
        setSupportActionBar(toolbar);
        //to display back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initializing the ArrayList
        username = new ArrayList<String>();
          //fetch id to initializing
        text_absent = (AppCompatTextView) findViewById(R.id.text_absent);
        text_late_entry = (AppCompatTextView) findViewById(R.id.text_late_entry);
        text_late_exit = (AppCompatTextView) findViewById(R.id.text_late_exit);
        //Initializing Spinner
        nameSpinner= (AppCompatSpinner) findViewById(R.id.spinner);
        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        nameSpinner.setOnItemSelectedListener(this);

        text_late_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting late entry data
                getLateEntryData();

            }
        });
        text_late_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting late exit data
                getLateExitData();

            }
        });

        text_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting absent data
                getAbsentData();
                //alertDialog.setCancelable(true);

            }
        });
        //text_absent= (AppCompatButton)findViewById(R.id.text_absent);
        //Getting reference of select date button through its id.
        selectDate = (AppCompatEditText) findViewById(R.id.selectDate);

        //Initialization and adding date set listener to the date picker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        //Adding onclick listener to select date button
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceNewActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

              // getExtraData();

            }
        });

        //Getting user data to fill users name
        getUsersData();

    }

    private void getExtraData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.dashboardServerUrl + "/workingEffortSD" + "/1092" + "/2016" + "/5", null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("nout", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("workingEffortSDResult").getJSONObject(1).getJSONArray("data2");

                            Log.d("nout", jsonArray.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void getLateExitData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.dashboardServerUrl + "/attendanceStatusSD" + "/1092" + "/2016" + "/5" + "/2", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("OutPut", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("attendanceStatusSDResult");

                            Log.d("Resulttt", jsonArray.toString());
                            //Calling method getLateExitData to get the late exit data from the JSONArray
                            getLateExitData(jsonArray);


                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void getLateExitData(JSONArray jsonArray) {


        exitModelArrayList = new ArrayList<>();
        alertDialog = new AlertDialog.Builder(AttendanceNewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_late_exit, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Late Exit Detail");

        mRecycleView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
        mRecycleView.addItemDecoration(new SimpleDividerItemDecoration(AttendanceNewActivity.this));
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(AttendanceNewActivity.this);
        mRecycleView.setLayoutManager(mLayoutManager);

        //JSONObject json = jsonArray.getJSONObject(i);
        Gson converter = new Gson();
        Type type = new TypeToken<List<AlertAbsentModel>>() {
        }.getType();

        List<AlertAbsentModel> tempArrayList = converter.fromJson(String.valueOf(jsonArray), type);

        for (int i = 0; i < tempArrayList.size(); i++) {
            exitModelArrayList.add(tempArrayList.get(i));
        }

        lateExitAdapter = new AlertLateExitAdapter(AttendanceNewActivity.this, exitModelArrayList);
        mRecycleView.setAdapter(lateExitAdapter);
        alertDialog.show().getWindow().setLayout(600,500); //Controlling width and height.
    }

    private void getLateEntryData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.dashboardServerUrl + "/attendanceStatusSD" + "/1092" + "/2016" + "/5" + "/3", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("OutPut", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("attendanceStatusSDResult");
                            Log.d("Resulttt", jsonArray.toString());

                            //Calling method getLateEntryData to get the lateentrydata from the JSONArray
                             //fetch get late entry data
                            getLateEntryData(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void getLateEntryData(JSONArray jsonArray) {

        lateEntryModelArrayList = new ArrayList<>();
        alertDialog = new AlertDialog.Builder(AttendanceNewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_late_entry_data, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Late Entry Detail");

        mRecycleView = (RecyclerView) convertView.findViewById(R.id.my_recyclerview);
        mRecycleView.addItemDecoration(new SimpleDividerItemDecoration(AttendanceNewActivity.this));
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(AttendanceNewActivity.this);
        mRecycleView.setLayoutManager(mLayoutManager);

        Gson converter = new Gson();
        Type type = new TypeToken<List<AlertAbsentModel>>() {
        }.getType();
        List<AlertAbsentModel> tempArrayList = converter.fromJson(String.valueOf(jsonArray), type);

        for (int i = 0; i < tempArrayList.size(); i++) {
            lateEntryModelArrayList.add(tempArrayList.get(i));
        }

        lateEntryAdapter = new AlertLateEntryAdapter(AttendanceNewActivity.this, lateEntryModelArrayList);
        mRecycleView.setAdapter(lateEntryAdapter);
        alertDialog.show().getWindow().setLayout(600,500); //Controlling width and height.

    }

    private void getAbsentData() {

        JSONObject params = new JSONObject();
        String id = CustomPreference.with(AttendanceNewActivity.this).getString("id", "");
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);

        try {
            params.put("user_id", id);
            params.put("year", year);
            params.put("month", month);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.dashboardServerUrl + "/attendanceStatusSD" + "/1092" + "/2016" + "/5" + "/1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("OutPut", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("attendanceStatusSDResult");
                            Log.d("Resulttt", jsonArray.toString());
                            //Calling method getAbsentData to get the absentdata from the JSONArray
                            getAbsentData(jsonArray);
                            //alertDialog.setCancelable(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.setCancelable(true);
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void getAbsentData(JSONArray jsonArray) {


        absentModelArrayList = new ArrayList<AlertAbsentModel>();

        alertDialog = new AlertDialog.Builder(AttendanceNewActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_absent_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Absent Detail");
        alertDialog.setCancelable(true);

        mRecycleView = (RecyclerView) convertView.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(AttendanceNewActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.scrollToPosition(0);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addItemDecoration(new SimpleDividerItemDecoration(AttendanceNewActivity.this));
        Gson converter = new Gson();
        Type type = new TypeToken<List<AlertAbsentModel>>() {
        }.getType();

        List<AlertAbsentModel> tempArrayList = converter.fromJson(String.valueOf(jsonArray), type);

        for (int i = 0; i < tempArrayList.size(); i++) {
            absentModelArrayList.add(tempArrayList.get(i));
        }

        absentListAdapter = new AlertAbsentListAdapter(AttendanceNewActivity.this, absentModelArrayList);
        mRecycleView.setAdapter(absentListAdapter);
        alertDialog.show().getWindow().setLayout(600,500); //Controlling width and height..

    }

    //String url= ServerLinks.dashboardServerUrl+"/userSD/";

    private void getUsersData() {

        JSONObject params = new JSONObject();
        String id = CustomPreference.with(AttendanceNewActivity.this).getString("id", "");
        System.out.println("Result Id " + id);
        try {
            params.put("user_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.dashboardServerUrl + "/userSD/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Result", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("userSDResult");
                            Log.d("Resulttt", jsonArray.toString());
                            //Calling method getUserName to get the username from the JSONArray
                            //getUserName(jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //Getting json object
                                JSONObject json = jsonArray.getJSONObject(i);
                                //Adding the name of the student to array list
                                username.add(json.getString("USER_NAME"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Setting adapter to show the items in the spinner

                        userNameAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, username);
                        userNameAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        nameSpinner.setAdapter(userNameAdapter);
                        //AttNameSpiner.setAdapter(new ArrayAdapter<String>(AttendanceNewActivity.this,R.layout.spinner_dropdown_item, username));

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

    //to update the text view with selected date
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        selectDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //String userName = spinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
