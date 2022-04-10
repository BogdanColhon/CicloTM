package com.example.ciclotm.Admin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ciclotm.AdminMenuActivity2;
import com.example.ciclotm.DialogFragment;
import com.example.ciclotm.Models.PointOfInterestMarker;
import com.example.ciclotm.R;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminMapsFragment extends Fragment {

    private final int REQ_PERMISSION = 5;
    private MapView mapView;
    private GoogleMap map;
    private DatabaseReference reference;
    private TextView searchPointOfInterestTextView;
    private FloatingActionButton addPointsOfInterestFloatingButton;
    private LatLng newPointOfInterestMarker;
    private String fulladdress = "-";
    private ArrayList<PointOfInterestMarker> markerList = new ArrayList<>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminMapsFragment newInstance(String param1, String param2) {
        AdminMapsFragment fragment = new AdminMapsFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_maps, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        fetchPointOfInterestMarkers();
        addPointsOfInterestFloatingButton = view.findViewById(R.id.addPointsOfInterestFloatingButton);

        addPointsOfInterestFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPointOfInterestMarker != null) {
                    DialogFragment myDialogFragment = new DialogFragment();
                    myDialogFragment.show(getActivity().getSupportFragmentManager(), "MyFragment");
                    Bundle args = new Bundle();
                    args.putDouble("markerLat", newPointOfInterestMarker.latitude);
                    args.putDouble("markerLng", newPointOfInterestMarker.longitude);
                    args.putString("address", fulladdress);
                    myDialogFragment.putArguments(args);
                }
            }

        });

        searchPointOfInterestTextView = (TextView) view.findViewById(R.id.searchPointOfInterestTextView);
        Places.initialize(getContext(), getResources().getString(R.string.google_maps_api_key));
        searchPointOfInterestTextView.setFocusable(false);
        searchPointOfInterestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        , fieldList).build(getContext());
                startActivityForResult(intent, 200);
            }
        });

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
                if (checkPermission()) {
                    mMap.setMyLocationEnabled(true);
                } else
                    askPermission();

                LatLng city_center = new LatLng(45.75804742621145, 21.228941951330423);
                CameraPosition city = CameraPosition.builder().target(city_center).zoom(16).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(city));

            }
        });
        return view;

    }


    private void fetchPointOfInterestMarkers() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("PointsOfInterestMarkers");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PointOfInterestMarker newMarker = snapshot.getValue(PointOfInterestMarker.class);
                LatLng latLng = new LatLng(newMarker.getLat(), newMarker.getLng());
                System.out.println("\n\n" + newMarker.getType());
                markerList.add(newMarker);

                if (String.valueOf(newMarker.getType()).equals("Service")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_service)));
                }
                if (String.valueOf(newMarker.getType()).equals("Magazin")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_store_24)));
                }
                if (String.valueOf(newMarker.getType()).equals("Cafenea")) {
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(newMarker.getTitle())
                            .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_coffee_24)));
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PointOfInterestMarker updateMarker = snapshot.getValue(PointOfInterestMarker.class);
                for (PointOfInterestMarker m : markerList) {
                    if (m.getTitle().equals(updateMarker.getTitle())) {
                        markerList.set(markerList.indexOf(m), updateMarker);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode != 0) {
            if (data != null) {
                System.out.println(data);
                Place place = Autocomplete.getPlaceFromIntent(data);
                searchPointOfInterestTextView.setText(place.getAddress());
                newPointOfInterestMarker = place.getLatLng();
                fulladdress = place.getAddress();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AdminMenuActivity2) getActivity())
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


}