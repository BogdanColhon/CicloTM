package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Markers.LiveEventsMarker;
import com.example.ciclotm.Models.Markers.PointOfInterestMarker;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Repositories.MapsFragmentRepository;

import java.util.ArrayList;

public class MapsFragmentViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Report>> reportsList = new MutableLiveData<>(new ArrayList<Report>());
    public MutableLiveData<ArrayList<PointOfInterestMarker>> pointsOfInterestList = new MutableLiveData<>(new ArrayList<PointOfInterestMarker>());
    public MutableLiveData<ArrayList<LiveEventsMarker>> liveEventMarkerList = new MutableLiveData<>(new ArrayList<LiveEventsMarker>());
    private MapsFragmentRepository mMapsFragmentRepo;

    public void init() {
        mMapsFragmentRepo = MapsFragmentRepository.getInstance();
        mMapsFragmentRepo.getReports(new Action<ArrayList<Report>>() {
            @Override
            public void doSomething(ArrayList<Report> x) {
                reportsList.setValue(x);
            }
        });
        mMapsFragmentRepo.getPointOfInterestMarkers(new Action<ArrayList<PointOfInterestMarker>>() {
            @Override
            public void doSomething(ArrayList<PointOfInterestMarker> x) {
                pointsOfInterestList.setValue(x);
            }
        });
        mMapsFragmentRepo.getLiveEventMarkers(new Action<ArrayList<LiveEventsMarker>>() {
            @Override
            public void doSomething(ArrayList<LiveEventsMarker> x) {
                liveEventMarkerList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<Report>> getReports() {
        return reportsList;
    }

    public LiveData<ArrayList<PointOfInterestMarker>> getPointOfInterestMarkers() {
        return pointsOfInterestList;
    }

    public LiveData<ArrayList<LiveEventsMarker>> getLiveEventMarkers() {
        return liveEventMarkerList;
    }
}
