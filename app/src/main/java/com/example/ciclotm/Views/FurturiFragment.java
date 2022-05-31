package com.example.ciclotm.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Adapters.adminFurturiRecyclerViewAdapter;
import com.example.ciclotm.Adapters.furturiRecycleViewAdapter;
import com.example.ciclotm.ExpandedFurturiPostActivity;
import com.example.ciclotm.MainActivity;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.R;
import com.example.ciclotm.StatsActivity;
import com.example.ciclotm.ViewModels.FurturiFragmentViewModel;

import java.util.ArrayList;

public class FurturiFragment extends Fragment implements furturiRecycleViewAdapter.OnPostListener, adminFurturiRecyclerViewAdapter.OnPostListener {
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

        adapter = new furturiRecycleViewAdapter(getContext(), mFurturiFragmentViewModel.getFurturiPosts().getValue(), this);
        adminAdapter = new adminFurturiRecyclerViewAdapter(getContext(), mFurturiFragmentViewModel.getFurturiPosts().getValue(), this);

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
        intent.putExtra("clicked_report", mFurturiFragmentViewModel.getFurturiPosts().getValue().get(position));
        startActivity(intent);
    }


    @Override
    public void onPostFurturiClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedFurturiPostActivity.class);
        intent.putExtra("clicked_report", mFurturiFragmentViewModel.getFurturiPosts().getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void OnDeleteFurturiClick(int position) {
        removeFurturiItem(position);

    }

    public void removeFurturiItem(int position) {
        mFurturiFragmentViewModel.removeFurtPost(mFurturiFragmentViewModel.getFurturiPosts().getValue().get(position));
        adminAdapter.notifyItemRemoved(position);
    }
}