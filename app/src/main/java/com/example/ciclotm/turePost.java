package com.example.ciclotm;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

public class turePost implements Serializable {

    private String title;
    private String distance;


    private String duration;
    private String start_time;
    private String start_point;
    private int no_participants;
    private String description;
    private String uid;
    private Date date;
    private String userImageUrl;

    public turePost() {

    }

    public turePost(String title, String distance, String duration, String start_time, String start_point, int no_participants, String description, String uid, Date date,String userImageUrl) {
        this.title = title;
        this.distance = distance;
        this.duration = duration;
        this.start_time = start_time;
        this.start_point = start_point;
        this.no_participants = no_participants;
        this.description = description;
        this.uid = uid;
        this.date = date;
        this.userImageUrl = userImageUrl;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistance() {
        return distance;
    }

    public Date getDate() {
        return date;
    }

    public String getStart_point() {
        return start_point;
    }

    public int getNo_participants() {
        return no_participants;
    }

    public String getDescription() {
        return description;
    }

    public String getUid() {
        return uid;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public void setNo_participants(int no_participants) {
        this.no_participants = no_participants;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

}