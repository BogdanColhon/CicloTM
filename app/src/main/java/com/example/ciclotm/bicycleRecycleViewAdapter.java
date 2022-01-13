package com.example.ciclotm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Models.Bike;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class bicycleRecycleViewAdapter extends RecyclerView.Adapter<bicycleRecycleViewAdapter.MyViewHolder> {
    Context context;
    public static String bikeCount;
    private StorageReference storageReference;
    private ArrayList<Bike> bikeList;

    public bicycleRecycleViewAdapter(Context context, ArrayList<Bike> bikeList) {
        this.context = context;
        this.bikeList = bikeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nick_nameText;
        TextView brandText;
        TextView yearText;
        TextView weightText;
        TextView descriptionText;
        ImageView bikeImageView;

        public MyViewHolder(final View view) {
            super(view);
            nick_nameText = (TextView) view.findViewById(R.id.nicknameTextView);
            brandText = (TextView) view.findViewById(R.id.bikeBrandTextView);
            yearText = (TextView) view.findViewById(R.id.yearTextView);
            weightText = (TextView) view.findViewById(R.id.weightTextView);
            descriptionText = (TextView) view.findViewById(R.id.descriptionTextView);
            bikeImageView = (ImageView) view.findViewById(R.id.bike_photo);
        }
    }

    @NonNull
    @Override
    public bicycleRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_bicycle_card_layout, parent, false);
        return new bicycleRecycleViewAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull bicycleRecycleViewAdapter.MyViewHolder holder, int position) {
        String nickname = bikeList.get(position).getNick_name();
        holder.nick_nameText.setText(nickname);

        String model = bikeList.get(position).getModel();
        String year = bikeList.get(position).getYear();
        String brand = bikeList.get(position).getBrand();
        holder.brandText.setText(brand + " " + model + " " + year);


        String weight = bikeList.get(position).getWeight();
        holder.weightText.setText(weight + " kg");

        String description = bikeList.get(position).getDetails();
        holder.descriptionText.setText(description);

        String link = bikeList.get(position).getBikePhotoUrl();
        if (!link.equals(""))
            Picasso.get().load(link).resize(500, 500).centerInside().into(holder.bikeImageView);
    }

    @Override
    public int getItemCount() {

        return bikeList.size();
    }
}
