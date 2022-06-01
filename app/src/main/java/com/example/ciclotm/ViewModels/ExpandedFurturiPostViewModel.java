package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.Repositories.ExpandedFurturiPostRepository;

public class ExpandedFurturiPostViewModel extends ViewModel {
    private ExpandedFurturiPostRepository mExpandedFurturiPostRepo;
    private MutableLiveData<User> user = new MutableLiveData<User>();

    public void updateStatus(Report post, String owner) {
        mExpandedFurturiPostRepo = ExpandedFurturiPostRepository.getInstance();
        mExpandedFurturiPostRepo.updateStatus(post, owner);
    }

    public void init(Report post) {
        mExpandedFurturiPostRepo = ExpandedFurturiPostRepository.getInstance();
        mExpandedFurturiPostRepo.getUser(post, new Action<User>() {
            @Override
            public void doSomething(User x) {
                user.setValue(x);
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }
}
