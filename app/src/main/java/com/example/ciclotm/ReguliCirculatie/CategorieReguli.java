package com.example.ciclotm.ReguliCirculatie;

import java.io.Serializable;

public class CategorieReguli implements Serializable {
    private String category;
    private String rules;


    public CategorieReguli(String category, String rules) {
        this.category = category;
        this.rules = rules;

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
