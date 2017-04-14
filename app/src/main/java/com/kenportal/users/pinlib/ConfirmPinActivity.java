
package com.kenportal.users.pinlib;

import android.os.Bundle;


/**
 * Abstract class for PIN confirm activity.
 * Subclass this activity to show ConfirmPin screen.
 * All subclasses should implement isPinCorrect() method
 * @since 1.0.0
 */
public abstract class ConfirmPinActivity extends BasePinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabel(TEXT_FIRST_TRY);
    }


    /**
     * Implementation of BasePinActivity method
     * @param pin PIN value entered by user
     */
    @Override
    public final void onCompleted(String pin) {
        resetStatus();
        if (isPinCorrect(pin)) {
            setResult(SUCCESS);
            finish();
        } else {
            setLabel(TEXT_PIN_INVALID);
        }
    }


    /**
     * Abstract method which decides the PIN entered by user is correct or not
     * @param pin PIN value entered by user
     * @return Boolean value indicates the status of PIN entered
     */
    public abstract boolean isPinCorrect(String pin);


    /**
     * Abstract method which handles PIN forgot scenario
     */
    @Override
    public abstract void onForgotPin();


    @Override
    public void onBackPressed() {
        setResult(CANCELLED);
        finish();
    }
}
