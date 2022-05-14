package com.example.ciclotm;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ciclotm.Models.PointOfInterestMarker;
import com.example.ciclotm.Models.LiveEventsMarker;
import com.example.ciclotm.Models.Report;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    public static String newReportMarkerUrl;
    private final int REQ_PERMISSION = 5;
    private MapView mapView;
    private GoogleMap map;
    private DatabaseReference reference;
    private ArrayList<Report> reportList = new ArrayList<>();
    private ArrayList<WeightedLatLng> arr = new ArrayList<>();
    ArrayList<String> markers = new ArrayList<>();
    FloatingActionButton reportButton;
    FloatingActionButton liveEventButton;
    FloatingActionButton mapLayerButton;
    FusedLocationProviderClient client;
    private LatLng newMarkerPosition = null;


    private int i = 0;


    TextView t_markerWindow;
    ImageView i_markerWindow;
    int[] colors = {
            Color.rgb(102, 225, 0), // green
            Color.rgb(255, 0, 0)    // red
    };

    float[] startPoints = {
            0.2f, 1f
    };

    Gradient gradient = new Gradient(colors, startPoints);

    HashMap hash_markers = new HashMap();
    HashMap hash_markers2 = new HashMap();
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        reportButton = view.findViewById(R.id.reportFloatingButton);
        liveEventButton = view.findViewById(R.id.liveEventFloatingButton);
        mapLayerButton = view.findViewById(R.id.layersFloatingButton);

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        Location loc = null;
        getCurrentLocation(loc);

        fetchMarkers();

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), StolenBikeLocationActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        liveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newMarkerPosition != null) {
                    DialogFragmentLive myDialogFragment = new DialogFragmentLive();
                    myDialogFragment.show(getActivity().getSupportFragmentManager(), "MyFragment");

                    Bundle args = new Bundle();
                    args.putDouble("markerLat", newMarkerPosition.latitude);
                    args.putDouble("markerLng", newMarkerPosition.longitude);
                    myDialogFragment.putArguments(args);
                } else {
                    Toast.makeText(getContext(), "Trebuie sa confirmatia locatia curenta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mapLayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });


        return view;
    }

    public void getCurrentLocation(Location new_location) {
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                MapsInitializer.initialize(getContext());
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
                                    map.moveCamera(CameraUpdateFactory.newCameraPosition(current_camera_position));
                                }

                                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                    @Override
                                    public boolean onMyLocationButtonClick() {
                                        if (i >= 1) {
                                            getCurrentLocation(location);
                                        }
                                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                                        newMarkerPosition = current;
                                        System.out.println(newMarkerPosition.longitude + "\n" + newMarkerPosition.latitude);
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

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_map_layers);

        final Button furturiLayer = dialog.findViewById(R.id.furturiLayer);
        final Button liveLayer = dialog.findViewById(R.id.liveLayer);
        final Button utileLayer = dialog.findViewById(R.id.utileLayer);
        final Button heatMapLayer = dialog.findViewById(R.id.heatMapLayer);

        furturiLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                reportButton.setVisibility(View.VISIBLE);
                liveEventButton.setVisibility(View.INVISIBLE);
                fetchMarkers();
                dialog.dismiss();
            }
        });

        liveLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                reportButton.setVisibility(View.INVISIBLE);
                liveEventButton.setVisibility(View.VISIBLE);
                fetchLiveEventsMarkers();
                dialog.dismiss();
            }
        });

        utileLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                reportButton.setVisibility(View.VISIBLE);
                liveEventButton.setVisibility(View.INVISIBLE);
                fetchPointOfInterestMarkers();
                dialog.dismiss();
            }
        });

        heatMapLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                reportButton.setVisibility(View.VISIBLE);
                liveEventButton.setVisibility(View.INVISIBLE);

                HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder()
                        .weightedData(arr)
                        .radius(10)
                        .gradient(gradient)
                        .build();
                TileOverlayOptions tileoverlayoptions = new TileOverlayOptions().tileProvider(heatmapTileProvider);
                TileOverlay tileoverlay = map.addTileOverlay(tileoverlayoptions);
                tileoverlay.clearTileCache();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void fetchMarkers() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("furturiPosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report newMarker = snapshot.getValue(Report.class);
                System.out.println(newMarker.getAddress());
                LatLng latLng = new LatLng(newMarker.getTheftMarkerLat(), newMarker.getTheftMarkerLng());
                reportList.add(newMarker);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newMarker.getStolenDate());
                int month = calendar.get(Calendar.MONTH) + 1;
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Furată pe " + calendar.get(Calendar.DAY_OF_MONTH)
                                + "." + month
                                + "." + calendar.get(Calendar.YEAR))
                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_dot)));
                hash_markers.put(marker.getId(), newMarker.getBikeImageUrl());
                hash_markers2.put(marker.getId(), newMarker);
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Nullable
                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.marker_info_window, null);
                        String url = (String) hash_markers.get(marker.getId());
                        TextView t1 = (TextView) v.findViewById(R.id.textView);
                        ImageView i1 = (ImageView) v.findViewById(R.id.markerInfoWindowImageView);


                        if (!url.equals(""))
                            Picasso.get().load(url).resize(500, 500).centerInside().into(i1);
                        if (url.equals(""))
                            Picasso.get().load(newReportMarkerUrl).resize(500, 500).centerInside().into(i1);

                        t1.setText(marker.getTitle());
                        return v;
                    }

                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        return null;
                    }
                });
                map.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                    @Override
                    public void onInfoWindowLongClick(@NonNull Marker marker) {
                        Report clicked_report = (Report) hash_markers2.get(marker.getId());
                        Intent intent = new Intent(getContext(), ExpandedFurturiPostActivity.class);
                        intent.putExtra("clicked_report", clicked_report);
                        getActivity().startActivity(intent);
                    }

                });


                WeightedLatLng weightedLatLng = new WeightedLatLng(latLng, 10);
                arr.add(weightedLatLng);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report updateMarker = snapshot.getValue(Report.class);
                LatLng latLng = new LatLng(updateMarker.getTheftMarkerLat(), updateMarker.getTheftMarkerLng());
                for (Report rep : reportList) {
                    if (rep.getPublishDate().equals(updateMarker.getPublishDate())) {
                        reportList.set(reportList.indexOf(rep), updateMarker);
                        break;
                    }
                }

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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void fetchPointOfInterestMarkers() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("PointsOfInterestMarkers");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PointOfInterestMarker newMarker = snapshot.getValue(PointOfInterestMarker.class);
                LatLng latLng = new LatLng(newMarker.getLat(), newMarker.getLng());
                System.out.println("\n\n" + newMarker.getType());
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Nullable
                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.point_of_interest_marker_info, null);
                        TextView t1 = (TextView) v.findViewById(R.id.textView);
                        t1.setText(marker.getTitle());
                        return v;
                    }

                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        return null;
                    }
                });
                if (String.valueOf(newMarker.getType()).equals("Service")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_service)));
                }
                if (String.valueOf(newMarker.getType()).equals("Magazin")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_store_24)));
                }
                if (String.valueOf(newMarker.getType()).equals("Cafenea")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_coffee_24)));
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

    private void fetchLiveEventsMarkers() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("LiveEventsMarkers");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LiveEventsMarker newMarker = snapshot.getValue(LiveEventsMarker.class);
                LatLng latLng = new LatLng(newMarker.getLat(), newMarker.getLng());

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity())
                .setActionBarTitle("Hărți");
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
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(getContext(), "Raport adăugat", Toast.LENGTH_SHORT).show();

    }
}