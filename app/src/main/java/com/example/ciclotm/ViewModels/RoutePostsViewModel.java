package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Location;
import com.example.ciclotm.Models.Objects.Route;
import com.example.ciclotm.Repositories.RoutePostsRepository;

import java.util.ArrayList;

public class RoutePostsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Route>> routeList = new MutableLiveData<>(new ArrayList<Route>());
    private MutableLiveData<ArrayList<Location>> pointList = new MutableLiveData<>(new ArrayList<Location>());
    private RoutePostsRepository mRoutePostsRepo;

    public void init(String userID) {
        mRoutePostsRepo = RoutePostsRepository.getInstance();
        mRoutePostsRepo.getRouteList(userID, new Action<ArrayList<Route>>() {
            @Override
            public void doSomething(ArrayList<Route> x) {
                routeList.setValue(x);
            }
        });
    }

    public void setMapPointsList(Route route){
        mRoutePostsRepo.getMapPointsList(route, new Action<ArrayList<Location>>() {
        @Override
        public void doSomething(ArrayList<Location> x) {
            pointList.setValue(x);
        }
    });
    }


    public LiveData<ArrayList<Route>> getRouteList() {
        return routeList;
    }

    public LiveData<ArrayList<Location>> getMapPointsList() {
        return pointList;
    }
}
