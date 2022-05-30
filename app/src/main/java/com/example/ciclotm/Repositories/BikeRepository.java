package com.example.ciclotm.Repositories;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Bike;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.BicyclesActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BikeRepository {
    private static BikeRepository instance;
    private  DatabaseReference reference;
    private  DatabaseReference ref;

    public static BikeRepository getInstance() {
        if (instance == null) {
            instance = new BikeRepository();
        }
        return instance;
    }

    public void getBikes(Action<ArrayList<Bike>> callback){
        ArrayList<Bike> dataSet = new ArrayList<Bike>();
        reference =FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child("wbLbOXyvQDMIWvxNq9vXqXHOkwJ3");
        ref = reference.child("BikeCollection");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Bike bike = postSnapshot.getValue(Bike.class);
                    dataSet.add(bike);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    }






