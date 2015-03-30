package com.sharkeva.pressball.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.sharkeva.pressball.dao.CategoryDao;
import com.sharkeva.pressball.dao.imp.CategoryDaoImp;
import com.sharkeva.pressball.entities.Category;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CategoryDaoImpTest extends AndroidTestCase {

    private static final String TEST_FILE_PREFIX = "test_category";

    CategoryDao categoryDao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        categoryDao = new CategoryDaoImp(getDatabaseContext());
    }

    private Context getDatabaseContext(){
        return new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Category> categories = categoryDao.getAll();
    }

    @Test
    public void testAddAll(){
        for (Category category : getAll()){
            categoryDao.add(category);
        }
    }

    private List<Category> getAll(){
        ArrayList<Category> categories = new ArrayList<Category>();
        Collections.addAll(categories
                , new Category(0, "Все", "all")
                , new Category(1, "Футбол", "football")
                , new Category(2, "Хоккей", "hockey")
                , new Category(3, "Баскетбол", "basketball")
                , new Category(4, "Гандбол", "handball")
                , new Category(5, "Зимние виды", "winter")
                , new Category(6, "Биатлон", "biathlon")
                , new Category(7, "Летние виды", "summer")
                , new Category(8, "Разное", "other")
                , new Category(9, "Олимпиада", "olimpiada")
                , new Category(10, "Теннис", "tennis"));
        return categories;
    }
}