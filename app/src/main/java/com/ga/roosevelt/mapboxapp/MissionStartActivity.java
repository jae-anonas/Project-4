package com.ga.roosevelt.mapboxapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ga.roosevelt.mapboxapp.constants.APIConstants;
import com.ga.roosevelt.mapboxapp.constants.CustomFonts;
import com.ga.roosevelt.mapboxapp.custom_views.MenuButton;
import com.ga.roosevelt.mapboxapp.custom_views.TypewriterView;
import com.ga.roosevelt.mapboxapp.database.ThiefDatabaseHelper;
import com.ga.roosevelt.mapboxapp.game_logic.Mission;
import com.ga.roosevelt.mapboxapp.services.GPSService;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MissionStartActivity extends AppCompatActivity implements View.OnClickListener, TypewriterView.OnTypingListener {
    private static final String TAG = "MissionStartActivity";
    private static final int LOCATION_PERMISSION_CODE = 123;
    TypewriterView typWriterView;
    TextView lblCrimeComputer;
    MenuButton btnPressToContinue;
    int delay = 1000, lineCounter = 0;
    String searchTerm;

    ThiefDatabaseHelper dbHelper;

    long mission_id;
    Mission currentMission;
    List<Business> businesses;
    Location myLocation;

    private BroadcastReceiver mBroadcastReceiver;

    YelpAPI yelpAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mission_start);

        dbHelper = ThiefDatabaseHelper.getInstance(this);
        businesses = new ArrayList<>();

        //getExtra
        mission_id = getIntent().getLongExtra("mission_id", 0);

        currentMission = dbHelper.getMissionById(mission_id);

        Log.d(TAG, "onCreate: " + String.format(Locale.US, " mission_id: %s thief_name: %s; end_date: %s",
                String.valueOf(mission_id), currentMission.getThief().getName(), currentMission.getMissionEnd().toString()));


        searchTerm = currentMission.getThief().getFood();

        bindViews();

//        printLineHeading("Mexico City", "mask of Montezuma");

        if (!runtimePermissions()) {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            startService(i);
        }

        initializeYelpAPI();

    }

    private void initializeYelpAPI() {
        YelpAPIFactory apiFactory = new YelpAPIFactory(APIConstants.consumerKey,
                APIConstants.consumerSecret, APIConstants.token,
                APIConstants.tokenSecret);

        yelpAPI = apiFactory.createAPI();
    }


    public void getYelpData(Double lat, Double lng, String searchTerm, int limit, int sort) {
        Map<String, String> params = new HashMap<>();

        params.put("term", searchTerm);
//        params.put("limit", "3");
        params.put("limit", String.valueOf(limit));
        params.put("sort", String.valueOf(sort));

        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(lat)
                .longitude(lng).build();

        Call<SearchResponse> call = yelpAPI.search(coordinate, params);
        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, retrofit2.Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();

                String places = "";

                for (int i = 0; i < 4; i++) {
                    Business place = searchResponse.businesses().get(i);
                    businesses.add(place);
                    //TODO add these places to the database
                    dbHelper.addPlace(place);
                    //TODO add these places to current mission
                    places += place.id() + " ";
                }
                Log.d(TAG, "onResponse: places.trim()" + places.trim());
                dbHelper.addPlacesInCurrentMission(places.trim());

                Business firstBusiness =  searchResponse.businesses().get(0);
                Business secondBusiness = searchResponse.businesses().get(1);
                Business thirdBusiness = searchResponse.businesses().get(2);
                Business fourthBusiness = searchResponse.businesses().get(3);

                //TODO Store these businesses! Check first if they're already stored. (i.e. by place id)
                //TODO WHY CHECK??

                Log.d(TAG, "onResponse: distance, 1st business: " + firstBusiness.distance());
                Log.d(TAG, "onResponse: name: " + firstBusiness.name());

//                LatLng target1 = new LatLng(firstBusiness.location().coordinate().latitude(),
//                        firstBusiness.location().coordinate().longitude());

                printLineHeading(businesses.get(0).name(), "mask of Montezuma");
                //TODO randomize treasure stolen

                Log.d(TAG, "onResponse: " + String.format(Locale.US, "target1: %s; target2: %s; target3: %s; target4: %s",
                        firstBusiness.name(), secondBusiness.name(), thirdBusiness.name(), fourthBusiness.name()));


            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        };

        call.enqueue(callback);

    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    double lat = intent.getDoubleExtra("lat", 0.0d);
                    double lng = intent.getDoubleExtra("lng", 0.0d);

                    Location myLoc = new Location("provider");
                    myLoc.setLatitude(lat);
                    myLoc.setLongitude(lng);

                    Log.d(TAG, "onReceive: get Yelp data with lat: " + myLoc.getLatitude() + " lng: " + myLoc.getLongitude());

                    if (myLocation == null) {
                        myLocation = myLoc;
                        //TODO use myloc to get Yelp data
                        getYelpData(lat, lng, searchTerm, 4, 1);
                    }

                }
            };
        }

        registerReceiver(mBroadcastReceiver, new IntentFilter("location_update"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }


    private boolean runtimePermissions() {
        Log.d(TAG, "runtimePermissions: ");
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest
                .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "runtimePermissions: inside if!");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);

            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: ");
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(getApplicationContext(), GPSService.class);
                startService(i);
            } else {
                runtimePermissions();
            }
        }
    }

    private void printLineHeading(String place, String treasure){
        typWriterView.type("*******FLASH*******")
                .pause(delay)
                .type("\n\nNational treasure stolen from " + place + ".")
                .pause(delay)
                .type("\n\nThe treasure has been identified as the " + treasure +".");

        lineCounter = 1;

    }



    private void bindViews(){
        typWriterView = (TypewriterView) findViewById(R.id.typMissionWriter);
        typWriterView.setOnStopRunningListener(this);
        lblCrimeComputer = (TextView) findViewById(R.id.lblCrimeComputer);
        btnPressToContinue = (MenuButton) findViewById(R.id.btnPressToContinue);

        typWriterView.setTypeface(CustomFonts.getInstance(this).getBasicFont());
        lblCrimeComputer.setTypeface(CustomFonts.getInstance(this).getBasicFont());

        btnPressToContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.btnPressToContinue:
                //TODO print out current line in TypewriterView
                if (lineCounter == 1) {
                    //print next line
                    printLineSuspect(currentMission.getThief().getGender());
                    btnPressToContinue.setVisibility(View.INVISIBLE);
                }
                else if (lineCounter == 2) {
                    printLineAssignment(businesses.get(0).name(), currentMission.getThief().getGender());
                }
                else if (lineCounter == 3){
                    Log.d(TAG, "onClick: should now go to MainActivity");
                    //TODO go to Main Page
                    Intent startGame = new Intent(this, MainActivity.class);
                    startGame.putExtra("mission_id", mission_id);
                    startActivity(startGame);
                }
                Log.d(TAG, "onClick: lineCounter = " + lineCounter);
                break;
            default:
                break;
        }
    }

    private void printLineSuspect(String gender) {
        String sex = gender.substring(0, 1).toUpperCase() + gender.substring(1);
        typWriterView.type("\n\n" + sex +" suspect reported at the scene of the crime.");
        lineCounter = 2;
    }

    private void printLineAssignment(String place, String gender) {
        String pronoun1 = gender.equalsIgnoreCase("female") ? "her" : "his";
        String pronoun2 = gender.equalsIgnoreCase("female") ? "her" : "him";

        typWriterView.type("\n\nYour assignment:\nTrack the thief from " + place + " to " +
                pronoun1 + " hideout and arrest " + pronoun2 + "!");
        lineCounter = 3;

    }

    @Override
    public void onStopTyping() {
        btnPressToContinue.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartTyping() {
        btnPressToContinue.setVisibility(View.INVISIBLE);
    }

}
