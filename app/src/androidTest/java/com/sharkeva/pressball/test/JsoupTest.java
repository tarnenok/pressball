package com.sharkeva.pressball.test;

import android.test.AndroidTestCase;

import com.sharkeva.pressball.utils.PressballUtils;

import junit.framework.TestCase;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by tarnenok on 19.01.15.
 */
public class JsoupTest extends TestCase {

    private final String testUrl = "http://www.pressball.by/pbonline/hockey/80601";

    @Test
    public void testLoadPage() throws IOException {
        Connection.Response response = Jsoup.connect(testUrl)
                .userAgent("Mozilla")
                .execute();
        String str = new String(response.bodyAsBytes(), "utf-8");
        str = str.replaceAll("<div class=\"cl\"/></div>", "");
        Document document = Jsoup.parse(str);

//        URL url = new URL(testUrl);
//        Reader r = new InputStreamReader(url.openConnection().getInputStream(), "windows-1251");
//        StringBuilder buf = new StringBuilder();
//        while (true) {
//            int ch = r.read();
//            if (ch < 0)
//                break;
//            buf.append((char) ch);
//        }
//        String str = buf.toString();
    }

    public void testXmlhttprequest() throws IOException {
        Connection.Response res = Jsoup.connect("http://www.pressball.by/includes/online.update.php")
                .data("online_id", "56")
        .method(Connection.Method.POST)
        .header("Accept", "application/x-www-form-urlencoded")
        .header("X-Requested-With", "XMLHttpRequest")
        .execute();
    }
}
