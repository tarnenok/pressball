package com.sharkeva.pressball.dao.imp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharkeva.pressball.dao.CategoryDao;
import com.sharkeva.pressball.entities.Category;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CategoryDaoImp extends SQLiteOpenHelper implements CategoryDao {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "pressball.by";
    private static final String TABLE_CATEGORIES = "categories";

    //columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CLASSIFIER = "classifier";

    public CategoryDaoImp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CLASSIFIER + " TEXT" + ")";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        for (Category category : getAllTemp()){
            db.insert(TABLE_CATEGORIES, null, setCategory(category));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    @Override
    public Category getByName(String name){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_CATEGORIES
                , new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_CLASSIFIER}
                , COLUMN_NAME + "=?"
                , new String[]{name},
                null, null, null, null);
        Category category = null;
        if (cursor != null){
            cursor.moveToFirst();

            category = getCategory(cursor);

            cursor.close();
            database.close();
        }
        return category;
    }

    @Override
    public Category getById(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_CATEGORIES
                , new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_CLASSIFIER}
                , COLUMN_ID + "=?"
                , new String[]{String.valueOf(id)},
                null, null, null, null);
        Category category = null;
        if (cursor != null){
            cursor.moveToFirst();

            category = getCategory(cursor);

            cursor.close();
            database.close();
        }
        return category;
    }

    @Override
    public void add(Category category){
        SQLiteDatabase database = this.getWritableDatabase();

        database.insert(TABLE_CATEGORIES, null, setCategory(category));
        database.close();
    }

    @Override
    public List<Category> getAll(){

        List<Category> categories = new ArrayList<Category>();
        SQLiteDatabase database = this.getReadableDatabase();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_CATEGORIES;
        Cursor cursor = database.rawQuery(SELECT_QUERY, null);

        if(cursor.moveToFirst()){
            do{
                categories.add(getCategory(cursor));
            }while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return categories;
    }

    private List<Category> getAllTemp(){
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

    private Category getCategory(Cursor cursor){
        try {
            Category category = new Category();
            category.setId(cursor.getInt(0));
            category.setName(cursor.getString(1));
            category.setClassifier(cursor.getString(2));
            return category;
        } catch (Exception ex){
            return null;
        }
    }

    private ContentValues setCategory(Category category){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, category.getId());
        values.put(COLUMN_NAME, category.getName());
        values.put(COLUMN_CLASSIFIER, category.getClassifier());
        return values;
    }
}
