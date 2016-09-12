package com.ga.roosevelt.mapboxapp.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.R;
import com.ga.roosevelt.mapboxapp.constants.CustomFonts;

/**
 * Created by roosevelt on 9/11/16.
 */
public class CustomTextview extends TextView {
    Typeface typeface = CustomFonts.getInstance(this.getContext()).getBasicFont();

    public CustomTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(typeface);
        this.setGravity(Gravity.CENTER);
        this.setTextColor(getResources().getColor(R.color.colorText));
    }
}
