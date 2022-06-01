package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Route;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BikeStatsRepository {
    public static BikeStatsRepository instance;
    private DatabaseReference reference;
    private DatabaseReference ref;

    public static BikeStatsRepository getInstance() {
        if (instance == null) {
            instance = new BikeStatsRepository();
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
}

