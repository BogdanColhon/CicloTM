package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Models.Route;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class communityPostsRecyclerViewAdapter extends RecyclerView.Adapter<communityPostsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<generalPost> generalPosts;
    private ArrayList<turePost> turePosts;
    private ArrayList<furturiPost> furturiPosts;

    public communityPostsRecyclerViewAdapter(Context context, ArrayList<generalPost> generalPosts, ArrayList<turePost> turePosts, ArrayList<furturiPost> furturiPosts) {

        this.context = context;
        this.generalPosts = generalPosts;
        this.turePosts = turePosts;
        this.furturiPosts = furturiPosts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_posts_card_layout, viewGroup, false);
        return new communityPostsRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dateText;
        TextView distanceText;
        TextView elapsedTimeText;
        ImageView mapImageView;

        public MyViewHolder(final View view){
            super(view);
        }
    }



    @Override
    public int getItemCount() {
        return generalPosts.size();
    }
}
