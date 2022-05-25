package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ciclotm.Models.Route;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BikeStatsActivity extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser user;
    String owner;
    double biggestRide = 0.0;
    double fastestRide = 0.0;

    private TextView totalRoutes;
    private TextView biggestRideTV;
    private TextView fastestRideTV;
    private ArrayList<Route> allRouteList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_stats);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Statistici");
        actionBar.setDisplayHomeAsUpEnabled(true);

        totalRoutes = (TextView) findViewById(R.id.totalRoutesTextView);
        biggestRideTV = (TextView) findViewById(R.id.biggestRideTextView);
        fastestRideTV = (TextView) findViewById(R.id.fastestRideTextView);

        fetchAllRoutes();

    }

    private void fetchAllRoutes() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        owner = user.getUid();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("routePosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Route route = snapshot.getValue(Route.class);
                if (route != null) {
                    allRouteList.add(0, route);
                    totalRoutes.setText(String.valueOf(allRouteList.size()));
                    if(route.getDistance() > biggestRide)
                    {
                        biggestRide = route.getDistance();
                        biggestRideTV.setText(String.format("%.2f", biggestRide) + " km");
                    }
                    if(route.getAvgSpeed() > fastestRide)
                    {
                        fastestRide = route.getAvgSpeed();
                        fastestRideTV.setText(String.format("%.2f", fastestRide) + " km/h");
                    }
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