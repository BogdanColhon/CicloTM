package com.example.ciclotm.ReguliCirculatie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.R;

import java.util.ArrayList;
import java.util.List;

public class CategorieReguliAdapter extends RecyclerView.Adapter<CategorieReguliAdapter.CategorieReguliVH> {

    Context context;
    ArrayList<CategorieReguli> categorieReguliList;

    public CategorieReguliAdapter(Context context,ArrayList<CategorieReguli> categorieReguliList) {
        this.context=context;
        this.categorieReguliList = categorieReguliList;

    }

    @NonNull
    @Override
    public CategorieReguliAdapter.CategorieReguliVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expandable_row, viewGroup, false);
        return new CategorieReguliVH(view);
    }
    public class CategorieReguliVH extends RecyclerView.ViewHolder {

        TextView category, rules;


        public CategorieReguliVH(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.categoryTextView);
            rules = itemView.findViewById(R.id.rulesListTextView);




        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategorieReguliVH holder, int i) {
        String categorie = categorieReguliList.get(i).getCategory();
        holder.category.setText(categorie);
        String reguli = categorieReguliList.get(i).getRules();
        holder.rules.setText(reguli);


    }

    @Override
    public int getItemCount() {
        return categorieReguliList.size();
    }

}
