package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FurturiFragmentRepository {
    private static FurturiFragmentRepository instance;
    private DatabaseReference reference;

    public static FurturiFragmentRepository getInstance() {
        if (instance == null) {
            instance = new FurturiFragmentRepository();
        }
        return instance;
    }

    public void getFurturiPosts(Action<ArrayList<Report>> callback) {
        ArrayList<Report> dataSet = new ArrayList<Report>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("furturiPosts");
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
