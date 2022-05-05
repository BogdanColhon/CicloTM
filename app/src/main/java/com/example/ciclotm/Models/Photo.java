package com.example.ciclotm.Models;

public class Photo {
    private String PhotoTitle;
    private String PhotoUrl;

    public Photo(){}

    public Photo(String photoTitle, String photoUrl) {
        PhotoTitle  = photoTitle;
        PhotoUrl = photoUrl;
    }

    public String getPhotoTitle() {
        return PhotoTitle;
    }

    public void setPhotoTitle(String photoTitle) {
        PhotoTitle = photoTitle;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }
}
