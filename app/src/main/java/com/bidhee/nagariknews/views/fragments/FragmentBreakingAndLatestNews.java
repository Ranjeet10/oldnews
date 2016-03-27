package com.bidhee.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.BreakingAndLatestNews;
import com.bidhee.nagariknews.Utils.NewsData;
import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.widget.BreakingAndLatestnewsParentAdapter;
import com.bidhee.nagariknews.widget.EndlessScrollListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/19/16.
 */
public class FragmentBreakingAndLatestNews extends Fragment {
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    ArrayList<BreakingAndLatestNews> breakingAndLatestNewses;
    BreakingAndLatestnewsParentAdapter adapter;
    SessionManager sessionManager;

    private String categoryId;
    private String categoryName;

    public static FragmentBreakingAndLatestNews createNewInstance(TabModel tab) {

        FragmentBreakingAndLatestNews fragmentBreakingAndLatestNews = new FragmentBreakingAndLatestNews();
        Bundle box = new Bundle();
        box.putString(StaticStorage.NEWS_CATEGORY_ID, tab.cat_id);
        box.putString(StaticStorage.NEWS_CATEGORY, tab.cat_name);
        fragmentBreakingAndLatestNews.setArguments(box);

        return fragmentBreakingAndLatestNews;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());

        categoryId = getArguments().getString(StaticStorage.NEWS_CATEGORY_ID);
        categoryName = getArguments().getString(StaticStorage.NEWS_CATEGORY);

        Log.i("category", categoryId + " " + categoryName);

        breakingAndLatestNewses = sessionManager.getSwitchedNewsValue() == 0 ?
                NewsData.loadBreakingLatestNews(getActivity(), categoryName) :
                NewsData.loadMukhyaTathaTajaSamaarchar(getActivity(), categoryName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipable, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("category", categoryId + " " + categoryName);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new BreakingAndLatestnewsParentAdapter(breakingAndLatestNewses);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setEnabled(false);
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                ToggleRefresh.hideRefreshDialog((SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout));
//                ((ControllableAppBarLayout) getActivity().findViewById(R.id.app_bar_layout)).expandToolbar(true);
//            }
//        });


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager)recyclerView.getLayoutManager();
//                int po = linearLayoutManager1.findFirstCompletelyVisibleItemPosition();
//                if(po==0){
//                    Toast.makeText(getActivity(),"Reached",Toast.LENGTH_SHORT).show();
//                    ((ControllableAppBarLayout) getActivity().findViewById(R.id.app_bar_layout)).expandToolbar(true);
//                }
//
//
//            }
//        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
