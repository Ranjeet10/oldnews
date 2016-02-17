package com.bidhee.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.widget.FragmentPagerAdapter;

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

    SessionManager sessionManager;



    public static FragmentAllNews createNewInstance() {
        FragmentAllNews fragmentAllNews = new FragmentAllNews();
        return fragmentAllNews;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_all_news, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewPager();
        setTabLayout();
    }

    private void setTabLayout() {
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

    private void setViewPager() {

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager());

        //set the tab according the news switched to
        if (sessionManager.getSwitchedNewsValue() == 0) {
            for (int i = 0; i < StaticStorage.republicaTab.length; i++) {
                adapter.addFragment(SwipableFragment.createNewInstance(StaticStorage.republicaTab[i]), StaticStorage.republicaTab[i]);
            }

        } else {
            for (int i = 0; i < StaticStorage.nagarikTab.length; i++) {
                adapter.addFragment(SwipableFragment.createNewInstance(StaticStorage.nagarikTab[i]), StaticStorage.nagarikTab[i]);
            }
        }

        viewPager.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
