package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;
    String[] titles;
    int[] images;

    LayoutInflater inflater;

    public GridAdapter(Context context, String[] titles, int[] images){
        this.context=context;
        this.titles=titles;
        this.images=images;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.rules_grid_item,null);
        }

        ImageView imageView = convertView.findViewById(R.id.rulesGridItemImage);
        TextView textView = convertView.findViewById(R.id.rulesGridItemTextView);

        imageView.setImageResource(images[position]);
        textView.setText(titles[position]);
        return convertView;
    }
}
