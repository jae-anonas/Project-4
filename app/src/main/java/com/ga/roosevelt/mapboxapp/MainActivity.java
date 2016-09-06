package com.ga.roosevelt.mapboxapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.adapters.NeighborhoodListBaseAdapter;
import com.ga.roosevelt.mapboxapp.constants.APIConstants;
import com.ga.roosevelt.mapboxapp.models.Neighborhoods;
import com.ga.roosevelt.mapboxapp.services.GPSService;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements NeighborhoodListBaseAdapter.OnNeighborhoodChosenListener {

    private static final String TAG = "iiiiiii";
    private static final int LOCATION_PERMISSION_CODE = 123;

    private BroadcastReceiver mBroadcastReceiver;

    MapView mMap;
    TextView mLblNeighborhoodName, mLblNeighborhood;
    ListView mLstNeighborhoods;
    PolygonOptions currentNeighborhoodPolygon;
    MapboxMap mMapboxMap;

    MarkerViewOptions currentPosMarker, resultMarker1;

    YelpAPI yelpAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initializeYelpAPI();
        initializeViews();


        mMap.onCreate(savedInstanceState);

        mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                final LatLng target = new LatLng(40.752835, -73.981422);
//                MarkerViewOptions marker = new MarkerViewOptions()
//                        .position(target);
//                mapboxMap.addMarker(marker);
                mMapboxMap = mapboxMap;

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(40.752835, -73.981422))  // Sets the center of the map to the NYPL at 42nd, Bryant Park
                        .bearing(30)                                // 270 - Sets the orientation of the camera to look west
                        .tilt(30)                                   // Sets the tilt of the camera to 30 degrees
                        .zoom(13)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 4000);

                Gson gson = new Gson();

                InputStream raw =  getResources().openRawResource(R.raw.neighborhoodsgeojson);
                Reader rd = new BufferedReader(new InputStreamReader(raw));
                final Neighborhoods neighborhoods = gson.fromJson(rd, Neighborhoods.class);

                NeighborhoodListBaseAdapter baseAdapter = new
                        NeighborhoodListBaseAdapter(neighborhoods.getFeatures(),
                        MainActivity.this, MainActivity.this);
                mLstNeighborhoods.setAdapter(baseAdapter);

                Neighborhoods.Feature neighborhood = neighborhoods.getFeatures().get(21); //Midtown, Manhattan

                mLblNeighborhoodName.setText(neighborhood.getProperties().getNeighborhood());
//
//                List<LatLng> polygon = new ArrayList<>();
//
//                for (List<Double> coordinates : neighborhood.getGeometry().getCoordinates().get(0)) {
//                    polygon.add(new LatLng(coordinates.get(1), coordinates.get(0)));
//                }
//
//                currentNeighborhoodPolygon = new PolygonOptions()
//                        .addAll(polygon)
//                        .fillColor(Color.argb(20, 220, 20, 60));
//
//                mapboxMap.addPolygon(currentNeighborhoodPolygon);
                addNeighborhoodPolygon(neighborhood.getGeometry().getCoordinates().get(0));
            }
        });


        if (!runtimePermissions()) {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            startService(i);
        }

    }

    private void initializeYelpAPI() {
        YelpAPIFactory apiFactory = new YelpAPIFactory(APIConstants.consumerKey,
                APIConstants.consumerSecret, APIConstants.token,
                APIConstants.tokenSecret);

        yelpAPI = apiFactory.createAPI();
    }

    private void initializeViews(){
        mLblNeighborhoodName = (TextView) findViewById(R.id.lblNeighborhoodName);
        mLstNeighborhoods = (ListView) findViewById(R.id.lstNeighborhoods);
        mLblNeighborhood = (TextView) findViewById(R.id.lblNeighborhoods);

        mLblNeighborhood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WarrantActivity.class);
                startActivity(i);
            }
        });

        mMap = (MapView) findViewById(R.id.mapBox);

        mLblNeighborhoodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPosMarker != null) {
                    Map<String, String> params = new HashMap<>();

                    params.put("term", "Mexican+food");
                    params.put("limit", "3");
                    params.put("sort", "1");

                    CoordinateOptions coordinate = CoordinateOptions.builder()
                            .latitude(currentPosMarker.getPosition().getLatitude())
                            .longitude(currentPosMarker.getPosition().getLongitude()).build();

                    Call<SearchResponse> call = yelpAPI.search(coordinate, params);
                    Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                        @Override
                        public void onResponse(Call<SearchResponse> call, retrofit2.Response<SearchResponse> response) {
                            SearchResponse searchResponse = response.body();
                            Business firstBusiness =  searchResponse.businesses().get(0);
                            Log.d(TAG, "onResponse: total: " + firstBusiness.distance());

                            //TODO display marker

                            LatLng target = new LatLng(firstBusiness.location().coordinate().latitude(),
                                    firstBusiness.location().coordinate().longitude());

                            resultMarker1 = new MarkerViewOptions()
                                    .position(target);
                            mMapboxMap.addMarker(resultMarker1);

                        }

                        @Override
                        public void onFailure(Call<SearchResponse> call, Throwable t) {

                        }
                    };

                    call.enqueue(callback);

                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    double lat = intent.getDoubleExtra("lat", 0.0d);
                    double lng = intent.getDoubleExtra("lng", 0.0d);

                    Location myLoc = new Location("provider");
                    myLoc.setLatitude(lat);
                    myLoc.setLongitude(lng);

                    Log.d(TAG, "onReceive: lat: " + myLoc.getLatitude() + " lng: " + myLoc.getLongitude());

                    updateLocationMarker(myLoc);
                }
            };
        }

        registerReceiver(mBroadcastReceiver, new IntentFilter("location_update"));

    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        mMap.onDestroy();
    }

    @Override
    public void onClick(Neighborhoods.Feature neighborhood) {
        //TODO change neighborhood
        mLblNeighborhoodName.setText(neighborhood.getProperties().getNeighborhood());
        addNeighborhoodPolygon(neighborhood.getGeometry().getCoordinates().get(0));
    }

    public void addNeighborhoodPolygon(List<List<Double>> coordinateList){

        if (mMapboxMap != null){
            if(currentNeighborhoodPolygon != null)
                mMapboxMap.removePolygon(currentNeighborhoodPolygon.getPolygon());

            List<LatLng> polygon = new ArrayList<>();

            for (List<Double> coordinates : coordinateList) {
                polygon.add(new LatLng(coordinates.get(1), coordinates.get(0)));
            }

            currentNeighborhoodPolygon = new PolygonOptions()
                    .addAll(polygon)
                    .fillColor(Color.argb(20, 220, 20, 60));

            mMapboxMap.addPolygon(currentNeighborhoodPolygon);
        }
    }

    private void updateLocationMarker(Location location){

        if (mMapboxMap != null) {
            if (currentPosMarker != null) {
                mMapboxMap.removeMarker(currentPosMarker.getMarker());
            }
            LatLng currLoc = new LatLng(location.getLatitude(), location.getLongitude());
            currentPosMarker = new MarkerViewOptions()
                    .position(currLoc);
            mMapboxMap.addMarker(currentPosMarker);

            //move camera
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(currLoc)
//                    .tilt(30)
//                    .zoom(14)
//                    .build();
//            mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 800);
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


}
