package com.ga.roosevelt.mapboxapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.constants.CustomFonts;
import com.ga.roosevelt.mapboxapp.custom_views.MenuButton;

import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "iiiiiiiiiiiiii";
    TextView btnNew, btnContinue, btnOptions, btnQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        bindViews();
    }

    private void bindViews(){
        btnNew = (TextView) findViewById(R.id.btnNewGame);
        btnContinue = (TextView) findViewById(R.id.btnContinue);
        btnOptions = (TextView) findViewById(R.id.btnOptions);
        btnQuit = (TextView) findViewById(R.id.btnExit);

        Typeface typeface = CustomFonts.getInstance(this).getBasicFont();

        btnNew.setTypeface(typeface);
        btnContinue.setTypeface(typeface);
        btnOptions.setTypeface(typeface);
        btnQuit.setTypeface(typeface);

        btnContinue.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

        btnContinue.setOnTouchListener(this);
        btnNew.setOnTouchListener(this);
        btnQuit.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.btnContinue:
                Intent currentGame = new Intent(this, MainActivity.class);
                startActivity(currentGame);
                break;
            case R.id.btnNewGame:
                Intent newGame = new Intent(this, MissionStartActivity.class);
                startActivity(newGame);
                break;
            case R.id.btnOptions:
                //TODO new activity for options
                break;

            default:
                //TODO Exit dialog
                finish();

        }
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
