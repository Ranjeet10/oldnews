package com.bajratechnologies.nagariknews.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bajratechnologies.nagariknews.model.Multimedias;
import com.bajratechnologies.nagariknews.views.fragments.FragmentBannerSlider;
import com.bajratechnologies.nagariknews.views.fragments.FragmentGallerySwipable;

import java.util.ArrayList;

/**
 * Created by ronem on 5/11/16.
 */
public class PhotosCartoonPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Multimedias> pages;
    int type;
    Boolean isForBanner;

    public PhotosCartoonPagerAdapter(FragmentManager fm, ArrayList<Multimedias> pages, int type, Boolean isForBanner) {
        super(fm);
        this.pages = pages;
        this.type = type;
        this.isForBanner = isForBanner;
    }


    @Override
    public Fragment getItem(int position) {
        if (isForBanner) {
            return FragmentBannerSlider.newInstance(pages.get(position).getMultimediaPath());
        }
        return FragmentGallerySwipable.createNewInstance(pages.get(position), type);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
