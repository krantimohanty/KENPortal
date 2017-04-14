
package com.kenportal.users.pinlib;


/**
 * Interface for PinListener which handles all PIN events like
 * onComplete, onChange and onForgot
 * @since 1.0.0
 */
public interface PinListener {


    /**
     * Response code for operation success
     */
    int SUCCESS = 0;


    /**
     * Response code for operation cancelled
     */
    int CANCELLED = 1;


    /**
     * Response code for invalid PIN
     */
    int INVALID = 3;


    /**
     * Response code for forgot PIN
     */
    int FORGOT = 4;

    String TEXT_FIRST_TRY = "Enter PIN";
    String TEXT_PIN_INVALID = "Invalid PIN. Try again";

    String TEXT_FIRST_TRY_NEW = "Enter new PIN";
    String TEXT_CONFIRM_PIN = "Re enter PIN";
    String TEXT_PIN_MISMATCH = "PIN mismatch. Try again";


    /**
     * Invokes when user completes entering PIN
     * @param pin PIN value entered by user
     */
    void onCompleted(String pin);


    /**
     * Invokes when user clicks on Keypad
     * @param length Current length of PIN
     */
    void onPinValueChange(int length);


    /**
     * Invokes when user clicks forgot button
     */
    void onForgotPin();
}
