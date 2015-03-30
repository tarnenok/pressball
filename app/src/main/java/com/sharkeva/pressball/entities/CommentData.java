package com.sharkeva.pressball.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tarnenok on 25.02.15.
 */
public class CommentData implements Serializable {
    private List<Comment> comments;
    private int commentCount;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
