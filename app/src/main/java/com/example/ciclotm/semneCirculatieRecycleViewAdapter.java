package com.example.ciclotm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class semneCirculatieRecycleViewAdapter extends RecyclerView.Adapter<semneCirculatieRecycleViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<SemnCirculatie> items;

    public semneCirculatieRecycleViewAdapter(Context context, ArrayList<SemnCirculatie> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView =inflater.inflate(R.layout.rules_road_signs_card_layout, viewGroup, false);
        return new semneCirculatieRecycleViewAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements com.example.ciclotm.MyViewHolder {
        TextView name;
        TextView text;
        ImageView signImage;

        public MyViewHolder(final View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.signNameTextView);
            text = (TextView) view.findViewById(R.id.signDescriptionTextView);
            signImage = (ImageView) view.findViewById(R.id.signImageView);

        }


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String signName = items.get(i).getName();
        holder.name.setText(signName);

        String signDescription = items.get(i).getText();
        holder.text.setText(signDescription);

        int signImageId = items.get(i).getImage();
        holder.signImage.setImageResource(signImageId);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
