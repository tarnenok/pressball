package com.sharkeva.pressball.ui.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.sharkeva.pressball.R;
import com.sharkeva.pressball.services.NFUpdateCenter;
import com.sharkeva.pressball.ui.fragment.NewsFlashFragment;
import com.sharkeva.pressball.ui.fragment.TopNewsFlashFragment;

/**
 * Created by tarnenok on 12.01.15.
 */
public class NewsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private String[] TITLES;
    private NFUpdateCenter updateCenter;

    public NewsPagerAdapter(FragmentManager fragmentManager, Resources resources, NFUpdateCenter updateCenter) {
        super(fragmentManager);
        TITLES = resources.getStringArray(R.array.tabs);
        this.updateCenter = updateCenter;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TopNewsFlashFragment.newInstance(updateCenter);
            case 1:
                return NewsFlashFragment.newInstance(updateCenter);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
