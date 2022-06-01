package com.example.ciclotm.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.ExpandedTurePostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpandedTurePostActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView dateTextView;
    private TextView activityDateTextView;
    private TextView titleTextView;
    private TextView startPointTextView;
    private TextView startTimeTextView;
    private TextView kmTextView;
    private TextView durationTextView;
    private TextView ridersTextView;
    private TextView contentTextView;
    private Button joinButton;
    private ImageView userPhotoImageView;
    private FirebaseUser user;
    private List<String> participants;

    private DatabaseReference reference;
    private turePost post;
    private ExpandedTurePostViewModel mExpandedTurePostViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_ture_post);

        initActionBar();
        initLayout();
        initUser();
        setData();

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mExpandedTurePostViewModel = ViewModelProviders.of(this).get(ExpandedTurePostViewModel.class);


        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!participants.contains(uid)) {
                    participants.add(uid);
                    mExpandedTurePostViewModel.addParticipant(uid, post, participants);

                    ridersTextView.setText(String.valueOf(post.getNo_participants() + 1));

                } else
                    Toast.makeText(ExpandedTurePostActivity.this, "Deja participați", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUser() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(post.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName();
                    String lastname = userProfile.getLastName();

                    usernameTextView.setText(firstname + " " + lastname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initActionBar() {
        getSupportActionBar().setTitle("Postare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLayout() {
        Intent intent = getIntent();
        post = (turePost) intent.getSerializableExtra("clicked_post");

        usernameTextView = (TextView) findViewById(R.id.upperTextView);
        dateTextView = (TextView) findViewById(R.id.lowerTextView);
        activityDateTextView = (TextView) findViewById(R.id.expandedTurePostActivityDateTextView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        startPointTextView = (TextView) findViewById(R.id.startPointTextView2);
        startTimeTextView = (TextView) findViewById(R.id.departureTimeTextView);
        kmTextView = (TextView) findViewById(R.id.kmTextView2);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        ridersTextView = (TextView) findViewById(R.id.ridersTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);
        joinButton = (Button) findViewById(R.id.joinButton);
        userPhotoImageView = (ImageView) findViewById(R.id.user_photo);
    }

    private void setData() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(post.getDate());
        dateTextView.setText(output);

        Date dateComment = post.getActivityDate();
        df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        output = df.format(dateComment);

        activityDateTextView.setText(output);
        titleTextView.setText(post.getTitle());
        startPointTextView.setText(post.getStart_point());
        startTimeTextView.setText(post.getStart_time());
        kmTextView.setText("~" + post.getDistance() + " km");
        durationTextView.setText("~" + post.getDuration());
        ridersTextView.setText(String.valueOf(post.getNo_participants()));
        contentTextView.setText(post.getDescription());
        participants = post.getParticipants();

        String userImageUrl = post.getUserImageUrl();
        Glide.with(this).load(userImageUrl).into(userPhotoImageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}