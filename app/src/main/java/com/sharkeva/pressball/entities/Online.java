package com.sharkeva.pressball.entities;

import java.util.List;

/**
 * Created by tarnenok on 11.02.15.
 */
public class Online {
    private int id;
    private List<OnlineData> dataList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OnlineData> getDataList() {
        return dataList;
    }

    public void setDataList(List<OnlineData> dataList) {
        this.dataList = dataList;
    }
}
