package com.sharkeva.pressball.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.utils.PressballUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMLocator;

/**
 * Created by tarnenok on 20.01.15.
 */
public class NFUpdateCenter implements Serializable{
    private Category baseCategory;
    private boolean firstLoad;

    private ArrayList<Listener<Void>> startUpdateList;
    private ArrayList<Listener<Document>> endUpdateList;
    private ArrayList<Listener<Category>> initializeList;
    private ArrayList<Listener<Void>> errorList;

    public boolean isFirstLoad(){
        return firstLoad;
    }

    public Category getBaseCategory() {
        return baseCategory;
    }

    public NFUpdateCenter() {
        this.startUpdateList = new ArrayList<>();
        this.endUpdateList = new ArrayList<>();
        this.initializeList = new ArrayList<>();
        this.errorList = new ArrayList<>();
    }


    public void addOnStartUpdate(Listener<Void> listener) {
        startUpdateList.add(listener);
    }
    public void addOnEndUpdate(Listener<Document> listener) {
        endUpdateList.add(listener);
    }
    public void addOnError(Listener<Void> listener){ errorList.add(listener);}




    public void clearListeners() {
        startUpdateList.clear();
        endUpdateList.clear();
        initializeList.clear();
        errorList.clear();
    }

    private void startUpdate(){
        for(Listener<Void> listener : startUpdateList){
            listener.onExecute(null);
        }
    }

    private void endUpdate(Document context){
        for(Listener<Document> listener : endUpdateList){
            listener.onExecute(context);
        }
    }

    private void error(){
        for(Listener<Void> listener : errorList){
            listener.onExecute(null);
        }
    }


    public void initialize(Category baseCategory) {
        initialize(baseCategory, true);
    }

    public void initialize(Category baseCategory, boolean firstLoad) {
        this.baseCategory = baseCategory;
        this.firstLoad = firstLoad;
        for(Listener<Category> listener : initializeList){
            listener.onExecute(baseCategory);
        }
    }


    public void addOnInitialize(Listener<Category> listener) {
        initializeList.add(listener);
    }

    public void updateAsync(){
        startUpdate();

        new AsyncTask<Void, Void, Document>() {
            private Exception exception = null;
            @Override
            protected Document doInBackground(Void... params) {
                Document context = null;
                try {
                    context = Jsoup
                            .connect(PressballUtils.categoryUrl(baseCategory))
                            .timeout(0)
                            .get();
                } catch (IOException e) {
                    exception = e;
                }
                return context;
            }

            @Override
            protected void onPostExecute(Document document) {
                if(exception == null){
                    endUpdate(document);
                    firstLoad = false;
                } else {
                    Log.e("EXCEPTION", exception.toString());
                    error();
                }
            }
        }.execute();
    }

}
