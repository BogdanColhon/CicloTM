package com.example.ciclotm.Models.Objects;

import java.io.Serializable;
import java.util.Date;

public class Route implements Serializable {
    private Date publishDate;
    private String userId;
    private double distance;
    private String elapsedTime;
    private double avgSpeed;
    private double maxSpeed;
    private String mapImageUrl;

    public Route(){}

    public Route(Date publishDate, String userId, double distance, String elapsedTime, double avgSpeed, double maxSpeed, String mapImageUrl) {
        this.publishDate = publishDate;
        this.userId = userId;
        this.distance = distance;
        this.elapsedTime = elapsedTime;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.mapImageUrl = mapImageUrl;
    }

    public double calculateAverageSpeed(double speed,int samples)
    {
        return speed/(double) samples;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }
}


