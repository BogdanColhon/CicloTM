package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Location;
import com.example.ciclotm.Models.Objects.Route;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoutePostsRepository {
    private static RoutePostsRepository instance;
    private DatabaseReference reference;
    private DatabaseReference ref;

    public static RoutePostsRepository getInstance() {
        if (instance == null) {
            instance = new RoutePostsRepository();
        }
        return instance;
    }

    public void getRouteList(String userID, Action<ArrayList<Route>> callback) {
        ArrayList<Route> dataSet = new ArrayList<Route>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(userID);
        ref = reference.child("routePosts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Route route = postSnapshot.getValue(Route.class);
                    dataSet.add(route);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getMapPointsList(Route route, Action<ArrayList<Location>> callback) {
        ArrayList<Location> dataSet = new ArrayList<Location>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(route.getUserId()).child("routePosts").child(String.valueOf(route.getPublishDate()));
        ref = reference.child("coordinates");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Location point = postSnapshot.getValue(Location.class);
                    dataSet.add(point);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
