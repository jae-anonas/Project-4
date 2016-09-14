package com.ga.roosevelt.mapboxapp.constants;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.Locale;

/**
 * Created by roosevelt on 9/8/16.
 */
public class CustomFonts {

    static AssetManager mAssetManager;

    static CustomFonts sInstance;

    private CustomFonts(Context context) {
        mAssetManager = context.getAssets();
    }

    public static CustomFonts getInstance(Context context){
        if (sInstance == null)
            sInstance = new CustomFonts(context);
        return sInstance;
    }

    public Typeface getBasicFont(){
        return Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "PressStart2P.ttf"));
    }

    public Typeface getLogoFont(){
        return Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "vermin_vibes_1989.ttf"));
    }

}
