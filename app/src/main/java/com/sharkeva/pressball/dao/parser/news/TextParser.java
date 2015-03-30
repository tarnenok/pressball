package com.sharkeva.pressball.dao.parser.news;

import com.sharkeva.pressball.entities.news.TextElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tarnenok on 02.02.15.
 */
public class TextParser {

    private final String STYLE = "<style>" +
            ".news{font-size: 18px;}" +
            "a{text-decoration:none;color:#f17501;}" +
            "a:hover{text-decoration:none;color:#c86307;}" +
            ".question{padding-left: 15px;font-weight: bold;line-height: normal;margin-bottom: 3px;background: url(http://www.pressball.by/images/template/ico19.gif) no-repeat left 2px;}" +
            ".answer{padding-left: 15px;line-height: normal;margin-bottom: 0px;}" +
            "</style>";

    public TextElement parse(String str){
        StringBuilder stringBuilder = new StringBuilder("<div class='news'>");
        stringBuilder.append(str);
        stringBuilder.append("</div>");
        stringBuilder.append(STYLE);
        return new TextElement(stringBuilder.toString());
    }

    public String parseEm(String str){
        StringBuilder stringBuilder = new StringBuilder();
        for(Node node : Jsoup.parse(str).childNodes()){
            if(node instanceof TextNode){
                stringBuilder.append("<div>");
                stringBuilder.append(((TextNode) node).text());
                stringBuilder.append("</div>");
            }else {
                stringBuilder.append(node.outerHtml());
            }
        }
        return stringBuilder.toString();
    }
}
