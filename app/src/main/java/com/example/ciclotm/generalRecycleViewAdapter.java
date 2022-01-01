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

public class generalRecycleViewAdapter extends RecyclerView.Adapter<generalRecycleViewAdapter.MyViewHolder>{
    Context context;

    private ArrayList<generalPost> postsList;

    public generalRecycleViewAdapter(Context context, ArrayList<generalPost> postsList){
        this.context=context;
        this.postsList=postsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        ImageView user_photo;
        public MyViewHolder(final View view){
            super(view);
            title=(TextView) view.findViewById(R.id.upperTextView);
            content=(TextView) view.findViewById(R.id.lowerTextView);
            user_photo=(ImageView) view.findViewById(R.id.user_photo);
        }
    }
    @NonNull
    @Override
    public generalRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_general_card_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull generalRecycleViewAdapter.MyViewHolder holder, int position) {
        String title= postsList.get(position).getTitle();
        holder.title.setText(title);
        String content= postsList.get(position).getContent();
        holder.content.setText(content);
        Drawable user_photo=postsList.get(position).getUser_photo();
        holder.user_photo.setImageDrawable(user_photo);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
