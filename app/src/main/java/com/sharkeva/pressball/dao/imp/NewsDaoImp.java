package com.sharkeva.pressball.dao.imp;

import com.sharkeva.pressball.dao.parser.LightCommentParser;
import com.sharkeva.pressball.dao.parser.news.ImageParser;
import com.sharkeva.pressball.dao.parser.news.OnlineParser;
import com.sharkeva.pressball.dao.parser.news.TextParser;
import com.sharkeva.pressball.dao.parser.news.VideoParser;
import com.sharkeva.pressball.entities.Comment;
import com.sharkeva.pressball.entities.CommentData;
import com.sharkeva.pressball.entities.news.NewsElement;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tarnenok on 31.01.15.
 */
public class NewsDaoImp {
    Document context;

    private final String KEY_NEWS_ELEMENT1 = ".inner_news";
    private final String KEY_NEWS_ELEMENT2 = ".lenta_news";

    private final String KEY_NOT_TEXT_ELEMENT = "div";
    private final String KEY_IMG = "img";
    private final String KEY_ANSWER = "answer";
    private final String KEY_QUESTION = "question";
    private final String KEY_ONLINE = "online";

    private final String KEY_COMMENTS_BOX = ".comment_list .item";
    private final String KEY_COMMENTS_PAGE = ".dobav_comm a";

    private ImageParser imageParser;
    private VideoParser videoParser;
    private TextParser textParser;
    private OnlineParser onlineParser;

    private LightCommentParser commentParser;

    private ArrayList<NewsElement> elements;
    private StringBuilder tempTextElement;

    public NewsDaoImp(Document context) {
        this.context = context;
        elements = new ArrayList<NewsElement>();
        tempTextElement = new StringBuilder();

        imageParser = new ImageParser();
        videoParser = new VideoParser();
        textParser = new TextParser();
        onlineParser = new OnlineParser();

        commentParser = new LightCommentParser();
    }

    public List<NewsElement> load(){
        tempTextElement = new StringBuilder();

        boolean isText = false;

        for(Node node : getHtmlElements().first().childNodes()){
            if(node instanceof TextNode){
                tempTextElement.append(((TextNode) node).text());
                isText = true;
            } else if (!(node instanceof DataNode)){
                Element element = (Element)node;
                switch (element.tagName()){
                    case "div":
                        switch (element.className()){
                            case KEY_IMG:
                                if(element.getElementsByTag("img").first() != null){
                                    isText = tryInitializeTextElement(isText);
                                    elements.add(imageParser.parse(element));
                                }
                                if(element.getElementsByTag("iframe").first() != null){
                                    isText = tryInitializeTextElement(isText);
                                    elements.add(videoParser.parse(element));
                                }
                                break;
                            case KEY_ONLINE:
                                isText = tryInitializeTextElement(isText);
                                elements.add(onlineParser.parse(element));
                                break;
                            case KEY_ANSWER:
                                tempTextElement.append(element.outerHtml());
                                isText = true;
                                break;
                            case KEY_QUESTION:
                                tempTextElement.append(element.outerHtml());
                                isText = true;
                                break;
                        }
                        break;
                    case "table":
                        break;
                    case "center":
                        if(element.getElementsByTag("iframe").first() != null){
                            isText = tryInitializeTextElement(isText);
                            elements.add(videoParser.parse(element));
                        }
                        break;
                    case "em":
                        tempTextElement.append(textParser.parseEm(element.outerHtml()));
                        isText = true;
                        break;
                    default:
                        tempTextElement.append(element.outerHtml());
                        isText = true;
                        break;
                }
            }
        }
        if(isText){
            elements.add(textParser.parse(tempTextElement.toString()));
        }
        return elements;
    }

    private boolean tryInitializeTextElement(boolean isText){
        if(isText){
            elements.add(textParser.parse(tempTextElement.toString()));
            tempTextElement = new StringBuilder();
        }
        isText = false;
        return isText;
    }

    private Elements getHtmlElements() {
        Elements htmlElements = context.select(KEY_NEWS_ELEMENT1);
        if(htmlElements.size() == 0){
            htmlElements = context.select(KEY_NEWS_ELEMENT2);
        }
        return htmlElements;
    }

    public String getTitle(){
        if(context != null){
            return context.select(".center h1").first().text();
        }
        return null;
    }



    public CommentData getComments(){
        ArrayList<Comment> comments = new ArrayList<>();
        if(context != null){
            Elements elements = context.select(KEY_COMMENTS_BOX);
            for(Element element : elements){
                comments.add(commentParser.parse(element));
            }
        }
        CommentData commentData = new CommentData();
        commentData.setComments(comments);
        commentData.setCommentCount(getCommentsCount(context));

        return commentData;
    }

    private final String KEY_COMMENTS_COUNT = ".mb10 .middle h1";
    private final Pattern pattern = Pattern.compile("([0-9]+)");
    private int getCommentsCount(Document document){
        int commentCount = 0;
        Element element = document.select(KEY_COMMENTS_COUNT).first();
        Matcher matcher = pattern.matcher(element.text());
        if(matcher.find()){
            commentCount = Integer.valueOf(matcher.group());
        }
        return commentCount;
    }

    public String getCommentsPage(){
        String commentsPage = null;
        if(context != null){
            commentsPage = context.select(KEY_COMMENTS_PAGE).first().attr("href");
        }
        return commentsPage;
    }

}
