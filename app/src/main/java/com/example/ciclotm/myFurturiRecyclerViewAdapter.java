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
import com.example.ciclotm.Models.Objects.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class myFurturiRecyclerViewAdapter extends RecyclerView.Adapter<myFurturiRecyclerViewAdapter.MyViewHolder> {
    Context context;
    private ArrayList<Report> postsList;
    private myFurturiRecyclerViewAdapter.OnPostListener mOnPostListener;

    public myFurturiRecyclerViewAdapter(Context context, ArrayList<Report> postsList, myFurturiRecyclerViewAdapter.OnPostListener mOnPostListener) {
        this.context = context;
        this.postsList = postsList;
        this.mOnPostListener = mOnPostListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_community_furturi_card_layout, viewGroup, false);
        return new MyViewHolder(itemView, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Date date = postsList.get(position).getStolenDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm | dd/MM/yyyy", Locale.getDefault());
        String output = df.format(date);
        holder.date.setText(output);
        String location = postsList.get(position).getAddress();
        holder.location.setText(location);
        String description = postsList.get(position).getBike_model();
        holder.description.setText(description);
        String link = postsList.get(position).getBikeImageUrl();
        if (!link.equals(""))
            Glide.with(context).load(link).into(holder.bike_photo);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView location;
        TextView description;
        ImageView bike_photo;
        ImageView deleteButton;
        OnPostListener onPostListener;

        public MyViewHolder(final View view, myFurturiRecyclerViewAdapter.OnPostListener OnPostListener) {
            super(view);
            date = (TextView) view.findViewById(R.id.upperTextView);
            location = (TextView) view.findViewById(R.id.middleTextView);
            description = (TextView) view.findViewById(R.id.lowerTextView);
            bike_photo = (ImageView) view.findViewById(R.id.bike_photo);
            deleteButton = (ImageView) view.findViewById(R.id.deleteImageView);
            this.onPostListener = OnPostListener;

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnPostListener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            OnPostListener.OnDeleteFurturiClick(position);
                        }
                    }
                }
            });

            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostFurturiClick(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public interface OnPostListener {
        void onPostFurturiClick(int position);

        void OnDeleteFurturiClick(int position);
    }
}
