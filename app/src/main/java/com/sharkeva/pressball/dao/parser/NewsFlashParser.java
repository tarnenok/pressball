package com.sharkeva.pressball.dao.parser;

import android.util.Log;

import com.sharkeva.pressball.dao.CategoryDao;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.entities.Newsflash;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

/**
 * Created by tarnenok on 19.01.15.
 */
public class NewsFlashParser {
    private CategoryDao categoryDao;
    private Category baseCategory;

    private final String NEWSFLASH_ELEMENT = "td";

    private final int NEWSFLASH_TIME_NUM = 0;
    private final String NEWSFLASH_TIME = ".time";

    private final int NEWSFLASH_NAME_NUM = 1;
    private final String NEWSFLASH_NAME = "a";

    private final int NEWSFLASH_VIEWS_NUM = 1;
    private final String NEWSFLASH_VIEWS = "span";

    private final String NEWSFLASH_ID = "href";
    private final String NEWSFLASH_IMG = "img   ";

    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateFormat;

    //attributes
    private final String NEWSFLASH_LIVE = "live_ico.gif";
    private final String NEWSFLASH_PHOTO = "ico13.gif";
    private final String NEWSFLASH_VIDEO = "ico14.gif";

    public NewsFlashParser(CategoryDao categoryDao, Category baseCategory) {
        this.categoryDao = categoryDao;
        this.baseCategory = baseCategory;
        this.timeFormat = new SimpleDateFormat("HH:mm");
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    private Date parseTime(Elements columns){
        Element time = columns.get(NEWSFLASH_TIME_NUM).select(NEWSFLASH_TIME).first();
        if(time != null){
            try {
                return timeFormat.parse(time.text());
            } catch (ParseException e) { }
        }
        return null;
    }

    private Date parseDate(String date){
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            Log.i("newFlashParser", e.getStackTrace().toString());
            return null;
        }
    }

    private Object[] parseNameAndCategory(Elements columns){
        Object[] objects = new Object[2];
        Element name = columns.get(NEWSFLASH_NAME_NUM).getElementsByTag(NEWSFLASH_NAME).first();
        if(!withCategory()){
            String[] categoryAndName = getCategoryAndName(name.text());

            if(categoryAndName[0] == null || categoryAndName[1] == null){
                //object not a news
                return new Object[]{null, null};
            }
            objects[0] = categoryAndName[1];
            Category category = categoryDao.getByName(categoryAndName[0]);
            if(category == null){
                category = new Category();
                category.setId(-1);
                category.setName(categoryAndName[0]);
                category.setClassifier("");
            }
            objects[1] = category;
        }else {
            objects[1] = baseCategory;
            objects[0] = name.text();
        }
        return objects;
    }

    private int parseId(Elements columns){
        Element name = columns.get(NEWSFLASH_NAME_NUM).getElementsByTag(NEWSFLASH_NAME).first();
        String[] temp = name.attr(NEWSFLASH_ID).replaceAll("\\s", "").split("/");
        return Integer.parseInt(temp[temp.length - 1]);
    }

    private String[] parseViewAndComments(Elements columns){
        Element views = columns.get(NEWSFLASH_VIEWS_NUM).getElementsByTag(NEWSFLASH_VIEWS).first();
        if(views == null){
            return null;
        }
        return views.text().replaceAll("\\s", "").split("/");
    }

    private boolean parseIsMain(Elements columns){
        Element isMain = columns.get(NEWSFLASH_NAME_NUM).select(NEWSFLASH_NAME + " b").first();
        return isMain != null;
    }

    public Newsflash parse(Element newsFlash, String date){
        Newsflash news = new Newsflash();
        Elements columns = newsFlash.getElementsByTag(NEWSFLASH_ELEMENT);

        news.setTime(parseTime(columns));
        news.setDate(parseDate(date));

        Object[] temp = parseNameAndCategory(columns);
        if (temp[0] == null || temp[1] == null){
            //not a news
            return null;
        }
        news.setCategory((Category)temp[1]);
        news.setTitle((String)temp[0]);

//        news.setId(parseId(columns));

        String[] tempStr = parseViewAndComments(columns);
        //if no comments -> not add news
        if(tempStr == null){
            return null;
        }
        news.setViews(Integer.parseInt(tempStr[0]));
        if(tempStr.length == 2){
            news.setCommentsCount(Integer.parseInt(tempStr[1]));
        }

        news.setMain(parseIsMain(columns));
        news.setClassify(parseHref(columns));

        setAttributes(news, columns);

        return news;
    }

    private void setAttributes(Newsflash news, Elements columns) {
        Elements elements = columns.get(NEWSFLASH_NAME_NUM).getElementsByTag(NEWSFLASH_IMG);
        for(Element element : elements){
            String[] tempArr = element.attr("src").split("/");
            String str = tempArr[tempArr.length - 1];
            switch (str){
                case NEWSFLASH_LIVE:
                    news.setLive(true);
                    break;
                case NEWSFLASH_PHOTO:
                    news.setWithPhoto(true);
                    break;
                case NEWSFLASH_VIDEO:
                    news.setWithVideo(true);
                    break;
            }
        }
    }

    private String parseHref(Elements columns) {
        Element name = columns.get(NEWSFLASH_NAME_NUM).getElementsByTag(NEWSFLASH_NAME).first();
        return name.attr(NEWSFLASH_ID).replaceAll("\\s", "");
    }

    private boolean withCategory() {
        return baseCategory.getId() != 0;
    }

    private String[] getCategoryAndName(String name){
        String[] categoryAndName = new String[2];
        for (int i = 0; i < name.length(); i++){
            if(name.charAt(i) == '.'){
                categoryAndName[0] = name.substring(0, i);
                categoryAndName[1] = name.substring(i + 2, name.length());
                return categoryAndName;
            }
        }
        return categoryAndName;
    }
}
