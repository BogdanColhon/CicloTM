package com.example.ciclotm.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.ciclotm.ExpandedRecordedRouteActivity;
import com.example.ciclotm.Models.Objects.Location;
import com.example.ciclotm.Models.Objects.Route;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.RoutePostsViewModel;
import com.example.ciclotm.routePostsRecycleViewAdapter;
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
    private routePostsRecycleViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private String owner;
    private RoutePostsViewModel mRoutePostsViewModel;
    public static ArrayList<Location> expandedRoutePoints = new ArrayList<Location>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_posts);

        user = FirebaseAuth.getInstance().getCurrentUser();
        owner = user.getUid();

        initActionBar();

        mRoutePostsViewModel = ViewModelProviders.of(this).get(RoutePostsViewModel.class);
        mRoutePostsViewModel.init(owner);
        mRoutePostsViewModel.getRouteList().observe(this, new Observer<ArrayList<Route>>() {
            @Override
            public void onChanged(ArrayList<Route> routes) {
                adapter.updateRoutePostsList(routes);
            }
        });

        initRecyclerView();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ture");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.routePostsRView);
        layoutManager = new LinearLayoutManager(RoutePostsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new routePostsRecycleViewAdapter(RoutePostsActivity.this, mRoutePostsViewModel.getRouteList().getValue(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }

    @Override
    public void onPostClick(int position) {
        Route clicked_route = mRoutePostsViewModel.getRouteList().getValue().get(position);
        mRoutePostsViewModel.setMapPointsList(clicked_route);
        mRoutePostsViewModel.getMapPointsList().observe(this, new Observer<ArrayList<Location>>() {
            @Override
            public void onChanged(ArrayList<Location> locations) {
                expandedRoutePoints.clear();
                expandedRoutePoints.addAll(locations);
                start(position);
            }
        });

    }

    private void start(int position) {
        Intent intent = new Intent(RoutePostsActivity.this, ExpandedRecordedRouteActivity.class);
        intent.putExtra("clicked_post", mRoutePostsViewModel.getRouteList().getValue().get(position));
        startActivity(intent);
    }

}