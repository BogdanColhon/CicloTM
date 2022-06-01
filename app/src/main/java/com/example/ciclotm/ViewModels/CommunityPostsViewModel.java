package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Repositories.CommunityPostsRepository;

import java.util.ArrayList;

public class CommunityPostsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<generalPost>> generalList = new MutableLiveData<>(new ArrayList<generalPost>());
    private MutableLiveData<ArrayList<turePost>> tureList = new MutableLiveData<>(new ArrayList<turePost>());
    private MutableLiveData<ArrayList<Report>> reportList = new MutableLiveData<>(new ArrayList<Report>());
    private CommunityPostsRepository mCommunityPostsRepo;

    public void init(String owner) {
        mCommunityPostsRepo = CommunityPostsRepository.getInstance();

        mCommunityPostsRepo.getGeneralPosts(owner, new Action<ArrayList<generalPost>>() {
            @Override
            public void doSomething(ArrayList<com.example.ciclotm.Models.Posts.generalPost> x) {
                generalList.setValue(x);
            }
        });

        mCommunityPostsRepo.getTurePosts(owner, new Action<ArrayList<turePost>>() {
            @Override
            public void doSomething(ArrayList<turePost> x) {
                tureList.setValue(x);
            }
        });

        mCommunityPostsRepo.getReportPosts(owner, new Action<ArrayList<Report>>() {
            @Override
            public void doSomething(ArrayList<Report> x) {
                reportList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<generalPost>> getGeneralPosts() {
        return generalList;
    }

    public LiveData<ArrayList<turePost>> getTurePosts() {
        return tureList;
    }

    public LiveData<ArrayList<Report>> getReportPosts() {
        return reportList;
    }

    public void removeGeneralPost(generalPost post, String owner) {
        mCommunityPostsRepo.removeGeneralPost(post, owner);
    }

    public void removeTurePost(turePost post, String owner) {
        mCommunityPostsRepo.removeTurePost(post, owner);
    }

    public void removeReportPost(Report post, String owner) {
        mCommunityPostsRepo.removeReportPost(post, owner);
    }
}
