package com.example.ciclotm.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ciclotm.ExpandedGeneralPostActivity;
import com.example.ciclotm.GeneralPostActivity;
import com.example.ciclotm.R;
import com.example.ciclotm.generalPost;
import com.example.ciclotm.generalRecycleViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminGeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminGeneralFragment extends Fragment  implements adminGeneralRecycleViewAdapter.OnPostListener  {
    private ArrayList<generalPost> postsList = new ArrayList<>();
    TextView generalPostsNumberTextView;
    private RecyclerView recyclerView;
    adminGeneralRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    private DatabaseReference PostReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminGeneralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminGeneralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminGeneralFragment newInstance(String param1, String param2) {
        AdminGeneralFragment fragment = new AdminGeneralFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_general, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.generalRView);
        generalPostsNumberTextView = (TextView) view.findViewById(R.id.generalPostsNumberTextView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new adminGeneralRecycleViewAdapter(getContext(), postsList, this);
        recyclerView.setAdapter(adapter);
        fetchPostsInfo();
        System.out.println(postsList.size());
        ImageButton addPost = (ImageButton) view.findViewById(R.id.addPostImageButton);
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


    private void fetchPostsInfo() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("GeneralPosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                generalPost newPost = snapshot.getValue(generalPost.class);
                postsList.add(0, newPost);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fetchPostsNumber() {
        generalPostsNumberTextView.setText(String.valueOf(adminGeneralRecycleViewAdapter.generalPostsCount));
    }

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedGeneralPostActivity.class);
        intent.putExtra("clicked_post", postsList.get(position));
        startActivity(intent);
    }

    @Override
    public void OnDeleteClick(int position) {
        removeItem(position);
    }

    public void removeItem(int position){
        System.out.println("Removed position: "+position);
       PostReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("GeneralPosts").child(String.valueOf(postsList.get(position).getDate()));
        PostReference.removeValue();
        postsList.remove(position);
       adapter.notifyItemRemoved(position);
    }
}