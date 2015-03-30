package com.sharkeva.pressball.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.QuoteSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharkeva.pressball.R;
import com.sharkeva.pressball.entities.Comment;
import com.sharkeva.pressball.ui.MutableAdapter;

import org.xml.sax.XMLReader;

import java.util.List;

/**
 * Created by tarnenok on 15.02.15.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements MutableAdapter<Comment> {
    private List<Comment> comments;
    private Context context;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.author.setText(comment.getAuthor());
        holder.date.setText(comment.getDate());

        Spanned text = Html.fromHtml(comment.getText(),
                new CommentImageGetter(context),
                new CommentTagHandler(context)
        );
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void add(Comment element) {
        comments.add(element);
        notifyDataSetChanged();
    }

    @Override
    public void removeAt(int i) {
        comments.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public void update(List<Comment> elements) {
        comments.clear();
        comments.addAll(elements);
        notifyDataSetChanged();
    }

    public List<Comment> getItems() {
        return comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView author;
        public TextView date;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            author = (TextView)itemView.findViewById(R.id.comment_author);
            date = (TextView)itemView.findViewById(R.id.comment_date);

            text = (TextView)itemView.findViewById(R.id.comment_text);
            text.setClickable(true);
            text.setMovementMethod(new LinkMovementMethod());
        }
    }
}

class CommentTagHandler implements Html.TagHandler {
    private final String QUOTE = "quote";

    private Context context;
    CommentTagHandler(Context context) {
        this.context = context;
    }

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
                    output.setSpan(new QuoteSpan(context.getResources().getColor(R.color.pressballColor)),
                            where, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

    private Context context;
    CommentImageGetter(Context context) {
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {
//        Drawable image =  Glide.with(context)
//                .load(source)
//                ;

        return null;
    }
}
