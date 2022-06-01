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

import java.util.ArrayList;

public class BikeStatsActivity extends AppCompatActivity {

    private FirebaseUser user;
    private String owner;
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
                getStats(routes, routes.size());
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

    private void getStats(ArrayList<Route> x, int totalRides) {
        for (int i = 0; i < totalRides; i++) {
            if (x.get(i).getDistance() > biggestRide) {
                biggestRide = x.get(i).getDistance();
                biggestRideTV.setText(String.format("%.2f", biggestRide) + " km");
                System.out.println(biggestRide);
            }
            if (x.get(i).getAvgSpeed() > fastestRide) {
                fastestRide = x.get(i).getAvgSpeed();
                fastestRideTV.setText(String.format("%.2f", fastestRide) + " km/h");
            }
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}