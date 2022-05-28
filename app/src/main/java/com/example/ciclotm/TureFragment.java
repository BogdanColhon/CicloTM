package com.example.ciclotm;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ciclotm.Admin.adminTureRecyclerViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TureFragment extends Fragment  implements tureRecycleViewAdapter.OnPostListener, adminTureRecyclerViewAdapter.OnPostListener {

    private ArrayList<turePost> postsList = new ArrayList<>();
    TextView turePostsNumberTextView;
    private RecyclerView recyclerView;
    tureRecycleViewAdapter adapter;
    adminTureRecyclerViewAdapter adminAdapter;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TureFragment newInstance(String param1, String param2) {
        TureFragment fragment = new TureFragment();
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
        View view = inflater.inflate(R.layout.fragment_ture, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tureRView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new tureRecycleViewAdapter(getContext(), postsList,this);
        adminAdapter = new adminTureRecyclerViewAdapter(getContext(), postsList,this);

        if (MainActivity.role.equals("0"))
            recyclerView.setAdapter(adapter);
        else
            recyclerView.setAdapter(adminAdapter);

        turePostsNumberTextView = (TextView) view.findViewById(R.id.turePostsNumberTextView);
        fetchPostsInfo();
        ImageButton addPost = (ImageButton) view.findViewById(R.id.addPostImageButton);
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

    private void fetchPostsInfo() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("TurePosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                turePost newPost = snapshot.getValue(turePost.class);
                if (newPost != null) {
                    postsList.add(0, newPost);
                    if (MainActivity.role.equals("0"))
                        adapter.notifyDataSetChanged();
                    else
                        adminAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    turePost newPost = snapshot.getValue(turePost.class);
                    String data = snapshot.getKey();
                    for(turePost p:postsList){
                        String date = String.valueOf(newPost.getDate());
                        if(data.equals(date))
                        {
                            postsList.set(postsList.indexOf(p),newPost);
                        }
                    }
                    if (MainActivity.role.equals("0"))
                        adapter.notifyDataSetChanged();
                    else
                        adminAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        if (MainActivity.role.equals("0"))
        turePostsNumberTextView.setText(String.valueOf(tureRecycleViewAdapter.turePostsCount));
        else
            turePostsNumberTextView.setText(String.valueOf(adminTureRecyclerViewAdapter.turePostsCount));
    }


    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedTurePostActivity.class);
        intent.putExtra("clicked_post", postsList.get(position));
        startActivity(intent);
    }

    @Override
    public void onPostTureClick(int position) {
        Intent intent = new Intent(getContext(), ExpandedTurePostActivity.class);
        intent.putExtra("clicked_post", postsList.get(position));
        startActivity(intent);
    }

    @Override
    public void OnDeleteTureClick(int position) {
        removeTureItem(position);
    }

    public void removeTureItem(int position) {
        System.out.println("Removed position: " + position);
        DatabaseReference PostReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("TurePosts").child(String.valueOf(postsList.get(position).getDate()));
        PostReference.removeValue();
        postsList.remove(position);
        adminAdapter.notifyItemRemoved(position);
    }
}