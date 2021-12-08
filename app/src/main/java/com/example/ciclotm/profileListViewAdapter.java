package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class profileListViewAdapter extends ArrayAdapter<profileListViewButton> {

    public profileListViewAdapter(@NonNull Context context, @NonNull ArrayList<profileListViewButton> objects) {
        super(context,R.layout.profile_listview_button_layout, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        profileListViewButton button = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_listview_button_layout,parent,false);
        }

        TextView button_name =(TextView) convertView.findViewById(R.id.profile_button);
        button_name.setText(button.getButton_name());
        return convertView;
    }

    @Nullable
    @Override
    public profileListViewButton getItem(int position) {
        return super.getItem(position);
    }
}
