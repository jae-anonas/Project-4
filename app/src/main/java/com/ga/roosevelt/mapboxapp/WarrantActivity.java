package com.ga.roosevelt.mapboxapp;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.adapters.DossierSpinnerAdapter;
import com.ga.roosevelt.mapboxapp.constants.CustomFonts;
import com.ga.roosevelt.mapboxapp.custom_views.MenuButton;
import com.ga.roosevelt.mapboxapp.custom_views.TypewriterView;
import com.ga.roosevelt.mapboxapp.database.ThiefDatabaseHelper;
import com.ga.roosevelt.mapboxapp.game_logic.Mission;
import com.ga.roosevelt.mapboxapp.models.Thief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WarrantActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "iiiiiiii";

    TextView lblCrimeComputer, lblSex, lblHair, lblFeature, lblHobby, lblAuto;
    TypewriterView txtResults;
    MenuButton mBtnCompute, mBtnBack;
    Spinner spnSex, spnHair, spnHobby, spnFeature, spnAuto;

    ArrayAdapter<String> mAdapterSex, mAdapterHobby, mAdapterHair, mAdapterFeature, mAdapterAuto;
    String[] mSex = {"male", "female"};
    String[] mHobby = {"", "croquet", "mountain climbing", "skin diving", "tennis"};
    String[] mHair = {"", "black","blond","brown","red"};
    String[] mFeature = {"", "scar", "jewelry", "wig", "tattoo"};
    String[] mAuto = {"", "motorcycle", "convertible", "limousine", "skateboard"};
    String thiefGender;

    List<Thief> mSearchResults;

    ThiefDatabaseHelper dbHelper;

    long mission_id;
    Mission currentMission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warrant);
        //get thief gender to display in warrant
        thiefGender = getIntent().getStringExtra("sex");
        mission_id = getIntent().getLongExtra("mission_id", 0);

        dbHelper = ThiefDatabaseHelper.getInstance(this);
        currentMission = dbHelper.getMissionById(mission_id);


        bindViews();

    }


    private void bindViews(){
        lblCrimeComputer = (TextView) findViewById(R.id.lblCrimeComputer);
        lblHair = (TextView) findViewById(R.id.lblHair);
        lblHobby = (TextView) findViewById(R.id.lblHobby);
        lblFeature = (TextView) findViewById(R.id.lblFeature);
        lblAuto = (TextView) findViewById(R.id.lblAuto);
        lblSex = (TextView) findViewById(R.id.lblSex);
        txtResults = (TypewriterView) findViewById(R.id.txtResults);

        spnSex = (Spinner) findViewById(R.id.spnSex);
        //TODO set selected to sex of current thief
        spnHair = (Spinner) findViewById(R.id.spnHair);
        spnHobby = (Spinner) findViewById(R.id.spnHobby);
        spnFeature = (Spinner) findViewById(R.id.spnFeature);
        spnAuto = (Spinner) findViewById(R.id.spnAuto);

        spnHair.setOnTouchListener(this);
        spnHobby.setOnTouchListener(this);
        spnFeature.setOnTouchListener(this);
        spnAuto.setOnTouchListener(this);

        mBtnCompute = (MenuButton) findViewById(R.id.btnCompute);
        mBtnBack = (MenuButton) findViewById(R.id.btnBack);

        mBtnCompute.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);

        Typeface typeface = CustomFonts.getInstance(this).getBasicFont();
        lblCrimeComputer.setTypeface(typeface);
        lblHair.setTypeface(typeface);
        lblHobby.setTypeface(typeface);
        lblFeature.setTypeface(typeface);
        lblAuto.setTypeface(typeface);
        lblSex.setTypeface(typeface);
        txtResults.setTypeface(typeface);


        mBtnCompute.setTypeface(typeface);

        setAdapters();
    }

    private void setAdapters(){
        mAdapterSex = new DossierSpinnerAdapter(this, android.R.layout.simple_list_item_1, mSex);
        mAdapterHobby = new DossierSpinnerAdapter(this, android.R.layout.simple_list_item_1, mHobby);
        mAdapterHair = new DossierSpinnerAdapter(this, android.R.layout.simple_list_item_1, mHair);
        mAdapterFeature = new DossierSpinnerAdapter(this, android.R.layout.simple_list_item_1, mFeature);
        mAdapterAuto = new DossierSpinnerAdapter(this, android.R.layout.simple_list_item_1, mAuto);

        spnSex.setAdapter(mAdapterSex);
        if (thiefGender.equalsIgnoreCase("female")){
            spnSex.setSelection(1, false);
        } else {
            spnSex.setSelection(0, false);
        }

        spnHair.setAdapter(mAdapterHair);
        if(dbHelper.getGivenCluesInCurrentMission().contains("hair")){
            int index = 0;
            switch (currentMission.getThief().getHairColor()){
                case "black":
                    //1
                    spnHair.setSelection(1, false);
                    break;
                case "blond":
                    //2
                    spnHair.setSelection(2, false);
                    break;
                case "brown":
                    //3
                    spnHair.setSelection(3, false);
                    break;
                case "red":
                    //4
                    spnHair.setSelection(4, false);
                    break;
                default:
                    break;
            }
            spnHair.setSelection(index, false);
        }

        spnHobby.setAdapter(mAdapterHobby);
        spnFeature.setAdapter(mAdapterFeature);
        spnAuto.setAdapter(mAdapterAuto);



    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.btnCompute:
                //TODO search and display results in txt
                searchAndDisplayWarrant();
                break;
            case R.id.btnBack:
                finish();
                break;
            default:
                break;
        }
    }

    private void searchAndDisplayWarrant(){
        mSearchResults = new ArrayList<>();
        ContentValues values = new ContentValues();
        //add Spinner choices to details
        values.put("gender", spnSex.getSelectedItem().toString());
        values.put("hairColor", spnHair.getSelectedItem().toString());
        values.put("feature", spnFeature.getSelectedItem().toString());
        values.put("vehicle", spnAuto.getSelectedItem().toString());
        values.put("sport", spnHobby.getSelectedItem().toString());

        mSearchResults = ThiefDatabaseHelper.getInstance(this).searchThiefDatabase(values);

        Log.d(TAG, "searchAndDisplayWarrant: size of result is " +mSearchResults.size() + "    size of hair: " + values.getAsString("hairColor").length());

        txtResults.setText("");//clear the text first

        if (mSearchResults.size() == 1){
            txtResults.pause(1500)
                    .type("You now have a warrant for the arrest of: \n\n")
                    .type(mSearchResults.get(0).getName());
            //TODO if there is only one search result, store to Mission table
            ThiefDatabaseHelper.getInstance(this).setWarrantOnCurrentMission(mSearchResults.get(0));

        } else if(mSearchResults.size() > 1){
            txtResults.pause(1500)
                    .type(mSearchResults.size() + " possible suspects:\n");
            for (Thief thief : mSearchResults) {
                        txtResults.type("\n" + thief.getName())
                        .pause(500);
            }
        } else {
            txtResults.pause(1500)
                    .type("There is no one that matches that description.");
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP ) {
            Spinner spinner = (Spinner) view;
            spinner.setSelection((spinner.getSelectedItemPosition() + 1) % spinner.getCount(), true);

        }
        return true;
    }
}

