package com.sharkeva.pressball.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharkeva.pressball.R;
import com.sharkeva.pressball.dao.imp.NewsFlashDaoImp;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.entities.Newsflash;
import com.sharkeva.pressball.services.Listener;
import com.sharkeva.pressball.services.NFUpdateCenter;
import com.sharkeva.pressball.ui.TextDrawable;
import com.sharkeva.pressball.ui.activity.NewsActivity;
import com.sharkeva.pressball.ui.adapter.NewsflashAdapter;
import com.sharkeva.pressball.ui.RecyclerItemClickListener;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tarnenok on 12.01.15.
 */
public class NewsFlashFragment extends BaseNewsFlashFragment {
    private NewsFlashDaoImp newsFlashDaoImp;

    private static final String KEY_NEWSFLASHES = "newsFlashes";

    public static NewsFlashFragment newInstance(NFUpdateCenter service) {
        NewsFlashFragment fragment = new NewsFlashFragment();

        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NEWSFLASHES, newsflashes);
    }

    @Override
    public void initializeNFCenter(NFUpdateCenter center){
        updateService = center;
        updateService.addOnInitialize(new Listener<Category>() {
            @Override
            public void onExecute(Category context) {
                newsFlashDaoImp.initialize(context);
                if(updateService.isFirstLoad()){
                    recyclerView.setAdapter(new NewsflashAdapter(new Newsflash[0], getActivity()));
                }
            }
        });

        updateService.addOnEndUpdate(new Listener<Document>() {
            @Override
            public void onExecute(Document context) {
                update(context);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsFlashDaoImp = new NewsFlashDaoImp(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.view_news);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra(NewsActivity.KEY_NEWS_FLASH, newsflashes[position]);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .drawableProvider(new FlexibleDividerDecoration.DrawableProvider() {
                            @Override
                            public Drawable drawableProvider(int i, RecyclerView recyclerView) {
                                NewsflashAdapter adapter = (NewsflashAdapter)recyclerView.getAdapter();
                                if(i + 1 < recyclerView.getAdapter().getItemCount()
                                        && adapter.getItem(i).getDate().compareTo(adapter.getItem(i + 1).getDate()) != 0){

                                    TextDrawable text = new TextDrawable(getActivity());
                                    text.setText(
                                            new SimpleDateFormat("dd LLLL yyyy", Locale.getDefault()).format(adapter.getItem(i + 1).getDate())
                                    );
                                    text.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.date_text));
                                    text.setTextColor(getResources().getColor(R.color.ripple_material_light));

                                    return text;
                                }
                                return new TextDrawable(getActivity());
                            }
                        })
                        .margin(getActivity().getResources().getDimensionPixelSize(R.dimen.date_offset), 0)
                        .build()
        );

        if(savedInstanceState != null){
            Object[] objects = (Object[])savedInstanceState.getSerializable(KEY_NEWSFLASHES);
            if (objects != null){
                newsflashes = new Newsflash[objects.length];
                for(int i = 0; i < newsflashes.length; i++){
                    newsflashes[i] = (Newsflash)objects[i];
                }
                newsflashAdapter = new NewsflashAdapter(newsflashes, getActivity());
                recyclerView.setAdapter(newsflashAdapter);
            }
        }

        return view;
    }

    private void update(Document context){
        newsflashes = newsFlashDaoImp.update(context).toArray(new Newsflash[0]);
        newsflashAdapter = new NewsflashAdapter(newsflashes, getActivity());
        recyclerView.setAdapter(newsflashAdapter);
    }
}
