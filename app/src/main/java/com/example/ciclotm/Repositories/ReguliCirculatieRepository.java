package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Legislation.CategorieReguli;
import com.example.ciclotm.Models.Objects.Comment;
import com.example.ciclotm.Models.Posts.generalPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReguliCirculatieRepository {
    private static ReguliCirculatieRepository instance;
    private DatabaseReference reference;

    public static ReguliCirculatieRepository getInstance() {
        if (instance == null) {
            instance = new ReguliCirculatieRepository();
        }
        return instance;
    }

    public void getReguliCirculatie(Action<ArrayList<CategorieReguli>> callback) {
        ArrayList<CategorieReguli> dataSet = new ArrayList<CategorieReguli>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Legislație").child("Reguli_Circulatie");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    CategorieReguli regula = postSnapshot.getValue(CategorieReguli.class);
                    dataSet.add(regula);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
