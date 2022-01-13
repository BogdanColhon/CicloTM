package com.example.ciclotm.Models;

public class Photo {
    private String Name;
    private String PhotoUrl;

    public Photo(){

    }
    public Photo(String name, String photoUrl) {
        Name = name;
        PhotoUrl = photoUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }
}
