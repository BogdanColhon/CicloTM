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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

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
        joinButton = (Button) findViewById(R.id.joinButton);
        userPhotoImageView = (ImageView) findViewById(R.id.user_photo);

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

        try {
            getUserProfilePhoto(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap hashMap = new HashMap();
                hashMap.put("no_participants",post.getNo_participants()+1);
                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("TurePosts").child(post.getDate().toString()).updateChildren(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ridersTextView.setText(String.valueOf(post.getNo_participants()+1));
                                }
                            }
                        });
            }
        });
    }

    public void getUserProfilePhoto(turePost post) throws IOException {
        String userProfilePhoto = "UsersProfilePicture/" + post.getUid() + ".png";
        storageReference = FirebaseStorage.getInstance().getReference().child(userProfilePhoto);
        File localFile = File.createTempFile("tempFile", "png");
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
               userPhotoImageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}