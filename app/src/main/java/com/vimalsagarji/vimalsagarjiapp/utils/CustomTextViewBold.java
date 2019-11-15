package com.vimalsagarji.vimalsagarjiapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Pradip 18-Nov-16.
 */

@SuppressWarnings("ALL")
public class CustomTextViewBold extends android.support.v7.widget.AppCompatTextView {
    public CustomTextViewBold(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/calibrib_bold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }


}
