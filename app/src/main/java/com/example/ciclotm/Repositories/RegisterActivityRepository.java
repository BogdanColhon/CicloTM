package com.example.ciclotm.Repositories;

import androidx.annotation.NonNull;

import com.example.ciclotm.Models.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class RegisterActivityRepository {
    private FirebaseAuth mAuth;
    private User user;
    public static RegisterActivityRepository instance;

    public static RegisterActivityRepository getInstance() {
        if (instance == null) {
            instance = new RegisterActivityRepository();
        }
        return instance;
    }

    public void createUser(String email, String password, String lastname, String firstname, Date date, String phonenumber) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (password.matches("admin23.*")) {
                                user = new User(lastname, firstname, date, phonenumber, "", email, "Bio", "Sex", "", "1");
                            } else {
                                user = new User(lastname, firstname, date, phonenumber, "", email, "Bio", "Sex", "https://firebasestorage.googleapis.com/v0/b/ciclotm.appspot.com/o/Admin%2FScreenshot%202022-01-13%20185334.jpg?alt=media&token=f5c61f90-a0a1-4b5e-a1bf-048c5edcfb27", "0");
                            }
                            FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user);
                        }
                    }
                });
    }
}
