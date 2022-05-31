package com.example.ciclotm.ViewModels;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Repositories.BicyclePostRepository;

import java.io.File;
import java.util.Date;

public class BicyclePostViewModel extends ViewModel {
    private BicyclePostRepository mBicyclePostRepo;

    public void postData(String nick_name, String type, String brand, String model, String weight, String year, String serialNo, String description, String owner, String bikeImageLink, File f, Uri contentUri, Date currentTime) {
        mBicyclePostRepo = BicyclePostRepository.getInstance();
        mBicyclePostRepo.postData(nick_name, type, brand, model, weight, year, serialNo, description, owner, bikeImageLink);
        mBicyclePostRepo.uploadPhotos(owner, f, contentUri, nick_name, currentTime);
    }

}
