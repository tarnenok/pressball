package com.sharkeva.pressball.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharkeva.pressball.R;
import com.sharkeva.pressball.entities.Newsflash;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tarnenok on 13.01.15.
 */
public class NewsflashAdapter extends RecyclerView.Adapter<NewsflashAdapter.ViewHolder> {

    private Newsflash[] newsflashs;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageView categoryImage;
        public TextView category;
        public TextView views;
        public TextView date;
        public CardView cardView;

        public ImageView withPhoto;
        public ImageView withVideo;
        public TextView live;

        public ViewHolder(View itemView) {
            super(itemView);

            category = (TextView)itemView.findViewById(R.id.category_name);
            categoryImage = (ImageView)itemView.findViewById(R.id.category_image);
            title = (TextView)itemView.findViewById(R.id.news_title);
            views = (TextView)itemView.findViewById(R.id.news_views);
            date = (TextView)itemView.findViewById(R.id.news_date);
            cardView = (CardView)itemView.findViewById(R.id.card_view);

            withPhoto = (ImageView)itemView.findViewById(R.id.news_photo);
            withVideo = (ImageView)itemView.findViewById(R.id.news_video);
            live = (TextView)itemView.findViewById(R.id.news_live);
        }
    }

    public NewsflashAdapter(Newsflash[] newsflashs, Context context) {
        this.newsflashs = newsflashs;
        this.context = context;
    }

    @Override
    public NewsflashAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_news, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsflashAdapter.ViewHolder holder, int position) {
        Newsflash element = newsflashs[position];
        holder.category.setText(element.getCategory().getName());
        holder.title.setText(element.getTitle());
        if(element.getCommentsCount() == 0){
            holder.views.setText(String.valueOf(element.getViews()));
        }else{
            holder.views.setText(element.getCommentsCount()
                    + "/"
                    + element.getViews());
        }

        if(element.getTime() != null){
            holder.date.setText(new SimpleDateFormat("HH:mm").format(element.getTime()));
        }

        if(element.getCategory().getClassifier() != ""){
            holder.categoryImage.setImageResource(context.getResources().getIdentifier(
                    element.getCategory().getClassifier()
                    ,"drawable"
                    , context.getPackageName()
            ));
        }

        if(element.isMain()){
            holder.cardView.setCardBackgroundColor(
                    context.getResources().getColor(R.color.lightPressballColor));
        }else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        holder.live.setVisibility(View.GONE);
        holder.withPhoto.setVisibility(View.GONE);
        holder.withVideo.setVisibility(View.GONE);
        if(element.isLive()){
            holder.live.setVisibility(View.VISIBLE);
        }
        if(element.isWithPhoto()){
            holder.withPhoto.setVisibility(View.VISIBLE);
        }
        if(element.isWithVideo()){
            holder.withVideo.setVisibility(View.VISIBLE);
        }
    }

    public Newsflash getItem(int position){
        return newsflashs[position];
    }

    @Override
    public int getItemCount() {
        return newsflashs.length;
    }
}
