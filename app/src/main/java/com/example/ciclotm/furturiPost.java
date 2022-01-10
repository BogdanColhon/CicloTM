package com.example.ciclotm;

import android.graphics.drawable.Drawable;

public class furturiPost {
    private String date;
    private String location;
    private String description;
    private String uid;

    public furturiPost(String date, String location, String description,String uid) {
        this.date = date;
        this.location = location;
        this.description = description;
        this.uid=uid;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
