
package com.kenportal.users.pinlib.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.LinearLayout;

import com.kenportal.users.R;


/**
 * Component that constitutes a single dot on StatusDots
 * @since 1.0.0
 */
public class Dot extends View {


    /**
     * TypedArray of styled attributes passed to the element
     */
    private TypedArray styledAttributes;


    /**
     * @param context Calling activity as context
     * @param styledAttributes TypedArray of styled attributes passed to the element
     * @param filled Indicates whether to fill the dot or not
     */
    public Dot(Context context, TypedArray styledAttributes, boolean filled) {
        super(context);
        this.styledAttributes = styledAttributes;
        setBackground(filled);
        setLayoutParameters();
    }


    /**
     * Setting up layout dimensional parameters for the view
     */
    private void setLayoutParameters() {
        final int dotDiameter = styledAttributes.getDimensionPixelOffset(R.styleable.PinLock_statusDotDiameter, 100);
        final int margin = styledAttributes.getDimensionPixelOffset(R.styleable.PinLock_statusDotSpacing, 30);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotDiameter, dotDiameter);
        params.setMargins(margin, 0, margin, 0);
        setLayoutParams(params);
    }


    /**
     * Setting up background for the view. Should pass shapes for both filled and empty dots.
     * Otherwise will use the default backgrounds
     */
    private void setBackground(boolean filled) {
        if (filled) {
            final int background = styledAttributes
                    .getResourceId(R.styleable.PinLock_statusFilledBackground, R.drawable.dot_filled);
            setBackgroundResource(background);
        } else {
            final int background = styledAttributes
                    .getResourceId(R.styleable.PinLock_statusEmptyBackground, R.drawable.dot_empty);
            setBackgroundResource(background);
        }
    }
}
