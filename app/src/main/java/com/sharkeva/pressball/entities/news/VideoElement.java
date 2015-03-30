package com.sharkeva.pressball.entities.news;

/**
 * Created by tarnenok on 02.02.15.
 */
public class VideoElement extends NewsElement {

    private String data;

    public VideoElement(String data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }
}
