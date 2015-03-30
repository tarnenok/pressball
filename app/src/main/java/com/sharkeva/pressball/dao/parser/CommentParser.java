package com.sharkeva.pressball.dao.parser;

import com.sharkeva.pressball.entities.Comment;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;

/**
 * Created by tarnenok on 23.02.15.
 */
public class CommentParser extends BaseCommentParser {

    private final String KEY_CONTENT = ".inner .postbody";
    private final String KEY_AUTHOR = ".author strong";
    private final String KEY_TEXT = ".content";
    private final String KEY_BLOCKQOUTE = "blockquote";



    @Override
    public Comment parse(Element element) {
        Comment comment = new Comment();
        prettyComment(element);

        Element content = element.select(KEY_CONTENT).first();
        Element name = content.select(KEY_AUTHOR).first();
        comment.setAuthor(name.select("a").first().text());
        comment.setDate(getCommentDate(name.nextSibling().outerHtml()));
        setCommentText(comment, content);
        return comment;
    }

    private String getCommentDate(String str){
        int length = str.length();
        String date = str;
        if (length > 3){
            date = str.substring(3);
        }
        return date;
    }

    private void setCommentText(Comment comment, Element content) {
        Element text = content.select(KEY_TEXT).first();
        text.select(KEY_BLOCKQOUTE).tagName(COMMENT_QUOTE);
        text.select("cite").tagName("b").after("<br>");
        comment.setText(text.outerHtml());
    }
}
