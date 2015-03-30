package com.sharkeva.pressball.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharkeva.pressball.R;
import com.sharkeva.pressball.entities.OnlineData;

/**
 * Created by tarnenok on 06.02.15.
 */
public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ViewHolder> {

    private OnlineData[] data;
    private Context context;

    public OnlineAdapter(OnlineData[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OnlineData element = data[position];
        holder.name.setText(element.getName());
        if(element.isMain()){
            holder.name.setTypeface(null, Typeface.BOLD);
        }else {
            holder.name.setTypeface(null, Typeface.NORMAL);
        }
        holder.date.setText(element.getDate());
    }



    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView actionImage;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.online_name);
            actionImage = (ImageView)itemView.findViewById(R.id.online_action);
            date = (TextView)itemView.findViewById(R.id.online_date);
        }
    }
}
