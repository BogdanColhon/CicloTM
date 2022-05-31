package com.example.ciclotm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Models.Legislation.ImbunatatireCirculatieItem;
import com.example.ciclotm.R;

import java.util.ArrayList;

public class imbunatatireCirculatieRecycleViewAdapter extends RecyclerView.Adapter<imbunatatireCirculatieRecycleViewAdapter.MyViewHolder> {

    Context context;
    private ArrayList<ImbunatatireCirculatieItem> items;

    public imbunatatireCirculatieRecycleViewAdapter(Context context, ArrayList<ImbunatatireCirculatieItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView =inflater.inflate(R.layout.siguranta_expandable_card, viewGroup, false);
        return new MyViewHolder(itemView);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements com.example.ciclotm.MyViewHolder {
        TextView title;
        TextView text;
        ImageView imageView;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        public MyViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.categoryTextView);
            text = (TextView) view.findViewById(R.id.rulesListTextView);
            imageView = (ImageView) view.findViewById(R.id.rulesSigurantaImageView);
            linearLayout = view.findViewById(R.id.linearLayoutRules);
            expandableLayout = view.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImbunatatireCirculatieItem item = items.get(getAdapterPosition());
                    item.setExpandable(!item.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String title_text = items.get(i).getTitle();
        holder.title.setText(title_text);

        String description =items.get(i).getText();
        holder.text.setText(description);

        int imageViewId = items.get(i).getImageId();
        holder.imageView.setImageResource(imageViewId);

        boolean isExpandable = items.get(i).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}