package com.sharkeva.pressball.dao.parser.news;

import android.media.Image;

import com.sharkeva.pressball.entities.news.ImageElement;

import org.jsoup.nodes.Element;

/**
 * Created by tarnenok on 01.02.15.
 */
public class ImageParser {

    private final String KEY_IMG_TAG = "img";

    public ImageElement parse(Element element){
        return new ImageElement(element.getElementsByTag(KEY_IMG_TAG)
                .first()
                .attr("src"));
    }
}
