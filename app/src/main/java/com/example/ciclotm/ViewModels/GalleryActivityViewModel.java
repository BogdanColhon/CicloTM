package com.example.ciclotm.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Photo;
import com.example.ciclotm.Repositories.GalleryRepository;

import java.util.ArrayList;

public class GalleryActivityViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Photo>> photoList = new MutableLiveData<>(new ArrayList<Photo>());
    private GalleryRepository mGalleryRepo;

    public void init(String userID){
        mGalleryRepo = GalleryRepository.getInstance();
        mGalleryRepo.getPhotoList(userID, new Action<ArrayList<Photo>>() {
            @Override
            public void doSomething(ArrayList<Photo> x) {
                photoList.setValue(x);
            }
        });
    }

    public LiveData<ArrayList<Photo>> getPhotoList(){
        return photoList;
    }
}
