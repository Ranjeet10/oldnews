package com.bajratechnologies.nagariknews.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bajratechnologies.nagariknews.model.epaper.Page;
import com.bajratechnologies.nagariknews.views.fragments.FragmentEpaperSwipable;

import java.util.ArrayList;

/**
 * Created by ronem on 2/29/16.
 */
public class EpaperPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Page> pages;


    public EpaperPagerAdapter(FragmentManager fm, ArrayList<Page> pages) {
        super(fm);
        this.pages = pages;
    }


    @Override
    public Fragment getItem(int position) {
        return FragmentEpaperSwipable.createNewInstance(pages.get(position).getPageUrl());
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
