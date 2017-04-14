package com.kenportal.users.pinlib;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;

import com.kenportal.users.LoginActivity;
import com.kenportal.users.utils.CustomPreference;

public class ConfirmPinActivitySample extends ConfirmPinActivity {

    private String currentPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //currentPin = MPin.pinLockPrefs.getString("pin", "");
        currentPin = CustomPreference.with(ConfirmPinActivitySample.this).getString("pin", "");
    }

    @Override
    public boolean isPinCorrect(String pin) {
        return pin.equals(currentPin);
    }

    @Override
    public void onForgotPin() {
        CustomPreference.with(ConfirmPinActivitySample.this).removeAll();
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    startActivity(new Intent(ConfirmPinActivitySample.this, LoginActivity.class));
                    finish();
                }
            }
        }
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
}
