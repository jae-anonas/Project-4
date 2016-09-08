package com.ga.roosevelt.mapboxapp.custom_views;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.R;

/**
 * Created by roosevelt on 9/8/16.
 */
public class MenuButton extends TextView implements View.OnTouchListener{
    private static final String TAG = "Menu Button iiiiiiiiiii";

    public MenuButton(Context context) {
        super(context);
        this.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
