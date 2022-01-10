package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ciclotm.Models.MapMarker;
import com.example.ciclotm.Models.Report;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Repo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportStolenBikeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    File f, f2;
    Uri contentUri, contentUri2;
    Uri locationCaptureImageURI;
    Uri stolenCaptureImageURI;
    Bitmap captureImageBitmap;
    TextView dateReportTextView;
    TextView addressReportTextView;
    EditText brandReportEditText;
    EditText colorReportEditText;
    EditText bikeDescriptionEditText;
    EditText thiefDescriptionEditText;
    ImageView locationReportImageView;
    ImageView stolenBikeReportImageView;
    Spinner modelReportSpinner;
    FloatingActionButton sendReportButton;
    LatLng theftMarker;

    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference Folder;
    private final int PERMISSION_REQUEST_CODE = 100;
    Date currentTime;
    String user_Id;
    String bikeModel;
    Calendar calendar = Calendar.getInstance();
    String currentPhotoPath;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_stolen_bike);
        modelReportSpinner = (Spinner) findViewById(R.id.modelReportSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportStolenBikeActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.bike_models));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelReportSpinner.setAdapter(adapter);
        modelReportSpinner.setOnItemSelectedListener(this);

        sendReportButton = (FloatingActionButton) findViewById(R.id.sendReportButton);

        locationReportImageView = (ImageView) findViewById(R.id.locationReportImageView);
        stolenBikeReportImageView = (ImageView) findViewById(R.id.stolenBikeImageView);

        dateReportTextView = (TextView) findViewById(R.id.dateReportTextView);
        brandReportEditText = (EditText) findViewById(R.id.brandReportEditText);
        colorReportEditText = (EditText) findViewById(R.id.colorReportEditText);
        addressReportTextView = (TextView) findViewById(R.id.addressReportTextView);
        bikeDescriptionEditText = (EditText) findViewById(R.id.bikeDescriptionReportEditText);
        thiefDescriptionEditText = (EditText) findViewById(R.id.thiefDescriptionReportEditText);

        currentTime = Calendar.getInstance().getTime();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        user_Id = user.getUid();

        Intent intent = getIntent();
        String full_address = intent.getStringExtra("location");
        theftMarker = intent.getExtras().getParcelable("newTheftMarker");
        addressReportTextView.setText(full_address);

        locationReportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkCameraPermission();
                    dispatchTakePictureIntent(100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stolenBikeReportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkCameraPermission();
                    dispatchTakePictureIntent(101);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        dateReportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(ReportStolenBikeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateReportTextView.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                calendar.set(year, monthOfYear, dayOfMonth);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dpd.show();


            }
        });

        sendReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressReportTextView.getText().toString().trim();
                String bike_brand = brandReportEditText.getText().toString().trim();
                String bike_color = colorReportEditText.getText().toString().trim();
                String bike_model = bikeModel;
                String bike_description = bikeDescriptionEditText.getText().toString().trim();
                String thief_description = thiefDescriptionEditText.getText().toString().trim();
                String user_id = user_Id;
                Date postDate = currentTime;
                Date date_of_theft = calendar.getTime();


                if (address.isEmpty()) {
                    addressReportTextView.setError("Câmp obligatoriu!");
                    addressReportTextView.requestFocus();
                    return;
                }
                Double theftMarkerLat = theftMarker.latitude;
                Double theftMarkerLng = theftMarker.longitude;

                if (bike_color.isEmpty()) {
                    colorReportEditText.setError("Câmp obligatoriu!");
                    colorReportEditText.requestFocus();
                    return;
                }

                if (bikeModel == "model") {
                    bike_model = "-";
                }
                uploadPhotos();
                Report report = new Report(postDate, date_of_theft, address, user_id, bike_brand, bike_model, bike_color, bike_description, thief_description, theftMarkerLat, theftMarkerLng);
                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("furturiPosts").child(String.valueOf(postDate))
                        .setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ReportStolenBikeActivity.this, "Raport adaugat", Toast.LENGTH_SHORT).show();
                            StolenBikeLocationActivity.terminator.finish();
                            MenuActivity.terminator.finish();
                            Intent myIntent = new Intent(ReportStolenBikeActivity.this, MenuActivity.class);
                            ReportStolenBikeActivity.this.startActivity(myIntent);
                            finish();
                        }
                    }
                });
                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(user_id).child("Furturi").child(String.valueOf(postDate))
                        .setValue(report);
                MapMarker marker = new MapMarker(theftMarkerLat, theftMarkerLng, date_of_theft);
                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("StolenBikesMarkers").child("Coordonate").child(String.valueOf(postDate))
                        .setValue(marker);

            }
        });

    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(ReportStolenBikeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ReportStolenBikeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ReportStolenBikeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }
        ActivityCompat.requestPermissions(ReportStolenBikeActivity.this, new String[]{
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }

    public void uploadPhotos() {
        Folder = FirebaseStorage.getInstance().getReference().child(user_Id).child("ReportImages").child(currentTime.toString());
        StorageReference locationImageName = Folder.child(f.getName());
        if (contentUri != null) {
            locationImageName.putFile(contentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        StorageReference bikeImageName = Folder.child(f2.getName());
        if (contentUri2 != null) {
            bikeImageName.putFile(contentUri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bikeModel = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            f = new File(currentPhotoPath);
            Log.d("tag", "Absolute path URL" + Uri.fromFile(f));
            locationReportImageView.setImageURI(Uri.fromFile(f));
            contentUri = Uri.fromFile(f);
        }
        if (requestCode == 101) {
            System.out.println(data);

            f2 = new File(currentPhotoPath);
            stolenBikeReportImageView.setImageURI(Uri.fromFile(f2));
            contentUri2 = Uri.fromFile(f2);
        }

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
}