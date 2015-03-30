package com.sharkeva.pressball.dao.parser;

import com.sharkeva.pressball.entities.Comment;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by tarnenok on 23.02.15.
 */
public abstract class BaseCommentParser {
    public static final String COMMENT_QUOTE = "quote";
    public static final String KEY_COMMENT_QUOTE = "." + COMMENT_QUOTE;
    public final String KEY_NAME = ".name";

    public abstract Comment parse(Element element);
    protected void prettyComment(Element element){
        Elements elements = element.getElementsByTag("div");
        for (Element noneElement : elements){
            if(noneElement.attr("style").contains("display: none")){
                noneElement.remove();
            }
        }
    }
}
