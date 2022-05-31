package com.example.ciclotm.Views;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ciclotm.Models.Objects.Route;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.BikeStatsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class BikeStatsActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseUser user;
    private String owner;
    private int totalRides = 0;
    private double biggestRide = 0.0;
    private double fastestRide = 0.0;

    private TextView totalRoutes;
    private TextView biggestRideTV;
    private TextView fastestRideTV;
    private ArrayList<Route> allRouteList = new ArrayList<>();
    private BikeStatsViewModel mBikeStatsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_stats);

        user = FirebaseAuth.getInstance().getCurrentUser();
        owner = user.getUid();

        initActionBar();
        initLayout();
        mBikeStatsViewModel = ViewModelProviders.of(this).get(BikeStatsViewModel.class);
        mBikeStatsViewModel.init(owner);
        mBikeStatsViewModel.getRouteList().observe(this, new Observer<ArrayList<Route>>() {
            @Override
            public void onChanged(ArrayList<Route> routes) {
                totalRoutes.setText(String.valueOf(routes.size()));
                for (int i = 0; i < totalRides; i++) {
                    if (routes.get(i).getDistance() > biggestRide) {
                        biggestRide = routes.get(i).getDistance();
                        System.out.println(biggestRide);
                    }
                    if (routes.get(i).getAvgSpeed() > fastestRide) {
                        fastestRide = routes.get(i).getAvgSpeed();
                    }
                }
                biggestRideTV.setText(String.format("%.2f", biggestRide) + " km");
                fastestRideTV.setText(String.format("%.2f", fastestRide) + " km/h");
            }
        });

    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Statistici");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void initLayout() {
        totalRoutes = (TextView) findViewById(R.id.totalRoutesTextView);
        biggestRideTV = (TextView) findViewById(R.id.biggestRideTextView);
        fastestRideTV = (TextView) findViewById(R.id.fastestRideTextView);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}