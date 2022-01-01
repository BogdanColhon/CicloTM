package com.example.ciclotm;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class furturiRecycleViewAdapter extends RecyclerView.Adapter<furturiRecycleViewAdapter.MyViewHolder> {
    Context context;
    private ArrayList<furturiPost> postsList;

    public furturiRecycleViewAdapter(Context context, ArrayList<furturiPost> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView location;
        TextView description;
        ImageView bike_photo;

        public MyViewHolder(final View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.upperTextView);
            location = (TextView) view.findViewById(R.id.middleTextView);
            description = (TextView) view.findViewById(R.id.lowerTextView);
            bike_photo = (ImageView) view.findViewById(R.id.bike_photo);
        }
    }

    @NonNull
    @Override
    public furturiRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_furturi_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull furturiRecycleViewAdapter.MyViewHolder holder, int position) {
        String date = postsList.get(position).getDate();
        holder.date.setText(date);
        String location = postsList.get(position).getLocation();
        holder.location.setText(location);
        String description = postsList.get(position).getDescription();
        holder.description.setText(description);
        Drawable bike_photo=postsList.get(position).getBike_photo();
        holder.bike_photo.setImageDrawable(bike_photo);
    }


    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
