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
import com.example.ciclotm.Models.Comment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class generalCommentsRecyclerViewAdapter extends RecyclerView.Adapter<generalCommentsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<Comment> commentsList;
    private OnPostListener mOnPostListener;
    private DatabaseReference reference;
    private String userImageUrl = "";


    public generalCommentsRecyclerViewAdapter(Context context, ArrayList<Comment> commentsList, OnPostListener mOnPostListener) {
        this.context = context;
        this.commentsList = commentsList;
        this.mOnPostListener = mOnPostListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_card_layout, viewGroup, false);
        return new generalCommentsRecyclerViewAdapter.MyViewHolder(view,mOnPostListener);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView comment;
        TextView date;
        CircleImageView commentUserPhoto;
        ImageView delete;
        OnPostListener onPostListener;

        public MyViewHolder(final View view, OnPostListener OnPostListener) {
            super(view);

            comment = (TextView) view.findViewById(R.id.commentContentTextView);
            date = (TextView) view.findViewById(R.id.commentDateTextView);
            commentUserPhoto = (CircleImageView) view.findViewById(R.id.commentUserProfileImageView);
            delete = (ImageView) view.findViewById(R.id.deleteImageView);

            delete.setOnClickListener(new View.OnClickListener() {
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

        }
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if(GeneralFragment.userID.equals(commentsList.get(i).getUid()))
        {
            holder.delete.setVisibility(View.VISIBLE);
        }
        String userId = commentsList.get(i).getUid();
        setUserImage(userId, holder);
        Date dateComment = commentsList.get(i).getDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(dateComment);
        holder.date.setText(output);
        String commentContent = commentsList.get(i).getContent();
        holder.comment.setText(commentContent);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    private void setUserImage(String userID, MyViewHolder holder) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    userImageUrl = userProfile.getProfileImageUrl();
                    if (!userImageUrl.equals(""))
                        Glide.with(context).load(userImageUrl).into(holder.commentUserPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    public interface OnPostListener {
        void OnDeleteClick(int position);
    }
}
