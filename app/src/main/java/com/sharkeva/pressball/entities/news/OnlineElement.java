package com.sharkeva.pressball.entities.news;

import com.sharkeva.pressball.entities.Online;
import com.sharkeva.pressball.entities.OnlineData;

import java.util.List;

/**
 * Created by tarnenok on 06.02.15.
 */
public class OnlineElement extends NewsElement {

    private Online data;

    public OnlineElement(Online data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Class<?> getDataType() {
        return data.getClass();
    }
}
