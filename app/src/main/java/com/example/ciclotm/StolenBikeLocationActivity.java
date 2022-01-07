package com.example.ciclotm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StolenBikeLocationActivity extends AppCompatActivity {

    private final int REQ_PERMISSION = 5;
    public static Activity terminator;
    private MapView mapView;
    private GoogleMap map;
    private int i = 0;
    private LatLng newTheftMarker;
    FusedLocationProviderClient client;
    TextView searchPlaces;
    String full_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stolen_bike_location);
        mapView = findViewById(R.id.mapViewStolenBike);
        mapView.onCreate(savedInstanceState);
        terminator=this;
        FloatingActionButton checkButton = findViewById(R.id.checkFloatingButton);

        searchPlaces = (TextView) findViewById(R.id.stolenBikeLocationTextView);
        Places.initialize(StolenBikeLocationActivity.this,getResources().getString(R.string.google_maps_api_key));
        searchPlaces.setFocusable(false);
        searchPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        ,fieldList).build(StolenBikeLocationActivity.this);
                startActivityForResult(intent,200);
            }
        });


        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);
        Location loc = null;
        getCurrentLocation(loc);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode !=0) {
            if(data != null) {
                System.out.println(data);
                Place place = Autocomplete.getPlaceFromIntent(data);
                searchPlaces.setText(place.getAddress());
                full_address = place.getAddress();
                newTheftMarker=place.getLatLng();
            }
        }
    }

    public void startReportStolenBikeActivity(View v) {
        Intent myIntent = new Intent(StolenBikeLocationActivity.this, ReportStolenBikeActivity.class);
        myIntent.putExtra("location",full_address);
        myIntent.putExtra("newTheftMarker",newTheftMarker);
        StolenBikeLocationActivity.this.startActivity(myIntent);
    }


    public void getCurrentLocation(Location new_location) {
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    if (location != new_location) {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap mMap) {
                                MapsInitializer.initialize(StolenBikeLocationActivity.this);
                                map = mMap;
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                                if (checkPermission()) {
                                    mMap.setMyLocationEnabled(true);
                                } else
                                    askPermission();

                                LatLng city_center = new LatLng(45.75804742621145, 21.228941951330423);
                                CameraPosition city = CameraPosition.builder().target(city_center).zoom(16).build();
                                map.moveCamera(CameraUpdateFactory.newCameraPosition(city));

                                if (i >= 1) {
                                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                                    CameraPosition current_camera_position = CameraPosition.builder().target(current).zoom(16).build();
                                    convertCoordinatesToAddress(location);
                                    searchPlaces.setText(full_address);
                                    map.moveCamera(CameraUpdateFactory.newCameraPosition(current_camera_position));
                                }

                                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                    @Override
                                    public boolean onMyLocationButtonClick() {
                                        if (i >= 1) {
                                            getCurrentLocation(location);
                                        }
                                        convertCoordinatesToAddress(location);
                                        searchPlaces.setText(full_address);
                                        i++;

                                        return false;

                                    }

                                });

                            }
                        });
                    }
                }
            }
        });
    }

    private void convertCoordinatesToAddress(Location location) {
        try {
            Geocoder geo = new Geocoder(StolenBikeLocationActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                full_address = address;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Check for permission to access Location
    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(StolenBikeLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
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
}