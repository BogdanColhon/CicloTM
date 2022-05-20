package com.example.ciclotm;

public class Constants {
    public static final String ACTION_START_OR_RESUME_SERVICE = "com.example.ciclotm.Services.START_OR_RESUME_SERVICE";
    public static final String ACTION_RESTART_SERVICE = "com.example.ciclotm.Services.RESTART_SERVICE";
    public static final String ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT";
    public static final String ACTION_END_TRACKING_FRAGMENT = "ACTION_END_TRACKING_FRAGMENT";
    public static final int LOCATION_REFRESH_TIME = 5000; // 5 seconds to update
    public static final int LOCATION_REFRESH_DISTANCE = 3; // 3 meters to update
    public static final Double EARTH_RADIUS = 6371.00;
    public static final int REQ_PERMISSION = 5;
    public static final String ACTION_PAUSE_SERVICE = "com.example.ciclotm.Services.PAUSE_SERVICE";
    public static final String ACTION_STOP_SERVICE = "com.example.ciclotm.Services.STOP_SERVICE";
    public static final String NOTIFICATION_CHANNEL_ID = "tracking_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Tracking";
    public static final int NOTIFICATION_ID = 1;
}
