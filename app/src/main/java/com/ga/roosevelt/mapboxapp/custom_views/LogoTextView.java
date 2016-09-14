package com.ga.roosevelt.mapboxapp.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.R;
import com.ga.roosevelt.mapboxapp.constants.CustomFonts;

/**
 * Created by roosevelt on 9/13/16.
 */
public class LogoTextView extends TextView {
    Typeface typeface = CustomFonts.getInstance(this.getContext()).getLogoFont();

    public LogoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(typeface);
        this.setGravity(Gravity.CENTER);
        this.setTextSize(50.0f);
    }
}
