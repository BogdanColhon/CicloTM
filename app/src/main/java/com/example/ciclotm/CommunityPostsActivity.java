package com.example.ciclotm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CommunityPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_posts);
        getSupportActionBar().setTitle("Postări comunitate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}