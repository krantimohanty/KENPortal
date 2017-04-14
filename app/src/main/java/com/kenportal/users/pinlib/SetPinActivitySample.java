package com.kenportal.users.pinlib;

import com.kenportal.users.utils.CustomPreference;


public class SetPinActivitySample extends SetPinActivity {

    @Override
    public void onPinSet(String pin) {
        CustomPreference.with(SetPinActivitySample.this).save("pin", pin);
        setResult(SUCCESS);
        finish();
    }
}
