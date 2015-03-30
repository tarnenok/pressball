package com.sharkeva.pressball.entities;

import java.util.Date;

/**
 * Created by tarnenok on 06.02.15.
 */
public class OnlineData {
    private String name;
    private int imageResource;

    //date is string type because it's better to parse and we don't need use date type possibilities
    private String date;
    private boolean isMain;


    public OnlineData() {
    }

    //test constructor
    public OnlineData(String name) {
        this.name = name;
    }

    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }

    public boolean isMain() {
        return isMain;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
