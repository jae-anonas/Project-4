package com.ga.roosevelt.mapboxapp;

import android.app.Dialog;
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
import com.ga.roosevelt.mapboxapp.database.ThiefDatabaseHelper;

import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    long duration = 86400000L;
    private static final String TAG = "iiiiiiiiiiiiii";
    TextView btnNew, btnContinue, btnOptions, btnQuit;
    ThiefDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        bindViews();

        dbHelper = ThiefDatabaseHelper.getInstance(this);

        if (!dbHelper.hasThiefRecords()) {
            ThiefDatabaseHelper.getInstance(this).addAllThiefRecords();
        }
    }

    private void bindViews(){
        btnNew = (MenuButton) findViewById(R.id.btnNewGame);
        btnContinue = (MenuButton) findViewById(R.id.btnContinue);
        btnOptions = (MenuButton) findViewById(R.id.btnOptions);
        btnQuit = (MenuButton) findViewById(R.id.btnExit);

        btnContinue.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        btnOptions.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.btnContinue:
                //TODO check for current game in Mission table
                if (dbHelper.hasCurrentGame()) {
                    Intent currentGame = new Intent(this, MainActivity.class);
                    currentGame.putExtra("mission_id", dbHelper.getCurrentMissionId());
                    startActivity(currentGame);
                }
                break;
            case R.id.btnNewGame:
                //TODO ask first if user wants to cancel current game before starting a new one OPEN dialog
                if (dbHelper.hasCurrentGame()) {
                    //open dialog
                    openNewGameDialog().show();
                } else {
                    //Create new game!
                    long mission_id = dbHelper.createNewGame(duration);

                    Intent newGame = new Intent(MainMenuActivity.this, MissionStartActivity.class);
                    newGame.putExtra("mission_id", mission_id);
                    startActivity(newGame);


                }
                break;
            case R.id.btnOptions:
                //TODO new activity for options
                break;

            default:
                //TODO Exit dialog (maybe add a closing dialog or something to indicate exit)
                finish();

        }
    }

    private Dialog openNewGameDialog(){
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_new_mission);

        MenuButton btnYes = (MenuButton) dialog.findViewById(R.id.btnYes);
        MenuButton btnNo = (MenuButton) dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked yes");
                dialog.dismiss();
                //TODO if user proceeds, create new record in Mission, set all mission records that are "current" to "not current" (there should only be one)
                long mission_id = dbHelper.createNewGame(duration);
                Intent newGame = new Intent(MainMenuActivity.this, MissionStartActivity.class);
                newGame.putExtra("mission_id", mission_id);
                startActivity(newGame);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.d(TAG, "onClick: clicked no");
            }
        });

        Log.d(TAG, "openNewGameDialog: open dialog");
        return dialog;
    }

}
