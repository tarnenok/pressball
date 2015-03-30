package com.sharkeva.pressball.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sharkeva.pressball.R;
import com.sharkeva.pressball.entities.Category;

import java.util.ArrayList;

/**
 * Created by tarnenok on 11.01.15.
 */
public class NavigationAdapter extends ArrayAdapter<Category> {

    public NavigationAdapter(Context context, int resource, ArrayList<Category> arrayList) {
        super(context, resource, arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);
        String name = category.getName();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.navigation_item, parent, false);
        }
        TextView nameTextView = (TextView)convertView.findViewById(R.id.navigation_name);
        nameTextView.setText(name);
        return convertView;
    }
}
