package com.ga.roosevelt.mapboxapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ga.roosevelt.mapboxapp.game_logic.Mission;
import com.ga.roosevelt.mapboxapp.game_logic.MissionBusiness;
import com.ga.roosevelt.mapboxapp.models.Thief;
import com.yelp.clientlib.entities.Business;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by roosevelt on 9/2/16.
 */
public class ThiefDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = ThiefDatabaseHelper.class.getName() + "iiiiiii";

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "vile.db";
    public static final String THIEF_TABLE_NAME = "thief_table";
    public static final String PLACE_TABLE_NAME = "place_table";
    public static final String MISSION_TABLE_NAME = "mission_table";

    //table for thief
    public static final String COL_ID = "_id";
    public static final String COL_THIEF_NAME = "thief_name";
    public static final String COL_THIEF_HAIR_COLOR = "hair_color";
    public static final String COL_THIEF_EYE_COLOR = "eye_color";
    public static final String COL_THIEF_GENDER = "gender";
    public static final String COL_THIEF_VEHICLE = "vehicle";
    public static final String COL_THIEF_FEATURE = "feature";
    public static final String COL_THIEF_FOOD = "food";
    public static final String COL_THIEF_SPORT = "sport";

    //table for place
    public static final String COL_PLACE_ID = "_id";
    //should store place id from yelp
    public static final String COL_PLACE_NAME = "place_name";
    public static final String COL_PLACE_SNIPPET = "place_snippet";
    public static final String COL_PLACE_LAT = "place_lat";
    public static final String COL_PLACE_LNG = "place_lng";
    public static final String COL_PLACE_IMG_URL = "place_image_url";
    public static final String COL_PLACE_TIMES_VISITED = "place_times_visited";

    public static final String CREATE_TABLE_VISITED_PLACES =
            "CREATE TABLE " + PLACE_TABLE_NAME +
                    "(" +
                    COL_PLACE_ID + " TEXT PRIMARY KEY, " +
                    COL_PLACE_NAME + " TEXT, " +
                    COL_PLACE_SNIPPET + " TEXT, " +
                    COL_PLACE_IMG_URL + " TEXT, " +
                    COL_PLACE_LAT + " REAL, " +
                    COL_PLACE_LNG + " REAL, " +
                    COL_PLACE_TIMES_VISITED + " INTEGER )";

    //table for missions
    //adding to this on new game, setting COL_MISSION_CURRENT to true or 1
    public static final String COL_MISSION_ID = "_id";
    public static final String COL_MISSION_START_TIME = "mission_start_time";
    public static final String COL_MISSION_END_TIME = "mission_end_time";
    public static final String COL_MISSION_CURRENT = "mission_is_current";
    //will need function for setting current mission to not current
    public static final String COL_MISSION_ACTUAL_THIEF = "mission_thief_id";
    public static final String COL_MISSION_WARRANT_THIEF = "mission_warrant_thief_id";
    public static final String COL_MISSION_GIVEN_THIEF_CLUES = "mission_given_clues";
    //will store letters/numbers to represent the clues that have already been given
    //  to avoid repetition
    public static final String COL_MISSION_CURRENT_PLACE_IDS = "mission_available_places_ids";
    //3 at a time, comma-separated string of place_ids currently available

    public static final String CREATE_TABLE_MISSION =
            "CREATE TABLE " + MISSION_TABLE_NAME +
                    "(" +
                    COL_MISSION_ID + " INTEGER PRIMARY KEY, " +
                    COL_MISSION_START_TIME + " TEXT, " +
                    COL_MISSION_END_TIME + " TEXT, " +
                    COL_MISSION_CURRENT + " INTEGER, " +
                    COL_MISSION_ACTUAL_THIEF + " INTEGER, " + //MUST REFERENCE THIEF RECORD
                    COL_MISSION_WARRANT_THIEF + " INTEGER, " + //MUST REFERENCE THIEF RECORD
                    COL_MISSION_GIVEN_THIEF_CLUES + " TEXT, " + //STRING OF FLAGS TO REPRESENT CLUES THAT HAVE ALREADY BEEN GIVEN
                    COL_MISSION_CURRENT_PLACE_IDS + " TEXT, " + //IF this contains just 1 place_id, then it's just the start of the game
                    "FOREIGN KEY(" + COL_MISSION_ACTUAL_THIEF + ") " +
                        "REFERENCES " + THIEF_TABLE_NAME + "("+ COL_ID +")," +
                    "FOREIGN KEY(" + COL_MISSION_WARRANT_THIEF + ") " +
                        "REFERENCES " + THIEF_TABLE_NAME + "("+ COL_ID +")" +
                    ")"; //SPACE-SEPARATED STRING OF place_ids
//
//FOREIGN KEY(checklist_id) REFERENCES checklist(_id)

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
        sqLiteDatabase.execSQL(CREATE_TABLE_VISITED_PLACES);
        sqLiteDatabase.execSQL(CREATE_TABLE_MISSION);

        //TODO fill thief database
//        addAllThiefRecords();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + THIEF_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PLACE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MISSION_TABLE_NAME);
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

    public Thief getThiefById(long id){

        Thief thief = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(THIEF_TABLE_NAME,
                COLUMNS_THIEF,
                COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            thief = extractThiefFromCursor(cursor);
        }
        return thief;
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

        int randomIndex = Math.abs(rand.nextInt() % cursor.getCount());  //0 to n

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

    public long getCurrentMissionId(){
        long mission_id = 0;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(MISSION_TABLE_NAME,
                null,
                COL_MISSION_CURRENT + "=?",
                new String[]{"1"},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            mission_id = cursor.getLong(cursor.getColumnIndex(COL_MISSION_ID));
        }
        return mission_id;
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





    //TODO Check if there is current game: for when user clicks new game, it asks if user wants to quit current game
    public boolean hasCurrentGame(){
        boolean hasCurrentGame = false; //think of possible errors
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(MISSION_TABLE_NAME,
                null,
                COL_MISSION_CURRENT + "=?",
                new String[]{"1"},
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            hasCurrentGame = true;
        }

        return hasCurrentGame;

    }

    //TODO Set all game to non-current: for when user starts a new game; and it has to have only one current game, ALWAYS
    public void setAllGamesToNonCurrent(){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_MISSION_CURRENT, 0);

        db.update(MISSION_TABLE_NAME, values, null, null);

        db.close();
    }

    //call this when user creates a new game
    public long createNewGame(long duration){
        //set all games to non current
        setAllGamesToNonCurrent();

        //get a random thief
        Thief thief = getRandomThief(); //ACTUAL_THIEF

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = new Date();
        String currentTime = dateFormat.format(startDate); //START_TIME
        Date endDate = new Date(startDate.getTime() + duration);
        String endTime = dateFormat.format(endDate); //END_TIME

        ContentValues values = new ContentValues();

        values.put(COL_MISSION_START_TIME, currentTime);
        values.put(COL_MISSION_END_TIME, endTime);
        values.put(COL_MISSION_CURRENT, 1); //set this to CURRENT MISSION
        values.put(COL_MISSION_ACTUAL_THIEF, thief.getId());

        Log.d(TAG, "createNewGame: " + String.format(Locale.US, "start: %s; end: %s; thief: %s", currentTime, endTime, thief.getName()));

        SQLiteDatabase db = getWritableDatabase();

        final long mission_id = db.insertOrThrow(MISSION_TABLE_NAME, null, values);

        db.close();

        return mission_id;
    }


    //TODO call this on click of COMPUTE in WarrantActivity
    public void setWarrantOnCurrentMission(Thief thief){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_MISSION_WARRANT_THIEF, thief.getId());

        db.update(MISSION_TABLE_NAME,
                values,
                COL_MISSION_CURRENT + "=?",
                new String[]{"1"});

        db.close();
    }

    //TODO call whenever giving out a clue
    public String getGivenCluesInCurrentMission(){
        String clues = "";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(MISSION_TABLE_NAME,
                null,
                COL_MISSION_CURRENT + "=?",
                new String[]{"1"},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            clues = cursor.getString(cursor.getColumnIndex(COL_MISSION_GIVEN_THIEF_CLUES));
            Log.d(TAG, "getGivenCluesInCurrentMission: " + clues);
        }
        return clues;
    }

    //TODO Call when there's another random clue given
    public void addNewGivenClue(String clue){
        String currentClues = getGivenCluesInCurrentMission();

        String currentWithNewClue = currentClues + " " + clue;

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_MISSION_GIVEN_THIEF_CLUES, currentWithNewClue);

        db.update(MISSION_TABLE_NAME,
                values,
                COL_MISSION_CURRENT + "=?",
                new String[]{"1"});

        db.close();

    }

    //call after receiving data from Yelp API
    public String getPlacesInCurrentMission(){
        String places = "";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(MISSION_TABLE_NAME,
                null,
                COL_MISSION_CURRENT + "=?",
                new String[]{"1"},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            places = cursor.getString(cursor.getColumnIndex(COL_MISSION_CURRENT_PLACE_IDS));
            Log.d(TAG, "getVisitedPlacesInCurrentMission: " + "  current places: " + places);
        }

        return places;

        //TODO should i return a list of strings instead?
        //TODO or get the data from the places table now?

    }

    public List<MissionBusiness> getPlacesInCurrentMissionAsList(){
        List<MissionBusiness> businesses = new ArrayList<>();

        String[] places = getPlacesInCurrentMission().split(" ");

        for (String place : places) {
            MissionBusiness business = getPlaceById(place);
            businesses.add(business);
        }

        return businesses;
    }

    public void addPlacesInCurrentMission(String places){
        SQLiteDatabase db = getWritableDatabase();

        Log.d(TAG, "addPlacesInCurrentMission: places: " + places);
        ContentValues values = new ContentValues();
        values.put(COL_MISSION_CURRENT_PLACE_IDS, places);

        db.update(MISSION_TABLE_NAME,
                values,
                COL_MISSION_CURRENT + "=?",
                new String[]{"1"});

        db.close();
    }

    public boolean isPlaceAlreadyStored(Business business){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(PLACE_TABLE_NAME,
                null,
                COL_PLACE_ID + "=?",
                new String[]{String.valueOf(business.id())},
                null,
                null,
                null);

        return cursor.moveToFirst();
    }

    //call whenever doing Yelp API calls
    public void addPlace(Business business) {

        //check if place already exists

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PLACE_ID, business.id());
        values.put(COL_PLACE_NAME, business.name());
        values.put(COL_PLACE_SNIPPET, business.snippetText());
        values.put(COL_PLACE_IMG_URL, business.imageUrl());
        values.put(COL_PLACE_LAT, business.location().coordinate().latitude());
        values.put(COL_PLACE_LNG, business.location().coordinate().longitude());
        values.put(COL_PLACE_TIMES_VISITED, 0);
        //TODO method for changing this to 1 if already visited

        Log.d(TAG, "addPlace: " + String.format(Locale.US, " id: %s; name: %s; img_url: %s", business.id(), business.name(), business.imageUrl()));

        try{
            db.insertOrThrow(PLACE_TABLE_NAME, null, values);
        } catch (SQLException e){
            e.printStackTrace();
        }

        db.close();
    }

    public MissionBusiness getPlaceById(String place_id){
        MissionBusiness place = null;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PLACE_TABLE_NAME,
                null,
                COL_PLACE_ID + "=?",
                new String[]{place_id},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(COL_PLACE_NAME));
            String snippet = cursor.getString(cursor.getColumnIndex(COL_PLACE_SNIPPET));
            String imgUrl = cursor.getString(cursor.getColumnIndex(COL_PLACE_IMG_URL));
            Double lat = cursor.getDouble(cursor.getColumnIndex(COL_PLACE_LAT));
            Double lng = cursor.getDouble(cursor.getColumnIndex(COL_PLACE_LNG));
            long timesVisited = cursor.getLong(cursor.getColumnIndex(COL_PLACE_TIMES_VISITED));

            place = new MissionBusiness(place_id, name, snippet,imgUrl,lat, lng,timesVisited);
        }

        return place;
    }

    public Mission getMissionById(long id){
        Mission currentMission = null;
        long thief_id = 0;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                MISSION_TABLE_NAME,
                null,
                COL_MISSION_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            String startTimeText = cursor.getString(cursor.getColumnIndex(COL_MISSION_START_TIME));
            String endTimeText = cursor.getString(cursor.getColumnIndex(COL_MISSION_END_TIME));
            Date startTime = null, endTime = null;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
                startTime = format.parse(startTimeText);
                endTime = format.parse(endTimeText);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            thief_id = cursor.getLong(cursor.getColumnIndex(COL_MISSION_ACTUAL_THIEF));

            currentMission = new Mission(startTime, endTime, null, null);

            currentMission.setThief(getThiefById(thief_id));
        }


        return currentMission;
    }

    public boolean hasThiefRecords(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(THIEF_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        return cursor.moveToFirst();
    }


    /**
     TODO Start a new game:
        random thief
        set time to current time
        set end time to current time plus duration; needs parameter and time adder
        set COL_MISSION_CURRENT to 1 (or current)
        set ACTUAL_THIEF to random thief: randomize number 1 to length of Thieftable


        TODO need methods to:
            set COL_WARRANT_THIEF,
            add to COL_GIVEN_THIEF_CLUES, and retrieve COL_GIVEN_THIEF_CLUES: for checking what clue to give next

            add place/places to COL_MISSION_CURRENT_PLACE_IDS


     TODO THINK HOW



     */

    /**
     *
     public static final String CREATE_TABLE_MISSION =
     "CREATE TABLE " + MISSION_TABLE_NAME +
     "(" +
     COL_MISSION_ID + " INTEGER PRIMARY KEY, " +
     COL_MISSION_START_TIME + " TEXT, " +
     COL_MISSION_END_TIME + " TEXT, " +
     COL_MISSION_CURRENT + " INTEGER, " +
     COL_MISSION_ACTUAL_THIEF + " INTEGER, " + //MUST REFERENCE THIEF RECORD
     COL_MISSION_WARRANT_THIEF + " INTEGER, " + //MUST REFERENCE THIEF RECORD
     COL_MISSION_GIVEN_THIEF_CLUES + " TEXT, " + //STRING OF FLAGS TO REPRESENT CLUES THAT HAVE ALREADY BEEN GIVEN
            hair, hobby, feature, auto, sex
     COL_MISSION_CURRENT_PLACE_IDS + " TEXT, " + //IF this contains just 1 place_id, then it's just the start of the game
     "FOREIGN KEY(" + COL_MISSION_ACTUAL_THIEF + ") " +
     "REFERENCES " + THIEF_TABLE_NAME + "("+ COL_ID +")," +
     "FOREIGN KEY(" + COL_MISSION_WARRANT_THIEF + ") " +
     "REFERENCES " + THIEF_TABLE_NAME + "("+ COL_ID +")" +
     ")"; //SPACE-SEPARATED STRING OF place_ids
     */
}
