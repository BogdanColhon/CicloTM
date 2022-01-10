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
            yearText = (TextView) view.findViewById(R.id.bikePurchasingYearTextView);
            weightText = (TextView) view.findViewById(R.id.bikeWeightTextView);
            descriptionText = (TextView) view.findViewById(R.id.detailsTextView);
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
        String nickname= bikeList.get(position).getNick_name();
        holder.nick_nameText.setText(nickname);

        String brand = bikeList.get(position).getBrand();
        holder.brandText.setText(brand);

        String weight= bikeList.get(position).getWeight();
        holder.weightText.setText(weight);

        String year= bikeList.get(position).getYear();
        holder.yearText.setText(year);

        String description= bikeList.get(position).getDetails();
        holder.descriptionText.setText(description);

        String userProfilePicture = "UsersProfilePicture/" + bikeList.get(position).getOwner() + ".png";
        String a="26mTrrDNEAgRHz2OoSZ8Q3Ig3N22/ReportImages/Thu Jan 06 18:38:03 GMT+02:00 2022/89c0becee797f6a54cd65c66191db2cf.jpg";
        storageReference = FirebaseStorage.getInstance().getReference().child(a);
        File localFile = null;
        try {
            localFile = File.createTempFile("tempFile", "png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File finalLocalFile = localFile;
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                holder.bikeImageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {

        return bikeList.size();
    }
}
