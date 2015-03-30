package com.sharkeva.pressball.ui;

import java.util.List;

/**
 * Created by tarnenok on 25.02.15.
 */
public interface MutableAdapter<T> {
    void add (T element);
    void removeAt(int i);
    void update(List<T> elements);
}
