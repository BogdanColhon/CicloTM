package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleViewAdapter extends RecyclerView.Adapter<recycleViewAdapter.MyViewHolder>{
    Context context;

    private ArrayList<Post> postsList;

    public recycleViewAdapter(Context context, ArrayList<Post> postsList){
        this.context=context;
        this.postsList=postsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        public MyViewHolder(final View view){
            super(view);
            title=(TextView) view.findViewById(R.id.upperTextView);
            content=(TextView) view.findViewById(R.id.lowerTextView);
        }
    }
    @NonNull
    @Override
    public recycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_general_card_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleViewAdapter.MyViewHolder holder, int position) {
        String title= postsList.get(position).getTitle();
        holder.title.setText(title);
        String content= postsList.get(position).getContent();
        holder.content.setText(content);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
