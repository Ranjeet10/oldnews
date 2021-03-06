package com.bajratechnologies.nagariknews.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.model.TabModel;
import com.bajratechnologies.nagariknews.views.activities.Dashboard;
import com.bajratechnologies.nagariknews.widget.NewsPagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/9/16.
 */
public class FragmentAllNews extends Fragment {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.all_news_viewpager)
    ViewPager viewPager;

    ArrayList<TabModel> tabs;


    public static FragmentAllNews createNewInstance() {
        FragmentAllNews fragmentAllNews = new FragmentAllNews();
        return fragmentAllNews;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (Dashboard.sessionManager.getSwitchedNewsValue()) {
            case 1:
                tabs = StaticStorage.getTabData(0);
                break;
            case 2:
                tabs = StaticStorage.getTabData(1);
                break;
            case 3:
                tabs = StaticStorage.getTabData(2);
                break;
        }

        Log.d("called", "onCreate");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_all_news, container, false);
        ButterKnife.bind(this, fragmentView);

        Log.d("called", "onCreateView");
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("called", "onViewCreated");
        setViewPager(tabs);
        setTabLayout();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("called", "onattach");
    }

    private void setTabLayout() {
        switch (Dashboard.sessionManager.getSwitchedNewsValue()) {
            case 1:
                tabLayout.setBackgroundColor(getResources().getColor(R.color.republicaColorPrimaryDark));
                break;
            case 2:
                tabLayout.setBackgroundColor(getResources().getColor(R.color.nagarikColorPrimaryDark));
                break;
            case 3:
                tabLayout.setBackgroundColor(getResources().getColor(R.color.sukrabarColorPrimaryDark));
                break;
        }
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setViewPager(ArrayList<TabModel> tabs) {
        // since viewpager is within the fragment  we use getChildFragmentManager instead of getSupportFragmentManger

        NewsPagerAdapter adapter = new NewsPagerAdapter(getChildFragmentManager(), tabs);
        viewPager.setAdapter(adapter);

//        viewPager.setClipToPadding(false);
//        viewPager.setPadding(30, 0, 30, 0);
//        viewPager.setOffscreenPageLimit(1);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
        Log.d("called", "onDestroyView");
    }
}
