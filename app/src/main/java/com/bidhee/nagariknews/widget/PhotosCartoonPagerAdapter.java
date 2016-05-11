package com.bidhee.nagariknews.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.views.fragments.FragmentGallerySwipable;

import java.util.ArrayList;

/**
 * Created by ronem on 5/11/16.
 */
public class PhotosCartoonPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Multimedias> pages;
    int type ;

    public PhotosCartoonPagerAdapter(FragmentManager fm, ArrayList<Multimedias> pages,int type) {
        super(fm);
        this.pages = pages;
        this.type = type;
    }


    @Override
    public Fragment getItem(int position) {
        return FragmentGallerySwipable.createNewInstance(pages.get(position),type);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
