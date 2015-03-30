package com.sharkeva.pressball.entities.news;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by tarnenok on 31.01.15.
 */
public abstract class NewsElement implements Serializable {
    public abstract Object getData();
    public abstract Class<?> getDataType();
}
