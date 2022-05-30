package com.example.ciclotm.Models.Markers;

public class PointOfInterestMarker {

    private String Title;


    private String Type;
    private Double Lat;
    private Double Lng;

    public PointOfInterestMarker() {

    }

    public PointOfInterestMarker(String title, String type, Double lat, Double lng) {
        Title = title;
        Type = type;
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
}
