package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ciclotm.Models.Objects.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ArrayList<Photo> items = new ArrayList<>();
    private RecyclerView recyclerView;
    galleryRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        userID = user.getUid();

        initActionBar();


        reference.child(userID).child("Gallery").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Photo item = dataSnapshot.getValue(Photo.class);
                items.add(0,item);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setRecycleView();
    }

        public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Galerie");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initData(){
       // items.add(new GalleryItem("Mersul pe bicicletă noaptea",R.drawable.bike1));
       // items.add(new GalleryItem("12:53 12.01.1990",R.drawable.bike2));
    }

    public void setRecycleView(){
        recyclerView = (RecyclerView) findViewById(R.id.galleryRView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new galleryRecycleViewAdapter(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }



}