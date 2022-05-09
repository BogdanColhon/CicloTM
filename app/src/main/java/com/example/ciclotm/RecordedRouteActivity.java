package com.example.ciclotm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

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

   // private static ArrayList<Location> routePoints = new ArrayList<Location>();

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

        distance = (double) getIntent().getSerializableExtra("distanceRecorded");
        time = (String) getIntent().getSerializableExtra("timeRecorded");
        avgSpeed = (double) getIntent().getSerializableExtra("avgSpeedRecorded");

        elapsedTime.setText(time);
        distanceText.setText(String.format("%.2f", distance) + " km");
        avgSpeedText.setText(String.format("%.2f", avgSpeed) + " km/h");

        setMapLayout();
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

                LatLng finishLatLng = new LatLng(RecordFragment.routePoints.get(RecordFragment.routePoints.size()-1).getLatitude(), RecordFragment.routePoints.get(RecordFragment.routePoints.size()-1).getLongitude());
                options = new MarkerOptions().position(startLatLng).icon(bitmapDescriptorFromVector(RecordedRouteActivity.this, R.drawable.black_dot_icon_resized));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 16));
                Marker finishMarker = mMap.addMarker(options);

                for(int i = 1 ; i < RecordFragment.routePoints.size() ; i++)
                {
                    LatLng firstPoint = new LatLng(RecordFragment.routePoints.get(i-1).getLatitude(),RecordFragment.routePoints.get(i-1).getLongitude());
                    LatLng secondPoint = new LatLng(RecordFragment.routePoints.get(i).getLatitude(),RecordFragment.routePoints.get(i).getLongitude());

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

}