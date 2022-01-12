package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class ExpandedGeneralPostActivity extends AppCompatActivity {

    TextView titlePostTextView;
    TextView datePostTextView;
    TextView contentPostTextView;
    TextView userNamePostTextView;
    ImageView userProfileImageView;

    private DatabaseReference reference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_general_post);
        getSupportActionBar().setTitle("Postare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        generalPost post = (generalPost) intent.getSerializableExtra("clicked_post");
        userNamePostTextView = (TextView) findViewById(R.id.upperTextView);
        datePostTextView = (TextView) findViewById(R.id.lowerTextView);
        titlePostTextView = (TextView) findViewById(R.id.titleTextView);
        contentPostTextView = (TextView) findViewById(R.id.contentTextView);
        userProfileImageView = (ImageView) findViewById(R.id.user_photo);

        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(post.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName();
                    String lastname = userProfile.getLastName();

                    userNamePostTextView.setText(firstname + " " + lastname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(post.getDate());
        int month = calendar.get(Calendar.MONTH) + 1;
        datePostTextView.setText(calendar.get(Calendar.DAY_OF_MONTH)
                + "." + month
                + "." + calendar.get(Calendar.YEAR));

        titlePostTextView.setText(post.getTitle());
        contentPostTextView.setText(post.getContent());

        String userImageUrl = post.getUserImageUrl();
        Picasso.get().load(userImageUrl).rotate(90).fit().centerInside().into(userProfileImageView);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }

    public void getUserProfilePhoto(generalPost post) throws IOException {
        String userProfilePhoto = "UsersProfilePicture/" + post.getUid() + ".png";
        storageReference = FirebaseStorage.getInstance().getReference().child(userProfilePhoto);
        File localFile = File.createTempFile("tempFile", "png");
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                userProfileImageView.setImageBitmap(bitmap);
            }
        });
    }
}