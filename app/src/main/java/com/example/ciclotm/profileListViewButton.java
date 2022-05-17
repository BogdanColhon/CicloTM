package com.example.ciclotm;

public class profileListViewButton {
    private String button_name;
    private int button_icon;
    public profileListViewButton(String button_name,int button_icon){
        this.button_name=button_name;
        this.button_icon=button_icon;
    }

    public String getButton_name() {
        return button_name;
    }

    public int getButton_icon() {
        return button_icon;
    }

    public void setButton_name(String button_name) {
        this.button_name = button_name;
    }

    public void setButton_icon(int button_icon) {
        this.button_icon = button_icon;
    }
}
