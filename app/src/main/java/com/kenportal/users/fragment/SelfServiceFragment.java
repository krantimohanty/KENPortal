package com.kenportal.users.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.AttendanceNewActivity;
import com.kenportal.users.EmployeeDirectoryActivity;
import com.kenportal.users.MapsActivity;
import com.kenportal.users.R;
import com.kenportal.users.SalarySlipActivity;
import com.kenportal.users.adapter.EISAdapter;
import com.kenportal.users.datamodels.WishAdapterModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfServiceFragment extends Fragment {

    View rootView;
    GridView gridView;
    EISAdapter eisAdapter;
    WishAdapterModel[] wishAdapterModels;
    private String wish_number, action_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_self, container, false);

        //get badge count thread
        new GetBadgeNum().execute();

        gridView = (GridView) rootView.findViewById(R.id.grid_self_service);


        wishAdapterModels = new WishAdapterModel[]{

                new WishAdapterModel("{ic-attend}", "Attendance", "-"),//"dis" stands for disable icon
                new WishAdapterModel("{ic-empdir}", "Employee directory", "-"),
                new WishAdapterModel("{ic-emptrack}", "Location", "-"),
//                new WishAdapterModel("{ic-wish}", "Best Wishes", "-"),
                new WishAdapterModel("{ic-salslip}", "Salary Slip", "-")
        };
        eisAdapter = new EISAdapter(getActivity(), wishAdapterModels);
        gridView.setAdapter(eisAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), AttendanceNewActivity.class));
                        break;

                    case 1:
                        Intent intent = new Intent(getActivity(), EmployeeDirectoryActivity.class);
                        intent.putExtra("filter_flag", "dashbrd");
                        startActivity(intent);
                        break;

                    case 2:
                        Intent i = new Intent(getActivity(), MapsActivity.class);
                        i.putExtra("flag", "Dash");
                        startActivity(i);
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), SalarySlipActivity.class));
                        break;
                    default:
                        break;

                }
            }
        });
        return rootView;
    }

    public class GetBadgeNum extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getBadgeCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }

    //get badge count
    public void getBadgeCount() {

        //initialize progressdialog
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/get_badge_data/" + CustomPreference.with(getActivity()).getString("id", ""), new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            // Parsing json object response
                            // response will be a json object
                            JSONObject s = response.getJSONObject("get_badge_dataResult");
                            wish_number = s.getString("get_wish_count");
                            action_count = s.getString("get_action_count");
                            Log.d("no:", action_count);
                            //updateWishCount(wish_number);
                            //getActionCount(action_count);

                            wishAdapterModels = new WishAdapterModel[]{
                                    new WishAdapterModel("{ic-empdir}", "Employee directory", "-"),
                                    new WishAdapterModel("{ic-emptrack}", "Colleagues Location", "-"),
                                    new WishAdapterModel("{ic-wish}", "Best Wishes", wish_number),
                                    new WishAdapterModel("{ic-attend}", "Attendance", "-"),
                                    new WishAdapterModel("{ic-salslip}", "Salary Slip","-")
                            };
                            eisAdapter = new EISAdapter(getActivity(), wishAdapterModels);
                            //eisAdapter.clear();
                            gridView.setAdapter(eisAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", "error" + error.toString());


            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }


    private void snackBarAlert(){
        Snackbar.make(getActivity().findViewById(android.R.id.content), "Comming soon...", Snackbar.LENGTH_LONG) .show();
    }


}
