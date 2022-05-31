package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Comment;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpandedGeneralPostRepository {
    private static ExpandedGeneralPostRepository instance;
    private DatabaseReference reference;
public User dataSet = new User();
    public static ExpandedGeneralPostRepository getInstance() {
        if (instance == null) {
            instance = new ExpandedGeneralPostRepository();
        }
        return instance;
    }

    public void getComments(generalPost post, Action<ArrayList<Comment>> callback) {
        ArrayList<Comment> dataSet = new ArrayList<Comment>();
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("GeneralPosts").child(String.valueOf(post.getDate())).child("comments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Comment comment = postSnapshot.getValue(Comment.class);
                    dataSet.add(comment);
                }
                callback.doSomething(dataSet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
