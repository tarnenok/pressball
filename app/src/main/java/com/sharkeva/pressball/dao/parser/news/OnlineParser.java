package com.sharkeva.pressball.dao.parser.news;

import com.sharkeva.pressball.entities.Online;
import com.sharkeva.pressball.entities.OnlineData;
import com.sharkeva.pressball.entities.news.OnlineElement;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarnenok on 06.02.15.
 */
public class OnlineParser {
    private OnlineElementParser parser;
    private final String KEY_ELEMENTS1 = "#events tr";
    private final String KEY_ELEMENTS2 = ".scroll_end .events tr";
    private final String KEY_ID = "online_id";

    public OnlineParser() {
        this.parser = new OnlineElementParser();
    }

    public OnlineElement parse(Element element){
        ArrayList<OnlineData> list = new ArrayList<>();
        Elements elements = element.select(KEY_ELEMENTS1);
        if(elements.size() == 0){
            elements = element.select(KEY_ELEMENTS2);
        }

        if(elements.size() != 0){
            for (Element onlineElem : elements){
                list.add(parser.parse(onlineElem));
            }
        }

        Online online = new Online();
        online.setDataList(list);

        Element idElement = element.getElementById(KEY_ID);
        //id element doesn't exist if we update online pane
        if(idElement != null){
            online.setId(Integer.valueOf(idElement.val()));
        }

        return new OnlineElement(online);
    }
}
