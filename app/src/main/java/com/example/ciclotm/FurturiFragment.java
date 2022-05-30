package com.example.ciclotm;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ciclotm.Adapters.adminFurturiRecyclerViewAdapter;
import com.example.ciclotm.Adapters.furturiRecycleViewAdapter;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.ViewModels.FurturiFragmentViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FurturiFragment extends Fragment implements furturiRecycleViewAdapter.OnPostListener, adminFurturiRecyclerViewAdapter.OnPostListener {
    private ArrayList<Report> postsList = new ArrayList<>();
    private TextView furturiPostsNumberTextView;
    private TextView statsClickableTextView;
    private RecyclerView recyclerView;
    private furturiRecycleViewAdapter adapter;
    private adminFurturiRecyclerViewAdapter adminAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FurturiFragmentViewModel mFurturiFragmentViewModel;

    public FurturiFragment() {
        // Required empty public constructor
    }


    public static FurturiFragment newInstance() {
        FurturiFragment fragment = new FurturiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_furturi, container, false);

        initLayout(view);
        mFurturiFragmentViewModel = ViewModelProviders.of(this).get(FurturiFragmentViewModel.class);
        mFurturiFragmentViewModel.init();
        mFurturiFragmentViewModel.getFurturiPosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Report>>() {
            @Override
            public void onChanged(ArrayList<Report> reports) {
                if (MainActivity.role.equals("0"))
                    adapter.updateFurturiPostList(reports);
                else
                    adminAdapter.updateAdminFurturiPostList(reports);
            }
        });

        initRecyclerView();


        statsClickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatsActivity.class);
                startActivity(intent);
            }
        });


        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                fetchPostsNumber();
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(refresh, 100);
        return view;
    }

    private void initLayout(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.furturiRView);
        furturiPostsNumberTextView = (TextView) view.findViewById(R.id.furturiPostsNumberTextView);
        statsClickableTextView = (TextView) view.findViewById(R.id.furturiStatsClickableTextView);
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new furturiRecycleViewAdapter(getContext(), postsList, this);
        adminAdapter = new adminFurturiRecyclerViewAdapter(getContext(), postsList, this);

        if (MainActivity.role.equals("0"))
            recyclerView.setAdapter(adapter);
        else
            recyclerView.setAdapter(adminAdapter);
    }


    private void fetchPostsNumber() {
        if (MainActivity.role.equals("0"))
            furturiPostsNumberTextView.setText(String.valueOf(furturiRecycleViewAdapter.furturiPostsCount));
        else
            furturiPostsNumberTextView.setText(String.valueOf(adminFurturiRecyclerViewAdapter.furturiPostsCount));
    }

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedFurturiPostActivity.class);
        intent.putExtra("clicked_report", postsList.get(position));
        startActivity(intent);
    }


    @Override
    public void onPostFurturiClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedFurturiPostActivity.class);
        intent.putExtra("clicked_report", postsList.get(position));
        startActivity(intent);
    }

    @Override
    public void OnDeleteFurturiClick(int position) {
        removeFurturiItem(position);

    }

    public void removeFurturiItem(int position) {
        System.out.println("Removed position: " + position);
        DatabaseReference PostReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("furturiPosts").child(String.valueOf(postsList.get(position).getPublishDate()));
        PostReference.removeValue();
        DatabaseReference LocalPostsReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("Users").child(postsList.get(position).getUser_id()).child("Furturi").child(String.valueOf(postsList.get(position).getPublishDate()));
        LocalPostsReference.removeValue();
        postsList.remove(position);
        adminAdapter.notifyItemRemoved(position);
    }
}