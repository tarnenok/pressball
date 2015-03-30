package com.sharkeva.pressball.dao.imp;

import android.content.Context;

import com.sharkeva.pressball.dao.CategoryDao;
import com.sharkeva.pressball.dao.NewsFlashDao;
import com.sharkeva.pressball.dao.parser.NewsFlashParser;
import com.sharkeva.pressball.dao.parser.NewsFlashesParser;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.entities.Newsflash;
import com.sharkeva.pressball.services.Listener;
import com.sharkeva.pressball.utils.PressballUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarnenok on 13.01.15.
 */
public class NewsFlashDaoImp implements NewsFlashDao {

    private Category baseCategory;
    private Document context;
    private NewsFlashesParser parser;
    private final String SELECTOR = ".last_news1";

    public NewsFlashDaoImp(Context context) {
        this.parser = new NewsFlashesParser(
                new CategoryDaoImp(context)
        );
    }

    public void initialize(Category baseCategory) {
        this.baseCategory = baseCategory;
        this.parser.initialize(baseCategory);
    }

    public List<Newsflash> update(Document context){
        List<Newsflash> newsflashes = new ArrayList<>();
        this.context = context;


        Element dataElements = context.select(SELECTOR).first();
        String date = "0001-01-01";
        for (Element data : dataElements.children()){
            if (data.className().contains("date")){
                date = data.html();
            } else if (data.className().contains("mb10")){
                newsflashes.addAll(parser.parse(data, date));
            }
        }

        return newsflashes;
    }
}
