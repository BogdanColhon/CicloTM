package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ciclotm.Models.Bike;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BicyclePostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private File f;
    private Uri contentUri;
    private EditText nick_nameEditText;
    private EditText brandEditText;
    private EditText yearEditText;
    private EditText weightEditText;
    private EditText descriptionEditText;
    private EditText modelEditText;
    private EditText serialNumberEditText;
    private ImageView bikeImageView;
    private ImageView serialNoInfoImage;
    private Spinner typeSpinner;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String type;
    private String owner;
    private String currentPhotoPath;
    private String bikeImageLink = "";
    private String nick_name;
    private StorageReference Folder;
    private Date currentTime;
    private final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_post);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bicicletă");
        actionBar.setDisplayHomeAsUpEnabled(true);

        currentTime = Calendar.getInstance().getTime();

        nick_nameEditText = (EditText) findViewById(R.id.nicknameEditText);
        brandEditText = (EditText) findViewById(R.id.brandEditText);
        yearEditText = (EditText) findViewById(R.id.yearEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        modelEditText = (EditText) findViewById(R.id.modelEditText);
        serialNumberEditText = (EditText) findViewById(R.id.serialNumberEditText);

        bikeImageView = (ImageView) findViewById(R.id.bike_image);
        bikeImageView.setOnClickListener(new View.OnClickListener() {
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

        serialNoInfoImage = (ImageView) findViewById(R.id.serialNoInfoImageView);
        serialNoInfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(BicyclePostActivity.this).create();
                alertDialog.setMessage(getResources().getString(R.string.serial_no_info));
                alertDialog.show();
            }
        });

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BicyclePostActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.bike_models));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(this);
    }

    private void chooseOption(int code) {
        final Dialog dialog = new Dialog(BicyclePostActivity.this);
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
                startActivityForResult(pickPhoto,1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(BicyclePostActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(BicyclePostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(BicyclePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }
        ActivityCompat.requestPermissions(BicyclePostActivity.this, new String[]{
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_save_bicycle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                nick_name = nick_nameEditText.getText().toString();
                String brand = brandEditText.getText().toString();
                String model = modelEditText.getText().toString();
                String weight = weightEditText.getText().toString();
                String year = yearEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String serialNo = serialNumberEditText.getText().toString();

                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
                owner = user.getUid();
                Bike bike = new Bike(nick_name, type, brand, model, weight, year,serialNo, description, owner, bikeImageLink);

                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("BikeCollection").child(nick_name)
                        .setValue(bike).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            uploadPhotos();
                            Toast.makeText(BicyclePostActivity.this, "Bicicletă adaugată", Toast.LENGTH_SHORT).show();
                            BicyclesActivity.terminator.finish();
                            Intent myIntent = new Intent(BicyclePostActivity.this, BicyclesActivity.class);
                            startActivity(myIntent);
                            finish();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadPhotos() {
        Folder = FirebaseStorage.getInstance().getReference().child(owner).child("BikeCollectionImages").child(currentTime.toString());
        if (f != null) {
            StorageReference bikeImageName = Folder.child(f.getName());
            if (contentUri != null) {
                bikeImageName.putFile(contentUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                bikeImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("BikePhotoUrl", uri.toString());
                                        FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("BikeCollection").child(nick_name).updateChildren(hashMap)
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
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

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
        if (requestCode == 1) {
            contentUri = data.getData();
            f = new File(contentUri.getPath());
            bikeImageView.setImageURI(contentUri);
        }
        if (requestCode == 100) {
            f = new File(currentPhotoPath);
            Log.d("tag", "Absolute path URL" + Uri.fromFile(f));
            bikeImageView.setImageURI(Uri.fromFile(f));
            contentUri = Uri.fromFile(f);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}