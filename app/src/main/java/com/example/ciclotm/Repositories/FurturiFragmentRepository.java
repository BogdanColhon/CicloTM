package com.example.ciclotm.Repositories;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Services.FurturiPostFirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FurturiFragmentRepository {
    private static FurturiFragmentRepository instance;
    private DatabaseReference reference;
    private FurturiPostFirebaseUtil furturiPostFirebaseUtil = new FurturiPostFirebaseUtil();

    public static FurturiFragmentRepository getInstance() {
        if (instance == null) {
            instance = new FurturiFragmentRepository();
        }
        return instance;
    }

    public void getFurturiPosts(Action<ArrayList<Report>> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("furturiPosts");
        furturiPostFirebaseUtil.getPosts(callback, reference);
    }

    public void removeFurtPost(Report report) {
        DatabaseReference PostReference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("furturiPosts").child(String.valueOf(report.getPublishDate()));
        PostReference.removeValue();
        DatabaseReference LocalPostsReference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("Users").child(report.getUser_id()).child("Furturi").child(String.valueOf(report.getPublishDate()));
        LocalPostsReference.removeValue();
    }
}
