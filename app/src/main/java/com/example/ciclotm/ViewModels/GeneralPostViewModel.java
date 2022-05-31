package com.example.ciclotm.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Repositories.GeneralPostRepository;

import java.util.Date;

public class GeneralPostViewModel extends ViewModel {
    private GeneralPostRepository mGeneralPostRepo;
    public void postData(String title, String description, Date currentTime, String uid) {
        mGeneralPostRepo = GeneralPostRepository.getInstance();
        mGeneralPostRepo.postData(title,description,currentTime,uid);
    }
}
