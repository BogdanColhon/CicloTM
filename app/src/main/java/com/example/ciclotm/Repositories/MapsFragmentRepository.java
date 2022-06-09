package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Markers.LiveEventsMarker;
import com.example.ciclotm.Models.Markers.PointOfInterestMarker;
import com.example.ciclotm.Models.Objects.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsFragmentRepository {
    public static MapsFragmentRepository instance;
    private DatabaseReference reference;

    public static MapsFragmentRepository getInstance() {
        if (instance == null) {
            instance = new MapsFragmentRepository();
        }
        return instance;
    }

    public void getReports(Action<ArrayList<Report>> callback) {
        ArrayList<Report> dataSet = new ArrayList<Report>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("furturiPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    dataSet.add(report);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getPointOfInterestMarkers(Action<ArrayList<PointOfInterestMarker>> callback) {
        ArrayList<PointOfInterestMarker> dataSet = new ArrayList<PointOfInterestMarker>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("PointsOfInterestMarkers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    PointOfInterestMarker newMarker = postSnapshot.getValue(PointOfInterestMarker.class);
                    dataSet.add(newMarker);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getLiveEventMarkers(Action<ArrayList<LiveEventsMarker>> callback) {
        ArrayList<LiveEventsMarker> dataSet = new ArrayList<LiveEventsMarker>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("LiveEventsMarkers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    LiveEventsMarker newMarker = postSnapshot.getValue(LiveEventsMarker.class);
                    dataSet.add(newMarker);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

