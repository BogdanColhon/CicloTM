package com.example.ciclotm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ReportFragment;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    private MapView mapView;
    private GoogleMap map;

    private double distance;
    private String time;
    private double avgSpeed;
    private double maxSpeed;

    private FirebaseUser user;
    private DatabaseReference reference;
    private StorageReference Folder;
    private String userID;
    private Date today;

    String uri;

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
        mapView = findViewById(R.id.routeMapView);
        mapView.onCreate(savedInstanceState);

        initActionBar();
        initFirebaseUser();
        getRouteData();
        setRouteData();
        setMapLayout();


    }

    public void initActionBar(){
        getSupportActionBar().setTitle("Tură");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getRouteData(){
        distance = (double) getIntent().getSerializableExtra("distanceRecorded");
        time = (String) getIntent().getSerializableExtra("timeRecorded");
        avgSpeed = (double) getIntent().getSerializableExtra("avgSpeedRecorded");
        maxSpeed = (double) getIntent().getSerializableExtra("maxSpeedRecorded");
    }

    public void setRouteData(){
        elapsedTime.setText(time);
        distanceText.setText(String.format("%.2f", distance) + " km");
        avgSpeedText.setText(String.format("%.2f", avgSpeed) + " km/h");
        maxSpeedText.setText(String.format("%.2f", maxSpeed) + " km/h");
    }

    public void initFirebaseUser(){
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
                    today = Calendar.getInstance().getTime();

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
            Picasso.get().load(profileImageUrl).fit().centerInside().into(userImage);
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
                MarkerOptions options = new MarkerOptions().position(startLatLng).icon(bitmapDescriptorFromVector(RecordedRouteActivity.this, R.drawable.black_dot_icon_resized));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 16));
                Marker startMarker = mMap.addMarker(options);

                LatLng finishLatLng = new LatLng(RecordFragment.routePoints.get(RecordFragment.routePoints.size() - 1).getLatitude(), RecordFragment.routePoints.get(RecordFragment.routePoints.size() - 1).getLongitude());
                options = new MarkerOptions().position(startLatLng).icon(bitmapDescriptorFromVector(RecordedRouteActivity.this, R.drawable.black_dot_icon_resized));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 16));
                Marker finishMarker = mMap.addMarker(options);

                for (int i = 1; i < RecordFragment.routePoints.size(); i++) {
                    LatLng firstPoint = new LatLng(RecordFragment.routePoints.get(i - 1).getLatitude(), RecordFragment.routePoints.get(i - 1).getLongitude());
                    LatLng secondPoint = new LatLng(RecordFragment.routePoints.get(i).getLatitude(), RecordFragment.routePoints.get(i).getLongitude());

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .add(firstPoint)
                            .add(secondPoint)
                            .width(13)
                            .color(getResources().getColor(R.color.green, getResources().newTheme()));

                    Polyline polyline = map.addPolyline(polylineOptions);
                }

                map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        snapShot();
                    }
                });


            }
        });
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
                StorageReference fileRef = Folder.child("route_"+timeStamp+".jpg");

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
                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(userID).child("routePosts").child(String.valueOf(today))
                        .setValue(route).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RecordedRouteActivity.this, "Tură adăugată", Toast.LENGTH_SHORT).show();
                        RecordFragment.shouldRefreshOnResume = true;
                        RecordFragment.isFirst = false;
                        finish();

                    }

                });


        }
        return super.onOptionsItemSelected(item);
    }


}