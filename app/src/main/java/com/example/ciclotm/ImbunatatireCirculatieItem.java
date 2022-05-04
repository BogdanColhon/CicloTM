package com.example.ciclotm;

import java.io.Serializable;

public class ImbunatatireCirculatieItem implements Serializable {

    private String title;
    private String text;
    private boolean expandable;
    private int imageId;

    public ImbunatatireCirculatieItem(String title, String text, int imageId) {
        this.title = title;
        this.text = text;
        this.imageId = imageId;
        this.expandable = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
