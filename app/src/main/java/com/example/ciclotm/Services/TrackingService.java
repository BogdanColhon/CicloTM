package com.example.ciclotm.Services;


import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.ciclotm.Constants;
import com.example.ciclotm.MenuActivity;
import com.example.ciclotm.R;
import com.example.ciclotm.RecordFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

public class TrackingService extends Service implements LifecycleOwner {

    public static boolean isFirstRun = true;
    public static boolean isTracking = false;
    private FusedLocationProviderClient client;
    private LocationManager locationManager;
    private LocationListener locationListener = null;
    private int samples = 1;
    protected static double totalDistance = 0.0;
    private int seconds;
    private boolean running;
    public static boolean serviceKilled = false;
    private String time;
    private double speed;
    public static Intent intent;
    Notification notificationBuilder;
    NotificationManager notificationManager;
    public static ArrayList<com.example.ciclotm.Models.Location> serRoutePoints = new ArrayList<com.example.ciclotm.Models.Location>();
    public static ArrayList<Marker> serRouteMarker = new ArrayList<Marker>();
    com.example.ciclotm.Models.Location pointx;
    Marker markerx;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        client = LocationServices.getFusedLocationProviderClient(this);

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


                if (running) {
                    seconds++;
                    RecordFragment.setTimer(hours, minutes, secs);
                    Notification notificationBuilder2 = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATION_CHANNEL_ID)
                            .setAutoCancel(false)
                            .setOngoing(true)
                            .setSmallIcon(R.drawable.bike)
                            .setContentTitle("CicloTM")
                            .setContentText(String.format("%.2f", totalDistance) + " km")
                            .setSubText(time)
                            .setContentIntent(getMainActivityPendingIntent())
                            .build();
                    notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder2);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }


    private void updateLocationTracking(boolean isTracking) {
        if (isTracking == true) {
            locationListener = new MyLocationListener();
            if (checkPermission())
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.LOCATION_REFRESH_TIME, Constants.LOCATION_REFRESH_DISTANCE, locationListener);
        } else {
            locationManager.removeUpdates(locationListener);
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            final String action = intent.getAction();
            if (Constants.ACTION_START_OR_RESUME_SERVICE.equals(action)) {
                if (isFirstRun) {
                    serRoutePoints.add(RecordFragment.point);
                    serRouteMarker.add(RecordFragment.startMarker);
                    startForegroundService();
                    isFirstRun = false;
                } else {
                    running = true;
                    updateLocationTracking(true);
                }

            }
            if (Constants.ACTION_PAUSE_SERVICE.equals(action)) {
                pauseForegroundService();
            }
            if (Constants.ACTION_STOP_SERVICE.equals(action)) {
                stopForegroundService();

            }

        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void startForegroundService() {
        isTracking = true;
        running = true;
        serviceKilled = false;
        runTimer();
        updateLocationTracking(true);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }

        notificationBuilder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.drawable.bike)
                .setContentTitle("CicloTM")
                .setContentText("0:00:00")
                .setContentIntent(getMainActivityPendingIntent())
                .build();

        startForeground(Constants.NOTIFICATION_ID, notificationBuilder);

    }

    private void pauseForegroundService() {
        running = false;
        isTracking = false;
        locationManager.removeUpdates(locationListener);
        for (int i = 0; i < serRoutePoints.size(); i++) {
            System.out.println(serRoutePoints.get(i));
        }
    }

    private void stopForegroundService() {
       if(serviceKilled == false && isTracking == true)
       {
        running = false;
        isTracking = false;
        serviceKilled = true;
        locationManager.removeUpdates(locationListener);
        stopForeground(true);
        stopForegroundService();
        stopSelf();
       }
    }

    private PendingIntent getMainActivityPendingIntent() {
        intent = new Intent(this, MenuActivity.class);
        intent.setAction(Constants.ACTION_SHOW_TRACKING_FRAGMENT);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }


    private void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            addPoint(loc);
            RecordFragment.removeMarker();
            samples++;



            if (serRoutePoints.get(serRoutePoints.size() - 2) != null) {
                LatLng firstPoint = new LatLng(serRoutePoints.get(serRoutePoints.size() - 2).getLatitude(), serRoutePoints.get(serRoutePoints.size() - 2).getLongitude());
                LatLng secondPoint = new LatLng(serRoutePoints.get(serRoutePoints.size() - 1).getLatitude(), serRoutePoints.get(serRoutePoints.size() - 1).getLongitude());

                double distance = DistanceCalculation(RecordFragment.startPoint.getLatitude(),RecordFragment.startPoint.getLatitude(), secondPoint.latitude, secondPoint.longitude);
                if(distance > RecordFragment.maxDistance) {
                    RecordFragment.maxDistance = distance;
                    RecordFragment.maxPoint.setLatitude(secondPoint.latitude);
                    RecordFragment.maxPoint.setLongitude(secondPoint.longitude);
                }
                double pointsDistance = DistanceCalculation(firstPoint.latitude, firstPoint.longitude, secondPoint.latitude, secondPoint.longitude);
                totalDistance = totalDistance + pointsDistance;
                speed = pointsDistance / (double) (5.0 / 3600.0);




                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(firstPoint)
                        .add(secondPoint)
                        .width(13)
                        .color(getResources().getColor(R.color.green, getResources().newTheme()));

                RecordFragment.setData(totalDistance, speed, samples,pointx,markerx,polylineOptions);
                //RecordFragment.removeMarker();

                RecordFragment.checkProximityLiveEvents(secondPoint);
            }

        }


    }

    public void addPoint(Location location) {


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        pointx = new com.example.ciclotm.Models.Location();
        pointx.setLatitude(latLng.latitude);
        pointx.setLongitude(latLng.longitude);
        serRoutePoints.add(pointx);
        MarkerOptions options = new MarkerOptions().position(latLng).icon(RecordFragment.bitmapDescriptorFromVector(this, R.drawable.black_dot_icon_resized));
        RecordFragment.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        markerx = RecordFragment.map.addMarker(options);
        serRouteMarker.add(markerx);


    }

    public static double DistanceCalculation(double lat1, double lon1, double lat2, double lon2) {
        double Radius = Constants.EARTH_RADIUS;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


