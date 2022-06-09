package com.example.ciclotm.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Repositories.RegisterActivityRepository;

import java.util.Date;

public class RegisterActivityViewModel extends ViewModel {
    private RegisterActivityRepository mRegisterActivityRepo;

    public void createUser(String email, String password, String lastname, String firstname, Date date, String phonenumber) {
        mRegisterActivityRepo = RegisterActivityRepository.getInstance();
        mRegisterActivityRepo.createUser(email, password, lastname, firstname, date, phonenumber);
    }
}
