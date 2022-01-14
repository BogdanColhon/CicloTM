package com.example.ciclotm.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.R;
import com.example.ciclotm.generalPost;
import com.example.ciclotm.generalRecycleViewAdapter;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class adminGeneralRecycleViewAdapter  extends RecyclerView.Adapter<adminGeneralRecycleViewAdapter.MyViewHolder> {
    Context context;
    public static String generalPostsCount;
    private StorageReference storageReference;
    private int ItemCount = 0;
    private ArrayList<generalPost> postsList;
    private adminGeneralRecycleViewAdapter.OnPostListener mOnPostListener;


    public adminGeneralRecycleViewAdapter(Context context, ArrayList<generalPost> postsList, adminGeneralRecycleViewAdapter.OnPostListener onPostListener) {
        this.context = context;
        this.postsList = postsList;
        this.mOnPostListener = onPostListener;
    }

    @NonNull
    @Override
    public adminGeneralRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_admin_general_card_layout, parent, false);
        return new adminGeneralRecycleViewAdapter.MyViewHolder(itemView, mOnPostListener);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView content;
        TextView data;
        ImageView user_photo;
        ImageView deleteButton;
        adminGeneralRecycleViewAdapter.OnPostListener onPostListener;

        public MyViewHolder(final View view, adminGeneralRecycleViewAdapter.OnPostListener OnPostListener) {
            super(view);
            title = (TextView) view.findViewById(R.id.upperTextView);
            content = (TextView) view.findViewById(R.id.lowerTextView);
            data = (TextView) view.findViewById(R.id.middleGeneralTextView);
            user_photo = (ImageView) view.findViewById(R.id.user_photo);
            deleteButton = (ImageView) view.findViewById(R.id.deleteImageView);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(OnPostListener !=null){
                        int position = getAbsoluteAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            OnPostListener.OnDeleteClick(position);
                        }
                    }
                }
            });

            this.onPostListener = OnPostListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onPostListener.onPostClick(getAdapterPosition());

        }
    }


    @Override
    public void onBindViewHolder(@NonNull adminGeneralRecycleViewAdapter.MyViewHolder holder, int position) {
        String title = postsList.get(position).getTitle();
        holder.title.setText(title);
        String content = postsList.get(position).getContent();
        holder.content.setText(content);
        Date date = postsList.get(position).getDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(date);
        holder.data.setText(output);
        String userImageUrl = postsList.get(position).getUserImageUrl();
        Picasso.get().load(userImageUrl).fit().centerInside().into(holder.user_photo);

    }

    @Override
    public int getItemCount() {
        generalPostsCount = String.valueOf(postsList.size());
        System.out.println(generalPostsCount);

        return postsList.size();
    }

    public interface OnPostListener {
        void onPostClick(int position);
        void OnDeleteClick(int position);
    }
}
