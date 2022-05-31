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
import com.example.ciclotm.Models.Objects.Photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class galleryRecycleViewAdapter extends RecyclerView.Adapter<galleryRecycleViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<Photo> items;

    public galleryRecycleViewAdapter(Context context, ArrayList<Photo> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.gallery_card_layout, viewGroup, false);
        return new galleryRecycleViewAdapter.MyViewHolder(itemView);
    }

    public void updatePhotoList(ArrayList<Photo> photos) {
        items.clear();
        items.addAll(photos);
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements com.example.ciclotm.MyViewHolder {
        TextView date;
        ImageView imageView;

        public MyViewHolder(final View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.galleryItemDateTextView);
            imageView = (ImageView) view.findViewById(R.id.galleryItemImageView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Date dateItem = items.get(i).getPhotoDate();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault());
        String output = df.format(dateItem);
        holder.date.setText(output);

        String imageUrl = items.get(i).getPhotoUrl();
        System.out.println(imageUrl);
        Glide.with(context).load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
