package com.kenportal.users.pinlib;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kenportal.users.R;
import com.kenportal.users.pinlib.components.Keypad;
import com.kenportal.users.pinlib.components.StatusDots;


/**
 * Abstract class for basic PIN activity.
 * All subclasses should implement onCompleted(String) method.
 *
 * @since 1.0.0
 */
public abstract class BasePinActivity extends AppCompatActivity implements PinListener {


    /**
     * Holds reference to label added to the UI
     */
    private TextView label;


    /**
     * Holds reference to StatusDots added to the UI
     */
    private StatusDots statusDots;


    /**
     * Holds reference to forgot button added to the UI
     */
    private TextView forgetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Keypad keypad = (Keypad) findViewById(R.id.keypad);
        keypad.setPinListener(this);

        label = (TextView) findViewById(R.id.label);
        statusDots = (StatusDots) findViewById(R.id.statusDots);

        setupButtons();
        setupStyles();
    }


    /**
     * Setting forgot buttons and adding onClickListeners to them
     */
    private void setupButtons() {
        forgetButton = (TextView) findViewById(R.id.forgotPin);
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPin();
                setResult(FORGOT);
                finish();
            }
        });
    }


    /**
     * Setting up color and textSize for cancel/forgot buttons and info text
     */
    private void setupStyles() {
        TypedArray styledAttributes = obtainStyledAttributes(R.style.PinLock, R.styleable.PinLock);

        final int layoutBackground = styledAttributes.getColor(R.styleable.PinLock_backgroundColor, Color.WHITE);
        View layout = findViewById(R.id.pinLockLayout);
        layout.setBackgroundColor(layoutBackground);

        final int cancelForgotTextSize = styledAttributes.getDimensionPixelOffset(R.styleable.PinLock_cancelForgotTextSize, 20);
        //cancelButton.setTextSize(cancelForgotTextSize);
        forgetButton.setTextSize(cancelForgotTextSize);

        final int cancelForgotTextColor = styledAttributes.getColor(R.styleable.PinLock_cancelForgotTextColor, Color.BLACK);
        //cancelButton.setTextColor(cancelForgotTextColor);
        if (forgetButton.isEnabled()) {
            forgetButton.setTextColor(cancelForgotTextColor);
        } else {
            forgetButton.setTextColor(Color.parseColor("#a9abac"));
        }

        final int infoTextSize = styledAttributes.getDimensionPixelOffset(R.styleable.PinLock_infoTextSize, 20);
        final int infoTextColor = styledAttributes.getColor(R.styleable.PinLock_infoTextColor, Color.BLACK);
        label.setTextSize(infoTextSize);
        label.setTextColor(infoTextColor);
    }


    /**
     * Disabling forgot button on request
     */
    public void disableForgotButton() {
        forgetButton.setEnabled(false);
        forgetButton.setTextColor(Color.parseColor("#a9abac"));
    }


    /**
     * Setting label text as String value passed
     *
     * @param text Text to be set as label text
     */
    public void setLabel(String text) {
        label.setText(text);
    }


    /**
     * Reset StatusDots to initial state where no dots are filled
     */
    public void resetStatus() {
        statusDots.initialize();
    }


    /**
     * Abstract method. Should be implemented by all subclasses.
     * Called when user completes entering PIN
     *
     * @param pin PIN value entered by the user
     */
    @Override
    public abstract void onCompleted(String pin);


    /**
     * Called when user clicks on Keypad
     *
     * @param length Current length of PIN
     */
    @Override
    public void onPinValueChange(int length) {
        statusDots.updateStatusDots(length);
    }


    /**
     * Has to be implemented in confirm PIN activity
     */
    @Override
    public void onForgotPin() {
        // handle forgot PIN scenario


    }


}
