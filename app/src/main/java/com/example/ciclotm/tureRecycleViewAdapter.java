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

public class tureRecycleViewAdapter extends RecyclerView.Adapter<tureRecycleViewAdapter.MyViewHolder> {
    Context context;
    private ArrayList<turePost> postsList;

    public tureRecycleViewAdapter(Context context, ArrayList<turePost> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView distance;
        TextView time;
        TextView start_point;
        TextView no_participants;
        ImageView user_photo;

        public MyViewHolder(final View view) {
            super(view);
            distance = (TextView) view.findViewById(R.id.upperTextView);
            time = (TextView) view.findViewById(R.id.firstMiddleTextView);
            start_point = (TextView) view.findViewById(R.id.secondMiddleTextView);
            no_participants = (TextView) view.findViewById(R.id.lowerTextView);
            user_photo = (ImageView) view.findViewById(R.id.user_photo);
        }
    }

    @NonNull
    @Override
    public tureRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_ture_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull tureRecycleViewAdapter.MyViewHolder holder, int position) {
        String distance = postsList.get(position).getDistance();
        holder.distance.setText(distance);
        String time = postsList.get(position).getTime();
        holder.time.setText(time);
        String start_point = postsList.get(position).getStart_point();
        holder.start_point.setText(start_point);
        String no_participants = postsList.get(position).getNo_participants();
        holder.no_participants.setText(no_participants);
        Drawable user_photo=postsList.get(position).getUser_photo();
        holder.user_photo.setImageDrawable(user_photo);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
