package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommunityPostsRepository {
    public static CommunityPostsRepository instance;
    private DatabaseReference reference;

    public static CommunityPostsRepository getInstance() {
        if (instance == null) {
            instance = new CommunityPostsRepository();
        }
        return instance;
    }

    public void getGeneralPosts(String owner, Action<ArrayList<generalPost>> callback) {
        ArrayList<generalPost> dataSet = new ArrayList<generalPost>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("CommunityPosts").child("MyGeneralPosts");
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

    public void getTurePosts(String owner, Action<ArrayList<turePost>> callback) {
        ArrayList<turePost> dataSet = new ArrayList<turePost>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("CommunityPosts").child("MyTurePosts");
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

    public void getReportPosts(String owner, Action<ArrayList<Report>> callback) {
        ArrayList<Report> dataSet = new ArrayList<Report>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(owner).child("Furturi");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Report post = postSnapshot.getValue(Report.class);
                    dataSet.add(post);
                }
                callback.doSomething(dataSet);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
