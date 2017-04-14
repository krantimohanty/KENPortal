package com.kenportal.users.pinlib.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kenportal.users.R;


/**
 * Layout which contains set of Dots which shows PIN input status
 * @see Dot
 * @since 1.0.0
 */
public class StatusDots extends LinearLayout {


    /**
     * TypedArray of styled attributes passed to the element
     */
    private final TypedArray styledAttributes;


    /**
     * Current application context
     */
    private final Context context;

    public StatusDots(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        styledAttributes = context.obtainStyledAttributes(R.style.PinLock, R.styleable.PinLock);
        initialize();
    }


    /**
     * Adding Dot objects to layout
     * @param length Length of PIN entered so far
     */
    private void addDots(int length) {
        removeAllViews();
        final int pinLength = styledAttributes.getInt(R.styleable.PinLock_pinLength, 4);
        for (int i = 0; i < pinLength; i++) {
            Dot dot = new Dot(context, styledAttributes, i < length);
            addView(dot);
        }
    }


    /**
     * Executed just before destroying StatusDots object. Used to recycle StyledAttributes properly
     * @throws Throwable Throws exception
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        styledAttributes.recycle();
    }


    /**
     * Initialize StatusDots. Set backgrounds of all dots to empty background
     */
    public void initialize() {
        addDots(0);
    }


    /**
     * Updates StatusDots. Set backgrounds of {pinLength} dots to filled background
     * @param pinLength Current length of PIN entered
     */
    public void updateStatusDots(int pinLength) {
        addDots(pinLength);
    }
}
