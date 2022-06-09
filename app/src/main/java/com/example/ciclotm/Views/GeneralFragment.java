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

import com.example.ciclotm.Adapters.generalRecycleViewAdapter;
import com.example.ciclotm.Adapters.adminGeneralRecycleViewAdapter;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.GeneralFragmentViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GeneralFragment extends Fragment implements generalRecycleViewAdapter.OnPostListener, adminGeneralRecycleViewAdapter.OnPostListener {

    private FirebaseUser user;
    public static String userID;
    private TextView generalPostsNumberTextView;
    private RecyclerView recyclerView;
    private ImageButton addPost;
    private generalRecycleViewAdapter adapter;
    private adminGeneralRecycleViewAdapter adminAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    private GeneralFragmentViewModel mGeneralFragmentViewModel;

    public GeneralFragment() {
        // Required empty public constructor
    }

    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
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
        View view = inflater.inflate(R.layout.fragment_general, container, false);

        initLayout(view);
        mGeneralFragmentViewModel = ViewModelProviders.of(this).get(GeneralFragmentViewModel.class);
        mGeneralFragmentViewModel.init();
        mGeneralFragmentViewModel.getGeneralPosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<generalPost>>() {
            @Override
            public void onChanged(ArrayList<generalPost> generalPosts) {
                if (MainActivity.role.equals("0"))
                    adapter.updateGeneralPostList(generalPosts);
                else
                    adminAdapter.updateAdminGeneralPostList(generalPosts);

            }
        });

        initRecyclerView();
        getCurrentUser();

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GeneralPostActivity.class));
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
        recyclerView = (RecyclerView) view.findViewById(R.id.generalRView);
        generalPostsNumberTextView = (TextView) view.findViewById(R.id.generalPostsNumberTextView);
        addPost = (ImageButton) view.findViewById(R.id.addPostImageButton);

    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new generalRecycleViewAdapter(getContext(), mGeneralFragmentViewModel.getGeneralPosts().getValue(), this);
        adminAdapter = new adminGeneralRecycleViewAdapter(getContext(), mGeneralFragmentViewModel.getGeneralPosts().getValue(), this);

        if (MainActivity.role.equals("0"))
            recyclerView.setAdapter(adapter);
        else
            recyclerView.setAdapter(adminAdapter);
    }


    private void getCurrentUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        userID = user.getUid();

    }


    private void fetchPostsNumber() {
        if (MainActivity.role.equals("0"))
            generalPostsNumberTextView.setText(String.valueOf(generalRecycleViewAdapter.generalPostsCount));
        else
            generalPostsNumberTextView.setText(String.valueOf(adminGeneralRecycleViewAdapter.generalPostsCount));
    }

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedGeneralPostActivity.class);
        intent.putExtra("clicked_post", mGeneralFragmentViewModel.getGeneralPosts().getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void OnDeleteClick(int position) {
        removeItem(position);
    }

    public void removeItem(int position) {
        mGeneralFragmentViewModel.removeGeneralPost(mGeneralFragmentViewModel.getGeneralPosts().getValue().get(position));
        adminAdapter.notifyItemRemoved(position);
    }
}