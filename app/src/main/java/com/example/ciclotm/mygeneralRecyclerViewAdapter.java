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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class mygeneralRecyclerViewAdapter extends RecyclerView.Adapter<mygeneralRecyclerViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<generalPost> postsList;
    private mygeneralRecyclerViewAdapter.OnPostListener mOnPostListener;

    public mygeneralRecyclerViewAdapter(Context context, ArrayList<generalPost> postsList, mygeneralRecyclerViewAdapter.OnPostListener mOnPostListener) {
        this.context = context;
        this.postsList = postsList;
        this.mOnPostListener = mOnPostListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.community_admin_general_card_layout, viewGroup, false);
        return new MyViewHolder(itemView, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView content;
        TextView data;
        ImageView user_photo;
        ImageView deleteButton;
        mygeneralRecyclerViewAdapter.OnPostListener onPostListener;

        public MyViewHolder(final View view, mygeneralRecyclerViewAdapter.OnPostListener OnPostListener) {
            super(view);
            title = (TextView) view.findViewById(R.id.upperTextView);
            content = (TextView) view.findViewById(R.id.lowerTextView);
            data = (TextView) view.findViewById(R.id.middleGeneralTextView);
            user_photo = (ImageView) view.findViewById(R.id.user_photo);
            deleteButton = (ImageView) view.findViewById(R.id.deleteImageView);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnPostListener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            OnPostListener.OnDeleteClick(position);
                        }
                    }
                }
            });

            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }


    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public interface OnPostListener {
        void onPostClick(int position);

        void OnDeleteClick(int position);
    }

}
