package com.sharkeva.pressball.entities;

import java.io.Serializable;

/**
 * Created by tarnenok on 06.02.15.
 */
public class Comment implements Serializable {
    private String author;
    private String date;
    private String text;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
