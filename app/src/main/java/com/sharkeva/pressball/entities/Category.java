package com.sharkeva.pressball.entities;

import java.io.Serializable;

/**
 * Created by tarnenok on 13.01.15.
 */
public class Category implements Serializable {
    private int id;
    private String name;
    private String classifier;

    public Category() {
    }

    public Category(int id, String name, String classifier) {
        this.id = id;
        this.name = name;
        this.classifier = classifier;

    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getClassifier() {
        return classifier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
