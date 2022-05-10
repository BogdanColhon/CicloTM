package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Models.Route;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class routePostsRecycleViewAdapter extends RecyclerView.Adapter<routePostsRecycleViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<Route> routeList;

    public routePostsRecycleViewAdapter(Context context, ArrayList<Route> routeList) {
        this.context = context;
        this.routeList = routeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView distanceText;
        TextView elapsedTimeText;
        ImageView mapImageView;

        public MyViewHolder(final View view) {
            super(view);
            dateText = (TextView) view.findViewById(R.id.routePostsDateTextView);
            distanceText = (TextView) view.findViewById(R.id.routePostsDistanceTextView);
            elapsedTimeText = (TextView) view.findViewById(R.id.routePostsElapsedTimeTextView);

            mapImageView = (ImageView) view.findViewById(R.id.routePostsMapImageView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_posts_card_layout, viewGroup, false);
        return new routePostsRecycleViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Date time = routeList.get(i).getPublishDate();
        holder.dateText.setText(time.toString());

        double distance = routeList.get(i).getDistance();
        holder.distanceText.setText(String.valueOf(distance));


        String duration = routeList.get(i).getElapsedTime();
        holder.elapsedTimeText.setText(duration);

        String link = routeList.get(i).getMapImageUrl();
        if (!link.equals(""))
            Picasso.get().load(link).resize(500, 500).centerInside().into(holder.mapImageView);
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }


}
