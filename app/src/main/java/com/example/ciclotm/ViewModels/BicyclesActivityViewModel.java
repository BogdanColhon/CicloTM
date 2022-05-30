package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Bike;
import com.example.ciclotm.Repositories.BikeRepository;

import java.util.ArrayList;

public class BicyclesActivityViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Bike>> bikesList = new MutableLiveData<>(new ArrayList<Bike>());
    private BikeRepository mBikeRepo;

    public void init() {
        mBikeRepo = BikeRepository.getInstance();
        mBikeRepo.getBikes(new Action<ArrayList<Bike>>(){

            @Override
            public void doSomething(ArrayList<Bike> x) {
                bikesList.setValue(x);
            }
        });


    }

    public LiveData<ArrayList<Bike>> getBikes() {
        return bikesList;
    }
}
