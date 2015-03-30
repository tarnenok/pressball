package com.sharkeva.pressball.dao.parser.news;

import com.sharkeva.pressball.entities.OnlineData;
import com.sharkeva.pressball.entities.news.OnlineElement;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tarnenok on 10.02.15.
 */
public class OnlineElementParser {

    private final String KEY_ONLINE_ELEMENT = "td";
    private final String KEY_NAME_IS_MAIN = "strong";
    private final String KEY_IMAGE = "img";

    private final int MINUTE_NUM = 0;
    private final int ICO_NUM = 1;
    private final int TEXT_NUM = 2;

    private final Pattern pattern = Pattern.compile("\\d+_\\w+.gif");

    public OnlineData parse(Element element){
        OnlineData elem = new OnlineData();
        Elements onlineElements = element.select(KEY_ONLINE_ELEMENT);
        elem.setName(parseName(onlineElements));
        elem.setDate(parseDate(onlineElements));
        elem.setImageResource(parseImageResource(onlineElements));
        elem.setMain(parseMain(onlineElements));

        return elem;
    }

    private boolean parseMain(Elements onlineElements) {
        boolean isMain = false;
        if (onlineElements.size() > TEXT_NUM){
            isMain = onlineElements.get(TEXT_NUM).getElementsByTag(KEY_NAME_IS_MAIN).first() != null;
        }
        return isMain;
    }

    private int parseImageResource(Elements onlineElements) {
        int resource = -1;
        if(onlineElements.size() > ICO_NUM){
            Element temp = onlineElements.get(ICO_NUM).select(KEY_IMAGE).first();
            if(temp != null){
                String src = temp.attr("src");
                final Matcher matcher = pattern.matcher(src);
                if(matcher.find()){
                    try {
                        resource = Integer.parseInt(matcher.group());
                    } catch (NumberFormatException ex){}
                }
            }
        }
        return resource;
    }

    private String parseDate(Elements onlineElements) {
        String date = null;
        if(onlineElements.size() > MINUTE_NUM){
            date = onlineElements.get(MINUTE_NUM).text();
        }
        return date;
    }

    private String parseName(Elements elements){
        String name = null;
        if (elements.size() > TEXT_NUM){
            name = elements.get(TEXT_NUM).text();
        }
        return name;
    }
}
