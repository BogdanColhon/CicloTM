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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class tureRecycleViewAdapter extends RecyclerView.Adapter<tureRecycleViewAdapter.MyViewHolder> {
    Context context;
    public static String turePostsCount;
    private StorageReference storageReference;
    private ArrayList<turePost> postsList;
    private OnPostListener mOnPostListener;

    public tureRecycleViewAdapter(Context context, ArrayList<turePost> postsList, OnPostListener onPostListener) {
        this.context = context;
        this.postsList = postsList;
        this.mOnPostListener=onPostListener;
    }

    @NonNull
    @Override
    public tureRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_ture_card_layout, parent, false);
        return new MyViewHolder(itemView, mOnPostListener);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView distance;
        TextView start_time;
        TextView start_point;
        TextView no_participants;
        ImageView user_photo;
        OnPostListener onPostListener;

        public MyViewHolder(final View view, OnPostListener OnPostListener) {
            super(view);
            distance = (TextView) view.findViewById(R.id.upperTextView);
            start_time = (TextView) view.findViewById(R.id.firstMiddleTextView);
            start_point = (TextView) view.findViewById(R.id.secondMiddleTextView);
            no_participants = (TextView) view.findViewById(R.id.lowerTextView);
            user_photo = (ImageView) view.findViewById(R.id.user_photo);
            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull tureRecycleViewAdapter.MyViewHolder holder, int position) {
        String distance =postsList.get(position).getDistance() + " km";
        holder.distance.setText(distance);

        String start_point =postsList.get(position).getStart_point();
        holder.start_point.setText(start_point);

        int no_participants = postsList.get(position).getNo_participants();
        String text_participants = String.valueOf(no_participants);
        holder.no_participants.setText(text_participants);

        String start_time =postsList.get(position).getStart_time();
        holder.start_time.setText(start_time);

        String userProfilePicture = "UsersProfilePicture/" + postsList.get(position).getUid() + ".png";
        storageReference = FirebaseStorage.getInstance().getReference().child(userProfilePicture);
        String userImageUrl = postsList.get(position).getUserImageUrl();
        Picasso.get().load(userImageUrl).fit().centerInside().into(holder.user_photo);
    }

    @Override
    public int getItemCount() {

        turePostsCount = String.valueOf(postsList.size());

        return postsList.size();
    }
    public interface OnPostListener {
        void onPostClick(int position);
    }
}
