package com.kenportal.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.utils.GCMClientManager;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

//import com.babyloniaapp.app_util.CustomPreference;
//import com.babyloniaapp.app_util.GCMClientManager;
//import com.babyloniaapp.network_utils.ServiceCalls;

public class SplashActivity extends AppCompatActivity {
    String regId;
    private GCMClientManager pushClientManager;
    Context context;
    String PROJECT_NUMBER = "117726716444";
    private static int SPLASH_TIME_OUT = 3000;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if (TextUtils.isEmpty(CustomPreference.with(SplashActivity.this).getString("GCM_ID", ""))) {
            pushClientManager = new GCMClientManager(SplashActivity.this, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {
                    regId = registrationId;
                    CustomPreference.with(SplashActivity.this).save("GCM_ID", regId);
                    Log.d("RegisterActivity", "Success2: " + CustomPreference.with(SplashActivity.this).getString("GCM_ID", ""));
                    //check gcm is generated
                    if (TextUtils.isEmpty(CustomPreference.with(SplashActivity.this).getString("GCM_ID", ""))) {

                    } else {
                        //check gcm is send to server
//                        if (TextUtils.isEmpty(CustomPreference.with(SplashActivity.this).getString("REGD_GCM_ID", ""))) {
//                            ServiceCalls.postDeviceDetails(SplashActivity.this);
//                        }

                    }

                }

                @Override
                public void onFailure(String ex) {
                    super.onFailure(ex);
                }

            });
        }
//        else {
//            if (TextUtils.isEmpty(CustomPreference.with(SplashActivity.this).getString("REGD_GCM_ID", ""))) {
//                ServiceCalls.postDeviceDetails(SplashActivity.this);
//            }
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                finish();

            }
        }, SPLASH_TIME_OUT);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

}
