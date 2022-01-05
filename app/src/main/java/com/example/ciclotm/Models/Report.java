package com.example.ciclotm.Models;

import java.util.Date;

public class Report {
    private Date publishDate;
    private Date stolenDate;
    private String address;
    private String user_id;
    private String bike_brand;
    private String bike_model;
    private String bike_color;
    private String bike_description;
    private String thief_description;

    public Report() {

    }

    public Report(Date publishDate, Date stolenDate, String address, String user_id, String bike_brand, String bike_model, String bike_color, String bike_description, String thief_description) {
        this.publishDate = publishDate;
        this.stolenDate = stolenDate;
        this.address = address;
        this.user_id = user_id;
        this.bike_brand = bike_brand;
        this.bike_model = bike_model;
        this.bike_color = bike_color;
        this.bike_description = bike_description;
        this.thief_description = thief_description;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getStolenDate() {
        return stolenDate;
    }

    public void setStolenDate(Date stolenDate) {
        this.stolenDate = stolenDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBike_brand() {
        return bike_brand;
    }

    public void setBike_brand(String bike_brand) {
        this.bike_brand = bike_brand;
    }

    public String getBike_model() {
        return bike_model;
    }

    public void setBike_model(String bike_model) {
        this.bike_model = bike_model;
    }

    public String getBike_color() {
        return bike_color;
    }

    public void setBike_color(String bike_color) {
        this.bike_color = bike_color;
    }

    public String getBike_description() {
        return bike_description;
    }

    public void setBike_description(String bike_description) {
        this.bike_description = bike_description;
    }

    public String getThief_description() {
        return thief_description;
    }

    public void setThief_description(String thief_description) {
        this.thief_description = thief_description;
    }
}
