package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Services.TurePostFirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TureFragmentRepository {
    private static TureFragmentRepository instance;
    private DatabaseReference reference;
    private TurePostFirebaseUtil turePostFirebaseUtil = new TurePostFirebaseUtil();

    public static TureFragmentRepository getInstance() {
        if (instance == null) {
            instance = new TureFragmentRepository();
        }
        return instance;
    }

    public void getTurePosts(Action<ArrayList<turePost>> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("TurePosts");
        turePostFirebaseUtil.getPosts(callback, reference);
    }

    public void removeTurePost(turePost post) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("TurePosts").child(String.valueOf(post.getDate()));
        reference.removeValue();
    }
}
