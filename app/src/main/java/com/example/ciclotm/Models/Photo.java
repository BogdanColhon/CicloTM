package com.example.ciclotm.Models;

public class Photo {
    private String PhotoUrl;

    public Photo(){

    }
    public Photo( String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }
}
