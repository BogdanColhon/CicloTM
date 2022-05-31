package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Repositories.GeneralFragmentRepository;

import java.util.ArrayList;

public class GeneralFragmentViewModel extends ViewModel {

    private MutableLiveData<ArrayList<generalPost>> postList = new MutableLiveData<>(new ArrayList<generalPost>());
    private GeneralFragmentRepository mGeneralFragmentRepo;

    public void init() {
        mGeneralFragmentRepo = GeneralFragmentRepository.getInstance();
        mGeneralFragmentRepo.getGeneralPosts(new Action<ArrayList<com.example.ciclotm.Models.Posts.generalPost>>() {
            @Override
            public void doSomething(ArrayList<com.example.ciclotm.Models.Posts.generalPost> x) {
                postList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<generalPost>> getGeneralPosts() {
        return postList;
    }

    public void removeGeneralPost(generalPost post){
        mGeneralFragmentRepo.removeGeneralPost(post);
    }
}
