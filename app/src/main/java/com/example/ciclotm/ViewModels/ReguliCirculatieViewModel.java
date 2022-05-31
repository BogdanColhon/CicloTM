package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Legislation.CategorieReguli;
import com.example.ciclotm.Repositories.ReguliCirculatieRepository;

import java.util.ArrayList;

public class ReguliCirculatieViewModel extends ViewModel {
    private MutableLiveData<ArrayList<CategorieReguli>> reguliList = new MutableLiveData<>(new ArrayList<CategorieReguli>());
    private ReguliCirculatieRepository mReguliCirculatieRepo;

    public void init(){
        mReguliCirculatieRepo = ReguliCirculatieRepository.getInstance();
        mReguliCirculatieRepo.getReguliCirculatie(new Action<ArrayList<CategorieReguli>>() {
            @Override
            public void doSomething(ArrayList<CategorieReguli> x) {
                reguliList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<CategorieReguli>> getReguliCirculatie(){
        return reguliList;
    }
}
