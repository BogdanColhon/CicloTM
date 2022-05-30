package com.example.ciclotm;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    String currentPhotoPath;
    String user_id;
    String genderString;
    private File f;
    private StorageReference Folder;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Uri contentUri;
    private final int PERMISSION_REQUEST_CODE = 100;

    EditText firstNameEW;
    EditText lastNameEW;
    EditText bioEW;
    EditText birthDateEW;
    EditText phoneET;
    EditText genderET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Editează profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("uId");
        fetchInfo();

        firstNameEW = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEW = (EditText) findViewById(R.id.lastNameEditText);
        bioEW = (EditText) findViewById(R.id.bioEditText);
        phoneET = (EditText) findViewById(R.id.phone);
        profileImage = (ImageView) findViewById(R.id.userProfileImageView);
        genderET = (EditText) findViewById(R.id.genderET);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkCameraPermission();
                    chooseOption(100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void fetchInfo() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName();
                    String lastname = userProfile.getLastName();
                    String bio = userProfile.getBio();
                    String gender = userProfile.getGender();
                    String phone = userProfile.getPhoneNumber();
                    String profileImageUrl = userProfile.getProfileImageUrl();

                    firstNameEW.setText(firstname);
                    lastNameEW.setText(lastname);
                    bioEW.setText(bio);
                    phoneET.setText(phone);
                    genderET.setText(gender);

                    if (!profileImageUrl.equals("")) {
                        Glide.with(getApplicationContext()).load(profileImageUrl).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void chooseOption(int code) {
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.camera_gallery_dialog);


        ImageView cameraImageView = dialog.findViewById(R.id.cameraImageView);
        ImageView galleryImageView = dialog.findViewById(R.id.galleryImageView);

        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(code);
                dialog.dismiss();
            }
        });
        galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }
        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }

    private void dispatchTakePictureIntent(int request) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, request);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {
                contentUri = data.getData();
                f = new File(contentUri.getPath());
                profileImage.setImageURI(contentUri);
            }
        }
        if (requestCode == 100) {
            f = new File(currentPhotoPath);
            Log.d("tag", "Absolute path URL" + Uri.fromFile(f));
            if (contentUri != null)
                profileImage.setImageURI(Uri.fromFile(f));
            contentUri = Uri.fromFile(f);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void uploadPhotos() {
        Folder = FirebaseStorage.getInstance().getReference().child("UsersProfilePicture");
        StorageReference profileImageName = Folder.child(f.getName());
        if (contentUri != null) {
            profileImageName.putFile(contentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("profileImageUrl", uri.toString());
                                    FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(user_id).updateChildren(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {

                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                String firstname = firstNameEW.getText().toString().trim();
                String lastname = lastNameEW.getText().toString().trim();
                String bio = bioEW.getText().toString().trim();
                String gender = genderET.getText().toString().trim();
                String phone = phoneET.getText().toString().trim();
                HashMap hashMap = new HashMap();
                hashMap.put("FirstName", firstname);
                hashMap.put("LastName", lastname);
                hashMap.put("bio", bio);
                hashMap.put("Gender", gender);
                hashMap.put("PhoneNumber", phone);


                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(user_id).updateChildren(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditProfileActivity.this, "Modificări efectuate", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                if (contentUri != null)
                    uploadPhotos();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}