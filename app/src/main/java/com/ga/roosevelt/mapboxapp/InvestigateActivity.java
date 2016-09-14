package com.ga.roosevelt.mapboxapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.ga.roosevelt.mapboxapp.constants.APIConstants;
import com.ga.roosevelt.mapboxapp.custom_views.CustomTextview;
import com.ga.roosevelt.mapboxapp.custom_views.MenuButton;
import com.ga.roosevelt.mapboxapp.database.ThiefDatabaseHelper;
import com.ga.roosevelt.mapboxapp.game_logic.Mission;
import com.ga.roosevelt.mapboxapp.game_logic.PlaceInterviews;
import com.ga.roosevelt.mapboxapp.models.Thief;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Review;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestigateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InvestigateActivity";

    long mission_id;
    String place_id;

    ImageView imageView;
    CustomTextview lblPlaceName, txtReview;
    MenuButton btnOwner, btnCustomer, btnCook;

    YelpAPI yelpAPI;

    Business thisPlace;

    ThiefDatabaseHelper dbHelper;
    PlaceInterviews interviewHelper;

    Mission currentMission;

    private void initializeViews(){
        imageView = (ImageView) findViewById(R.id.imgView);
        lblPlaceName = (CustomTextview) findViewById(R.id.lblPlaceName);
        txtReview = (CustomTextview) findViewById(R.id.txtReview);

        btnOwner = (MenuButton) findViewById(R.id.btnOwner);
        btnCustomer = (MenuButton) findViewById(R.id.btnCustomer);
        btnCook = (MenuButton) findViewById(R.id.btnCook);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_investigate);

        mission_id = getIntent().getLongExtra("mission_id", 0);
        place_id = getIntent().getStringExtra("place_id");

        dbHelper = ThiefDatabaseHelper.getInstance(this);
        interviewHelper = PlaceInterviews.getInstance();

        currentMission = dbHelper.getMissionById(mission_id);

        initializeViews();
        initializeYelpAPI();

        getBusinessDetailsById(place_id);

    }

    private void initializeYelpAPI() {
        YelpAPIFactory apiFactory = new YelpAPIFactory(APIConstants.consumerKey,
                APIConstants.consumerSecret, APIConstants.token,
                APIConstants.tokenSecret);

        yelpAPI = apiFactory.createAPI();
    }

    private void getBusinessDetailsById(String place_id){

        Call<Business> call = yelpAPI.getBusiness(place_id);
        Callback<Business> callback = new Callback<Business>(){

            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                thisPlace = response.body();

                lblPlaceName.setText(thisPlace.name());

                btnOwner.setOnClickListener(InvestigateActivity.this);
                btnCustomer.setOnClickListener(InvestigateActivity.this);
                btnCook.setOnClickListener(InvestigateActivity.this);

                Log.d(TAG, "onResponse: reviews count: " + thisPlace.reviewCount());
                //TODO edit layout address beside image
                Picasso.with(InvestigateActivity.this).load(thisPlace.imageUrl()).resize(150, 150).into(imageView);

            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {

            }
        };

        call.enqueue(callback);
    }

    @Override
    public void onClick(View view) {

        String assignedClue = dbHelper.getAssignedClueToPlace(mission_id, place_id);
        String comment = "";
        Thief missionThief = currentMission.getThief();

        String givenClues = dbHelper.getGivenCluesInCurrentMission();
        Log.d(TAG, "onClick: given clues:  " + givenClues);

        Log.d(TAG, "onClick: thief gender = " + missionThief.getGender());

        switch(assignedClue){
            case "hair":
                comment = interviewHelper.getRandomHairComment(missionThief.getGender(), missionThief.getHairColor());
                //add to given clues
                if(!givenClues.contains("hair"))
                    dbHelper.addNewGivenClue("hair");
                break;
            case "hobby":
                comment = interviewHelper.getRandomHobbyComment(missionThief.getGender(), missionThief.getSport());
                //add to given clues
                if(!givenClues.contains("hobby"))
                    dbHelper.addNewGivenClue("hobby");
                break;
            case "feature":
                comment = interviewHelper.getRandomFeatureComment(missionThief.getGender(), missionThief.getFeature());
                //add to given clues
                if(!givenClues.contains("feature"))
                    dbHelper.addNewGivenClue("feature");
                break;
            case "auto":
                comment = interviewHelper.getRandomAutoComment(missionThief.getGender(), missionThief.getVehicle());
                //add to given clues
                if(!givenClues.contains("auto"))
                    dbHelper.addNewGivenClue("auto");
                break;
            default:
                comment = "I'm busy, bruh.";
                break;
        }

        int id = view.getId();
        switch(id){
            case R.id.btnCook:
                String clueCook = "Cook: \n  " + comment;
                txtReview.setText(clueCook);
                break;
            case R.id.btnOwner:
                String clueOwner = "Owner: \n  " + comment;
                txtReview.setText(clueOwner);
                break;
            case R.id.btnCustomer:
                String clueCustomer = "Customer: \n  " + comment;
                txtReview.setText(clueCustomer);
                break;
            default:
                break;
        }
    }

    private boolean hasCaughtThief(){
        String clues = "";
        return true;
    }
}
