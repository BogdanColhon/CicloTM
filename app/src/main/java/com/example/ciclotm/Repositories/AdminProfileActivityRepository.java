package com.example.ciclotm.Repositories;

import com.example.ciclotm.Models.Users.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminProfileActivityRepository {
    public static AdminProfileActivityRepository instance;
    private DatabaseReference reference;

    public static AdminProfileActivityRepository getInstance() {
        if (instance == null) {
            instance = new AdminProfileActivityRepository();
        }
        return instance;
    }

    public void postData(User user) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        reference.child(user.getUserId()).child("Status").setValue(1);
    }
}
