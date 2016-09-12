package com.ga.roosevelt.mapboxapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ga.roosevelt.mapboxapp.constants.APIConstants;
import com.ga.roosevelt.mapboxapp.custom_views.CustomTextview;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestigateActivity extends AppCompatActivity {

    long mission_id;
    String place_id;

    ImageView imageView;
    CustomTextview lblPlaceName;

    YelpAPI yelpAPI;

    Business thisPlace;

    private void initializeViews(){
        imageView = (ImageView) findViewById(R.id.imgView);
        lblPlaceName = (CustomTextview) findViewById(R.id.lblPlaceName);


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
//                thisPlace.snippetImageUrl()
                Picasso.with(InvestigateActivity.this).load(thisPlace.imageUrl()).resize(150, 150).into(imageView);

            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {

            }
        };
        call.enqueue(callback);

    }
}
