package com.ga.roosevelt.mapboxapp.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ga.roosevelt.mapboxapp.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by roosevelt on 9/6/16.
 */
public class DossierSpinnerAdapter extends ArrayAdapter<String> {

    AssetManager am = getContext().getAssets();

    Typeface typeface = Typeface.createFromAsset(am,
            String.format(Locale.US, "fonts/%s", "PressStart2P.ttf"));

    public DossierSpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public DossierSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public DossierSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtView = (TextView) super.getView(position, convertView, parent);
        txtView.setTypeface(typeface);
        txtView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorText));
        return txtView;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txtView = (TextView) super.getDropDownView(position, convertView, parent);
        txtView.setTypeface(typeface);
        txtView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorText));
        return txtView;
    }


}
