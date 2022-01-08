package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext=context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.stolen_bike_window,null);
    }

    private void renderWindowText(Marker marker, View view){
        String title=marker.getTitle();
        TextView t1 = (TextView) view.findViewById(R.id.title);
        t1.setText(title);
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindowText(marker,mWindow);
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker,mWindow);
        return null;
    }
}
