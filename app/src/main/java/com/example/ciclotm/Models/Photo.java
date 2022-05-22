package com.example.ciclotm.Models;

import java.util.Date;

public class Photo {
    private Date PhotoDate;
    private String PhotoUrl;

    public Photo(){}

    public Photo(Date photoDate, String photoUrl) {
        PhotoDate  = photoDate;
        PhotoUrl = photoUrl;
    }

    public Date getPhotoDate() {
        return PhotoDate;
    }

    public void setPhotoDate(Date photoDate) {
        PhotoDate = photoDate;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }
}
