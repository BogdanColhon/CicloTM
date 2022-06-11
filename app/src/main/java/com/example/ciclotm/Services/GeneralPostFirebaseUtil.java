package com.example.ciclotm.Services;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Posts.generalPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GeneralPostFirebaseUtil {
    public void getPosts(Action<ArrayList<generalPost>> callback, DatabaseReference reference) {
        ArrayList<generalPost> dataSet = new ArrayList<generalPost>();
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
