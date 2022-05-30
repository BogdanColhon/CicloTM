package com.example.ciclotm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Objects.Location;
import com.example.ciclotm.Models.Objects.Photo;
import com.example.ciclotm.Models.Objects.Route;
import com.example.ciclotm.Models.Users.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecordedRouteActivity extends AppCompatActivity {

    private CircleImageView userImage;

    private TextView userNameText;
    private TextView date;
    private TextView elapsedTime;
    private TextView distanceText;
    private TextView maxSpeedText;
    private TextView avgSpeedText;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private LinearLayout addPhotosLayout;

    private MapView mapView;
    private GoogleMap map;

    private double distance;
    private String time;
    private double avgSpeed;
    private double maxSpeed;
    private String postTime;

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private StorageReference Folder;
    private String userID;
    private Date today;
    ArrayList<ImageView> gallery = new ArrayList<>();
    int gallery_counter=0;
    int i = 0;
    int index= 0;
    double maxDistance;
    LatLng maxPoint;

    private final int PERMISSION_REQUEST_CODE = 100;

    ArrayList<File> f = new ArrayList<>();
    String uri;
    ArrayList<Uri> contentUri= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_route);

        userImage = findViewById(R.id.routeUserImageView);
        userNameText = findViewById(R.id.routeUpperTextView);
        date = findViewById(R.id.routeLowerTextView);
        elapsedTime = findViewById(R.id.routeElapsedTimeTextView);
        distanceText = findViewById(R.id.routeDistanceTextView);
        maxSpeedText = findViewById(R.id.routeMaxSpeedTextView);
        avgSpeedText = findViewById(R.id.routeAvgSpeedTextView);
        imageView1 = findViewById(R.id.recordedRouteImageView1);
        imageView2 = findViewById(R.id.recordedRouteImageView2);
        imageView3 = findViewById(R.id.recordedRouteImageView3);
        imageView4 = findViewById(R.id.recordedRouteImageView4);
        addPhotosLayout = findViewById(R.id.recordedRouteAddPhotosLayout);
        mapView = findViewById(R.id.routeMapView);
        mapView.onCreate(savedInstanceState);
        today = Calendar.getInstance().getTime();

        initActionBar();
        initFirebaseUser();
        getRouteData();
        setRouteData();
        setMapLayout();

        gallery.add(imageView1);
        gallery.add(imageView2);
        gallery.add(imageView3);
        gallery.add(imageView4);

        addPhotosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkCameraPermission();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 2);
                    if(gallery_counter==3)
                    {
                        addPhotosLayout.setVisibility(View.GONE);
                        imageView4.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void initActionBar() {
        getSupportActionBar().setTitle("Tură");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getRouteData() {
        distance = (double) getIntent().getSerializableExtra("distanceRecorded");
        time = (String) getIntent().getSerializableExtra("timeRecorded");
        avgSpeed = (double) getIntent().getSerializableExtra("avgSpeedRecorded");
        maxSpeed = (double) getIntent().getSerializableExtra("maxSpeedRecorded");
    }

    public void setRouteData() {
        elapsedTime.setText(time);
        distanceText.setText(String.format("%.2f", distance) + " km");
        avgSpeedText.setText(String.format("%.2f", avgSpeed) + " km/h");
        maxSpeedText.setText(String.format("%.2f", maxSpeed) + " km/h");
    }

    public void initFirebaseUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName().toString();
                    String lastname = userProfile.getLastName().toString();

                    userNameText.setText(firstname + " " + lastname);

                    SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
                    String output = df.format(today);
                    date.setText(output);

                    try {
                        getUserProfilePhoto(userProfile.getProfileImageUrl());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getUserProfilePhoto(String profileImageUrl) throws IOException {
        if (!profileImageUrl.equals("")) {
            Glide.with(this).load(profileImageUrl).into(userImage);
        }
    }

    private void setMapLayout() {

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                MapsInitializer.initialize(RecordedRouteActivity.this);
                map = mMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                LatLng startLatLng = new LatLng(RecordFragment.routePoints.get(0).getLatitude(), RecordFragment.routePoints.get(0).getLongitude());
                Location startPoint = new Location();
                startPoint.setLatitude(startLatLng.latitude);
                startPoint.setLongitude(startLatLng.longitude);
                maxPoint = startLatLng;


                for (int i = 1; i < RecordFragment.routePoints.size(); i++) {
                    LatLng firstPoint = new LatLng(RecordFragment.routePoints.get(i - 1).getLatitude(), RecordFragment.routePoints.get(i - 1).getLongitude());
                    LatLng secondPoint = new LatLng(RecordFragment.routePoints.get(i).getLatitude(), RecordFragment.routePoints.get(i).getLongitude());

                    Location currentPoint = new Location();
                    currentPoint.setLatitude(secondPoint.latitude);
                    currentPoint.setLongitude(secondPoint.longitude);

                    double currentDistance =DistanceCalculation(startLatLng.latitude, startLatLng.longitude, secondPoint.latitude,secondPoint.longitude);
                    if(currentDistance > maxDistance)
                    {
                        maxDistance = currentDistance;
                        maxPoint = secondPoint;
                    }

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .add(firstPoint)
                            .add(secondPoint)
                            .width(13)
                            .color(getResources().getColor(R.color.green, getResources().newTheme()));

                    Polyline polyline = map.addPolyline(polylineOptions);
                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( LatLngBounds.builder().include(startLatLng).include(maxPoint).build().getCenter(), 14));

                map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        snapShot();
                    }
                });


            }
        });
    }
    private double DistanceCalculation(double lat1, double lon1, double lat2, double lon2) {
        double Radius = Constants.EARTH_RADIUS;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }


    public void snapShot() {

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] data = stream.toByteArray();
                Folder = FirebaseStorage.getInstance().getReference().child(userID).child("RouteImages").child(today.toString());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                StorageReference fileRef = Folder.child("route_" + timeStamp + ".jpg");

                Task<Uri> urlTask = fileRef.putBytes(data).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        uri = downloadUri.toString();

                    } else {
                        // Handle failures
                        // ...
                    }

                });

            }
        };
        map.snapshot(callback);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_route, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:


                Route route = new Route(today, userID, distance, time, avgSpeed, maxSpeed, uri);
                postTime = String.valueOf(today);
                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(userID).child("routePosts").child(postTime)
                        .setValue(route).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        for (i = 0; i < contentUri.size(); i++) {
                            uploadPhotos(contentUri.get(i),f.get(i));
                        }
                        Toast.makeText(RecordedRouteActivity.this, "Tură adăugată", Toast.LENGTH_SHORT).show();
                        RecordFragment.shouldRefreshOnResume = true;
                        RecordFragment.isFirst = false;
                        finish();

                    }

                });
                for( int j = 0 ;j < RecordFragment.routePoints.size();j++)
                {
                    FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(userID).child("routePosts").child(postTime).child("coordinates").child("point"+ String.valueOf(System.currentTimeMillis())).setValue(RecordFragment.routePoints.get(j));
                }


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Uri local_uri = data.getData();
            contentUri.add(local_uri);
            File fl = new File(local_uri.getPath());
            f.add(fl);
            gallery.get(gallery_counter).setImageURI(local_uri);
            gallery_counter++;
        }

    }

    public void uploadPhotos(Uri contentUri,File f) {

            Folder = FirebaseStorage.getInstance().getReference().child(userID).child("RoutesImages");
            if (f != null) {
                StorageReference routePhotosImageName = Folder.child(f.getName());
                if (contentUri != null) {
                    routePhotosImageName.putFile(contentUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    routePhotosImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Photo photo = new Photo(today, uri.toString());
                                            FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(userID).child("routePosts").child(postTime).child("Photos").child("Photo" + index).setValue(photo)
                                                    .addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {

                                                        }
                                                    });
                                            FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(userID).child("Gallery").child(String.valueOf(-1*(today.getTime())))
                                                    .setValue(photo);
    index++;
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


    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(RecordedRouteActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(RecordedRouteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(RecordedRouteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }
        ActivityCompat.requestPermissions(RecordedRouteActivity.this, new String[]{
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }


}