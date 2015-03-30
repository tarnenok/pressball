package com.sharkeva.pressball.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tarnenok on 13.01.15.
 */
public class Newsflash implements Serializable {
    private int id;
    private Category category;
    private String title;
    private Date time;
    private Date date;
    private int views;
    private int commentsCount;
    private boolean isMain;
    private String classify;

    private boolean isLive;
    private boolean isWithPhoto;
    private boolean isWithVideo;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public boolean isWithPhoto() {
        return isWithPhoto;
    }

    public void setWithPhoto(boolean isWithPhoto) {
        this.isWithPhoto = isWithPhoto;
    }

    public boolean isWithVideo() {
        return isWithVideo;
    }

    public void setWithVideo(boolean isWithVideo) {
        this.isWithVideo = isWithVideo;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public Newsflash() {
        isMain = false;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
