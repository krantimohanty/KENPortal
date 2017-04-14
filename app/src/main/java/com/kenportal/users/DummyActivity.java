package com.kenportal.users;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.utils.GCMClientManager;

public class DummyActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    String regId;
    private GCMClientManager pushClientManager;
    Context context;
//    String PROJECT_NUMBER = "put your project no insted of my mob no -- 117726716444";
    String PROJECT_NUMBER = "99378085085";
    SharedPreferences appPreferences;
    boolean isAppInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        //add shortcut
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        if (isAppInstalled == false) {
//            addShortcut();
        }

        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
        }

       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(DummyActivity.this, LoginActivity.class));
                finish();

            }
        }, SPLASH_TIME_OUT);
            }
//
//                if (!CustomPreference.with(DummyActivity.this).getString("pin", "").toString().equals("")) {
//                    if (CustomPreference.with(DummyActivity.this).getBoolean("pin_lock", true)) {
//                        Intent i = new Intent(DummyActivity.this, PinActivity.class);
//                        i.putExtra("chng_flag", "new");
//                        startActivity(i);
//                        finish();
//                    } else {
//                        Intent i = new Intent(DummyActivity.this, PortalDashboardActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//                } else {
//                    Intent i = new Intent(DummyActivity.this, LoginActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            }
//        }, SPLASH_TIME_OUT);
//
//    }

    public String registerGCM() {
        regId = getRegistrationId(context);
        if (TextUtils.isEmpty(regId)) {
            registerInBackground();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        String registrationId = CustomPreference.with(DummyActivity.this).getString("GCM_ID", "");
        if (registrationId.isEmpty()) {
            return "";
        }
        return registrationId;
    }

    //region "GCM Backgroud Service with shared preference"
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    pushClientManager = new GCMClientManager(DummyActivity.this, PROJECT_NUMBER);
                    pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                        @Override
                        public void onSuccess(String registrationId, boolean isNewRegistration) {
                            regId = registrationId;
                            CustomPreference.with(DummyActivity.this).save("GCM_ID", regId);
                        }
                        @Override
                        public void onFailure(String ex) {
                            super.onFailure(ex);
                        }
                    });
                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                saveRegisterId(context, regId);
            }
        }.execute(null, null, null);
    }

    private void saveRegisterId(Context context, String regId) {
        CustomPreference.with(DummyActivity.this).save("GCM_ID", regId);
    }

    //add shortcut
    public void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                DummyActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        Intent.ShortcutIconResource.fromContext(getApplicationContext(),R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);

        /**
         * Make preference true
         */
//        SharedPreferences.Editor editor = appPreferences.edit();
//        editor.putBoolean("isAppInstalled", true);
//        editor.commit();
    }

//    private void ShortcutIcon(){
//
//        Intent shortcutIntent = new Intent(getApplicationContext(), DummyActivity.class);
//        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        Intent addIntent = new Intent();
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//        addIntent.putExtra("duplicate", false);
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
////        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
////        getApplicationContext().sendBroadcast(addIntent);
//
//        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
//        getApplicationContext().sendBroadcast(addIntent);
//
//        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//        getApplicationContext().sendBroadcast(addIntent);
//
//        /**
//         * Make preference true
//         */
//        SharedPreferences.Editor editor = appPreferences.edit();
//        editor.putBoolean("isAppInstalled", true);
//        editor.commit();
//    }
}
