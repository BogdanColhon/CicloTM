package com.example.ciclotm.Repositories;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.ciclotm.Models.Objects.Bike;
import com.example.ciclotm.Views.BicyclePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class BicyclePostRepository {
    public static BicyclePostRepository instance;
    private StorageReference Folder;


    public static BicyclePostRepository getInstance() {
        if (instance == null) {
            instance = new BicyclePostRepository();
        }
        return instance;
    }

    public void postData(String nick_name, String type, String brand, String model, String weight, String year, String serialNo, String description, String owner, String bikeImageLink) {

        Bike bike = new Bike(nick_name, type, brand, model, weight, year, serialNo, description, owner, bikeImageLink);

        FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("BikeCollection").child(nick_name)
                .setValue(bike).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //uploadPhotos();
                    BicyclePostActivity.terminator.finish();
                }
            }
        });
    }

    public void uploadPhotos(String owner, File f, Uri contentUri, String nick_name, Date currentTime) {
        Folder = FirebaseStorage.getInstance().getReference().child(owner).child("BikeCollectionImages").child(currentTime.toString());
        if (f != null) {
            StorageReference bikeImageName = Folder.child(f.getName());
            if (contentUri != null) {
                bikeImageName.putFile(contentUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                  @Override
                                                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                      bikeImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                          @Override
                                                          public void onSuccess(Uri uri) {
                                                              HashMap hashMap = new HashMap();
                                                              hashMap.put("BikePhotoUrl", uri.toString());
                                                              FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("BikeCollection").child(nick_name).updateChildren(hashMap)
                                                                      .addOnSuccessListener(new OnSuccessListener() {
                                                                          @Override
                                                                          public void onSuccess(Object o) {

                                                                          }
                                                                      });

                                                          }
                                                      });
                                                  }
                                              }
                        ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
    }


}
