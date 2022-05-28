package com.example.ciclotm.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.MyViewHolder;
import com.example.ciclotm.R;
import com.example.ciclotm.ReguliCirculatie.CategorieReguli;

import java.util.ArrayList;

public class adminReguliCirculatieRecyclerViewAdapter extends RecyclerView.Adapter<adminReguliCirculatieRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<CategorieReguli> categorieReguliList;
    private adminReguliCirculatieRecyclerViewAdapter.OnPostListener mOnPostListener;

    public adminReguliCirculatieRecyclerViewAdapter(Context context, ArrayList<CategorieReguli> categorieReguliList, adminReguliCirculatieRecyclerViewAdapter.OnPostListener onPostListener) {
        this.context = context;
        this.categorieReguliList = categorieReguliList;
        this.mOnPostListener = onPostListener;
    }

    @NonNull
    @Override
    public adminReguliCirculatieRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_reguli_card, viewGroup, false);
        return new MyViewHolder(view, mOnPostListener);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EditText category, rules;
        ImageButton save, delete;
        adminReguliCirculatieRecyclerViewAdapter.OnPostListener onPostListener;

        public MyViewHolder(@NonNull View itemView, adminReguliCirculatieRecyclerViewAdapter.OnPostListener OnPostListener) {
            super(itemView);
            category = itemView.findViewById(R.id.categoryEditText);
            rules = itemView.findViewById(R.id.rulesListEditText);
            save = itemView.findViewById(R.id.saveRulesImageButton);
            delete = itemView.findViewById(R.id.deleteRulesImageButton);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String modified_category = category.getText().toString();
                    String modified_rules = rules.getText().toString();

                    if (OnPostListener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            OnPostListener.onPostSave(position, modified_category, modified_rules);
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
    public void onBindViewHolder(@NonNull adminReguliCirculatieRecyclerViewAdapter.MyViewHolder holder, int i) {
        String categorie = categorieReguliList.get(i).getCategory();
        holder.category.setText(categorie);
        String reguli = categorieReguliList.get(i).getRules();
        holder.rules.setText(reguli);
    }

    @Override
    public int getItemCount() {
        return categorieReguliList.size();
    }

    public interface OnPostListener {
        void onPostSave(int position, String category, String rules);

        void onPostDelete(int position);
    }
}
