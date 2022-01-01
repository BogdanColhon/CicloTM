package com.example.ciclotm;

import android.graphics.drawable.Drawable;

public class furturiPost {
    private String date;
    private String location;
    private String description;
    private Drawable bike_photo;

    public furturiPost(String date, String location, String description,Drawable bike_photo) {
        this.date = date;
        this.location = location;
        this.description = description;
        this.bike_photo=bike_photo;
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

    public Drawable getBike_photo() {
        return bike_photo;
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

    public void setBike_photo(Drawable bike_photo) {
        this.bike_photo = bike_photo;
    }
}
