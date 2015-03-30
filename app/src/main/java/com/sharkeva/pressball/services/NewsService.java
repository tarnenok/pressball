package com.sharkeva.pressball.services;

import com.sharkeva.pressball.dao.imp.NewsDaoImp;
import com.sharkeva.pressball.entities.News;
import com.sharkeva.pressball.entities.Newsflash;
import com.sharkeva.pressball.utils.PressballUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by tarnenok on 31.01.15.
 */
public class NewsService {
    private NewsDaoImp daoImp;
    private Newsflash newsflash;


    public NewsService(Newsflash newsflash) {
        this.newsflash = newsflash;
    }

    public News load(){
        Document context = null;
        News news = new News();
        try {
            String str = PressballUtils.loadPage(newsflash.getClassify());
            context = Jsoup.parse(normolize(str));

            daoImp = new NewsDaoImp(context);
            news.setNewsElements(daoImp.load());
            news.setTitle(getTitle());
            news.setComments(daoImp.getComments());
            news.setCommentPage(daoImp.getCommentsPage());
            return news;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String normolize(String str) {
        //should use regex with whitespaces
        String str1 = str.replaceAll("<div class=\"cl\"/></div>", "");
        //str1 = new String(str1.getBytes(), "");
        return str1;
    }

    public String getTitle(){
        if(daoImp != null){
            return daoImp.getTitle();
        }
        return null;
    }
}
