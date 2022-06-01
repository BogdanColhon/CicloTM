package com.example.ciclotm.Repositories;

import com.example.ciclotm.Models.Posts.turePost;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class ExpandedTurePostRepository {
    public static ExpandedTurePostRepository instance;

    public static ExpandedTurePostRepository getInstance() {
        if (instance == null) {
            instance = new ExpandedTurePostRepository();
        }
        return instance;
    }

    public void addParticipant(String uid, turePost post, List<String> participants) {

        HashMap hashMap = new HashMap();
        hashMap.put("no_participants", post.getNo_participants() + 1);
        hashMap.put("participants", participants);
        FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("TurePosts").child(post.getDate().toString()).updateChildren(hashMap);

    }
}
