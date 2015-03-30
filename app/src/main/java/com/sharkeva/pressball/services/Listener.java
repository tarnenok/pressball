package com.sharkeva.pressball.services;

import com.sharkeva.pressball.entities.Newsflash;

import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by tarnenok on 20.01.15.
 */
public interface Listener<T> {
    void onExecute(T context);
}
