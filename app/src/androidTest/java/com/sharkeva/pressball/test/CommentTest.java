package com.sharkeva.pressball.test;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.QuoteSpan;

import com.sharkeva.pressball.R;

import junit.framework.TestCase;

import org.junit.Test;
import org.xml.sax.XMLReader;

/**
 * Created by tarnenok on 21.02.15.
 */
public class CommentTest extends TestCase {
    private final String str = "dgdf\n<quote>123456</quote>";

    private final String str1 =
    "<div> <br><quote class=\"quote\">"
    + "<b>\"Бобруйчанин 1980\" писал(а):</b>"
    + "<br>Мотор - красавцы!"
    + "<br>Побеждают и, как и в прошлом сезоне (с Веспремом), договорятся на домашнюю победу с Виве и выйдут в ПО."
    + "<br>Штохла полякам подарят)))"
    + "</quote><br><br>Мотор - фавориты дома, по 1,8, тогда как победа поляков за 2,3.<br>Начали! Мотор!!! </div>";

    @Test
    public void testQoute(){
        Spanned spannable = Html.fromHtml(str, new CommentImageGetter(), new CommentTagHandler());
    }
}


class CommentTagHandler implements Html.TagHandler {
    private final String QUOTE = "quote";

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(tag.equalsIgnoreCase(QUOTE)){
            int length = output.length();
            if(opening){
                output.setSpan(new QuoteSpan(),
                        length, length, Spanned.SPAN_MARK_MARK);
            }else {
                Object obj = getLast(output, QuoteSpan.class);
                int where = output.getSpanStart(obj);
                output.removeSpan(obj);
                if (where != length) {
                    output.setSpan(new QuoteSpan(),
                            where, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            for(int i = objs.length;i>0;i--) {
                if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i-1];
                }
            }
            return null;
        }
    }

    public final static Spannable revertSpanned(Spanned stext) {
        Object[] spans = stext.getSpans(0, stext.length(), Object.class);
        Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
        if (spans != null && spans.length > 0) {
            for(int i = spans.length - 1; i >= 0; --i) {
                ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
            }
        }

        return ret;
    }
}

class CommentImageGetter implements Html.ImageGetter{
    @Override
    public Drawable getDrawable(String source) {
        return null;
    }
}
