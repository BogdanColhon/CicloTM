package com.example.ciclotm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ciclotm.Models.LiveEventsMarker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    private int LOCATION_REFRESH_TIME = 5000; // 5 seconds to update
    private int LOCATION_REFRESH_DISTANCE = 3; // 3 meters to update
    static final Double EARTH_RADIUS = 6371.00;
    private final int REQ_PERMISSION = 5;

    private TextView elapsedTime;
    private TextView distanceText;
    private TextView speedText;
    private ImageButton startButton;
    private ImageButton finishButton;
    private ImageButton resumeButton;
    private ImageButton stopButton;
    private FloatingActionButton addLiveEventButton;
    private LinearLayout startLinear;
    private LinearLayout finishLinear;

    private LocationListener locationListener = null;
    private LocationManager locationManager;
    private FusedLocationProviderClient client;
    private DatabaseReference reference;

    private MapView mapView;
    private GoogleMap map;
    protected static ArrayList<Location> routePoints = new ArrayList<Location>();
    private static ArrayList<Marker> routeMarker = new ArrayList<Marker>();
    private ArrayList<LiveEventsMarker> liveEventsMarker = new ArrayList<LiveEventsMarker>();


    protected static double totalDistance = 0.0;
    private int seconds;
    private boolean running;
    private String time;
    private double speed;
    private double maxSpeed = 0;
    private double avgSpeed;
    private double speedSum = 0;
    private int samples = 1;

    private AlertDialog alertDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);

        elapsedTime = view.findViewById(R.id.recordElapsedTimeTextView);
        distanceText = view.findViewById(R.id.distanceTV);
        speedText = view.findViewById(R.id.speedTV);
        startButton = view.findViewById(R.id.recordStartImageButton);
        finishButton = view.findViewById(R.id.recordFinishImageButton);
        resumeButton = view.findViewById(R.id.recordResumeImageButton);
        stopButton = view.findViewById(R.id.recordStopImageButton);
        startLinear = view.findViewById(R.id.startLinearLayout);
        finishLinear = view.findViewById(R.id.finishLinearLayout);
        addLiveEventButton = view.findViewById(R.id.liveEventAddFloatingButton);
        mapView = view.findViewById(R.id.recordMapView);
        mapView.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        alertDialog = new AlertDialog.Builder(getContext()).create();

        runTimer();


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                running = true;

                startLinear.setVisibility(View.INVISIBLE);
                finishLinear.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.VISIBLE);
                addLiveEventButton.setVisibility(View.VISIBLE);

                fetchLiveEventsMarkers();

                locationListener = new MyLocationListener();
                if (checkPermission())
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
                if (checkPermission())
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                locationManager.removeUpdates(locationListener);
                for (int i = 0; i < routePoints.size(); i++) {
                    System.out.println(routePoints.get(i));
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("Sigur vrei să închei înregistrarea?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Da",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(), RecordedRouteActivity.class);
                                intent.putExtra("distanceRecorded", totalDistance);
                                intent.putExtra("timeRecorded", time);
                                intent.putExtra("avgSpeedRecorded", speedSum / (double) samples);
                                intent.putExtra("maxSpeedRecorded", maxSpeed);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        addLiveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng newMarkerPosition = new LatLng(routePoints.get(routePoints.size() - 1).getLatitude(), routePoints.get(routePoints.size() - 1).getLongitude());
                if (newMarkerPosition != null) {
                    DialogFragmentLive myDialogFragment = new DialogFragmentLive();
                    myDialogFragment.show(getActivity().getSupportFragmentManager(), "MyFragment");

                    Bundle args = new Bundle();
                    args.putDouble("markerLat", newMarkerPosition.latitude);
                    args.putDouble("markerLng", newMarkerPosition.longitude);
                    myDialogFragment.putArguments(args);
                }
            }
        });

        client = LocationServices.getFusedLocationProviderClient(getContext());
        getStartingLocation();
        return view;

    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            setMarker(loc);

            samples++;

            if (routeMarker.get(routeMarker.size() - 2) != null) {
                System.out.println(routeMarker.get(1));
                routeMarker.get(routeMarker.size() - 2).remove();
            }

            if (routePoints.get(routePoints.size() - 2) != null) {
                LatLng firstPoint = new LatLng(routePoints.get(routePoints.size() - 2).getLatitude(), routePoints.get(routePoints.size() - 2).getLongitude());
                LatLng secondPoint = new LatLng(routePoints.get(routePoints.size() - 1).getLatitude(), routePoints.get(routePoints.size() - 1).getLongitude());

                double pointsDistance = DistanceCalculation(firstPoint.latitude, firstPoint.longitude, secondPoint.latitude, secondPoint.longitude);
                totalDistance = totalDistance + pointsDistance;
                speed = pointsDistance / (double) (5.0 / 3600.0);
                if (speed > maxSpeed) {
                    maxSpeed = speed;
                }
                speedSum = speedSum + speed;

                distanceText.setText(String.format("%.2f", totalDistance));
                speedText.setText(String.format("%.2f", speed));


                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(firstPoint)
                        .add(secondPoint)
                        .width(13)
                        .color(getResources().getColor(R.color.green, getResources().newTheme()));

                Polyline polyline = map.addPolyline(polylineOptions);

                checkProximityLiveEvents(secondPoint);
            }


        }
    }

    public void checkProximityLiveEvents(LatLng currentPoint) {
        for (int i = 0; i < liveEventsMarker.size(); i++) {

            if (DistanceCalculation(currentPoint.latitude, currentPoint.longitude, liveEventsMarker.get(i).getLat(), liveEventsMarker.get(i).getLng()) <= 0.01) {
                if (!alertDialog.isShowing()) {
                    alertDialog.setMessage("Confirmi evenimentul?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Da",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    //Toast.makeText(getContext(),"Eveniment in apropiere",Toast.LENGTH_SHORT).show();

                    // Hide after some seconds
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                        }
                    };

                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            handler.removeCallbacks(runnable);
                        }
                    });

                    handler.postDelayed(runnable, 15000);
                }
            }
        }
    }

    public double DistanceCalculation(double lat1, double lon1, double lat2, double lon2) {
        double Radius = EARTH_RADIUS;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }


    private void runTimer() {
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                elapsedTime.setText(time);

                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }


    private void getStartingLocation() {
        if (checkPermission()) {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            MapsInitializer.initialize(getActivity().getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap mMap) {
                                MapsInitializer.initialize(getContext());
                                map = mMap;
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                Location point = new Location("point");
                                point.setLatitude(latLng.latitude);
                                point.setLongitude(latLng.longitude);
                                routePoints.add(point);
                                MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptorFromVector(getContext(), R.drawable.black_dot_icon_resized));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                Marker marker = mMap.addMarker(options);
                                routeMarker.add(marker);

                            }
                        });
                    }
                    ;
                }
            });
        } else {
            askPermission();
        }
    }

    public void setMarker(Location location) {


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Location point = new Location("point");
        point.setLatitude(latLng.latitude);
        point.setLongitude(latLng.longitude);
        routePoints.add(point);
        MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptorFromVector(getContext(), R.drawable.black_dot_icon_resized));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        Marker marker = map.addMarker(options);
        routeMarker.add(marker);


    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void fetchLiveEventsMarkers() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("LiveEventsMarkers");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LiveEventsMarker newMarker = snapshot.getValue(LiveEventsMarker.class);
                LatLng latLng = new LatLng(newMarker.getLat(), newMarker.getLng());
                liveEventsMarker.add(newMarker);

                if (String.valueOf(newMarker.getType()).equals("Groapă")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.broken_glass_5)));
                }
                if (String.valueOf(newMarker.getType()).equals("Gheață")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.snowflake_2)));
                }
                if (String.valueOf(newMarker.getType()).equals("Cioburi")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.broken_glass_5)));
                }
                if (String.valueOf(newMarker.getType()).equals("Lucrări")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_coffee_24)));
                }
                if (String.valueOf(newMarker.getType()).equals("Accident")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_coffee_24)));
                }

                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Nullable
                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.live_events_marker_info, null);
                        TextView t1 = (TextView) v.findViewById(R.id.liveEventsMarkerInfoTitleTextView);
                        TextView t2 = (TextView) v.findViewById(R.id.liveEventsMarkerInfoTimeTextView);
                        TextView t3 = (TextView) v.findViewById(R.id.liveEventsMarkerContentTextView);
                        t1.setText(newMarker.getTitle());
                        t2.setText(newMarker.getPublishDate().toString());
                        t3.setText(newMarker.getDescription());
                        return v;
                    }

                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        return null;
                    }
                });

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity())
                .setActionBarTitle("Înregistrează");
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

    // Check for permission to access Location
    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }


}