package com.ga.roosevelt.mapboxapp.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.R;
import com.ga.roosevelt.mapboxapp.constants.CustomFonts;

/**
 * Created by roosevelt on 9/8/16.
 */
public class MenuButton extends TextView implements View.OnTouchListener{
    private static final String TAG = "Menu Button iiiiiiiiiii";
    Typeface typeface = CustomFonts.getInstance(this.getContext()).getBasicFont();

    public MenuButton(Context context) {
        super(context);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(typeface);
        this.setOnTouchListener(this);
        this.setTextColor(getResources().getColor(R.color.colorText));
        this.setGravity(Gravity.CENTER);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //change colors
        Log.d(TAG, "onTouch: inside");
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouch: pressed");
                view.setBackgroundColor(getResources().getColor(R.color.colorText));
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorBackground));
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouch: released");
                view.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorText));
                break;
            default:
                break;
        }
        return false;
    }
}
