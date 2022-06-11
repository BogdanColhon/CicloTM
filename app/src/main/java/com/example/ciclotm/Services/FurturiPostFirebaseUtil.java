package com.example.ciclotm.Services;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FurturiPostFirebaseUtil {

    public void getPosts(Action<ArrayList<Report>> callback, DatabaseReference reference) {
        ArrayList<Report> dataSet = new ArrayList<Report>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Report post = postSnapshot.getValue(Report.class);
                    if (post.getStatus() == 0) {
                        dataSet.add(post);
                    }
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
