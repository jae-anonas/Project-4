package com.ga.roosevelt.mapboxapp.game_logic;

import android.content.Context;

import com.ga.roosevelt.mapboxapp.models.Thief;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by roosevelt on 9/8/16.
 */
public class Mission {
    Date missionStart, missionEnd;
    Thief thief;

    List<MissionBusiness> missionPlaces;

    public Mission(Date missionStart, Date missionEnd, Thief thief, List<MissionBusiness> missionPlaces) {
        this.missionStart = missionStart;
        this.missionEnd = missionEnd;
        this.thief = thief;
        this.missionPlaces = missionPlaces;
    }

    public Date getMissionStart() {
        return missionStart;
    }

    public void setMissionStart(Date missionStart) {
        this.missionStart = missionStart;
    }

    public Date getMissionEnd() {
        return missionEnd;
    }

    public void setMissionEnd(Date missionEnd) {
        this.missionEnd = missionEnd;
    }

    public Thief getThief() {
        return thief;
    }

    public void setThief(Thief thief) {
        this.thief = thief;
    }

    public List<MissionBusiness> getMissionPlaces() {
        return missionPlaces;
    }

    public void setMissionPlaces(List<MissionBusiness> missionPlaces) {
        this.missionPlaces = missionPlaces;
    }

    /**

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
     }*/


    /*TODO:
        needs variables for:

            Date missionStartTime, missionEndTime
            Thief thief;
            CrimeDatabaseHelper crimeDBHelper;
            MissionBusiness[3] currentMissionPlaces;         --holds current place object
                MissionBusiness (may extend Business)




        constructor:
            Mission(Context context, long duration){
                ...initialize the following:
                    missionStartTime = time now
                    missionEndTime = time now + duration


            }

        needs methods for:
            1 - fillCurrentAvailablePlaces(w/ API Call or SQLite database query)
            2 - getRandomThief
            3 -
     */

}
