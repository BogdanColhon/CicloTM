package com.example.ciclotm;

import android.graphics.drawable.Drawable;

public class turePost {
    private String distance;
    private String time;
    private String start_point;
    private String no_participants;
    private Drawable user_photo;


    public turePost(String distance, String time, String start_point, String no_participants,Drawable user_photo) {
        this.distance = distance;
        this.time = time;
        this.start_point = start_point;
        this.no_participants = no_participants;
        this.user_photo=user_photo;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public String getStart_point() {
        return start_point;
    }

    public String getNo_participants() {
        return no_participants;
    }

    public Drawable getUser_photo() {
        return user_photo;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public void setNo_participants(String no_participants) {
        this.no_participants = no_participants;
    }

    public void setUser_photo(Drawable user_photo) {
        this.user_photo = user_photo;
    }
}