package com.sharkeva.pressball.dao.parser.news;

import com.sharkeva.pressball.entities.news.ImageElement;
import com.sharkeva.pressball.entities.news.VideoElement;

import org.jsoup.nodes.Element;

/**
 * Created by tarnenok on 02.02.15.
 */
public class VideoParser {
    private final String KEY_VIDEO_TAG = "iframe";

    public VideoElement parse(Element element){
        return new VideoElement(element.getElementsByTag(KEY_VIDEO_TAG)
                .first()
                .outerHtml());
    }
}
