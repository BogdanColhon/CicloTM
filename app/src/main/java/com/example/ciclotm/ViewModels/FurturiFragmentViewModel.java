package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Repositories.FurturiFragmentRepository;

import java.util.ArrayList;

public class FurturiFragmentViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Report>> postList = new MutableLiveData<>(new ArrayList<Report>());
    private FurturiFragmentRepository mFurturiFragmentRepo;

    public void init(){
        mFurturiFragmentRepo = FurturiFragmentRepository.getInstance();
        mFurturiFragmentRepo.getFurturiPosts(new Action<ArrayList<Report>>() {
            @Override
            public void doSomething(ArrayList<Report> x) {
                postList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<Report>> getFurturiPosts(){
        return postList;
    }
    public void removeFurtPost(Report report){
        mFurturiFragmentRepo.removeFurtPost(report);
    }
}
