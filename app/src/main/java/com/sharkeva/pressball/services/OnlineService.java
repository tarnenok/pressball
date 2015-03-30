package com.sharkeva.pressball.services;

import com.sharkeva.pressball.dao.parser.news.OnlineParser;
import com.sharkeva.pressball.entities.Online;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

/**
 * Created by tarnenok on 11.02.15.
 */
public class OnlineService {

    private OnlineParser parser;

    private final String KEY_ONLINE_ID = "online_id";

    public OnlineService() {
        parser = new OnlineParser();
    }

    public Online load(int id) throws IOException {
        Connection.Response res = Jsoup.connect("http://www.pressball.by/includes/online.update.php")
                .data("online_id", String.valueOf(id))
                .method(Connection.Method.POST)
                .header("Accept", "application/x-www-form-urlencoded")
                .header("X-Requested-With", "XMLHttpRequest")
                .execute();
        Online newOnline =((Online)(parser.parse(res.parse()).getData()));
        newOnline.setId(id);
        return newOnline;
    }
}
