package com.example.ciclotm;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Models.Report;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class furturiRecycleViewAdapter extends RecyclerView.Adapter<furturiRecycleViewAdapter.MyViewHolder> {
    Context context;
    public static String furturiPostsCount;
    private StorageReference storageReference;
    private ArrayList<Report> postsList;

    public furturiRecycleViewAdapter(Context context, ArrayList<Report> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public furturiRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_furturi_card_layout, parent, false);
        return new MyViewHolder(itemView);
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

    @Override
    public void onBindViewHolder(@NonNull furturiRecycleViewAdapter.MyViewHolder holder, int position) {
        Date date = postsList.get(position).getStolenDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(date);
        holder.date.setText(output);
        String location = postsList.get(position).getAddress();
        holder.location.setText(location);
        String description = postsList.get(position).getBike_model();
        holder.description.setText(description);
        String link = postsList.get(position).getBikeImageUrl();
        Picasso.get().load(link).into(holder.bike_photo);
    }


    @Override
    public int getItemCount() {

       furturiPostsCount = String.valueOf(postsList.size());
        return postsList.size();
    }
}
