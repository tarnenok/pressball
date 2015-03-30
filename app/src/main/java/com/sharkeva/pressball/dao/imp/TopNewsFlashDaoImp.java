package com.sharkeva.pressball.dao.imp;

import android.content.Context;

import com.sharkeva.pressball.dao.parser.NewsFlashesParser;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.entities.Newsflash;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by tarnenok on 19.01.15.
 */
public class TopNewsFlashDaoImp {
    private Category baseCategory;
    private Document context;
    private NewsFlashesParser parser;
    private final String SELECTOR = ".lenta_top_news .mb10";

    public TopNewsFlashDaoImp(Context context) {
        this.parser = new NewsFlashesParser(
                new CategoryDaoImp(context)
        );
    }

    public void initialize(Category baseCategory) {
        this.baseCategory = baseCategory;
        this.parser.initialize(baseCategory);
    }

    public List<Newsflash> update(Document context){
        List<Newsflash> newsflashes;
        this.context = context;

        Element topNewFlashes = context.select(SELECTOR).first();
        newsflashes = parser.parse(topNewFlashes, "0001-01-01");

        return newsflashes;
    }
}
