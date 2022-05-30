package com.example.ciclotm.Models.Legislation;

import java.io.Serializable;
import java.util.Date;

public class CategorieReguli implements Serializable {
    private Date publishDate;
    private String category;
    private String rules;

    public CategorieReguli(){}

    public CategorieReguli(Date publishDate,String category, String rules) {
        this.publishDate = publishDate;
        this.category = category;
        this.rules = rules;

    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
