package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ciclotm.Models.Objects.ColorObject;

import java.util.ArrayList;

public class ColorSpinnerAdapter extends ArrayAdapter<ColorObject> {

   public ColorSpinnerAdapter ( Context context, ArrayList<ColorObject> colorList){
       super(context,0,colorList);
   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
       if(convertView == null){
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.color_spinner_item,parent,false);
       }
       View color_blob = convertView.findViewById(R.id.colorBlob);
       TextView textColor = convertView.findViewById(R.id.colorName);

       ColorObject currentItem = getItem(position);

        if(currentItem !=null) {
            color_blob.setBackgroundColor(currentItem.getColor());
            textColor.setText(currentItem.getName());
        }

       return convertView;

    }
}
