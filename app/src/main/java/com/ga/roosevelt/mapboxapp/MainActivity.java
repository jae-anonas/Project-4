package com.ga.roosevelt.mapboxapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.ga.roosevelt.mapboxapp.models.Neighborhoods;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NeighborhoodListBaseAdapter.OnNeighborhoodChosenListener {

    private static final String TAG = "iiiiiii";
    MapView mMap;
    TextView mLblNeighborhoodName;
    ListView mLstNeighborhoods;
    PolygonOptions currentNeighborhoodPolygon;
    MapboxMap mMapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mLblNeighborhoodName = (TextView) findViewById(R.id.lblNeighborhoodName);

        mLstNeighborhoods = (ListView) findViewById(R.id.lstNeighborhoods);

        mMap = (MapView) findViewById(R.id.mapBox);
        mMap.onCreate(savedInstanceState);

        mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                final LatLng target = new LatLng(40.752835, -73.981422);
                MarkerViewOptions marker = new MarkerViewOptions()
                        .position(target);
                mapboxMap.addMarker(marker);
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

    }
    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
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
}
