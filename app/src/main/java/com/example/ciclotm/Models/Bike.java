package com.example.ciclotm.Models;

public class Bike {

    private String Nick_name;
    private String Type;
    private String Brand;
    private String Model;
    private String Weight;
    private String Year;
    private String Details;
    private String Owner;
    private String BikePhotoUrl;

    public Bike() {

    }

    public Bike(String nick_name, String type, String brand, String model, String weight, String year, String details, String owner, String bikePhotoUrl) {
        Nick_name = nick_name;
        Type = type;
        Brand = brand;
        Model = model;
        Weight = weight;
        Year = year;
        Details = details;
        Owner = owner;
        BikePhotoUrl = bikePhotoUrl;
    }

    public String getNick_name() {
        return Nick_name;
    }

    public void setNick_name(String nick_name) {
        Nick_name = nick_name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getBikePhotoUrl() {
        return BikePhotoUrl;
    }

    public void setBikePhotoUrl(String bikePhotoUrl) {
        BikePhotoUrl = bikePhotoUrl;
    }

}
