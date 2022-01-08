package com.example.ciclotm.Models;

import java.util.Date;

public class MapMarker {
    private Double Lat;
    private Double Lng;

    private Date Date;

    public MapMarker(){

    }

    public MapMarker(Double lat, Double lng,Date date) {
        Lat = lat;
        Lng = lng;
        Date=date;
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
}
