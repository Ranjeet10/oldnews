package com.bidhee.nagariknews.views.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.epaper.Epaper;
import com.bidhee.nagariknews.model.epaper.Page;
import com.bidhee.nagariknews.widget.EpaperPagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/29/16.
 */
public class EpaperActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.epaper_viewpager)
    ViewPager epaperViewpager;

    Epaper epaper;
    EpaperPagerAdapter epaperPagerAdapter;
    SessionManager sessionManager;
    String currentNewsType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epaper_activity_layout);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);

        currentNewsType = sessionManager.getSwitchedNewsValue() == 0 ?
                getResources().getString(R.string.republica) :
                getResources().getString(R.string.nagarik);

        epaper = getIntent().getExtras().getParcelable(StaticStorage.KEY_EPAPER);

        if (epaper == null) {
            setPageTitle(currentNewsType, 0);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViewPagerData();

    }

    private void setViewPagerData() {
        epaperPagerAdapter = new EpaperPagerAdapter(getSupportFragmentManager(), (ArrayList<Page>) epaper.getPages());
        epaperViewpager.setAdapter(epaperPagerAdapter);
        epaperViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                setPageTitle(currentNewsType, position + 1);
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
        getSupportActionBar().setTitle(currentNewsType + " ("+ epaper.getDate() + ") " + currentPageNumber + "/" + epaper.getPages().size());
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
