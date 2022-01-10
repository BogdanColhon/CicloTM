package com.example.ciclotm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ciclotm.Models.Report;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FurturiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FurturiFragment extends Fragment {
    private ArrayList<Report> postsList = new ArrayList<>();
    TextView furturiPostsNumberTextView;
    private RecyclerView recyclerView;
    furturiRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FurturiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FurturiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FurturiFragment newInstance(String param1, String param2) {
        FurturiFragment fragment = new FurturiFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_furturi, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.furturiRView);
        furturiPostsNumberTextView = (TextView) view.findViewById(R.id.furturiPostsNumberTextView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new furturiRecycleViewAdapter(getContext(), postsList);
        recyclerView.setAdapter(adapter);
        fetchPostsInfo();
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
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("furturiPosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report newPost = snapshot.getValue(Report.class);
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
        furturiPostsNumberTextView.setText(String.valueOf(furturiRecycleViewAdapter.furturiPostsCount));
    }


}