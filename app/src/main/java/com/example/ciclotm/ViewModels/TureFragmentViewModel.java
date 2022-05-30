package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Repositories.TureFragmentRepository;

import java.util.ArrayList;

public class TureFragmentViewModel extends ViewModel {

    private MutableLiveData<ArrayList<turePost>> postList = new MutableLiveData<>(new ArrayList<turePost>());
    private TureFragmentRepository mTureFragmentRepo;

    public void init() {
        mTureFragmentRepo = TureFragmentRepository.getInstance();
        mTureFragmentRepo.getTurePosts(new Action<ArrayList<turePost>>() {
            @Override
            public void doSomething(ArrayList<turePost> x) {
                postList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<turePost>> getTurePosts() {
        return postList;
    }
}
