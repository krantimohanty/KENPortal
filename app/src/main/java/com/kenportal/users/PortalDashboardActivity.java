package com.kenportal.users;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.fragment.CommunicationFragment;
import com.kenportal.users.fragment.FeedFragment;
import com.kenportal.users.fragment.InformationFragment;
import com.kenportal.users.fragment.SelfServiceFragment;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.local_db.DbModel;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.utils.IcoMoonIcons;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortalDashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView office_in, office_out;
    private int wish_number = 0;
    private TextView ui_wish_num = null;
    private TextView action_count, empName, empDesignation;
    ImageView profilePic;
    private RelativeLayout action_notification;
    //    IconTextView status_badge;
    private String getEmployeeresponse, id, emp_name, photo, emp_code, date_joining, designation, section_id, section, dept_id, dept, office_loc, ra_id, ra, email, mobile, pre_address, pre_city, pre_city_id, pre_state, pre_country, pre_tel_r, pre_tel_o, per_address, per_city, per_city_id, per_state, per_country, per_tel_r, per_tel_o, offc_id;
    //DbHelper
    DbHelper dbHelper = new DbHelper(PortalDashboardActivity.this);
    Dialog dialog;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView coverPic;
    CallbackManager callbackManager;
    ImageView pic;
    SharedPreferences facebook_prefs;

    int[] pop_img = {R.drawable.green_dot,
            R.drawable.yellow_dot,
            R.drawable.blue_dot,
            R.drawable.red_dot,
            R.drawable.gray_dot};
    String[] pop_text = {"Present", "Tour", "Leave", "Absent", "Local Visit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("KENPortal");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        toolbar.setContentInsetsAbsolute(0, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.other_wish);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getVibrantSwatch().getRgb();
                collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });

        /*
        * Shared preferences for map activity
        * */
        try {
            SharedPreferences prefs = getSharedPreferences("MAP_FLAG", Context.MODE_PRIVATE);
            String flg = prefs.getString("check_flsg", "");
            if (flg.equals("")) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("check_flsg", "true");
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                PortalDashboardActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(PortalDashboardActivity.this);
        View v = navigationView.getHeaderView(0);

        TextView text1 = (TextView) v.findViewById(R.id.navEmpName);
        text1.setText(CustomPreference.with(PortalDashboardActivity.this).getString("full_name", ""));
        TextView text2 = (TextView) v.findViewById(R.id.navEmpDesg);
        text2.setText(CustomPreference.with(PortalDashboardActivity.this).getString("designation", ""));

        //status dropdown
//        status_badge = (IconTextView) findViewById(R.id.status_badge);
//        status_badge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PortalDashboardActivity.this);
//                LayoutInflater inflater = getLayoutInflater();
//                View convertView = (View) inflater.inflate(R.layout.pop_list, null);
//                alertDialog.setView(convertView);
//                //alertDialog.setTitle("Empo");
//                ListView lv = (ListView) convertView.findViewById(R.id.pop_list);
//                lv.setActivated(false);
//                PopUpAdapter popUpAdapter = new PopUpAdapter();
//                lv.setAdapter(popUpAdapter);
//                AlertDialog dialog = alertDialog.show();
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(dialog.getWindow().getAttributes());
//                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                lp.width = 420;
//                dialog.getWindow().setAttributes(lp);
//                //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//            }
//        });

        office_in = (TextView) findViewById(R.id.office_in);
        office_out = (TextView) findViewById(R.id.office_out);

        /*action_count = (TextView) findViewById(R.id.action_count);
        action_notification = (RelativeLayout) findViewById(R.id.action_notification);*/
        empName = (TextView) findViewById(R.id.empName);
        empName.setText(CustomPreference.with(PortalDashboardActivity.this).getString("full_name", ""));
        empDesignation = (TextView) findViewById(R.id.empDesignation);
        // empDesignation.setText(CustomPreference.with(PortalDashboardActivity.this).getString("designation", "") + " & " + CustomPreference.with(PortalDashboardActivity.this).getString("dept_Name", ""));
        empDesignation.setText(CustomPreference.with(PortalDashboardActivity.this).getString("designation", ""));
        //get action count

//        new GetBadgeNum().execute();
        /*action_notification.setOnClickListener(new View.OnClickListener() {
        /*action_notification.setOnClickListener(new View.OnClickListener() {
        /*action_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PortalDashboardActivity.this, ActionActivity.class));
            }
        });*/
        //nested scrollview
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);
        scrollView.setSmoothScrollingEnabled(true);

        //tab and View pager
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        RelativeLayout tabParent = (RelativeLayout) findViewById(R.id.tabLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getBackground().setAlpha(0);

        coverPic = (ImageView) findViewById(R.id.coverPic);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        pic = (ImageView) v.findViewById(R.id.emp_pic);
         /*
        * Shared preferences for facebook connect profilr
        * */
        try {

            Log.d("atag", CustomPreference.with(this).getBoolean("fb_permission", false)+"  hello");

            if (CustomPreference.with(this).getBoolean("fb_permission", false)) {
                facebook_prefs = getSharedPreferences("FB_Flag", Context.MODE_PRIVATE);
                String prf_flg = facebook_prefs.getString("profile_pic", "");
                if (prf_flg.equals("")) {
                    facebookAlert();
                } else {
                    facebookImgBind();
                }
            }else {
                getPortalFeed();
                startEmployeeThread();
            }
        } catch (Exception e) {
            getPortalFeed();
            startEmployeeThread();
            e.printStackTrace();
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PortalDashboardActivity.this, ViewEmployeeDetail.class);
//                i.putExtra("flag", "dash");
//                i.putExtra("id", CustomPreference.with(PortalDashboardActivity.this).getString("id", ""));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetBadgeNum().execute();
        Log.i("atag", "resume called");

    }

    private void facebookAlert() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        Button ysBtn = (Button) dialog.findViewById(R.id.yesBtn);
        // if button is clicked, close the custom dialog
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPortalFeed();
                startEmployeeThread();
                dialog.cancel();
            }
        });

        ysBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (ConnectionDetector.staticisConnectingToInternet(PortalDashboardActivity.this)) {
                    intializeFacebookSdk();
                } else {
                    showErrorMessage("Network connection is not available.");
                    getPortalFeed();
                    startEmployeeThread();
                }
            }
        });
        dialog.show();


//        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Do you want to link it with your Facebook profile ?");
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                if (ConnectionDetector.staticisConnectingToInternet(PortalDashboardActivity.this)) {
//                    intializeFacebookSdk();
//                }else{
//                    Snackbar.make(findViewById(android.R.id.content), "Network connection is not available.", Snackbar.LENGTH_LONG).show();
//                    getPortalFeed();
//                    startEmployeeThread();
//                }
//            }
//        });
//
//        alertDialog.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                getPortalFeed();
//                startEmployeeThread();
//                dialog.cancel();
//            }
//        });
//
//        alertDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getPortalFeed() {
        String profilePicurl = ServerLinks.smallPhoto + CustomPreference.with(PortalDashboardActivity.this).getString("profilePic", "").replace(" ", "%20");
//        String coverPicurl=object.getJSONObject("cover").getString("source");
        //load profile image
        Picasso.with(PortalDashboardActivity.this)
                .load(profilePicurl)
                .placeholder(R.drawable.userpic)
                .error(R.drawable.userpic)
                .into(profilePic);
        //load navigation profile image
        Picasso.with(PortalDashboardActivity.this)
                .load(profilePicurl)
                .placeholder(R.drawable.userpic)
                .error(R.drawable.userpic)
                .into(pic);
        //load cover image
//        Picasso.with(PortalDashboardActivity.this)
//                .load(coverPicurl)
//                .placeholder(R.drawable.other_wish)
//                .error(R.drawable.other_wish)
//                .into(coverPic);

//        coverPic.setBackgroundResource(R.drawable.cover_pic);

    }


    private void intializeFacebookSdk() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile,publish_actions "));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    String profilePic = "https://graph.facebook.com/" + object.getString("id") + "/picture?width=80&height=80";
                                    String coverPicurl = object.getJSONObject("cover").getString("source");
                                    SharedPreferences.Editor editor = facebook_prefs.edit();
                                    editor.putString("profile_pic", profilePic);
                                    editor.putString("cover_pic", coverPicurl);
                                    editor.commit();
                                    facebookImgBind();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,cover");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        getPortalFeed();
                        startEmployeeThread();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        getPortalFeed();
                        startEmployeeThread();
                    }
                });
    }

    private void facebookImgBind() {

        try {
            String prf_img = facebook_prefs.getString("profile_pic", "");
            String cvr_img = facebook_prefs.getString("cover_pic", "");

            Picasso.with(PortalDashboardActivity.this)
                    .load(prf_img)
                    .placeholder(R.drawable.userpic)
                    .error(R.drawable.userpic)
                    .into(profilePic);
            //load navigation profile image
            Picasso.with(PortalDashboardActivity.this)
                    .load(prf_img)
                    .placeholder(R.drawable.userpic)
                    .error(R.drawable.userpic)
                    .into(pic);
            //load cover image
            Picasso.with(PortalDashboardActivity.this)
                    .load(cvr_img)
                    .into(coverPic);

            startEmployeeThread();


        } catch (Exception e) {
            startEmployeeThread();
        }

    }

    private void startEmployeeThread() {
        if (ConnectionDetector.staticisConnectingToInternet(this)) {
            if (dbHelper.getDbCount() == 0) {
                new GetEmployeeThread().execute();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new SelfServiceFragment(), "Self Service");
        adapter.addFragment(new InformationFragment(), "Information");
        adapter.addFragment(new FeedFragment(), "Social");
        adapter.addFragment(new CommunicationFragment(), "Communication");
        viewPager.setAdapter(adapter);
    }

    //::::::to clear cache
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //getemployee thread
    public class GetEmployeeThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Include dialog.xml file
            dialog = new Dialog(PortalDashboardActivity.this, R.style.Theme_D1NoTitleDim);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.0f;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_progress_view);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getEmployee();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    dialog.dismiss();
                }
            }, 2000);

            super.onPostExecute(aVoid);
        }

    }


    public class GetBadgeNum extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAttendanceStatus();
            getBadgeCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }

    //GetAll employee
    public void getEmployee() {
        //appProgressDialog.initializeProgress();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/getemployees", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONObject empObject = response.getJSONObject("getemployeesResult");
                    JSONArray jr = empObject.getJSONArray("employees");
                    for (int i = 0; i < jr.length(); i++) {

                        JSONObject jsonObjectProfile = (JSONObject) jr.get(i);
                        emp_name = jsonObjectProfile.getString("name");
                        designation = jsonObjectProfile.getString("designation");
                        photo = jsonObjectProfile.getString("photo");
                        id = jsonObjectProfile.getString("id");
                        emp_code = jsonObjectProfile.getString("emp_code");
                        date_joining = jsonObjectProfile.getString("date_joining");
                        designation = jsonObjectProfile.getString("designation");
                        section_id = jsonObjectProfile.getString("section_id");
                        section = jsonObjectProfile.getString("section");
                        dept_id = jsonObjectProfile.getString("dept_id");
                        dept = jsonObjectProfile.getString("dept");
                        office_loc = jsonObjectProfile.getString("office_location");
                        ra_id = jsonObjectProfile.getString("ra_id");
                        ra = jsonObjectProfile.getString("ra");
                        email = jsonObjectProfile.getString("email");
                        mobile = jsonObjectProfile.getString("mobile");
                        pre_address = jsonObjectProfile.getString("pre_address");
                        pre_city = jsonObjectProfile.getString("pre_city");
                        pre_city_id = jsonObjectProfile.getString("pre_city_id");
                        pre_state = jsonObjectProfile.getString("pre_state");
                        pre_country = jsonObjectProfile.getString("pre_country");
                        pre_tel_r = jsonObjectProfile.getString("pre_tel_r");
                        pre_tel_o = jsonObjectProfile.getString("pre_tel_o");
                        per_address = jsonObjectProfile.getString("per_address");
                        per_city = jsonObjectProfile.getString("per_city");
                        per_city_id = jsonObjectProfile.getString("per_city_id");
                        per_state = jsonObjectProfile.getString("per_state");
                        per_country = jsonObjectProfile.getString("per_country");
                        per_tel_r = jsonObjectProfile.getString("per_tel_r");
                        per_tel_o = jsonObjectProfile.getString("per_tel_o");
                        offc_id = jsonObjectProfile.getString("office_locationid");
                        //List<String> empStatus = new ArrayList<String>();
                        //inserting to local sqlite db
                        dbHelper.addAllEmp(new DbModel(id, emp_name, photo, emp_code, date_joining, designation, section_id, section, dept_id, dept, office_loc, ra_id, ra, email, mobile, pre_address, pre_city, pre_city_id, pre_state, pre_country, pre_tel_r, pre_tel_o, per_address, per_city, per_city_id, per_state, per_country, per_tel_r, offc_id, per_tel_o, ""));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }


                //appProgressDialog.hideProgressDialog();
               /* Intent i = new Intent(DownloadingActivity.this, PortalDashboardActivity.class);
                // i.putExtra("chng_flag", "new");
                startActivity(i);
                finish();*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.Please try later.", Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snack.getView();
                group.setBackgroundColor(ContextCompat.getColor(PortalDashboardActivity.this, R.color.colorPrimary));
                snack.setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetEmployeeThread().execute();
                    }
                })
                        .setActionTextColor(Color.WHITE).show();
            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }


    //get badge count
    public void getBadgeCount() {

        //initialize progressdialog
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/get_badge_data/" + CustomPreference.with(PortalDashboardActivity.this).getString("id", ""), new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.i("atag", response.toString());
                            CustomPreference.with(PortalDashboardActivity.this).save("badge_count", response.toString());
                            JSONObject s = response.getJSONObject("get_badge_dataResult");
                            if (s.getString("get_action_count").equals("")) {
                            } else {
                                updateWishCount(Integer.parseInt(s.getString("get_action_count")));
                            }

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
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }


    //get attendance
    //get badge count
    public void getAttendanceStatus() {

        //initialize progressdialog
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/get_today_attendance/" + CustomPreference.with(PortalDashboardActivity.this).getString("id", ""), new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parsing json object response
                            // response will be a json object
                            JSONObject s = response.getJSONObject("get_today_attendanceResult");
                            Animation popnaim = AnimationUtils.loadAnimation(PortalDashboardActivity.this, R.anim.popup_in);
                            if (s.getString("office_in_time").equals("")) {
                                office_in.setText(s.getString("--"));
                            } else {
                                office_in.setText(s.getString("office_in_time"));
                                office_in.setAnimation(popnaim);
                            }

                            if (s.getString("office_out_time").equals("")) {
                                office_out.setText(s.getString("--"));
                            } else {
                                office_out.setText(s.getString("office_out_time"));
                                office_out.setAnimation(popnaim);
                            }

                            /*SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
                            try {
                                Date inTime = parser.parse(s.getString("office_in_time").replace("AM", "").replace("PM", "").trim());
                                Date outTime = parser.parse(s.getString("office_out_time").replace("AM", "").replace("PM", "").trim());
                                Date userInDate = parser.parse("9:10:00");
                                Date userOutDate = parser.parse("6:00:00");

                                Drawable img = getResources().getDrawable(R.drawable.red_dot);
                                img.setBounds(0, 0, 9, 9);

                                if (userInDate.after(inTime)) {
                                    office_in.setCompoundDrawables(img, null, null, null);
                                    office_out.setCompoundDrawables(img, null, null, null);
                                } else if (userOutDate.before(outTime)) {
                                    office_in.setCompoundDrawables(img, null, null, null);
                                    office_out.setCompoundDrawables(img, null, null, null);
                                } else {
                                    Drawable img1 = getResources().getDrawable(R.drawable.green_dot);
                                    img.setBounds(0, 0, 9, 9);
                                    office_in.setCompoundDrawables(img1, null, null, null);
                                    office_out.setCompoundDrawables(img1, null, null, null);
                                }
                            } catch (ParseException p) {

                            }*/

                            //updateWishCount(wish_number);
                            //getActionCount(action_count);

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
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
//            finish();
            this.moveTaskToBack(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent intent = new Intent(PortalDashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.change_Pin) {
            Intent i = new Intent(PortalDashboardActivity.this, PinActivity.class);
            i.putExtra("chng_flag", "change");
            startActivity(i);
        } else if (id == R.id.sync_with_portal) {
//            new GetEmployeeThread().execute();
            getEmployeeData();
        } else if (id == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PortalDashboardActivity.this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Logout");
            builder.setMessage("Do you want to logout ?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CustomPreference.with(PortalDashboardActivity.this).removeAll();

                    File cache = getCacheDir();
                    File appDir = new File(cache.getParent());
                    if (appDir.exists()) {
                        String[] children = appDir.list();
                        for (String s : children) {
                            if (!s.equals("lib")) {
                                deleteDir(new File(appDir, s));
                                startActivity(new Intent(PortalDashboardActivity.this, LoginActivity.class));
                            }
                        }
                    }
                }
            });

            builder.show();
        } else if (id == R.id.feedback) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("vnd.android.cursor.item/email");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"support@swashconvergence.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
        }
//        else if (id == R.id.rate_app) {
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("market://details?id=" + PortalDashboardActivity.this.getPackageName())));
//            } catch (android.content.ActivityNotFoundException e)
//            {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + PortalDashboardActivity.this.getPackageName())));
//            }
//        }
//        else if (id == R.id.app_version) {
//            int versionCode = BuildConfig.VERSION_CODE;
//            String versionName = BuildConfig.VERSION_NAME;
//            AlertDialog.Builder builder = new AlertDialog.Builder(PortalDashboardActivity.this, R.style.AppCompatAlertDialogStyle);
//            builder.setTitle("Swash Online");
//            builder.setMessage("Version Name: " + versionName + "\n" + "Version Code: " + versionCode);
//            builder.setCancelable(true);
//            builder.show();
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getEmployeeData() {

        dialog = new Dialog(PortalDashboardActivity.this, R.style.Theme_D1NoTitleDim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_view);
        dialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/getemployees", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject empObject = response.getJSONObject("getemployeesResult");
                    JSONArray jr = empObject.getJSONArray("employees");
                    dbHelper.deleteEmployeeData();
                    for (int i = 0; i < jr.length(); i++) {

                        JSONObject jsonObjectProfile = (JSONObject) jr.get(i);
                        emp_name = jsonObjectProfile.getString("name");
                        designation = jsonObjectProfile.getString("designation");
                        photo = jsonObjectProfile.getString("photo");
                        id = jsonObjectProfile.getString("id");
                        emp_code = jsonObjectProfile.getString("emp_code");
                        date_joining = jsonObjectProfile.getString("date_joining");
                        designation = jsonObjectProfile.getString("designation");
                        section_id = jsonObjectProfile.getString("section_id");
                        section = jsonObjectProfile.getString("section");
                        dept_id = jsonObjectProfile.getString("dept_id");
                        dept = jsonObjectProfile.getString("dept");
                        office_loc = jsonObjectProfile.getString("office_location");
                        ra_id = jsonObjectProfile.getString("ra_id");
                        ra = jsonObjectProfile.getString("ra");
                        email = jsonObjectProfile.getString("email");
                        mobile = jsonObjectProfile.getString("mobile");
                        pre_address = jsonObjectProfile.getString("pre_address");
                        pre_city = jsonObjectProfile.getString("pre_city");
                        pre_city_id = jsonObjectProfile.getString("pre_city_id");
                        pre_state = jsonObjectProfile.getString("pre_state");
                        pre_country = jsonObjectProfile.getString("pre_country");
                        pre_tel_r = jsonObjectProfile.getString("pre_tel_r");
                        pre_tel_o = jsonObjectProfile.getString("pre_tel_o");
                        per_address = jsonObjectProfile.getString("per_address");
                        per_city = jsonObjectProfile.getString("per_city");
                        per_city_id = jsonObjectProfile.getString("per_city_id");
                        per_state = jsonObjectProfile.getString("per_state");
                        per_country = jsonObjectProfile.getString("per_country");
                        per_tel_r = jsonObjectProfile.getString("per_tel_r");
                        per_tel_o = jsonObjectProfile.getString("per_tel_o");
                        offc_id = jsonObjectProfile.getString("office_locationid");
                        //List<String> empStatus = new ArrayList<String>();
                        //inserting to local sqlite db
                        dbHelper.addAllEmp(new DbModel(id, emp_name, photo, emp_code, date_joining, designation, section_id, section, dept_id, dept, office_loc, ra_id, ra, email, mobile, pre_address, pre_city, pre_city_id, pre_state, pre_country, pre_tel_r, pre_tel_o, per_address, per_city, per_city_id, per_state, per_country, per_tel_r, offc_id, per_tel_o, ""));

                    }

                    dialog.hide();

                    showErrorMessage("Portal sync done successfully.");

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.hide();
                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.Please try later.", Snackbar.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) snack.getView();
                    group.setBackgroundColor(ContextCompat.getColor(PortalDashboardActivity.this, R.color.colorPrimary));
                    snack.setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new GetEmployeeThread().execute();
                        }
                    })
                            .setActionTextColor(Color.WHITE).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.Please try later.", Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snack.getView();
                group.setBackgroundColor(ContextCompat.getColor(PortalDashboardActivity.this, R.color.colorPrimary));
                snack.setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetEmployeeThread().execute();
                    }
                })
                        .setActionTextColor(Color.WHITE).show();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");

    }

    public class PopUpAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pop_img.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.pop_list_item, null);
            TextView popTxt = (TextView) convertView.findViewById(R.id.pop_txt);
            ImageView popImg = (ImageView) convertView.findViewById(R.id.pop_img);
            IconTextView popCheck = (IconTextView) convertView.findViewById(R.id.pop_check);

            popTxt.setText(pop_text[position]);
            popImg.setImageResource(pop_img[position]);
            if (position == 0) {
                popCheck.setVisibility(View.VISIBLE);
            } else {
                popCheck.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        MenuItem item = menu.findItem(R.id.action_hot_news);

        MenuItemCompat.setActionView(item, R.layout.wish_feed_update_count);
        View view = MenuItemCompat.getActionView(item);
        ImageView img = (ImageView) view.findViewById(R.id.wishes);
        img.setImageDrawable(
                new IconDrawable(this, IcoMoonIcons.ic_notofictn)
                        .colorRes(R.color.white)
                        .actionBarSize());

        ui_wish_num = (TextView) view.findViewById(R.id.num_of_wish);
        ui_wish_num.setVisibility(View.VISIBLE);
        //new GetBadgeNum.execute();
        //updateWishCount(wish_number);
        // updateWishCount(3);
        new MyMenuItemStuffListener(view, "Swash Alerts") {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PortalDashboardActivity.this, ActionActivity.class));
            }
        };
        return super.onCreateOptionsMenu(menu);
    }

    public void updateWishCount(final int num_of_wish) {
        wish_number = num_of_wish;
        if (ui_wish_num == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (num_of_wish == 0)
                    ui_wish_num.setVisibility(View.GONE);
                else {
                    ui_wish_num.setVisibility(View.VISIBLE);
                    ui_wish_num.setText(Integer.toString(num_of_wish));
                }
            }
        });
    }

    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        abstract public void onClick(View v);

        @Override
        public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }

    private void showErrorMessage(String msg) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(PortalDashboardActivity.this, R.color.colorPrimary));
        snack.setActionTextColor(Color.WHITE).show();
    }

}
