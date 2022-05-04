package com.example.ciclotm;

public class SemnCirculatie {
    String name, text;
    int image;

    public SemnCirculatie(String name, String text, int image) {
        this.name = name;
        this.text = text;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
