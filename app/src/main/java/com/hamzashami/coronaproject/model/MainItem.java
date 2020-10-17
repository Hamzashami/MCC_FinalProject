package com.hamzashami.coronaproject.model;

public class MainItem {
    private String title;
    private int number;
    private int colorResId;

    public MainItem(String title, int number, int colorResId) {
        this.title = title;
        this.number = number;
        this.colorResId = colorResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getColorResId() {
        return colorResId;
    }

    public void setColorResId(int colorResId) {
        this.colorResId = colorResId;
    }
}
