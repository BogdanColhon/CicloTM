package com.example.ciclotm.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ciclotm.BicyclesActivity;
import com.example.ciclotm.CommunityPostsActivity;
import com.example.ciclotm.R;
import com.example.ciclotm.StatsActivity;
import com.example.ciclotm.User;
import com.example.ciclotm.profileListViewAdapter;
import com.example.ciclotm.profileListViewButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdminProfileActivity extends AppCompatActivity {

    private String[] button_names = {"Postări general", "Postări ture", "Postări furturi"};
    private ArrayList<profileListViewButton> profile_buttons = new ArrayList<>();
    private ImageView userProfileImageView;
    private  User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("clicked_user");


        final TextView nameTextView = (TextView) findViewById(R.id.profileNameTextView);
        final TextView ageTextView = (TextView) findViewById(R.id.profileAgeTextView);
        userProfileImageView = (ImageView) findViewById(R.id.userProfileImageView);

        nameTextView.setText(user.getFirstName() + " " + user.getLastName());
        ageTextView.setText(String.valueOf(calculateAge(user.getBirthDate())) + " ani");
        try {
            getUserProfilePhoto(user.getProfileImageUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView profileListView = (ListView) findViewById(R.id.profileListView);
        for (int i = 0; i < button_names.length; i++) {
            profileListViewButton button = new profileListViewButton(button_names[i], ">");
            profile_buttons.add(button);
        }
        profileListViewAdapter adapter = new profileListViewAdapter(AdminProfileActivity.this, profile_buttons);
        profileListView.setAdapter(adapter);
        profileListView.setClickable(true);
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(AdminProfileActivity.this, BicyclesActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(AdminProfileActivity.this, StatsActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(AdminProfileActivity.this, CommunityPostsActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });


    }

    private int calculateAge(Date birthDate) {

        Calendar birthday = Calendar.getInstance();
        birthday.setTime(birthDate);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getUserProfilePhoto(String profileImageUrl) throws IOException {
        if (!profileImageUrl.equals("")) {
            Picasso.get().load(profileImageUrl).fit().centerInside().into(userProfileImageView);
        }
    }
}