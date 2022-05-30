package com.example.ciclotm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ciclotm.Models.Users.User;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        User user= (User) intent.getSerializableExtra("clicked_user");
        TextView a = (TextView) findViewById(R.id.userProfileNameTextView);
        a.setText(user.getFirstName());
    }
}