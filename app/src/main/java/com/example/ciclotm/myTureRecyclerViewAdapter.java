package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class myTureRecyclerViewAdapter extends RecyclerView.Adapter<myTureRecyclerViewAdapter.MyViewHolder> {
    Context context;
    private ArrayList<turePost> postsList;
    private StorageReference storageReference;

    private myTureRecyclerViewAdapter.OnPostListener mOnPostListener;

    public myTureRecyclerViewAdapter(Context context, ArrayList<turePost> postsList, myTureRecyclerViewAdapter.OnPostListener mOnPostListener) {
        this.context = context;
        this.postsList = postsList;
        this.mOnPostListener = mOnPostListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_community_ture_card_layout, viewGroup, false);
        return  new MyViewHolder(itemView, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Date dateComment = postsList.get(position).getActivityDate();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String output = df.format(dateComment);
        holder.activityDate.setText(output);

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


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView distance;
        TextView start_time;
        TextView start_point;
        TextView no_participants;
        TextView activityDate;
        ImageView user_photo;
        ImageView deleteButton;
        OnPostListener onPostListener;

        public MyViewHolder(final View view, myTureRecyclerViewAdapter.OnPostListener OnPostListener) {
            super(view);
            distance = (TextView) view.findViewById(R.id.upperTextView);
            start_time = (TextView) view.findViewById(R.id.firstMiddleTextView);
            start_point = (TextView) view.findViewById(R.id.secondMiddleTextView);
            no_participants = (TextView) view.findViewById(R.id.lowerTextView);
            activityDate = (TextView) view.findViewById(R.id.tureCardDateTextView);
            user_photo = (ImageView) view.findViewById(R.id.user_photo);
            deleteButton = (ImageView) view.findViewById(R.id.deleteImageView);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnPostListener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            OnPostListener.OnDeleteTureClick(position);
                        }
                    }
                }
            });

            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostTureClick(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public interface OnPostListener {
        void onPostTureClick(int position);

        void OnDeleteTureClick(int position);
    }
}
