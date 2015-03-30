package com.sharkeva.pressball.test;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tarnenok on 12.02.15.
 */
public class ParserTest extends TestCase {
    @Test
    public void testImageResourceParser(){
        final Pattern pattern = Pattern.compile("\\d+(?=_\\w+.gif)");
        final String url = "http://www.pressball.by/images/textonline/10_ico.gif";
        final Matcher matcher = pattern.matcher(url);
        if(matcher.find()){
            matcher.group(1);
        }
    }
}
