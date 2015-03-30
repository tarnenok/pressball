package com.sharkeva.pressball.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.sharkeva.pressball.entities.Newsflash;
import com.sharkeva.pressball.services.NFUpdateCenter;
import com.sharkeva.pressball.ui.ToolbarActivity;
import com.sharkeva.pressball.ui.adapter.NewsflashAdapter;
import com.sharkeva.pressball.ui.OnFragmentCreatedListener;
import com.sharkeva.pressball.utils.ToolbarUtils;

/**
 * Created by tarnenok on 19.01.15.
 */
public abstract class BaseNewsFlashFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    protected NewsflashAdapter newsflashAdapter;
    protected Newsflash[] newsflashes;

    protected NFUpdateCenter updateService;

    public final LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public void setRecyclerViewOnScrollListener(final SwipeRefreshLayout swipeRefreshLayout){
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0){
                    swipeRefreshLayout.setEnabled(true);
                }else{
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    public abstract void initializeNFCenter(NFUpdateCenter center);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((OnFragmentCreatedListener)getActivity()).onFragmentCreated(this);
    }
}
