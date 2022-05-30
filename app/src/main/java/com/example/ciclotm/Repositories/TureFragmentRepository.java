package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TureFragmentRepository {
    private static TureFragmentRepository instance;
    private DatabaseReference reference;

    public static TureFragmentRepository getInstance() {
        if (instance == null) {
            instance = new TureFragmentRepository();
        }
        return instance;
    }

    public void getTurePosts(Action<ArrayList<turePost>> callback) {
        ArrayList<turePost> dataSet = new ArrayList<turePost>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("TurePosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    turePost post = postSnapshot.getValue(turePost.class);
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
