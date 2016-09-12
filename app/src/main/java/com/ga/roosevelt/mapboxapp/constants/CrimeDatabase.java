package com.ga.roosevelt.mapboxapp.constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by roosevelt on 9/2/16.
 */
public class CrimeDatabase extends SQLiteOpenHelper{

    public static final String DB_NAME = "crime.db";
    public static final int DB_VERSION = 1;

    private static CrimeDatabase sInstance;

    public static CrimeDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CrimeDatabase(context);
        }
        return sInstance;
    }

    private CrimeDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
