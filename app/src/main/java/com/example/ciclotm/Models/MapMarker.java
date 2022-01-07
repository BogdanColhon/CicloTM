package com.example.ciclotm.Models;

public class MapMarker {
    private Double Lat;
    private Double Lng;

    public MapMarker(){

    }

    public MapMarker(Double lat, Double lng) {
        Lat = lat;
        Lng = lng;
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
