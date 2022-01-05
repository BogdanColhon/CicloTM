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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class tureRecycleViewAdapter extends RecyclerView.Adapter<tureRecycleViewAdapter.MyViewHolder> {
    Context context;
    public static String turePostsCount;
    private StorageReference storageReference;
    private ArrayList<turePost> postsList;

    public tureRecycleViewAdapter(Context context, ArrayList<turePost> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView distance;
        TextView start_time;
        TextView start_point;
        TextView no_participants;
        ImageView user_photo;

        public MyViewHolder(final View view) {
            super(view);
            distance = (TextView) view.findViewById(R.id.upperTextView);
            start_time = (TextView) view.findViewById(R.id.firstMiddleTextView);
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
        String distance = "Distanța: " + postsList.get(position).getDistance() + " km";
        holder.distance.setText(distance);

        String start_point = "Punct plecare: " + postsList.get(position).getStart_point();
        holder.start_point.setText(start_point);

        int no_participants = postsList.get(position).getNo_participants();
        String text_participants = "Nr. participanți: " + no_participants;
        holder.no_participants.setText(text_participants);

        String start_time = "Oră plecare: " + postsList.get(position).getStart_time();
        holder.start_time.setText(start_time);

        String userProfilePicture = "UsersProfilePicture/" + postsList.get(position).getUid() + ".png";
        storageReference = FirebaseStorage.getInstance().getReference().child(userProfilePicture);
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
                holder.user_photo.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {

        turePostsCount = String.valueOf(postsList.size());

        return postsList.size();
    }
}
