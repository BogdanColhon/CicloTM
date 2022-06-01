package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Users.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpandedFurturiPostRepository {
    public static ExpandedFurturiPostRepository instance;
    private DatabaseReference reference, localReference;

    public static ExpandedFurturiPostRepository getInstance() {
        if (instance == null) {
            instance = new ExpandedFurturiPostRepository();
        }
        return instance;
    }

    public void updateStatus(Report post, String owner) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("furturiPosts").child(String.valueOf(post.getPublishDate()));
        reference.child("status").setValue(1);

        localReference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference()
                .child("Users").child(owner).child("Furturi").child(String.valueOf(post.getPublishDate()));
        localReference.child("status").setValue(1);
    }

    public void getUser(Report post, Action<User> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        reference.child(post.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    callback.doSomething(userProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
