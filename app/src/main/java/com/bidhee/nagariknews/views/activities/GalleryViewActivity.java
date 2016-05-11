package com.bidhee.nagariknews.views.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.BaseThemeActivity;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.model.epaper.Epaper;
import com.bidhee.nagariknews.model.epaper.Page;
import com.bidhee.nagariknews.widget.EpaperPagerAdapter;
import com.bidhee.nagariknews.widget.PhotosCartoonPagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/29/16.
 */
public class GalleryViewActivity extends BaseThemeActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_header_layout)
    RelativeLayout toolbarHeaderLayout;
    @Bind(R.id.epaper_viewpager)
    ViewPager epaperViewpager;

    Epaper epaper;
    ArrayList<Multimedias> multimedias;
    EpaperPagerAdapter epaperPagerAdapter;
    PhotosCartoonPagerAdapter photosCartoonPagerAdapter;
    private int POSITION;

    String galleryType;
    int TYPE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * main content view for the {@link GalleryViewActivity}
         */
        setContentView(R.layout.epaper_activity_layout);
        ButterKnife.bind(this);

        galleryType = getIntent().getStringExtra(StaticStorage.KEY_GALLERY_TYPE);

        TYPE = getIntent().getIntExtra(StaticStorage.FOLDER_TYPE, 1);
        if (galleryType.equals(StaticStorage.KEY_PHOTO_CARTOON)) {
            multimedias = getIntent().getExtras().getParcelableArrayList(StaticStorage.KEY_PHOTO_CARTOON);
            POSITION = getIntent().getIntExtra(StaticStorage.KEY_PHOTOS_CARTOON_POSITION, 0);

        } else if (galleryType.equals(StaticStorage.KEY_EPAPER)) {
            epaper = getIntent().getExtras().getParcelable(StaticStorage.KEY_EPAPER);
            if (epaper == null) {
                setPageTitle(Dashboard.currentNewsType, 0);
            }
        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarHeaderLayout.setVisibility(View.GONE);
        setViewPagerData();

    }

    private void setViewPagerData() {
        if (galleryType.equals(StaticStorage.KEY_EPAPER)) {
            epaperPagerAdapter = new EpaperPagerAdapter(getSupportFragmentManager(), (ArrayList<Page>) epaper.getPages());
            epaperViewpager.setAdapter(epaperPagerAdapter);
        } else {
            photosCartoonPagerAdapter = new PhotosCartoonPagerAdapter(getSupportFragmentManager(), multimedias, TYPE);
            epaperViewpager.setAdapter(photosCartoonPagerAdapter);
            epaperViewpager.setCurrentItem(POSITION);
        }
        epaperViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setPageTitle(Dashboard.currentNewsType, position + 1);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPageTitle(String currentNewsType, int currentPageNumber) {
        if (galleryType.equals(StaticStorage.KEY_EPAPER)) {
            getSupportActionBar().setTitle(currentNewsType + " (" + epaper.getDate() + ") " + currentPageNumber + "/" + epaper.getPages().size());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
