package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class galleryRecycleViewAdapter extends RecyclerView.Adapter<galleryRecycleViewAdapter.MyViewHolder>{

    Context context;
    private ArrayList<Photo> items;

    public galleryRecycleViewAdapter(Context context, ArrayList<Photo> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView =inflater.inflate(R.layout.gallery_card_layout, viewGroup, false);
        return new galleryRecycleViewAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements com.example.ciclotm.MyViewHolder {
        TextView title;
        ImageView imageView;

        public MyViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.galleryItemDateTextView);
            imageView = (ImageView) view.findViewById(R.id.galleryItemImageView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        String title_text = items.get(i).getPhotoTitle();
        holder.title.setText(title_text);

        String imageUrl = items.get(i).getPhotoUrl();
        System.out.println(imageUrl);
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}