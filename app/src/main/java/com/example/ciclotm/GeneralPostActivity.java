package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class GeneralPostActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userImageUrl;
    private generalPost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_post);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("General");
        actionBar.setDisplayHomeAsUpEnabled(true);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_bicycles, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();

                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
                String uid = user.getUid();


                reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile != null) {
                            userImageUrl = String.valueOf(userProfile.getProfileImageUrl());
                            post = new generalPost(title, description, currentTime, uid, userImageUrl);

                            FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("GeneralPosts").child(String.valueOf(currentTime))
                                    .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                        Toast.makeText(GeneralPostActivity.this, "Postare adaugată", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(uid).child("CommunityPosts").child(String.valueOf(currentTime))
                                    .setValue(post);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });





                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}