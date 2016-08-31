package com.ga.roosevelt.mapboxapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ga.roosevelt.mapboxapp.models.Neighborhoods;

import java.util.List;

/**
 * Created by roosevelt on 8/30/16.
 */
public class NeighborhoodListBaseAdapter extends BaseAdapter {
    List<Neighborhoods.Feature> mNeighborhoods;
    Context mContext;
    OnNeighborhoodChosenListener onNeighborhoodChosenListener;

    public interface OnNeighborhoodChosenListener{
        void onClick(Neighborhoods.Feature neighborhood);
    }


    public NeighborhoodListBaseAdapter(List<Neighborhoods.Feature> mNeighborhoods, Context mContext,
                                       OnNeighborhoodChosenListener listener) {
        this.mNeighborhoods = mNeighborhoods;
        this.mContext = mContext;
        this.onNeighborhoodChosenListener = listener;
    }

    @Override
    public int getCount() {
        return mNeighborhoods.size();
    }

    @Override
    public Object getItem(int i) {
        return mNeighborhoods.get(i).getProperties();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater li = LayoutInflater.from(mContext);
            view = li.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }
        final Neighborhoods.Feature neighborhood = mNeighborhoods.get(i);
        TextView lblNeighborhood = (TextView) view.findViewById(android.R.id.text1);
        lblNeighborhood.setTextColor(Color.parseColor("#FFFFFF"));
        lblNeighborhood.setText(neighborhood.getProperties().getNeighborhood());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNeighborhoodChosenListener.onClick(neighborhood);
                Toast.makeText(mContext, neighborhood.getProperties().getNeighborhood(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
