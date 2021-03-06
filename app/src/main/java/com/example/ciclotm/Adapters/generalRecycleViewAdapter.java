package com.example.ciclotm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class generalRecycleViewAdapter extends RecyclerView.Adapter<generalRecycleViewAdapter.MyViewHolder> {
    Context context;
    public static String generalPostsCount;
    private ArrayList<generalPost> postsList;
    private OnPostListener mOnPostListener;


    public generalRecycleViewAdapter(Context context, ArrayList<generalPost> postsList, OnPostListener onPostListener) {
        this.context = context;
        this.postsList = postsList;
        this.mOnPostListener = onPostListener;
    }

    @NonNull
    @Override
    public generalRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_general_card_layout, parent, false);
        return new MyViewHolder(itemView, mOnPostListener);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView content;
        TextView data;
        ImageView user_photo;
        OnPostListener onPostListener;

        public MyViewHolder(final View view, OnPostListener OnPostListener) {
            super(view);
            title = (TextView) view.findViewById(R.id.upperTextView);
            content = (TextView) view.findViewById(R.id.lowerTextView);
            data = (TextView) view.findViewById(R.id.middleGeneralTextView);
            user_photo = (ImageView) view.findViewById(R.id.user_photo);
            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onPostListener.onPostClick(getAdapterPosition());

        }
    }


    @Override
    public void onBindViewHolder(@NonNull generalRecycleViewAdapter.MyViewHolder holder, int position) {
        String title = postsList.get(position).getTitle();
        holder.title.setText(title);
        String content = postsList.get(position).getContent();
        holder.content.setText(content);
        Date date = postsList.get(position).getDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(date);
        holder.data.setText(output);
        String userImageUrl = postsList.get(position).getUserImageUrl();
        Glide.with(context).load(userImageUrl).into(holder.user_photo);

    }

    public void updateGeneralPostList(ArrayList<generalPost> x) {
        x.removeAll(postsList);
        postsList.clear();
        postsList.addAll(x);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        generalPostsCount = String.valueOf(postsList.size());

        return postsList.size();
    }

    public interface OnPostListener {
        void onPostClick(int position);
    }

}
