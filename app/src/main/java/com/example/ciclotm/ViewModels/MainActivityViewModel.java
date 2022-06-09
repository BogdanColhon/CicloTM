package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.Repositories.MainActivityRepository;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityViewModel extends ViewModel {
    private MainActivityRepository mMainActivityRepo;
    private MutableLiveData<User> user = new MutableLiveData<User>();

    public void init(FirebaseUser firebaseUseruser) {
        mMainActivityRepo = MainActivityRepository.getInstance();
        mMainActivityRepo.getUser(firebaseUseruser, new Action<User>() {
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
