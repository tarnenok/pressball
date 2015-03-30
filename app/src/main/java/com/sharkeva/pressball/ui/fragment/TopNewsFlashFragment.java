package com.sharkeva.pressball.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharkeva.pressball.R;
import com.sharkeva.pressball.dao.imp.TopNewsFlashDaoImp;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.entities.Newsflash;
import com.sharkeva.pressball.services.Listener;
import com.sharkeva.pressball.services.NFUpdateCenter;
import com.sharkeva.pressball.ui.activity.NewsActivity;
import com.sharkeva.pressball.ui.adapter.NewsflashAdapter;
import com.sharkeva.pressball.ui.RecyclerItemClickListener;

import org.jsoup.nodes.Document;

/**
 * Created by tarnenok on 19.01.15.
 */
public class TopNewsFlashFragment extends BaseNewsFlashFragment {
    private static final String KEY_TOPNEWSFLASHES = "topNewsFlashes";
    private TopNewsFlashDaoImp topNewsFlashDaoImp;

    public static TopNewsFlashFragment newInstance(NFUpdateCenter service) {
        TopNewsFlashFragment fragment = new TopNewsFlashFragment();

        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_TOPNEWSFLASHES, newsflashes);
    }

    @Override
    public void initializeNFCenter(NFUpdateCenter center){
        updateService = center;

        updateService.addOnInitialize(new Listener<Category>() {
            @Override
            public void onExecute(Category context) {
                topNewsFlashDaoImp.initialize(context);
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

        topNewsFlashDaoImp = new TopNewsFlashDaoImp(getActivity());

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


        if(savedInstanceState != null){
            Object[] objects = (Object[])savedInstanceState.getSerializable(KEY_TOPNEWSFLASHES);
            if(objects != null){
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
        newsflashes = topNewsFlashDaoImp.update(context).toArray(new Newsflash[0]);
        newsflashAdapter = new NewsflashAdapter(newsflashes, getActivity());
        recyclerView.setAdapter(newsflashAdapter);
    }
}
