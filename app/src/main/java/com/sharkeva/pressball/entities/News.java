package com.sharkeva.pressball.entities;

import com.sharkeva.pressball.entities.news.NewsElement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tarnenok on 03.02.15.
 */
public class News implements Serializable {
    private String Title;
    private List<NewsElement> newsElements;
    private CommentData comments;
    private String commentPage;

    public String getCommentPage() {
        return commentPage;
    }

    public void setCommentPage(String commentPage) {
        this.commentPage = commentPage;
    }

    public String getTitle() {
        return Title;
    }

    public List<NewsElement> getNewsElements() {
        return newsElements;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setNewsElements(List<NewsElement> newsElements) {
        this.newsElements = newsElements;
    }

    public News(String title, List<NewsElement> newsElements) {
        Title = title;
        this.newsElements = newsElements;
    }

    public CommentData getComments() {
        return comments;
    }

    public void setComments(CommentData comments) {
        this.comments = comments;
    }

    public News() {
    }
}
