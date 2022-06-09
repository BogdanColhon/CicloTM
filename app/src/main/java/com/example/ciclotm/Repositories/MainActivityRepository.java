package com.example.ciclotm.Repositories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ciclotm.Action;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.example.ciclotm.Views.AdminMenuActivity2;
import com.example.ciclotm.Views.MainActivity;
import com.example.ciclotm.Views.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityRepository {
    private FirebaseAuth mAuth;
    public static String role;
    private DatabaseReference reference;
    public static MainActivityRepository instance;
    public static MainActivityRepository getInstance() {
        if(instance == null)
        {
            instance = new MainActivityRepository();
        }
        return instance;
    }

    public void getUser(FirebaseUser user, Action<User> callback) {
        reference = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
