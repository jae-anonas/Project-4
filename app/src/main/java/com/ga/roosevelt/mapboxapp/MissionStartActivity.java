package com.ga.roosevelt.mapboxapp;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ga.roosevelt.mapboxapp.constants.CustomFonts;
import com.ga.roosevelt.mapboxapp.custom_views.TypewriterView;

import java.util.Locale;

public class MissionStartActivity extends AppCompatActivity {
    TypewriterView typWriterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mission_start);

        bindViews();

        typeOutMission("****FLASH****"); //TODO should get a mission object like typeOutMission(Mission m, int delayInMills)
    }

    private void typeOutMission(String s) {
        int delay = 1000;
        String place = "Mexico City";
        String treasure = "mask of Montezuma";
        String gender = "Female";
        String pronoun = "her";

        typWriterView.pause(delay)
                .type(s)
                .pause(delay)
                .type("\n\nNational treasure stolen from " + place + ".")
                .pause(delay)
                .type("\n\nThe treasure has been identified as the " + treasure +".")
                .pause(delay)
                .type("\n\n" + gender +" suspect reported at the scene of the crime.")
                .pause(delay)
                .type("\n\nYour assignment:\nTrack the thief from " + place + " to " +
                pronoun + " hideout and arrest her!");
    }

    private void bindViews(){
        typWriterView = (TypewriterView) findViewById(R.id.typMissionWriter);

        typWriterView.setTypeface(CustomFonts.getInstance(this).getBasicFont());

    }
}
