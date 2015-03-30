package com.sharkeva.pressball.utils;

import com.sharkeva.pressball.entities.Category;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tarnenok on 19.01.15.
 */
public class PressballUtils {
    public static final String baseUrl = "http://www.pressball.by/";
    public static final String newsUrl = "http://www.pressball.by/news/";
    private static final String CHARSET = "windows-1251";

    public static boolean isPressballUrl(String url){
        return url.contains("pressball.by");
    }

    public static String categoryUrl(Category category){
        String categoryUrl = newsUrl;
        if(category.getId() != 0){
            categoryUrl += category.getClassifier() + "/";
        }
        return categoryUrl;
    }

    public static String loadPage(String baseUrl) throws IOException {
        URL url = new URL(baseUrl);
        Reader r = new InputStreamReader(url.openConnection().getInputStream(), CHARSET);
        StringBuilder buf = new StringBuilder();
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }
        return buf.toString();
    }
}
