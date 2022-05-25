package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ciclotm.Models.Bike;
import com.example.ciclotm.Models.Location;
import com.example.ciclotm.Models.Route;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoutePostsActivity extends AppCompatActivity implements routePostsRecycleViewAdapter.OnPostListener {

    private FirebaseUser user;
    private ArrayList<Route> routeList = new ArrayList<>();
    private RecyclerView recyclerView;
    routePostsRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    String owner;
    public static ArrayList<com.example.ciclotm.Models.Location> expandedRoutePoints = new ArrayList<Location>();



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
        adapter = new routePostsRecycleViewAdapter(RoutePostsActivity.this, routeList, this);
        recyclerView.setAdapter(adapter);
        fetchRoutes();
    }

    private void fetchRoutes() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        owner = user.getUid();
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

    @Override
    public void onPostClick(int position) {
        fetchMapPoints(routeList.get(position));
        Intent intent = new Intent(RoutePostsActivity.this, ExpandedRecordedRouteActivity.class);
        intent.putExtra("clicked_post", routeList.get(position));
        startActivity(intent);
    }

    public void fetchMapPoints(Route route){

        reference2 = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("routePosts").child(String.valueOf(route.getPublishDate())).child("coordinates");
        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                com.example.ciclotm.Models.Location point = snapshot.getValue(com.example.ciclotm.Models.Location.class);
                if (point != null) {
                    expandedRoutePoints.add(point);
                    System.out.println(point.getLatitude());
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
}