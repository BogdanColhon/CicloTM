package com.example.ciclotm.Repositories;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Services.FurturiPostFirebaseUtil;
import com.example.ciclotm.Services.GeneralPostFirebaseUtil;
import com.example.ciclotm.Services.TurePostFirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CommunityPostsRepository {
    public static CommunityPostsRepository instance;
    private DatabaseReference reference;
    private TurePostFirebaseUtil turePostFirebaseUtil = new TurePostFirebaseUtil();
    private GeneralPostFirebaseUtil generalPostFirebaseUtil = new GeneralPostFirebaseUtil();
    private FurturiPostFirebaseUtil furturiPostFirebaseUtil = new FurturiPostFirebaseUtil();


    public static CommunityPostsRepository getInstance() {
        if (instance == null) {
            instance = new CommunityPostsRepository();
        }
        return instance;
    }

    public void getGeneralPosts(String owner, Action<ArrayList<generalPost>> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("CommunityPosts").child("MyGeneralPosts");
        generalPostFirebaseUtil.getPosts(callback, reference);
    }

    public void getTurePosts(String owner, Action<ArrayList<turePost>> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("CommunityPosts").child("MyTurePosts");
        turePostFirebaseUtil.getPosts(callback, reference);

    }

    public void getReportPosts(String owner, Action<ArrayList<Report>> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("Furturi");
        furturiPostFirebaseUtil.getPosts(callback, reference);
    }

    public void removeGeneralPost(generalPost post, String owner) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("GeneralPosts").child(String.valueOf(post.getDate()));
        reference.removeValue();
        DatabaseReference localReference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("Users").child(owner).child("CommunityPosts").child("MyGeneralPosts").child(String.valueOf(post.getDate()));
        localReference.removeValue();
    }

    public void removeTurePost(turePost post, String owner) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("TurePosts").child(String.valueOf(post.getDate()));
        reference.removeValue();
        DatabaseReference localReference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("Users").child(owner).child("CommunityPosts").child("MyTurePosts").child(String.valueOf(post.getDate()));
        localReference.removeValue();
    }

    public void removeReportPost(Report post, String owner) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("furturiPosts").child(String.valueOf(post.getPublishDate()));
        reference.removeValue();
        DatabaseReference localReference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("Users").child(owner).child("Furturi").child(String.valueOf(post.getPublishDate()));
        localReference.removeValue();
    }
}
