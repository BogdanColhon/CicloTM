package com.example.ciclotm.Models;

import java.util.Date;

public class Comment {
    private String content;
    private Date date;
    private String uid;

    public Comment() {
    }

    public Comment(String content, Date date, String uid) {
        this.content = content;
        this.date = date;
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
