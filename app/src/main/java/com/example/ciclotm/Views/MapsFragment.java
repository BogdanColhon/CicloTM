package com.example.ciclotm.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Constants;
import com.example.ciclotm.Models.Markers.LiveEventsMarker;
import com.example.ciclotm.Models.Markers.PointOfInterestMarker;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.R;
import com.example.ciclotm.StolenBikeLocationActivity;
import com.example.ciclotm.ViewModels.MapsFragmentViewModel;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class MapsFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

    private MapView mapView;
    private GoogleMap map;
    private ArrayList<WeightedLatLng> arr = new ArrayList<>();

    private FloatingActionButton reportButton;
    private FloatingActionButton liveEventButton;
    private FloatingActionButton mapLayerButton;

    public static String newReportMarkerUrl;
    private LatLng newMarkerPosition = null;
    private Location city_center;
    private int i = 0;
    private MapsFragmentViewModel mMapsFragmentViewModel;

    int[] colors = {
            Color.rgb(102, 225, 0), // green
            Color.rgb(255, 0, 0)    // red
    };

    float[] startPoints = {
            0.2f, 1f
    };

    Gradient gradient = new Gradient(colors, startPoints);

    HashMap hash_markers = new HashMap();


    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        init(view, savedInstanceState);
        getCurrentLocation(city_center, null);

        mMapsFragmentViewModel = ViewModelProviders.of(this).get(MapsFragmentViewModel.class);
        mMapsFragmentViewModel.init();
        fetchFurturiMarkers();

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
                    Toast.makeText(getContext(), "Trebuie să confirmația locația curentă!", Toast.LENGTH_SHORT).show();
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


    private void init(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        reportButton = view.findViewById(R.id.reportFloatingButton);
        liveEventButton = view.findViewById(R.id.liveEventFloatingButton);
        mapLayerButton = view.findViewById(R.id.layersFloatingButton);

        city_center = new Location("provider");
        city_center.setLatitude(45.75804742621145);
        city_center.setLongitude(21.228941951330423);
    }

    public void getCurrentLocation(Location location, Location new_location) {
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

                        LatLng current_position = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraPosition current_camera_position = CameraPosition.builder().target(current_position).zoom(16).build();
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(current_camera_position));


                        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                                @SuppressLint("MissingPermission") Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (i >= 1) {
                                    getCurrentLocation(new_location, lastLocation);
                                }
                                LatLng current = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
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
                fetchFurturiMarkers();
                dialog.dismiss();
            }
        });

        liveLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                reportButton.setVisibility(View.INVISIBLE);
                liveEventButton.setVisibility(View.VISIBLE);
                mMapsFragmentViewModel.getLiveEventMarkers().observe(getViewLifecycleOwner(), new Observer<ArrayList<LiveEventsMarker>>() {
                    @Override
                    public void onChanged(ArrayList<LiveEventsMarker> liveEventsMarkers) {
                        showLiveEventsMarkers(liveEventsMarkers);
                    }
                });
                dialog.dismiss();
            }
        });

        utileLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                reportButton.setVisibility(View.VISIBLE);
                liveEventButton.setVisibility(View.INVISIBLE);
                mMapsFragmentViewModel.getPointOfInterestMarkers().observe(getViewLifecycleOwner(), new Observer<ArrayList<PointOfInterestMarker>>() {
                    @Override
                    public void onChanged(ArrayList<PointOfInterestMarker> pointOfInterestMarkers) {
                        showPointOfInterestMarkers(pointOfInterestMarkers);
                    }
                });
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

    private void fetchFurturiMarkers(){
        mMapsFragmentViewModel.getReports().observe(getViewLifecycleOwner(), new Observer<ArrayList<Report>>() {
            @Override
            public void onChanged(ArrayList<Report> reports) {
                showFurturiMarkers(reports);
            }
        });
    }

    private void showFurturiMarkers(ArrayList<Report> reports) {
        for (int num = 0; num < reports.size(); num++) {
            LatLng latLng = new LatLng(reports.get(num).getTheftMarkerLat(), reports.get(num).getTheftMarkerLng());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reports.get(num).getStolenDate());
            int month = calendar.get(Calendar.MONTH) + 1;
            if (getContext() != null) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Furată pe " + calendar.get(Calendar.DAY_OF_MONTH)
                                + "." + month
                                + "." + calendar.get(Calendar.YEAR))
                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_dot)));
                hash_markers.put(marker.getId(), reports.get(num));
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Nullable
                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.marker_info_window, null);
                        Report report = (Report) hash_markers.get(marker.getId());
                        String url = (String) report.getBikeImageUrl();
                        TextView t1 = (TextView) v.findViewById(R.id.textView);
                        ImageView i1 = (ImageView) v.findViewById(R.id.markerInfoWindowImageView);

                        if (!url.equals(""))
                            Glide.with(getContext()).load(url).into(i1);
                        if (url.equals(""))
                            Glide.with(getContext()).load(newReportMarkerUrl).into(i1);

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
                        Report clicked_report = (Report) hash_markers.get(marker.getId());
                        Intent intent = new Intent(getContext(), ExpandedFurturiPostActivity.class);
                        intent.putExtra("clicked_report", clicked_report);
                        getActivity().startActivity(intent);
                    }

                });

                WeightedLatLng weightedLatLng = new WeightedLatLng(latLng, 10);
                arr.add(weightedLatLng);
            }
        }
    }

    private void showPointOfInterestMarkers(ArrayList<PointOfInterestMarker> potmList) {

        for (int num = 0; num < potmList.size(); num++) {
            LatLng latLng = new LatLng(potmList.get(num).getLat(), potmList.get(num).getLng());
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
            if (String.valueOf(potmList.get(num).getType()).equals("Service")) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(potmList.get(num).getTitle())
                        .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_service)));
            }
            if (String.valueOf(potmList.get(num).getType()).equals("Magazin")) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(potmList.get(num).getTitle())
                        .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_store_24)));
            }
            if (String.valueOf(potmList.get(num).getType()).equals("Cafenea")) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(potmList.get(num).getTitle())
                        .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ic_baseline_coffee_24)));
            }

        }

    }

    private void showLiveEventsMarkers(ArrayList<LiveEventsMarker> lemList) {
        int num;
        for (num = 0; num < lemList.size(); num++) {
            Date currentTime = Calendar.getInstance().getTime();
            if (lemList.get(num).getExpiringDate().after(currentTime)) {
                LatLng latLng = new LatLng(lemList.get(num).getLat(), lemList.get(num).getLng());

                if (getContext() != null) {
                    if (String.valueOf(lemList.get(num).getType()).equals("Groapă")) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(lemList.get(num).getTitle())
                                .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.ground_hole_marker_2)));
                    }
                    if (String.valueOf(lemList.get(num).getType()).equals("Gheață")) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(lemList.get(num).getTitle())
                                .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.snowflake_2)));
                    }
                    if (String.valueOf(lemList.get(num).getType()).equals("Cioburi")) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(lemList.get(num).getTitle())
                                .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.broken_bottle_2)));
                    }
                    if (String.valueOf(lemList.get(num).getType()).equals("Lucrări")) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(lemList.get(num).getTitle())
                                .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.road_work_marker_4)));
                    }
                    if (String.valueOf(lemList.get(num).getType()).equals("Accident")) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(lemList.get(num).getTitle())
                                .icon(bitmapDescriptorFromVector(getActivity().getApplicationContext(), R.drawable.accident_marker_3)));
                    }

                    int finalNum = num;
                    map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Nullable
                        @Override
                        public View getInfoContents(@NonNull Marker marker) {
                            View v = getLayoutInflater().inflate(R.layout.live_events_marker_info, null);
                            TextView t1 = (TextView) v.findViewById(R.id.liveEventsMarkerInfoTitleTextView);
                            TextView t2 = (TextView) v.findViewById(R.id.liveEventsMarkerInfoTimeTextView);
                            TextView t3 = (TextView) v.findViewById(R.id.liveEventsMarkerContentTextView);
                            t1.setText(lemList.get(finalNum).getTitle());
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
                            String output = df.format(lemList.get(finalNum).getPublishDate());
                            t2.setText(output);
                            t3.setText(lemList.get(finalNum).getDescription());
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
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.REQ_PERMISSION_LOCATION
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(getContext(), "Raport adăugat", Toast.LENGTH_SHORT).show();

    }
}