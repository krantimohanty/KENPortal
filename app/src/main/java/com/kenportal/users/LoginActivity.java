package com.kenportal.users;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.utils.CustomPreference;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText uname, pass;
    String username, password, device_id, gcm_id;
    Button skip;
    Dialog dialog;
    String tag_json_obj = "json_obj_req";
    String[] char_spec={"!","<",">","%",".","'","(",")",";"," "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uname = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);
        skip=(Button)findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PortalDashboardActivity.class));
            }
            });

        //User name text change listener for sql injection validation
        uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                for (String x : char_spec) {
                    if (s.toString().startsWith(x)) {
                        uname.setText("");
                        break;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Password  text change listener for sql injection validation
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                for (String x : char_spec) {
                    if (s.toString().startsWith(x)) {
                        pass.setText("");
                        break;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
                    if(ConnectionDetector.staticisConnectingToInternet(LoginActivity.this)){
//                        doLogin();
                        username = uname.getText().toString(); ;
                        password = Encryptor.md5(pass.getText().toString());
                        gcm_id = CustomPreference.with(LoginActivity.this).getString("GCM_ID", "");
                        //Device id
                        device_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                        checkLogin();

                    }else{
                        showErrorMessage("Please check your internet connection.");
                    }
                }
                return false;
            }
        });

        login = (Button) findViewById(R.id.btn_signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_Name= uname.getText().toString();
                String passwd=pass.getText().toString();
                if (user_Name.equals("")) {
//                    uname.setError("User name cannot be left blank");
                    showErrorMessage("User name cannot be left blank");
                } else if (passwd.equals("")) {
//                    pass.setError("Password cannot be left blank");
                    showErrorMessage("Password cannot be left blank");
                }else {
                    if(ConnectionDetector.staticisConnectingToInternet(LoginActivity.this)){
                        username = user_Name;
                        password = Encryptor.md5(passwd);
                        gcm_id = CustomPreference.with(LoginActivity.this).getString("GCM_ID", "");
                        //Device id
                        device_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                        checkLogin();
                    }else{
                        showErrorMessage("Please check your internet connection.");
                    }
                }
            }
        });
    }
    //Make login request
    public void checkLogin() {

        dialog = new Dialog(LoginActivity.this, R.style.Theme_D1NoTitleDim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        //dialog.setTitle(".Logging In..");
        dialog.setContentView(R.layout.custom_progress_view);
        dialog.show();

        JSONObject params = new JSONObject();
        try {
            params.put("name", username);
            params.put("password", password);
            params.put("device_id", device_id);
            params.put("gcm_id", gcm_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServerLinks.ServerUrl + "/login", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if ((response.getString("status")).equals("success")) {
                                JSONObject jsonObjectProfile = response.getJSONArray("profile").getJSONObject(0);
                                String id = jsonObjectProfile.getString("id");
                                String full_name = jsonObjectProfile.getString("full_name");
                                String profile_pic_url = jsonObjectProfile.getString("profile_pic");
                                String dept_Id = jsonObjectProfile.getString("dept_id");
                                String dept_Name = jsonObjectProfile.getString("dept_Name");
                                String designation = jsonObjectProfile.getString("designation");
//                                Log.i("atag", id + ":::::" + full_name + ":::::" + profile_pic_url + ":::::::::" + dept_Id + "::::::::" + dept_Name + "::::::::::" + designation);
                                //Saving value to preference
                                CustomPreference.with(LoginActivity.this).save("id", id);
                                CustomPreference.with(LoginActivity.this).save("full_name", full_name);
                                CustomPreference.with(LoginActivity.this).save("device_id", device_id);
                                CustomPreference.with(LoginActivity.this).save("username", username);
                                CustomPreference.with(LoginActivity.this).save("password", password);
                                CustomPreference.with(LoginActivity.this).save("profilePic", profile_pic_url);
                                CustomPreference.with(LoginActivity.this).save("dept_id", dept_Id);
                                CustomPreference.with(LoginActivity.this).save("dept_Name", dept_Name);
                                CustomPreference.with(LoginActivity.this).save("designation", designation);
                                dialog.hide();
                                Intent i = new Intent(LoginActivity.this, PinActivity.class);
                                i.putExtra("chng_flag", "new");
                                startActivity(i);
                                finish();

                            } else if ((response.getString("status")).equals("Invalid")) {
                                dialog.hide();
                                uname.setText("");
                                pass.setText("");
                                showErrorMessage("Please enter valid username or password.");
                            } else {
                                dialog.hide();
                                uname.setText("");
                                pass.setText("");
                                showErrorMessage("Something went wrong.Please try later.");
                            }
                        } catch (JSONException e) {
                            dialog.hide();
                            showErrorMessage("Something went wrong.Please try later.");
                            e.printStackTrace();
                        } catch (Exception e) {
                            dialog.hide();
                            showErrorMessage("Something went wrong.Please try later.");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.hide();
                        showErrorMessage("Something went wrong.Please try later.");
                    }
            });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
    private void showErrorMessage(String msg){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
        snack.setActionTextColor(Color.WHITE).show();
    }
}
