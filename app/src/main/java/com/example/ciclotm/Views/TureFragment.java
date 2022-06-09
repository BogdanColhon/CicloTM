package com.example.ciclotm.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Adapters.adminTureRecyclerViewAdapter;
import com.example.ciclotm.Adapters.tureRecycleViewAdapter;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.TureFragmentViewModel;

import java.util.ArrayList;

public class TureFragment extends Fragment implements tureRecycleViewAdapter.OnPostListener, adminTureRecyclerViewAdapter.OnPostListener {

    private TextView turePostsNumberTextView;
    private ImageButton addPost;
    private RecyclerView recyclerView;
    private tureRecycleViewAdapter adapter;
    private adminTureRecyclerViewAdapter adminAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TureFragmentViewModel mTureFragmentViewModel;

    public TureFragment() {
        // Required empty public constructor
    }


    public static TureFragment newInstance() {
        TureFragment fragment = new TureFragment();
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
        View view = inflater.inflate(R.layout.fragment_ture, container, false);

        initLayout(view);
        mTureFragmentViewModel = ViewModelProviders.of(this).get(TureFragmentViewModel.class);
        mTureFragmentViewModel.init();
        mTureFragmentViewModel.getTurePosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<turePost>>() {
            @Override
            public void onChanged(ArrayList<turePost> turePosts) {
                if (MainActivity.role.equals("0"))
                    adapter.updateTurePostList(turePosts);
                else
                    adminAdapter.updateAdminTurePostList(turePosts);
            }
        });
        initRecyclerView();

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TurePostActivity.class));
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
        recyclerView = (RecyclerView) view.findViewById(R.id.tureRView);
        turePostsNumberTextView = (TextView) view.findViewById(R.id.turePostsNumberTextView);
        addPost = (ImageButton) view.findViewById(R.id.addPostImageButton);


    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new tureRecycleViewAdapter(getContext(), mTureFragmentViewModel.getTurePosts().getValue(), this);
        adminAdapter = new adminTureRecyclerViewAdapter(getContext(), mTureFragmentViewModel.getTurePosts().getValue(), this);

        if (MainActivity.role.equals("0"))
            recyclerView.setAdapter(adapter);
        else
            recyclerView.setAdapter(adminAdapter);

    }


    private void fetchPostsNumber() {
        if (MainActivity.role.equals("0"))
            turePostsNumberTextView.setText(String.valueOf(tureRecycleViewAdapter.turePostsCount));
        else
            turePostsNumberTextView.setText(String.valueOf(adminTureRecyclerViewAdapter.turePostsCount));
    }


    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedTurePostActivity.class);
        intent.putExtra("clicked_post", mTureFragmentViewModel.getTurePosts().getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void onPostTureClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedTurePostActivity.class);
        intent.putExtra("clicked_post", mTureFragmentViewModel.getTurePosts().getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void OnDeleteTureClick(int position) {
        removeTureItem(position);
    }

    public void removeTureItem(int position) {
        mTureFragmentViewModel.removeTurePost(mTureFragmentViewModel.getTurePosts().getValue().get(position));
        adminAdapter.notifyItemRemoved(position);
    }
}