package com.sharkeva.pressball.dao.parser;

import com.sharkeva.pressball.dao.CategoryDao;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.entities.Newsflash;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarnenok on 19.01.15.
 */
public class NewsFlashesParser {
    private final String NEWSFLASH_ELEMENT = "tr";
    private NewsFlashParser newsFlashParser;
    private CategoryDao categoryDao;

    public NewsFlashesParser(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public void initialize(Category baseCategory){
        newsFlashParser = new NewsFlashParser(categoryDao, baseCategory);
    }

    public List<Newsflash> parse(Element element, String date){
        List<Newsflash> newsflashes = new ArrayList<Newsflash>();
        Elements elements = element.getElementsByTag(NEWSFLASH_ELEMENT);
        for(Element flashNews : elements){
           Newsflash newsflash = newsFlashParser.parse(flashNews, date);
           if(newsflash != null){
               newsflashes.add(newsflash);
           }
        }
        return newsflashes;
    }
}
