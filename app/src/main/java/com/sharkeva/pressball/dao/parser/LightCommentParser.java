package com.sharkeva.pressball.dao.parser;

import com.sharkeva.pressball.entities.Comment;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by tarnenok on 15.02.15.
 */
public class LightCommentParser extends BaseCommentParser {

    protected void prettyComment(Element element){
        super.prettyComment(element);

        Elements elements = element.select(KEY_COMMENT_QUOTE);
        for (Element newElement : elements){
            newElement.wrap("<quote></quote>");
        };
    }

    public Comment parse(Element element){
        prettyComment(element);

        Comment comment = new Comment();
        Element el = element.select(KEY_NAME).first();
        Node node = el;
        List<Node> nodes = el.childNodes();
        comment.setAuthor(((Element) nodes.get(0)).text());
        comment.setDate(nodes.get(1).outerHtml());

        StringBuilder stringBuilder = new StringBuilder("<div>");
        while (node != null) {
            node = node.nextSibling();
            if (node == null) { break; }
            stringBuilder.append(node.outerHtml());
        }
        stringBuilder.append("</div>");
        comment.setText(stringBuilder.toString());
        return comment;
    }
}
