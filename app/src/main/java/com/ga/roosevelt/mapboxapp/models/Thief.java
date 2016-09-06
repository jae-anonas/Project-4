package com.ga.roosevelt.mapboxapp.models;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by roosevelt on 9/2/16.
 */
public class Thief {
    long id;
    String name;
    String hairColor;
    String eyeColor;
    String gender;
    String vehicle;
    String feature;
    String sport;
    String food;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Thief(String name, String hairColor, String eyeColor, String gender, String vehicle, String feature, String sport, String food) {
        this.name = name;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.gender = gender;
        this.vehicle = vehicle;
        this.feature = feature;
        this.sport = sport;
        this.food = food;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

}
