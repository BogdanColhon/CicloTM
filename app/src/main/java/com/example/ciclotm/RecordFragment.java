package com.example.ciclotm;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.LiveEventsMarker;
import com.example.ciclotm.Services.TrackingService;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    public static TextView elapsedTime;
    public static TextView distanceText;
    private static TextView speedText;
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
    private static Context context;

    private MapView mapView;
    public static GoogleMap map;
    public static ArrayList<com.example.ciclotm.Models.Location> routePoints = new ArrayList<com.example.ciclotm.Models.Location>();
    public static ArrayList<Marker> routeMarker = new ArrayList<Marker>();
    private static ArrayList<LiveEventsMarker> liveEventsMarker = new ArrayList<LiveEventsMarker>();
    LatLng newMarkerPosition;


    protected static double totalDistance = 0.0;
    public static String time;
    private static double maxSpeed = 0;
    private static double speedSum = 0;
    private static int samples = 1;
    public static com.example.ciclotm.Models.Location point;
    public static Marker startMarker;

    public static Fragment terminator;

    private Dialog dialog;
    private static AlertDialog alertDialog;

    public static BottomNavigationView rfNavBar;

    public static boolean shouldRefreshOnResume = false;
    public static boolean isFirst = true;
    private boolean firstEvent = true;
    static int i = 0;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename parameter arguments, choose names that match
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
        context = getContext();
        terminator = this;
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


        rfNavBar = getActivity().findViewById(R.id.bottomNavigationView);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        alertDialog = new AlertDialog.Builder(getContext()).create();


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE);

                startLinear.setVisibility(View.INVISIBLE);
                finishLinear.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.VISIBLE);
                addLiveEventButton.setVisibility(View.VISIBLE);
                rfNavBar.setVisibility(View.INVISIBLE);
                fetchLiveEventsMarkers();

            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommandToService(Constants.ACTION_PAUSE_SERVICE);
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
                                sendCommandToService(Constants.ACTION_STOP_SERVICE);
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
                newMarkerPosition = new LatLng(routePoints.get(routePoints.size() - 1).getLatitude(), routePoints.get(routePoints.size() - 1).getLongitude());
                if (newMarkerPosition != null) {

                    View view = getLayoutInflater().inflate(R.layout.custom_live_event_dialog_big, null);
                    dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                    dialog.setContentView(view);
                    dialog.setCancelable(true);

                    final ImageView holeButton = dialog.findViewById(R.id.liveDialogHoleImageView);
                    final ImageView glassButton = dialog.findViewById(R.id.liveDialogGlassImageView);
                    final ImageView iceButton = dialog.findViewById(R.id.liveDialogIceImageView);
                    final ImageView roadWorkButton = dialog.findViewById(R.id.liveDialogRoadWorkImageView);
                    final ImageView accidentButton = dialog.findViewById(R.id.liveDialogAccidentImageView);

                    holeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDescriptionDialog(R.drawable.ground_hole_marker_3, "Groapă");
                        }
                    });

                    glassButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDescriptionDialog(R.drawable.broken_bottle, "Cioburi");
                        }
                    });
                    iceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDescriptionDialog(R.drawable.snowflake, "Gheață");
                        }
                    });
                    roadWorkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDescriptionDialog(R.drawable.road_work_marker_5, "Lucrări");
                        }
                    });
                    accidentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDescriptionDialog(R.drawable.accident_marker_2, "Accident");
                        }
                    });

                    dialog.show();
                }
            }
        });


        client = LocationServices.getFusedLocationProviderClient(getContext());
        getStartingLocation();


        return view;

    }


    public static void removeMarker() {
        if (TrackingService.serRouteMarker.get(TrackingService.serRouteMarker.size() - 2) != null) {
            System.out.println(TrackingService.serRouteMarker.get(1));
            TrackingService.serRouteMarker.get(TrackingService.serRouteMarker.size() - 2).remove();
        }
    }

    private void sendCommandToService(String action) {
        Intent intent = new Intent(getActivity(), TrackingService.class);
        intent.setAction(action);
        intent.putExtra("startLat", routePoints.get(0).getLatitude());
        intent.putExtra("startLng", routePoints.get(0).getLongitude());
        getActivity().startService(intent);
    }

    public void openDescriptionDialog(int drawable, String type) {
        View view = getLayoutInflater().inflate(R.layout.custom_live_event_dialog_description, null);
        Dialog dialog2 = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog2.setContentView(view);
        dialog2.setCancelable(true);

        final ImageView icon = dialog2.findViewById(R.id.liveEventDialogDescriptionIconImageView);
        final TextView dialogType = dialog2.findViewById(R.id.liveEventDialogDescriptionTypeImageView);
        final TextView dialogDate = dialog2.findViewById(R.id.liveEventDialogDescriptionDateImageView);
        final EditText dialogDescriptionEditText = dialog2.findViewById(R.id.liveEventDialogDescriptionEditText);
        final Button dialogPostButton = dialog2.findViewById(R.id.liveEventDialogDescriptionPostButton);
        Glide.with(getContext()).load(drawable).into(icon);
        dialogType.setText(type);
        Date currentTime = Calendar.getInstance().getTime();
        Date expiringTime = new Date();
        expiringTime.setTime(System.currentTimeMillis() + (6 * 60 * 60 * 1000));
        dialogDate.setText(String.valueOf(currentTime));
        String dialogDescription = dialogDescriptionEditText.getText().toString().trim();
        dialogPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveEventsMarker marker = new LiveEventsMarker(type, type, dialogDescription, currentTime, expiringTime, newMarkerPosition.latitude, newMarkerPosition.longitude, 0);

                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("LiveEventsMarkers").child(String.valueOf(currentTime))
                        .setValue(marker).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            dialog2.dismiss();
                        }
                    }
                });
            }
        });
        dialog2.show();
    }

    public static void checkProximityLiveEvents(LatLng currentPoint) {
        String text;
        int type = 0;
        for (i = 0; i < liveEventsMarker.size(); i++) {
            if (TrackingService.DistanceCalculation(currentPoint.latitude, currentPoint.longitude, liveEventsMarker.get(i).getLat(), liveEventsMarker.get(i).getLng()) <= 0.05) {
                LiveEventsMarker liveEvent = liveEventsMarker.get(i);
                text = liveEvent.getType() + " în apropiere";
                switch (liveEvent.getType()) {
                    case "Groapă":
                        type = R.drawable.ground_hole_marker_2;
                        break;
                    case "Gheață":
                        type = R.drawable.snowflake_2;
                        break;
                    case "Cioburi":
                        type = R.drawable.broken_bottle_2;
                        break;
                    case "Lucrări":
                        type = R.drawable.road_work_marker_4;
                        break;
                    case "Accident":
                        type = R.drawable.accident_marker_3;
                        break;
                }

                alertDialog.setContentView(R.layout.live_event_dialog);
                ImageView image = alertDialog.findViewById(R.id.liveEventDialogImageView);
                TextView title = alertDialog.findViewById(R.id.liveEventDialogTitle);
                TextView positive = alertDialog.findViewById(R.id.liveEventDialogPositive);
                TextView negative = alertDialog.findViewById(R.id.liveEventDialogNegative);

                title.setText(text);
                Glide.with(context).load(type).into(image);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeExpiringTime(liveEvent);
                        liveEventsMarker.remove(liveEvent);
                        alertDialog.dismiss();
                    }
                });
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateEventStatus(liveEvent);
                        liveEventsMarker.remove(liveEvent);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        }

    }

    public static void changeExpiringTime(LiveEventsMarker liveEvent) {
        Date expiringTime = new Date();
        expiringTime.setTime(System.currentTimeMillis() + (6 * 60 * 60 * 1000));
        DatabaseReference ref = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app/").getReference("LiveEventsMarkers");
        ref.child(String.valueOf(liveEvent.getPublishDate())).child("expiringDate").setValue(expiringTime);
    }

    public static void updateEventStatus(LiveEventsMarker liveEvent) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app/").getReference("LiveEventsMarkers");
        int newValue = liveEvent.getConfirmationCounter() + 1;
        if (newValue == 1) {
            ref.child(String.valueOf(liveEvent.getPublishDate())).child("confirmationCounter").setValue(newValue);
        }
        if (newValue == 2) {
            ref.child(String.valueOf(liveEvent.getPublishDate())).removeValue();
        }

    }


    public static void setTimer(int hours, int minutes, int secs) {
        time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
        elapsedTime.setText(time);


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
                                point = new com.example.ciclotm.Models.Location();
                                point.setLatitude(latLng.latitude);
                                point.setLongitude(latLng.longitude);
                                routePoints.add(point);
                                MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptorFromVector(getContext(), R.drawable.black_dot_icon_resized));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                startMarker = mMap.addMarker(options);
                                routeMarker.add(startMarker);

                            }
                        });
                    }
                }
            });
        } else {
            askPermission();
        }
    }

    public static void setData(double rfDistance, double rfSpeed, int rfSamples, com.example.ciclotm.Models.Location rfPoint, Marker rfMarker, PolylineOptions rfPolylineOptions) {

        distanceText.setText(String.format("%.2f km", rfDistance));
        speedText.setText(String.format("%.2f km/h", rfSpeed));
        totalDistance = rfDistance;
        if (rfSpeed > maxSpeed) {
            maxSpeed = rfSpeed;
        }
        speedSum = speedSum + rfSpeed;
        samples = rfSamples;

        routePoints.add(rfPoint);
        routeMarker.add(rfMarker);


        Polyline polyline = map.addPolyline(rfPolylineOptions);

    }


    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

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
                if (newMarker != null) {
                    LatLng latLng = new LatLng(newMarker.getLat(), newMarker.getLng());

                    Date currentTime = Calendar.getInstance().getTime();
                    if (newMarker.getExpiringDate().after(currentTime)) {
                        liveEventsMarker.add(newMarker);

                        if (String.valueOf(newMarker.getType()).equals("Groapă")) {
                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(newMarker.getTitle())
                                    .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ground_hole_marker_2)));
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
                                    .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.broken_bottle_2)));
                        }
                        if (String.valueOf(newMarker.getType()).equals("Lucrări")) {
                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(newMarker.getTitle())
                                    .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.road_work_marker_4)));
                        }
                        if (String.valueOf(newMarker.getType()).equals("Accident")) {
                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(newMarker.getTitle())
                                    .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.accident_marker_3)));
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
                }

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
        System.out.println("start--------------------------------");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity())
                .setActionBarTitle("Înregistrează");
        mapView.onResume();
        if (shouldRefreshOnResume == true) {
            resetFragment();
            if (isFirst == false) {
                client = LocationServices.getFusedLocationProviderClient(getContext());
                getStartingLocation();
            }

        }
        System.out.println("resume--------------------------------");
    }

    private void resetFragment() {
        map.clear();
        rfNavBar.setVisibility(View.VISIBLE);
        startLinear.setVisibility(View.VISIBLE);
        finishLinear.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.INVISIBLE);
        addLiveEventButton.setVisibility(View.INVISIBLE);
        routeMarker = new ArrayList<Marker>();
        routePoints = new ArrayList<com.example.ciclotm.Models.Location>();
        totalDistance = 0.0;
        distanceText.setText("0.0 km");
        time = "0:00:00";
        elapsedTime.setText("0:00:00");
        speedText.setText("0.0 km/h");
        speedSum = 0;
        samples = 1;
        TrackingService.isFirstRun = true;
        TrackingService.serviceKilled = true;
        shouldRefreshOnResume = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("detach--------------------------------");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        System.out.println("pause--------------------------------");
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        System.out.println("stop--------------------------------");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
        System.out.println("memory--------------------------------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        sendCommandToService(Constants.ACTION_STOP_SERVICE);
        System.out.println("destroy--------------------------------");
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
                Constants.REQ_PERMISSION_LOCATION
        );
    }

}


