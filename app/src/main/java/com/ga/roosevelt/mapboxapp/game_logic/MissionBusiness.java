package com.ga.roosevelt.mapboxapp.game_logic;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by roosevelt on 9/11/16.
 */

public class MissionBusiness{
    String place_id, name, snippet, imgUrl;
    LatLng location;
    long timesVisited;

    public MissionBusiness(String place_id, String name, String snippet, String imgUrl, Double lat, Double lng, long timesVisited) {
        this.place_id = place_id;
        this.name = name;
        this.snippet = snippet;
        this.imgUrl = imgUrl;
        LatLng loc = new LatLng(lat, lng);
        this.location = loc;
        this.timesVisited = timesVisited;
    }


    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public long getTimesVisited() {
        return timesVisited;
    }

    public void setTimesVisited(long timesVisited) {
        this.timesVisited = timesVisited;
    }

        /*TODO:
            needs variables for:

                long place_id
                LatLng location
                String name
                long timesVisitedInGame

            needs methods for:





     */


}