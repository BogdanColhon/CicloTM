package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Comment;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.Repositories.ExpandedGeneralPostRepository;

import java.util.ArrayList;

public class ExpandedGeneralPostViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Comment>> commentList = new MutableLiveData<>(new ArrayList<Comment>());
    private ExpandedGeneralPostRepository mExpandedGeneralPostRepo;

    public void init(generalPost post) {
        mExpandedGeneralPostRepo = ExpandedGeneralPostRepository.getInstance();
        mExpandedGeneralPostRepo.getComments(post, new Action<ArrayList<Comment>>() {
            @Override
            public void doSomething(ArrayList<Comment> x) {
                commentList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<Comment>> getComments() {
        return commentList;
    }
}
