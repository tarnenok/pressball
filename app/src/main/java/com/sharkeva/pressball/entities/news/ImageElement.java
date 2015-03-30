package com.sharkeva.pressball.entities.news;

/**
 * Created by tarnenok on 01.02.15.
 */
public class ImageElement extends NewsElement {

    private String imgUrl;

    public ImageElement(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public Object getData() {
        return imgUrl;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }
}
