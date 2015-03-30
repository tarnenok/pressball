package com.sharkeva.pressball.entities.news;

import java.lang.reflect.Type;

/**
 * Created by tarnenok on 31.01.15.
 */
public class TextElement extends NewsElement {

    private String data;

    public TextElement(String data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Class<String> getDataType() {
        return String.class;
    }


}
