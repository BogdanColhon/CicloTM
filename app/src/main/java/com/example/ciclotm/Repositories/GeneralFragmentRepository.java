package com.example.ciclotm.Repositories;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Services.GeneralPostFirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GeneralFragmentRepository {
    private static GeneralFragmentRepository instance;
    private DatabaseReference reference;
    private GeneralPostFirebaseUtil generalPostFirebaseUtil = new GeneralPostFirebaseUtil();

    public static GeneralFragmentRepository getInstance() {
        if (instance == null) {
            instance = new GeneralFragmentRepository();
        }
        return instance;
    }

    public void getGeneralPosts(Action<ArrayList<generalPost>> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("GeneralPosts");
        generalPostFirebaseUtil.getPosts(callback, reference);
    }

    public void removeGeneralPost(generalPost post) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("GeneralPosts").child(String.valueOf(post.getDate()));
        reference.removeValue();
    }


}

