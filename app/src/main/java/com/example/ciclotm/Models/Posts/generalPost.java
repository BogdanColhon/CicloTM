package com.example.ciclotm.Models.Posts;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

public class generalPost implements Serializable {
    private String title;
    private String content;
    private Date date;
    private String uid;
    private String userImageUrl;

    public generalPost() {

    }

    public generalPost(String title, String content, Date date, String uid, String userImageUrl) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.uid = uid;
        this.userImageUrl = userImageUrl;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getUid() {
        return uid;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

}
