package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ciclotm.Models.Bike;
import com.example.ciclotm.Models.Route;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoutePostsActivity extends AppCompatActivity {

    private FirebaseUser user;
    private ArrayList<Route> routeList = new ArrayList<>();
    private RecyclerView recyclerView;
    routePostsRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_posts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ture");
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.routePostsRView);
        layoutManager = new LinearLayoutManager(RoutePostsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new routePostsRecycleViewAdapter(RoutePostsActivity.this, routeList);
        recyclerView.setAdapter(adapter);
        fetchBikeCollection();
    }

    private void fetchBikeCollection()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        String owner = user.getUid();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("routePosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Route route = snapshot.getValue(Route.class);
                if (route != null) {
                    routeList.add(0, route);
                    adapter.notifyDataSetChanged();
                }

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }

}