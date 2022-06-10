package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.Repositories.MenuActivityRepository;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivityViewModel extends ViewModel {
    private MenuActivityRepository mMenuActivityRepo;
    private MutableLiveData<User> user = new MutableLiveData<User>();

    public void init(FirebaseUser firebaseUser) {
        mMenuActivityRepo = MenuActivityRepository.getInstance();
        mMenuActivityRepo.getUser(firebaseUser, new Action<User>() {
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
