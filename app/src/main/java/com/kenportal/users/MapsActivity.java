package com.kenportal.users;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.custom_searchview.SearchBox;
import com.kenportal.users.custom_searchview.SearchResult;
import com.kenportal.users.utils.ConnectionDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.local_db.DbModel;
import com.kenportal.users.map.Mapmarker;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.progress_dialog.AppProgressDialog;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.utils.GPSTracker;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private ArrayList<Mapmarker> mMyMarkersArray;
    private ArrayList<Mapmarker> dbmMyMarkersArray;
    private HashMap<Marker, Mapmarker> mMarkersHashMap;
    private GoogleApiClient mGoogleApiClient;
    GPSTracker gps;
    LocationRequest mLocationRequest;
    ArrayList<LatLng> latLngArrayList;
    ArrayList<LatLng> dblatLngArrayList;
    ArrayList<Mapmarker> allMarkerArrayList;
    LatLng currentCoordinates;
    Toolbar toolbar;

    String latlongdt = "";
    String name = "";
    String designation = "";
    String pic = "";

    AppProgressDialog appProgressDialog;
    String id = "";
    JSONArray empData;
    Marker now;
    SharedPreferences prefs;
    SharedPreferences user_prefs;
    String strDateTime;
    String flg_check, use_flg_check;
    SearchBox search;
    List<DbModel> allEmpDirectoryModelList;
    DbHelper dbHelper;
    Bitmap icon,icon1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        IconDrawable drawable = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.colorAccent).sizeDp(35);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        icon = bitmap;

        IconDrawable drawable1 = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.colorPrimary).sizeDp(35);
        Bitmap bitmap1 = Bitmap.createBitmap(drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap1);
        drawable1.setBounds(0, 0, canvas1.getWidth(), canvas1.getHeight());
        drawable1.draw(canvas1);
        icon1 = bitmap1;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        //Getting search box
        search = (SearchBox) findViewById(R.id.searchbox);
//        search.enableVoiceRecognition(this);
        allEmpDirectoryModelList = new ArrayList<DbModel>();
//        new GetEmployeeThread().execute();
        dbHelper = new DbHelper(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        strDateTime = sdf.format(c.getTime());

        prefs = getSharedPreferences("MAP_FLAG", Context.MODE_PRIVATE);
        user_prefs = getSharedPreferences("MAP_USER_FLAG", Context.MODE_PRIVATE);

        flg_check = prefs.getString("check_flsg", "");
        use_flg_check = user_prefs.getString("user_check_flsg", "");

        mMyMarkersArray = new ArrayList<Mapmarker>();
        dbmMyMarkersArray = new ArrayList<Mapmarker>();
        latLngArrayList = new ArrayList<LatLng>();
        dblatLngArrayList = new ArrayList<LatLng>();
        allMarkerArrayList = new ArrayList<Mapmarker>();
        //progress
        appProgressDialog = new AppProgressDialog(MapsActivity.this, "Sharing...");
        //Getting user name

        id = CustomPreference.with(MapsActivity.this).getString("id", "");
        name = CustomPreference.with(MapsActivity.this).getString("full_name", "");
        pic = CustomPreference.with(MapsActivity.this).getString("profilePic", "");
        designation = CustomPreference.with(MapsActivity.this).getString("designation", "");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMap();
        mapFragment.getMapAsync(this);

        // Initialize the HashMap for Markers and MyMarker object
        mMarkersHashMap = new HashMap<Marker, Mapmarker>();

        // Connect the client.
        mGoogleApiClient.connect();
    }

    private void getUserMarker() {

        //initialize progressdialog
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServerLinks.ServerUrl + "/get_employee_location/" + id, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            empData = response.getJSONObject("get_employee_locationResult").getJSONArray("employee_location");
                            for (int i = 0; i < empData.length(); i++) {
                                String emp_id = empData.getJSONObject(i).getString("employee_id");
                                String time = empData.getJSONObject(i).getString("shared_time");
                                if (!emp_id.equals(id)) {
                                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                                    String latlongLoca = empData.getJSONObject(i).getString("location");
                                    String lotlong[] = latlongLoca.split(",");
                                    Double lat = Double.parseDouble(lotlong[0]);
                                    Double lng = Double.parseDouble(lotlong[1]);

                                    DbModel dbm = dbHelper.getObjectEmpById(emp_id);
                                    List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
                                    currentCoordinates = new LatLng(lat, lng);
                                    dblatLngArrayList.add(currentCoordinates);

                                    if (addresses.size() > 0) {
                                        dbmMyMarkersArray.add(new Mapmarker(dbm.getEmp_name(), dbm.getEmp_designation(), dbm.getEmp_photo(), addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + "\n" + addresses.get(0).getAddressLine(2), "R.mipmap.ic_launcher", lat, lng, time));
                                        allMarkerArrayList.add(new Mapmarker(dbm.getEmp_name(), dbm.getEmp_designation(), dbm.getEmp_photo(), addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + "\n" + addresses.get(0).getAddressLine(2), "R.mipmap.ic_launcher", lat, lng, time));
                                    }
                                    addAllMarker(dbm.getEmp_name(), dbmMyMarkersArray, dblatLngArrayList);
                                }
                            }
                        } catch (JSONException e) {
                            showErrorMessage("Something went wrong.");
                        } catch (IOException e) {
                            showErrorMessage("Something went wrong.");
                        } catch (Exception e) {
                            showErrorMessage("Something went wrong.");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showErrorMessage("Something went wrong.");
                    }
        });

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "get_employee_location");


    }

    private void shareAllUserLocation(final String ids) {
        appProgressDialog.initializeProgress();
        String url = ServerLinks.ServerUrl + "/share_employee_location";
        JSONObject params = new JSONObject();
        String id = CustomPreference.with(MapsActivity.this).getString("id", "");
        try {
            params.put("user_id", id);
            params.put("user_geolocation", latlongdt);
            params.put("to_user_id", ids);
        } catch (Exception e) {
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        appProgressDialog.hideProgressDialog();
                        try {

                            String res = response.getString("result");
                            if (res.equals("success")) {
                                String msg;
                                if (ids.equals("0")) {
                                    msg = "Your location is successfully shared to all employees of Swash.";
                                } else {
                                    msg = "Your location is successfully shared to some specific employees of Swash.";
                                }
                                showErrorMessage(msg);

                            } else {
                                showErrorMessage("Something went wrong.Please try after sometimes.");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            showErrorMessage("Something went wrong.Please try after sometimes.");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appProgressDialog.hideProgressDialog();
                        showErrorMessage("Something went wrong.Please try after sometimes.");
                    }
                });
            //Retry policy of request queue
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, "share_all_location");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        // Connect the client.
//        Log.i("atag", "on start called");
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
//        Log.i("atag", "on stop called");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        addOwnmarker("auto");

    }


    private void addOwnmarker(String flg){
    try {

        gps = new GPSTracker(MapsActivity.this);
        //Getting user's marker

        // check if GPS enabled
        if (gps.canGetLocation()) {
            getUserMarker();
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            // mLocationRequest.setInterval(1000); // Update location every second
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            if (now != null) {
                now.remove();
            }
            List<Address> addresses = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            currentCoordinates = new LatLng(gps.getLatitude(), gps.getLongitude());
            latLngArrayList.add(currentCoordinates);
            latlongdt = currentCoordinates.latitude + "," + currentCoordinates.longitude;

            if(flg.equals("auto")) {

                if (latlongdt.equals("0.0,0.0") || latlongdt.equals("")) {
                    showErrorMessage("System is unable to get your current location .Please check your location setting .");
                } else {

                    if (!use_flg_check.equals("")) {
                        shareAllUserLocation(use_flg_check);
                    } else if (flg_check.equals("true")) {
                        shareAllUserLocation("0");
                    }
                }
            }

            try {
                addresses = gcd.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
                if (addresses.size() > 0) {
                    mMyMarkersArray.add(new Mapmarker(name, designation, pic, addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + "\n" + addresses.get(0).getAddressLine(2), "R.mipmap.ic_launcher", currentCoordinates.latitude, currentCoordinates.longitude, strDateTime));
                    allMarkerArrayList.add(new Mapmarker(name, designation, pic, addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + "\n" + addresses.get(0).getAddressLine(2), "R.mipmap.ic_launcher", currentCoordinates.latitude, currentCoordinates.longitude, strDateTime));
                }

                addAllMarker(name, mMyMarkersArray, latLngArrayList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();

        }

    }catch (Exception e){

    }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Log.i(TAG, "GoogleApiClient connection has failed");
    }


    @Override
    public void onLocationChanged(Location location) {

//        if (now != null) {
//            now.remove();
//        }
//        List<Address> addresses = null;
//        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//        currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
//        latLngArrayList.add(currentCoordinates);
//        latlongdt = currentCoordinates.latitude + "," + currentCoordinates.longitude;
//        if (location != null) {
//                try {
//                    addresses = gcd.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
//                    if (addresses.size() > 0) {
//                        mMyMarkersArray.add(new Mapmarker(name, designation, addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + "\n" + addresses.get(0).getAddressLine(2), "R.mipmap.ic_launcher", currentCoordinates.latitude, currentCoordinates.longitude));
//                    }
//
//                    addAllMarker();
//
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//        }
    }

    private void addAllMarker(String nm, ArrayList<Mapmarker> marker, ArrayList<LatLng> ltlg) {

//        Log.i("atag", "hello   " + marker.size());

//        now = mMap.addMarker(new MarkerOptions().position(ltlg.get(i)).icon(BitmapDescriptorFactory.fromBitmap(icon)));



        for (int i = 0; i < marker.size(); i++) {
            if (nm.equals(name)) {
//                now = mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));
                now = mMap.addMarker(new MarkerOptions()
                        .position(ltlg.get(i))
                        .icon(BitmapDescriptorFactory.fromBitmap(icon1)));
            } else {
                now = mMap.addMarker(new MarkerOptions()
                        .position(ltlg.get(i))
                        .icon(BitmapDescriptorFactory.fromBitmap(icon)));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltlg.get(i), 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMarkersHashMap.put(now, marker.get(i));
            mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
//            now.showInfoWindow();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mGoogleApiClient.disconnect();
        finish();
    }

    //Custom Marker
    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        public MarkerInfoWindowAdapter() {

        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View v = getLayoutInflater().inflate(R.layout.custom_map_marker, null);
            Mapmarker myMarker = mMarkersHashMap.get(marker);
            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);
            TextView markerLabel = (TextView) v.findViewById(R.id.marker_label);
            TextView markerLabel1 = (TextView) v.findViewById(R.id.marker_label1);
            TextView markerLabel2 = (TextView) v.findViewById(R.id.marker_label2);
            TextView time = (TextView) v.findViewById(R.id.time);
//            if (myMarker.getmLabel1().equals(name)) {
//                time.setVisibility(View.GONE);
//            } else {
//            time.setVisibility(View.VISIBLE);
            time.setText(myMarker.getTime());
//            }

//            markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));
//            markerIcon.setImageResource(R.mipmap.ic_launcher);

            String emp_img = myMarker.getMpic().replace(" ", "%20");
            //load image
            Picasso.with(MapsActivity.this)
                    .load(ServerLinks.smallPhoto + emp_img)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(markerIcon);

            markerLabel.setText(myMarker.getmLabel1());
            markerLabel1.setText(myMarker.getmLabel2());
            markerLabel2.setText(myMarker.getmLabel3());

            return v;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_location, menu);

        if(!use_flg_check.equals("")){
            menu.getItem(0).getSubMenu().getItem(0).setChecked(false);
        }else if (flg_check.equals("true")) {
            menu.getItem(0).getSubMenu().getItem(0).setChecked(true);
        } else {
            menu.getItem(0).getSubMenu().getItem(0).setChecked(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.isCheckable()) {
            if (item.isChecked()) {
                item.setChecked(false);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("check_flsg", "false");
                editor.commit();
            } else {
                item.setChecked(true);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("check_flsg", "true");
                editor.commit();
            }
        }
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.specificUser) {

            if (latlongdt.equals("")) {
                showErrorMessage("Please wait ! System is trying to get your current location");
            } else if (latlongdt.equals("0.0,0.0")) {
                showErrorMessage("System is unable to get your current location .Please check your location setting .");
            } else {
                ServerLinks.latLong = latlongdt;
                Intent intent = new Intent(MapsActivity.this, AllEmployeeActivity.class);
                finish();
                startActivity(intent);
            }

        }else if(id==R.id.action_search){
              openSearch();
        }else if(id==R.id.action_arefresh){
            if (ConnectionDetector.staticisConnectingToInternet(MapsActivity.this)) {
                mMap.clear();
                dbmMyMarkersArray.clear();
                allMarkerArrayList.clear();
                dblatLngArrayList.clear();
                latLngArrayList.clear();
                mMyMarkersArray.clear();
                getUserMarker();
                mGoogleApiClient.connect();
                addOwnmarker("");
            }else{
                showErrorMessage("Network connection is not available..");
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void openSearch(){

        search.revealFromMenuItem(R.id.action_search, this);
        search.requestFocus();

        search.setSearchListener(new SearchBox.SearchListener() {
            @Override
            public void onSearchOpened() {
//                Log.i("atag","open");
                search.setLogoText("");
                search.clearResults();
            }

            @Override
            public void onSearchClosed() {
//                Log.i("atag", "closed");
                search.hideCircularly(MapsActivity.this);
                search.setLogoText("");
            }

            @Override
            public void onSearchTermChanged(String term) {
//                Log.i("atag", "term " + term);
                final List<String> filteredModelList = dbHelper.getSearchName(term);
                search.clearSearchable();
                for (int x = 0; x < filteredModelList.size(); x++) {
                    SearchResult option = new SearchResult(filteredModelList.get(x), getResources().getDrawable(R.drawable.userpic));
                    search.addSearchable(option);
                }
            }

            @Override
            public void onSearch(String searchTerm) {
//                Log.i("atag","search ");
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to result being clicked
//                Log.i("atag",result.toString());
                int check_flg=0;
                now.hideInfoWindow();
                for (int i = 0; i < allMarkerArrayList.size(); i++) {
                    Mapmarker mrk = allMarkerArrayList.get(i);
                    if ((mrk.getmLabel1()).equals(result.toString())) {
                        check_flg=1;
//                        Log.i("atag",mrk.getmLatitude()+"   :::::::::::   "+mrk.getmLongitude());
                        LatLng lng = new LatLng(mrk.getmLatitude(), mrk.getmLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 15));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        mMarkersHashMap.put(now, mrk);
                        mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
                        now.showInfoWindow();

                    }
//                    Log.i("atag",mrk.getmLabel1()+"");
                }

                if(check_flg==0){
                    showErrorMessage(result+" is not shared his location yet.");
                }

                search.clearResults();
            }

            @Override
            public void onSearchCleared() {
//                Log.i("atag","search cleared ");
            }

        });

    }

    private void showErrorMessage(String msg){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, R.color.colorPrimary));
        snack.setActionTextColor(Color.WHITE).show();
    }
}




