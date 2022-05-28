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
import com.example.ciclotm.Models.Location;
import com.example.ciclotm.Models.Photo;
import com.example.ciclotm.Models.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpandedRecordedRouteActivity extends AppCompatActivity {


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

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private StorageReference Folder;
    private String userID;

    ArrayList<ImageView> gallery = new ArrayList<>();
    ArrayList<String> gallery_links = new ArrayList<>();

    int gallery_counter = 0;
    int i = -1;
    int index = 0;
    boolean fetched = false;

    private final int PERMISSION_REQUEST_CODE = 100;
    private Route openedRoute;
    ArrayList<File> fAux = new ArrayList<>();
    String uri;
    ArrayList<Uri> contentUriAux = new ArrayList<>();
    private ArrayList<com.example.ciclotm.Models.Location> expandedRoutePoints = new ArrayList<Location>();

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

        initActionBar();
        getRouteData();
        initFirebaseUser();
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
                    fetched = true;
                    if (i == 2) {
                        addPhotosLayout.setVisibility(View.GONE);
                        imageView4.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reference.child(userID).child("routePosts").child(String.valueOf(openedRoute.getPublishDate())).child("Photos").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                if (photo.getPhotoUrl() != null) {
                    if (fetched == false) {
                        if (i == 2) {
                            addPhotosLayout.setVisibility(View.GONE);
                            imageView4.setVisibility(View.VISIBLE);
                        }
                        System.out.println(photo.getPhotoUrl() + "\n");
                        gallery_links.add(0, photo.getPhotoUrl());
                        i++;
                        Glide.with(getApplicationContext()).load(photo.getPhotoUrl()).into(gallery.get(i));

                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });


    }

    public void initActionBar() {
        getSupportActionBar().setTitle("Tură");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getRouteData() {
        openedRoute = (Route) getIntent().getSerializableExtra("clicked_post");
    }


    public void setRouteData() {
        elapsedTime.setText(openedRoute.getElapsedTime());
        distanceText.setText(String.format("%.2f", openedRoute.getDistance()) + "km");
        avgSpeedText.setText(String.format("%.2f", openedRoute.getAvgSpeed()) + " km/h");
        maxSpeedText.setText(String.format("%.2f", openedRoute.getMaxSpeed()) + " km/h");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(openedRoute.getPublishDate());
        date.setText(output);
    }

    public void initFirebaseUser() {
        userID = openedRoute.getUserId();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName().toString();
                    String lastname = userProfile.getLastName().toString();

                    userNameText.setText(firstname + " " + lastname);
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

        System.out.println(expandedRoutePoints.size());


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                MapsInitializer.initialize(ExpandedRecordedRouteActivity.this);
                map = mMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                LatLng startLatLng = new LatLng(RoutePostsActivity.expandedRoutePoints.get(0).getLatitude(), RoutePostsActivity.expandedRoutePoints.get(0).getLongitude());
                MarkerOptions options = new MarkerOptions().position(startLatLng).icon(bitmapDescriptorFromVector(ExpandedRecordedRouteActivity.this, R.drawable.black_dot_icon_resized));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 16));
                Marker startMarker = mMap.addMarker(options);

                LatLng finishLatLng = new LatLng(RoutePostsActivity.expandedRoutePoints.get(RoutePostsActivity.expandedRoutePoints.size() - 1).getLatitude(), RoutePostsActivity.expandedRoutePoints.get(RoutePostsActivity.expandedRoutePoints.size() - 1).getLongitude());
                options = new MarkerOptions().position(startLatLng).icon(bitmapDescriptorFromVector(ExpandedRecordedRouteActivity.this, R.drawable.black_dot_icon_resized));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 16));
                Marker finishMarker = mMap.addMarker(options);

                for (int y = 1; y < RoutePostsActivity.expandedRoutePoints.size(); y++) {
                    LatLng firstPoint = new LatLng(RoutePostsActivity.expandedRoutePoints.get(y - 1).getLatitude(), RoutePostsActivity.expandedRoutePoints.get(y - 1).getLongitude());
                    LatLng secondPoint = new LatLng(RoutePostsActivity.expandedRoutePoints.get(y).getLatitude(), RoutePostsActivity.expandedRoutePoints.get(y).getLongitude());

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .add(firstPoint)
                            .add(secondPoint)
                            .width(13)
                            .color(getResources().getColor(R.color.green, getResources().newTheme()));

                    Polyline polyline = map.addPolyline(polylineOptions);
                }

            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Uri local_uri = data.getData();
            contentUriAux.add(local_uri);
            File fl = new File(local_uri.getPath());
            fAux.add(fl);
            i++;
            gallery.get(i).setImageURI(local_uri);


        }

    }

    public void uploadPhotos(Uri contentUri, File f) {

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
                                        Photo photo = new Photo(openedRoute.getPublishDate(), uri.toString());
                                        FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(userID).child("routePosts").child(String.valueOf(openedRoute.getPublishDate())).child("Photos").child("Photo" + i).setValue(photo)
                                                .addOnSuccessListener(new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {

                                                    }
                                                });
                                        FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(userID).child("Gallery").child(String.valueOf(openedRoute.getPublishDate()) + i)
                                                .setValue(photo);

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                fetched = true;
                System.out.println(contentUriAux.size());
                for (int k = 0; k < contentUriAux.size(); k++) {
                    uploadPhotos(contentUriAux.get(k), fAux.get(k));
                    Toast.makeText(ExpandedRecordedRouteActivity.this,"Imagine adaugată",Toast.LENGTH_SHORT).show();
                    i++;
                }

        }
        return super.onOptionsItemSelected(item);
    }


    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(ExpandedRecordedRouteActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ExpandedRecordedRouteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ExpandedRecordedRouteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }
        ActivityCompat.requestPermissions(ExpandedRecordedRouteActivity.this, new String[]{
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }


}