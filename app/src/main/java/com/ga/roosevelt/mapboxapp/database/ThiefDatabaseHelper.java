package com.ga.roosevelt.mapboxapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ga.roosevelt.mapboxapp.models.Thief;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by roosevelt on 9/2/16.
 */
public class ThiefDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = ThiefDatabaseHelper.class.getName() + "iiiiiii";

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "vile.db";
    public static final String THIEF_TABLE_NAME = "thief_table";

    public static final String COL_ID = "_id";
    public static final String COL_THIEF_NAME = "thief_name";
    public static final String COL_THIEF_HAIR_COLOR = "hair_color";
    public static final String COL_THIEF_EYE_COLOR = "eye_color";
    public static final String COL_THIEF_GENDER = "gender";
    public static final String COL_THIEF_VEHICLE = "vehicle";
    public static final String COL_THIEF_FEATURE = "feature";
    public static final String COL_THIEF_FOOD = "food";
    public static final String COL_THIEF_SPORT = "sport";

    public static final String[] COLUMNS_THIEF = new String[]{
            COL_ID, COL_THIEF_NAME, COL_THIEF_HAIR_COLOR, COL_THIEF_EYE_COLOR,
            COL_THIEF_GENDER, COL_THIEF_VEHICLE, COL_THIEF_FEATURE, COL_THIEF_FOOD,
            COL_THIEF_SPORT};

    private static final String CREATE_THIEF_TABLE =
            "CREATE TABLE " + THIEF_TABLE_NAME +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY, " +
                    COL_THIEF_NAME + " TEXT, " +
                    COL_THIEF_HAIR_COLOR + " TEXT, " +
                    COL_THIEF_EYE_COLOR + " TEXT, " +
                    COL_THIEF_GENDER + " TEXT, " +
                    COL_THIEF_VEHICLE + " TEXT, " +
                    COL_THIEF_FEATURE + " TEXT, " +
                    COL_THIEF_FOOD + " TEXT," +
                    COL_THIEF_SPORT + " TEXT )";

    private static ThiefDatabaseHelper sInstance;

    public static ThiefDatabaseHelper getInstance(Context context){
        if (sInstance == null) {
            sInstance = new ThiefDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return sInstance;
    }

    private ThiefDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_THIEF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + THIEF_TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public long addThief(Thief thief){

        ContentValues thiefValues = new ContentValues();
        thiefValues.put(COL_THIEF_NAME, thief.getName());
        thiefValues.put(COL_THIEF_EYE_COLOR, thief.getEyeColor());
        thiefValues.put(COL_THIEF_HAIR_COLOR, thief.getHairColor());
        thiefValues.put(COL_THIEF_GENDER, thief.getGender());
        thiefValues.put(COL_THIEF_VEHICLE, thief.getVehicle());
        thiefValues.put(COL_THIEF_FEATURE, thief.getFeature());
        thiefValues.put(COL_THIEF_FOOD, thief.getFood());
        thiefValues.put(COL_THIEF_SPORT, thief.getSport());

        SQLiteDatabase db = getWritableDatabase();
        return db.insertOrThrow(THIEF_TABLE_NAME, null, thiefValues);
    }

    public Thief getRandomThief() {
        Thief thief = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(THIEF_TABLE_NAME,
                COLUMNS_THIEF,
                null,
                null,
                null,
                null,
                null);
        Random rand = new SecureRandom();

        int randomIndex = Math.abs(rand.nextInt() % cursor.getCount());

        Log.d(TAG, "getRandomThief: " + cursor.getCount());
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                if(randomIndex-- == 0){
                    thief = extractThiefFromCursor(cursor);
                    break;
                }
                cursor.moveToNext();
            }
        }

        cursor.close();

        return thief;
    }



    //Helper method
    private Thief extractThiefFromCursor(Cursor cursor){
        long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
        String name = cursor.getString(cursor.getColumnIndex(COL_THIEF_NAME));
        String hairColor = cursor.getString(cursor.getColumnIndex(COL_THIEF_HAIR_COLOR));
        String eyeColor = cursor.getString(cursor.getColumnIndex(COL_THIEF_EYE_COLOR));
        String gender = cursor.getString(cursor.getColumnIndex(COL_THIEF_GENDER));
        String vehicle = cursor.getString(cursor.getColumnIndex(COL_THIEF_VEHICLE));
        String feature = cursor.getString(cursor.getColumnIndex(COL_THIEF_FEATURE));
        String sport = cursor.getString(cursor.getColumnIndex(COL_THIEF_SPORT));
        String food = cursor.getString(cursor.getColumnIndex(COL_THIEF_FOOD));

        Thief thief = new Thief(name, hairColor, eyeColor, gender, vehicle, feature, sport, food);
        thief.setId(id);
        return thief;
    }

    public void addAllThiefRecords(){
        List<Thief> thieves = new ArrayList<>();
        thieves.add(new Thief("Yul B. Sorry", "black", "gray", "male", "motorcycle", "scar", "tennis", "asian"));
        thieves.add(new Thief("Nick Brunch","black","green","male","motorcycle","jewelry","mountain climbing","mexican"));
        thieves.add(new Thief("Fast Eddie B.","black","brown","male","convertible","jewelry","croquet","mexican"));
        thieves.add(new Thief("Robin Banks","brown","brown","male","convertible","wig","mountain climbing","seafood"));
        thieves.add(new Thief("Sandy Dunes","brown","blue","male","skateboard","wig","skin diving","chinese"));
        thieves.add(new Thief("Sam O'Nella","blonde","green","male","skateboard","scar","skin diving","chinese"));
        thieves.add(new Thief("Bjorn Toulouse","blonde","blue","male","limousine","tattoo","tennis","asian"));
        thieves.add(new Thief("Ihor Ihorovich","blonde","green","male","limousine","tattoo","croquet","seafood"));
        thieves.add(new Thief("Len Bulk","red","blue","male","convertible","tattoo","mountain climbing","seafood"));
        thieves.add(new Thief("Scar Graynolt","red","gray","male","limousine","jewelry","croquet","mexican"));

        thieves.add(new Thief("Li Non Mee","black","brown","female","skateboard","wig","croquet","mexican"));
        thieves.add(new Thief("Bessie May Mucho","black","gray","female","convertible","jewelry","skin diving","asian"));
        thieves.add(new Thief("Carmen Sandiego","brown","brown","female","convertible","jewelry","tennis","mexican"));
        thieves.add(new Thief("Irma Dillow","brown","green","female","skateboard","wig","mountain climbing","asian"));
        thieves.add(new Thief("Merey LaRoc","brown","green","female","limousine","jewelry","mountain climbing","mexican"));
        thieves.add(new Thief("Katherine Drib","brown","blue","female","motorcycle","tattoo","mountain climbing","seafood"));
        thieves.add(new Thief("Sarah Nade","blond","gray","female","limousine","scar","skin diving","chinese"));
        thieves.add(new Thief("Dazzle Annie","blonde","blue","female","limousine","tattoo","tennis","seafood"));
        thieves.add(new Thief("Rosa Sarrosas-Arroz","red","brown","female","motorcycle","scar","croquet","chinese"));
        thieves.add(new Thief("Lady Agatha Wayland","red","green","female","convertible","jewelry","tennis","mexican"));

        for (Thief thief : thieves) {
            //add to database
            addThief(thief);
        }

    }

    //method to use for issuing warrant
    //intentionally left out food: so that player will have to figure it out based on the places he/she is lead to
    private List<Thief> searchThiefDatabase(String gender, String hairColor, String eyeColor,
                                            String vehicle, String feature, String sport){
        List<Thief> thieves = new ArrayList<>();
        
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(THIEF_TABLE_NAME,
                null,
                COL_THIEF_EYE_COLOR + " LIKE " + "'%" + eyeColor + "%' AND " +
                    COL_THIEF_HAIR_COLOR + " LIKE " + "'%" + hairColor + "%' AND " +
                    COL_THIEF_GENDER + " LIKE " + "'%" + gender + "%' AND " +
                    COL_THIEF_VEHICLE + " LIKE " + "'%" + vehicle + "%' AND " +
                    COL_THIEF_FEATURE + " LIKE " + "'%" + feature + "%' AND " +
                    COL_THIEF_SPORT + " LIKE " + "'%" + sport + "%'",
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                thieves.add(extractThiefFromCursor(cursor));
                cursor.moveToNext();
            }
        }

        return thieves;
    }
    public List<Thief> searchThiefDatabase(ContentValues searchValues){
        List<Thief> thieves = new ArrayList<>();
        Log.d(TAG, "searchThiefDatabase:--------------- gender " + searchValues.getAsString("gender"));

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(THIEF_TABLE_NAME,
                null,
                COL_THIEF_HAIR_COLOR + " LIKE " + "'%" + searchValues.getAsString("hairColor") + "%' AND " +
                        COL_THIEF_GENDER + "='" + searchValues.getAsString("gender") + "' AND " +
                        COL_THIEF_VEHICLE + " LIKE " + "'%" + searchValues.getAsString("vehicle") +  "%' AND " +
                        COL_THIEF_FEATURE + " LIKE " + "'%" + searchValues.getAsString("feature") +  "%' AND " +
                        COL_THIEF_SPORT + " LIKE " + "'%" + searchValues.getAsString("sport") +  "%'",
                null,
                null,
                null,
                null
        );
        Log.d(TAG, "searchThiefDatabase: cursor count: " + cursor.getCount());

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                thieves.add(extractThiefFromCursor(cursor));
                cursor.moveToNext();
            }
        }
        Log.d(TAG, "searchThiefDatabase: list count: " + thieves.size());
        db.close();

        return thieves;
    }

    public void removeAllThiefRecords(){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(THIEF_TABLE_NAME,
                null,
                null);
        db.close();
    }

}
