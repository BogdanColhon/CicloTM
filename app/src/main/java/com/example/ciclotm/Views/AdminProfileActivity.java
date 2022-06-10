package com.example.ciclotm.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.AdminProfileActivityViewModel;

import java.io.IOException;

public class AdminProfileActivity extends AppCompatActivity {

    private ImageView userProfileImageView;
    private User user;
    private AdminProfileActivityViewModel mAdminProfileActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("clicked_user");

        mAdminProfileActivityViewModel = ViewModelProviders.of(this).get(AdminProfileActivityViewModel.class);

        final TextView nameTextView = (TextView) findViewById(R.id.profileNameTextView);
        userProfileImageView = (ImageView) findViewById(R.id.userProfileImageView);

        nameTextView.setText(user.getFirstName() + " " + user.getLastName());
        try {
            getUserProfilePhoto(user.getProfileImageUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                mAdminProfileActivityViewModel.changeStatus(user);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getUserProfilePhoto(String profileImageUrl) throws IOException {
        if (!profileImageUrl.equals("")) {
            Glide.with(this).load(profileImageUrl).into(userProfileImageView);
        }
    }
}