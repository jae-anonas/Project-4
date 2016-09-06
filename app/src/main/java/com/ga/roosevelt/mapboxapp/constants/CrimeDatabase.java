package com.ga.roosevelt.mapboxapp.constants;

/**
 * Created by roosevelt on 9/2/16.
 */
public class CrimeDatabase {

    private static CrimeDatabase ourInstance = new CrimeDatabase();

    public static CrimeDatabase getInstance() {
        return ourInstance;
    }

    private CrimeDatabase() {
    }
}
