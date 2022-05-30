package com.example.ciclotm.Models.Markers;

import java.util.Date;

public class LiveEventsMarker {

    private String Title;
    private String Type;
    private String Description;
    private Date publishDate;
    private Date expiringDate;
    private Double Lat;
    private Double Lng;
    private int ConfirmationCounter;

    public LiveEventsMarker() {

    }

    public LiveEventsMarker(String title, String type, String description, Date publishDate, Date expiringDate, Double lat, Double lng, int confirmationCounter) {
        Title = title;
        Type = type;
        Description = description;
        this.publishDate = publishDate;
        this.expiringDate = expiringDate;
        Lat = lat;
        Lng = lng;
        ConfirmationCounter = confirmationCounter;
    }

    public int getConfirmationCounter() {
        return ConfirmationCounter;
    }

    public void setConfirmationCounter(int confirmationCounter) {
        ConfirmationCounter = confirmationCounter;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(Date expiringDate) {
        this.expiringDate = expiringDate;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }
}
