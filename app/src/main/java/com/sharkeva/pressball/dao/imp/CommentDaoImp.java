package com.sharkeva.pressball.dao.imp;

import com.sharkeva.pressball.dao.parser.BaseCommentParser;
import com.sharkeva.pressball.dao.parser.CommentParser;
import com.sharkeva.pressball.entities.Comment;
import com.sharkeva.pressball.entities.CommentData;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tarnenok on 23.02.15.
 */
public class CommentDaoImp {
    private final String KEY_POST = ".post";
    private BaseCommentParser commentParser;

    private final String KEY_COMMENT_COUNT = ".pagination";
    private final Pattern pattern = Pattern.compile("[0-9]+");

    public CommentDaoImp() {
        commentParser = new CommentParser();
    }

    public CommentData update(Document document){
        ArrayList<Comment> comments = new ArrayList<>();
        for (Element comment : document.select(KEY_POST)){
            comments.add(commentParser.parse(comment));
        }

        CommentData commentData = new CommentData();

        commentData.setComments(comments);
        commentData.setCommentCount(getCommentCount(document));

        return commentData;
    }



    private int getCommentCount(Document document){
        int commentCount = 0;
        Element element = document.select(KEY_COMMENT_COUNT).first();
        if (element != null){
            Matcher m = pattern.matcher(element.text());
            if(m.find()){
                commentCount = Integer.valueOf(m.group());
            }
        }
        return commentCount;
    }
}
