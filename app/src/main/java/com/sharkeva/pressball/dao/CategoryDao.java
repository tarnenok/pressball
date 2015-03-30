package com.sharkeva.pressball.dao;

import com.sharkeva.pressball.entities.Category;

import java.util.List;

/**
 * Created by tarnenok on 19.01.15.
 */
public interface CategoryDao {
    List<Category> getAll();
    Category getByName(String name);
    Category getById(int id);
    void add(Category category);
}
