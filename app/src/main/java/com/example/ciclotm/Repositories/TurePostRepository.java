package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.Views.TurePostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class TurePostRepository {
    private static TurePostRepository instance;
    private DatabaseReference reference;
    private DatabaseReference ref;
    private String userImageUrl;

    public static TurePostRepository getInstance() {
        if (instance == null) {
            instance = new TurePostRepository();
        }
        return instance;
    }

    public void postData(String title, String distance, String duration, String timeUnit, String startTime, String startPoint, int no_participants, String description, String uid, Date currentTime, Date activityDate, List<String> participants) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        ref = reference.child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    userImageUrl = String.valueOf(userProfile.getProfileImageUrl());
                    turePost post = new turePost(title, distance, duration + " " + timeUnit, startTime, startPoint, 1, description, uid, currentTime, activityDate, userImageUrl, participants);

                    FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("TurePosts").child(String.valueOf(currentTime))
                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                TurePostActivity.terminator.finish();
                            }
                        }
                    });
                    FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(uid).child("CommunityPosts").child("MyTurePosts").child(String.valueOf(currentTime))
                            .setValue(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
