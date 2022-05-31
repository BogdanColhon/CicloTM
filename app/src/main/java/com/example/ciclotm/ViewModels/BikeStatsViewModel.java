package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Route;
import com.example.ciclotm.Repositories.BikeStatsRepository;

import java.util.ArrayList;

public class BikeStatsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Route>> routeList = new MutableLiveData<>(new ArrayList<Route>());
    private BikeStatsRepository mBikeStatsRepo;
    private int totalRides = 0;
    private double biggestRide = 0.0;
    private double fastestRide = 0.0;

    public void init(String userID) {
        mBikeStatsRepo = BikeStatsRepository.getInstance();
        mBikeStatsRepo.getRouteList(userID, new Action<ArrayList<Route>>() {
            @Override
            public void doSomething(ArrayList<Route> x) {
                routeList.setValue(x);
               // getStats(x);
            }
        });
    }

    private void getStats(ArrayList<Route> x) {
        totalRides = x.size();
        System.out.println(totalRides);
        for (int i = 0; i < totalRides; i++) {
            if (x.get(i).getDistance() > biggestRide) {
                biggestRide = x.get(i).getDistance();
            }
            if (x.get(i).getAvgSpeed() > fastestRide) {
                fastestRide = x.get(i).getAvgSpeed();
            }
        }

    }

    public LiveData<ArrayList<Route>> getRouteList() {
        return routeList;
    }

    public int getTotalRides(){
        return totalRides;
    }
    public double getBiggestRide(){
        return biggestRide;
    }
    public  double getFastestRide(){
        return fastestRide;
    }
}
