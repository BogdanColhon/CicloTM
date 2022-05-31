package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Objects.Route;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class routePostsRecycleViewAdapter extends RecyclerView.Adapter<routePostsRecycleViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<Route> routeList;
    private OnPostListener mOnPostListener;

    public routePostsRecycleViewAdapter(Context context, ArrayList<Route> routeList, OnPostListener onPostListener) {
        this.context = context;
        this.routeList = routeList;
        this.mOnPostListener = onPostListener;
    }

    public void updateRoutePostsList(ArrayList<Route> routes) {
        routeList.clear();
        routeList.addAll(routes);
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateText;
        TextView distanceText;
        TextView elapsedTimeText;
        ImageView mapImageView;
        OnPostListener onPostListener;

        public MyViewHolder(final View view, OnPostListener OnPostListener) {
            super(view);
            dateText = (TextView) view.findViewById(R.id.routePostsDateTextView);
            distanceText = (TextView) view.findViewById(R.id.routePostsDistanceTextView);
            elapsedTimeText = (TextView) view.findViewById(R.id.routePostsTimeTextView);

            mapImageView = (ImageView) view.findViewById(R.id.routePostsMapImageView);
            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_posts_card_layout, viewGroup, false);
        return new routePostsRecycleViewAdapter.MyViewHolder(itemView, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Date time = routeList.get(i).getPublishDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(time);
        holder.dateText.setText(output);

        double distance = routeList.get(i).getDistance();
        holder.distanceText.setText(String.format("%.2f", distance) + " km");


        String duration = routeList.get(i).getElapsedTime();
        holder.elapsedTimeText.setText(duration);

        String link = routeList.get(i).getMapImageUrl();
        if (!link.equals(""))
            Glide.with(context).load(link).into(holder.mapImageView);
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public interface OnPostListener {
        void onPostClick(int position);
    }

}
