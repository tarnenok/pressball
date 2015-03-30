package com.sharkeva.pressball.services;

import android.os.AsyncTask;
import android.util.Log;

import com.sharkeva.pressball.dao.imp.CommentDaoImp;
import com.sharkeva.pressball.entities.Comment;
import com.sharkeva.pressball.entities.CommentData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tarnenok on 23.02.15.
 */
public class CommentService {
    private CommentDaoImp daoImp;
    private String baseUrl;

    private boolean firstStart = true;
    private Lock lock;

    public CommentService(final String url) {
        this.daoImp = new CommentDaoImp();
        this.baseUrl = url;
        lock = new ReentrantLock();

        //вот так вот, потому что этот ебаный прессболл редиректит на другой урл
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                     lock.lock();
                     baseUrl = Jsoup.connect(url).timeout(0).followRedirects(true).execute().url().toString();
                    Log.i("URL",baseUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
                return null;
            }
        }.execute();

    }

    private final int COMMENTS_ON_PAGE = 25;
    private final int MAX_COMMENT_PAGE_COUNT = 1000;
    private int currentPage = -1;

    public CommentService(String commentPage, boolean csFirstStart, int csCurrentPage) {
        this(commentPage);

        this.firstStart = csFirstStart;
        this.currentPage = csCurrentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isFirstStart() {
        return firstStart;
    }

    private boolean isBusy = false;
    public boolean Busy(){
        return isBusy;
    }

    public CommentData update() throws IOException {
        Log.i("CS", "update");

        firstStart = false;
        currentPage = MAX_COMMENT_PAGE_COUNT;
        String commentsUrl = baseUrl + "&sd=a&start=" + currentPage*COMMENTS_ON_PAGE;
        Document context = Jsoup
                .connect(commentsUrl)
                .timeout(0)
                .get();
        CommentData data = daoImp.update(context);
        Collections.reverse(data.getComments());
        currentPage = data.getCommentCount()/COMMENTS_ON_PAGE;
        return data;
    }

    public List<Comment> getMore() throws IOException {

        List<Comment> comments = null;
        if(lock.tryLock()){
            isBusy = true;
            currentPage--;
            Log.i("CS", "load more");
            if(currentPage >= 0){
                String commentsUrl = baseUrl + "&sd=a&start=" + currentPage*COMMENTS_ON_PAGE;
                Document context = Jsoup
                        .connect(commentsUrl)
                        .get();
                comments = daoImp.update(context).getComments();
                Collections.reverse(comments);
            }

            if(firstStart){
                comments = update().getComments();
                if(comments.size() > 5){
                    comments = comments.subList(5 , comments.size());
                }else {
                    comments = null;
                }

            }
            isBusy = false;
            lock.unlock();
        }
        return comments;
    }
}
