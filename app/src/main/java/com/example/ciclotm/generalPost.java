package com.example.ciclotm;

import android.graphics.drawable.Drawable;

public class generalPost {
    private String title;
    private String content;
    private Drawable user_photo;

    public generalPost(String title, String content, Drawable user_photo) {
        this.title = title;
        this.content = content;
        this.user_photo = user_photo;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getUser_photo() {
        return user_photo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser_photo(Drawable user_photo) {
        this.user_photo = user_photo;
    }
}
