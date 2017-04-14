package com.kenportal.users;

import android.content.Intent;
import android.os.Bundle;
import com.kenportal.users.pinlib.ConfirmPinActivitySample;
import com.kenportal.users.pinlib.PinListener;
import com.kenportal.users.pinlib.SetPinActivitySample;
import com.kenportal.users.utils.CustomPreference;

public class MPin extends BaseActivity {

    public static final int REQUEST_CODE_SET_PIN = 0;
    public static final int REQUEST_CODE_CHANGE_PIN = 1;
    public static final int REQUEST_CODE_CONFIRM_PIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String pin = CustomPreference.with(MPin.this).getString("pin", "");
        String pege_pin = getIntent().getStringExtra("chng_flag");//("chng_flag").toString();
        if (pege_pin.equals("change")) {
            Intent intent = new Intent(MPin.this, SetPinActivitySample.class);
            startActivityForResult(intent, REQUEST_CODE_SET_PIN);
        } else {
            if (pin.equals("")) {
                Intent intent = new Intent(MPin.this, SetPinActivitySample.class);
                startActivityForResult(intent, REQUEST_CODE_SET_PIN);
            } else {
                Intent intent = new Intent(MPin.this, ConfirmPinActivitySample.class);
                startActivityForResult(intent, REQUEST_CODE_CONFIRM_PIN);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SET_PIN: {
                if (resultCode == PinListener.SUCCESS) {
                    Intent intent = new Intent(MPin.this, PortalDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else if (resultCode == PinListener.CANCELLED) {
                    finish();
                }
                break;
            }
            case REQUEST_CODE_CHANGE_PIN: {
                if (resultCode == PinListener.SUCCESS) {
                    Intent intent = new Intent(MPin.this, SetPinActivitySample.class);
                    startActivityForResult(intent, REQUEST_CODE_SET_PIN);
                } else if (resultCode == PinListener.CANCELLED) {
                    finish();
                }
                break;
            }
            case REQUEST_CODE_CONFIRM_PIN: {
                if (resultCode == PinListener.SUCCESS) {
                    Intent intent = new Intent(MPin.this, PortalDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else if (resultCode == PinListener.CANCELLED) {
                    finish();
                }
                break;
            }
        }
    }
}
