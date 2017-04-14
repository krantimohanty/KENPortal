package com.kenportal.users.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.kenportal.users.R;
import com.kenportal.users.utils.FontCache;


/**
 * Created by csmpl on 10/28/2015.
 */
public class CustomEditView extends AutoFitEditText {

    public CustomEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomEditView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
            String fontName = a.getString(R.styleable.MyTextView_fontName);
            if (fontName != null) {
                Typeface typeface = FontCache.get(fontName, getContext());
                setTypeface(typeface);
            }
            a.recycle();
        }
    }

}
