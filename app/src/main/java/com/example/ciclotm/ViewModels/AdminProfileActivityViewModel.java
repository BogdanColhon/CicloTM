package com.example.ciclotm.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.Repositories.AdminProfileActivityRepository;

public class AdminProfileActivityViewModel extends ViewModel {
    private AdminProfileActivityRepository mAdminProfileActivityRepo;
    public void changeStatus(User user) {
        mAdminProfileActivityRepo = AdminProfileActivityRepository.getInstance();
        mAdminProfileActivityRepo.postData(user);
    }
}
