package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.Views.GeneralPostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class GeneralPostRepository {
    private static GeneralPostRepository instance;
    private DatabaseReference reference;
    private DatabaseReference ref;
    private String userImageUrl;
    private generalPost post;

    public static GeneralPostRepository getInstance() {
        if (instance == null) {
            instance = new GeneralPostRepository();
        }
        return instance;
    }

    public void postData(String title, String description, Date currentTime, String uid) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        ref = reference.child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    userImageUrl = String.valueOf(userProfile.getProfileImageUrl());
                    post = new generalPost(title, description, currentTime, uid, userImageUrl);

                    FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("GeneralPosts").child(String.valueOf(currentTime))
                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                GeneralPostActivity.terminator.finish();
                            }
                        }
                    });
                    FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(uid).child("CommunityPosts").child("MyGeneralPosts").child(String.valueOf(currentTime))
                            .setValue(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

