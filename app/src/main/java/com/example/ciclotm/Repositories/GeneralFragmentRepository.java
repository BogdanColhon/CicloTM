package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Posts.generalPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GeneralFragmentRepository {
    private static GeneralFragmentRepository instance;
    private DatabaseReference reference;

    public static GeneralFragmentRepository getInstance() {
        if (instance == null) {
            instance = new GeneralFragmentRepository();
        }
        return instance;
    }

    public void getGeneralPosts(Action<ArrayList<generalPost>> callback) {
        ArrayList<generalPost> dataSet = new ArrayList<generalPost>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("GeneralPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    generalPost post = postSnapshot.getValue(generalPost.class);
                    dataSet.add(post);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

