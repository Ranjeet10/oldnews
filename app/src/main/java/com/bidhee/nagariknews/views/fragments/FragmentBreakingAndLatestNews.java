package com.bidhee.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.BreakingAndLatestNews;
import com.bidhee.nagariknews.Utils.NewsData;
import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.widget.BreakingAndLatestnewsParentAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/19/16.
 */
public class FragmentBreakingAndLatestNews extends Fragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    ArrayList<BreakingAndLatestNews> breakingAndLatestNewses;
    BreakingAndLatestnewsParentAdapter adapter;
    SessionManager sessionManager;

    private  String categoryId;
    private  String categoryName;

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
                NewsData.loadBreakingLatestNews(getActivity(),categoryName) :
                NewsData.loadMukhyaTathaTajaSamaarchar(getActivity(),categoryName);
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


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new BreakingAndLatestnewsParentAdapter(breakingAndLatestNewses);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
