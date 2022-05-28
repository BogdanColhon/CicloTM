package com.example.ciclotm.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ciclotm.R;
import com.example.ciclotm.SemnCirculatie;

import java.util.ArrayList;

public class adminSemneCirculatieRecyclerViewAdapter extends RecyclerView.Adapter<adminSemneCirculatieRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<SemnCirculatie> semnCirculatieList;
    private OnPostListener mOnPostListener;

    public adminSemneCirculatieRecyclerViewAdapter(Context context, ArrayList<SemnCirculatie> semnCirculatieList, OnPostListener mOnPostListener) {
        this.context = context;
        this.semnCirculatieList = semnCirculatieList;
        this.mOnPostListener = mOnPostListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_rules_road_signs_card_layout, viewGroup, false);
        return new MyViewHolder(view, mOnPostListener);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EditText name;
        EditText text;
        ImageView signImage;
        ImageButton save, delete;
        OnPostListener onPostListener;

        public MyViewHolder(@NonNull View itemView, OnPostListener OnPostListener) {
            super(itemView);
            name = itemView.findViewById(R.id.signNameEditText);
            text = itemView.findViewById(R.id.signDescriptionEditText);
            signImage = itemView.findViewById(R.id.signImageView);
            save = itemView.findViewById(R.id.saveRulesImageButton);
            delete = itemView.findViewById(R.id.deleteRulesImageButton);

            signImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "apasati pe poza", Toast.LENGTH_SHORT).show();
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String modified_name = name.getText().toString();
                    String modified_text = text.getText().toString();

                    if (OnPostListener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            OnPostListener.onPostSave(position, modified_name, modified_text);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnPostListener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            OnPostListener.onPostDelete(position);
                        }
                    }
                }
            });
            this.onPostListener = OnPostListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String signName = semnCirculatieList.get(i).getName();
        holder.name.setText(signName);

        String signDescription = semnCirculatieList.get(i).getText();
        holder.text.setText(signDescription);

        int signImageId = semnCirculatieList.get(i).getImage();
        Glide.with(context).load(signImageId).into(holder.signImage);
    }

    @Override
    public int getItemCount() {
        return semnCirculatieList.size();
    }

    public interface OnPostListener {
        void onPostSave(int position, String name, String text);

        void onPostDelete(int position);
    }
}
