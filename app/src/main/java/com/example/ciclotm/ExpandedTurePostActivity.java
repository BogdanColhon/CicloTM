package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    Button joinButton;
    ImageView userPhotoImageView;
    private FirebaseUser user;

    private DatabaseReference reference;
    private DatabaseReference reference1;
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
        joinButton = (Button) findViewById(R.id.joinButton);
        userPhotoImageView = (ImageView) findViewById(R.id.user_photo);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference1 = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        String uid = user.getUid();

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(post.getDate());
        int month = calendar.get(Calendar.MONTH) + 1;
        dateTextView.setText(calendar.get(Calendar.DAY_OF_MONTH)
                + "." + month
                + "." + calendar.get(Calendar.YEAR));

        titleTextView.setText(post.getTitle());
        startPointTextView.setText(post.getStart_point());
        startTimeTextView.setText(post.getStart_time());
        kmTextView.setText("~" + post.getDistance() + " km");
        durationTextView.setText("~" + post.getDuration());
        ridersTextView.setText(String.valueOf(post.getNo_participants()));
        contentTextView.setText(post.getDescription());
        List<String> participants = post.getParticipants();

        String userImageUrl = post.getUserImageUrl();
        Picasso.get().load(userImageUrl).fit().centerInside().into(userPhotoImageView);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!participants.contains(uid)) {
                    participants.add(uid);
                    HashMap hashMap = new HashMap();
                    hashMap.put("no_participants", post.getNo_participants() + 1);
                    hashMap.put("participants",participants);
                    FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("TurePosts").child(post.getDate().toString()).updateChildren(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ridersTextView.setText(String.valueOf(post.getNo_participants() + 1));
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(ExpandedTurePostActivity.this,"Deja participați",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}