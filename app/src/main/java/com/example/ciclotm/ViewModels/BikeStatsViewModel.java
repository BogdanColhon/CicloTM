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

    public void init(String userID) {
        mBikeStatsRepo = BikeStatsRepository.getInstance();
        mBikeStatsRepo.getRouteList(userID, new Action<ArrayList<Route>>() {
            @Override
            public void doSomething(ArrayList<Route> x) {
                routeList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<Route>> getRouteList() {
        return routeList;
    }

}
