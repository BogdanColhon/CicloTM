package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Bike;
import com.example.ciclotm.Models.Objects.Photo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryRepository {
    private static GalleryRepository instance;
    private DatabaseReference reference;
    private  DatabaseReference ref;

    public static GalleryRepository getInstance() {
        if (instance == null) {
            instance = new GalleryRepository();
        }
        return instance;
    }

    public void getPhotoList(String userID,Action<ArrayList<Photo>> callback){
        ArrayList<Photo> dataSet = new ArrayList<Photo>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(userID);
        ref = reference.child("Gallery");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Photo photo = postSnapshot.getValue(Photo.class);
                    dataSet.add(photo);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
