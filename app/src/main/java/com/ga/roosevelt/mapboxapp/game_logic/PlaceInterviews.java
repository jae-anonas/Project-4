package com.ga.roosevelt.mapboxapp.game_logic;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by roosevelt on 9/12/16.
 */
public class PlaceInterviews {

    List<String> interviewFormatsHair, interviewFormatsAuto, interviewFormatsSport, interviewFormatsFeature;

    private static PlaceInterviews ourInstance = new PlaceInterviews();

    public static PlaceInterviews getInstance() {
        return ourInstance;
    }

    private PlaceInterviews() {
        fillInterviewFormats();
    }

    private void fillInterviewFormats() {
        interviewFormatsHair = new LinkedList<>();
        interviewFormatsHair.add("%s had %s hair");

        interviewFormatsSport = new LinkedList<>();
        interviewFormatsSport.add("%s said %s came from a %s tournament.");
        interviewFormatsSport.add("%s said %s can't wait to do more %s.");

        interviewFormatsFeature = new LinkedList<>();
        interviewFormatsFeature.add("%s had %s."); //remember, when clue is not jewelry, add "a"

        interviewFormatsAuto = new LinkedList<>();
        interviewFormatsAuto.add("%s arrived on a %s.");
        interviewFormatsAuto.add("%s came in riding a %s.");


    }
    //TODO getHeaderComment like "I saw the person you are looking for"

    public String getRandomHairComment(String gender, String clueValue){
        String comment;
        String pronoun1 = gender == "female" ? "She": "He";

        Random rand = new SecureRandom();
        int index = Math.abs(rand.nextInt() % interviewFormatsHair.size());

        comment = String.format(Locale.US, interviewFormatsHair.get(index), pronoun1, clueValue);

        return comment;
    }

    public String getRandomHobbyComment(String gender, String clueValue){
        String comment;
        String pronoun1 = gender == "female" ? "She": "He";
        String pronoun2 = gender == "female" ? "she": "he";

        Random rand = new SecureRandom();
        int index = Math.abs(rand.nextInt() % interviewFormatsSport.size());

        comment = String.format(Locale.US, interviewFormatsSport.get(index), pronoun1, pronoun2, clueValue);

        return comment;
    }

    public String getRandomFeatureComment(String gender, String clueValue){
        String comment;
        String pronoun = gender == "female" ? "She": "He";
        if (!clueValue.equals("jewelry")){
            clueValue = "a " + clueValue;
        }

        Random rand = new SecureRandom();
        int index = Math.abs(rand.nextInt() % interviewFormatsFeature.size());

        comment = String.format(Locale.US, interviewFormatsFeature.get(index), pronoun, clueValue);

        return comment;
    }


    public String getRandomAutoComment(String gender, String clueValue){
        String comment;
        String pronoun = gender == "female" ? "She": "He";

        Random rand = new SecureRandom();
        int index = Math.abs(rand.nextInt() % interviewFormatsAuto.size());

        comment = String.format(Locale.US, interviewFormatsAuto.get(index), pronoun, clueValue);

        return comment;
    }
}
