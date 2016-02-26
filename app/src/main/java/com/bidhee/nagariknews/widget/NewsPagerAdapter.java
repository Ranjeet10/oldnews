package com.bidhee.nagariknews.widget;

/**
 * Created by ronem on 2/9/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.views.fragments.FragmentBreakingAndLatestNews;
import com.bidhee.nagariknews.views.fragments.SwipableFragment;

import java.util.ArrayList;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<TabModel> tabs;

    public NewsPagerAdapter(FragmentManager fm, ArrayList<TabModel> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {

        // always let the adapter class to create fragment
        //never make fragment list as arguments of the adapter constructor
        //it helps to prevent the memory leakage
        Log.i("returned", tabs.get(position).cat_name);
        switch (position) {
            case 0:
                return FragmentBreakingAndLatestNews.createNewInstance(tabs.get(position));

            default:
                return SwipableFragment.createNewInstance(tabs.get(position));

        }


    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabs.get(position).cat_name;
    }

}
