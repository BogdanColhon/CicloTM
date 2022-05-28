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
import com.example.ciclotm.Models.Report;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class furturiRecycleViewAdapter extends RecyclerView.Adapter<furturiRecycleViewAdapter.MyViewHolder> {
    Context context;
    public static String furturiPostsCount;
    private StorageReference storageReference;
    private ArrayList<Report> postsList;
    private OnPostListener mOnPostListener;

    public furturiRecycleViewAdapter(Context context, ArrayList<Report> postsList, OnPostListener onPostListener) {
        this.context = context;
        this.postsList = postsList;
        this.mOnPostListener=onPostListener;
    }

    @NonNull
    @Override
    public furturiRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_furturi_card_layout, parent, false);
        return new MyViewHolder(itemView,mOnPostListener);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView location;
        TextView description;
        ImageView bike_photo;
        OnPostListener onPostListener;

        public MyViewHolder(final View view, OnPostListener OnPostListener) {
            super(view);
            date = (TextView) view.findViewById(R.id.upperTextView);
            location = (TextView) view.findViewById(R.id.middleTextView);
            description = (TextView) view.findViewById(R.id.lowerTextView);
            bike_photo = (ImageView) view.findViewById(R.id.bike_photo);
            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull furturiRecycleViewAdapter.MyViewHolder holder, int position) {
        Date date = postsList.get(position).getStolenDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm | dd/MM/yyyy", Locale.getDefault());
        String output = df.format(date);
        holder.date.setText(output);
        String location = postsList.get(position).getAddress();
        holder.location.setText(location);
        String description = postsList.get(position).getBike_model();
        holder.description.setText(description);
        String link = postsList.get(position).getBikeImageUrl();
        if(!link.equals(""))
            Glide.with(context).load(link).into(holder.bike_photo);
    }


    @Override
    public int getItemCount() {

       furturiPostsCount = String.valueOf(postsList.size());
        return postsList.size();
    }
    public interface OnPostListener {
        void onPostClick(int position);
    }
}
