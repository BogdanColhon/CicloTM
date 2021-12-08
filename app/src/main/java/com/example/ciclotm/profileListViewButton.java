package com.example.ciclotm;

public class profileListViewButton {
    private String button_name;
    private String arrow;
    public profileListViewButton(String button_name,String arrow){
        this.button_name=button_name;
        this.arrow=arrow;
    }

    public String getButton_name() {
        return button_name;
    }

    public String getArrow() {
        return arrow;
    }

    public void setButton_name(String button_name) {
        this.button_name = button_name;
    }

    public void setArrow(String arrow) {
        this.arrow = arrow;
    }
}
