package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class ExpandedTurePostActivity extends AppCompatActivity {

    TextView usernameTextView;
    TextView dateTextView;
    TextView titleTextView;
    TextView startPointTextView;
    TextView startTimeTextView;
    TextView kmTextView;
    TextView durationTextView;
    TextView ridersTextView;
    TextView contentTextView;

    private DatabaseReference reference;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_ture_post);
        getSupportActionBar().setTitle("Postare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        turePost post = (turePost) intent.getSerializableExtra("clicked_post");

        usernameTextView = (TextView) findViewById(R.id.upperTextView);
        dateTextView = (TextView) findViewById(R.id.lowerTextView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        startPointTextView = (TextView) findViewById(R.id.startPointTextView2);
        startTimeTextView = (TextView) findViewById(R.id.departureTimeTextView);
        kmTextView = (TextView) findViewById(R.id.kmTextView2);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        ridersTextView = (TextView) findViewById(R.id.ridersTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);

        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(post.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.FirstName;
                    String lastname = userProfile.LastName;

                    usernameTextView.setText(firstname + " " + lastname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(post.getDate());
        int month = calendar.get(Calendar.MONTH) + 1;
        dateTextView.setText(calendar.get(Calendar.DAY_OF_MONTH)
                + "." + month
                + "." + calendar.get(Calendar.YEAR));

        titleTextView.setText(post.getTitle());
        startPointTextView.setText(post.getStart_point());
        startTimeTextView.setText(post.getStart_time());
        kmTextView.setText("~"+post.getDistance()+" km");
        durationTextView.setText("~"+post.getDuration());
        ridersTextView.setText(String.valueOf(post.getNo_participants()));
        contentTextView.setText(post.getDescription());

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}