package com.example.ciclotm.Models.Markers;

import java.util.Date;

public class MapMarker {
    private Double Lat;
    private Double Lng;
    private Date Date;
    private String BikeImageUrl;
    private String LocationImageUrl;

    public MapMarker() {

    }

    public MapMarker(Double lat, Double lng, Date date, String bikeImageUrl, String locationImageUrl) {
        Lat = lat;
        Lng = lng;
        Date = date;
        BikeImageUrl = bikeImageUrl;
        LocationImageUrl = locationImageUrl;

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

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getBikeImageUrl() {
        return BikeImageUrl;
    }

    public void setBikeImageUrl(String bikeImageUrl) {
        BikeImageUrl = bikeImageUrl;
    }

    public String getLocationImageUrl() {
        return LocationImageUrl;
    }

    public void setLocationImageUrl(String locationImageUrl) {
        LocationImageUrl = locationImageUrl;
    }
}
