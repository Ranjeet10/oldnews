package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.NewsData;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.views.activities.NewsDetailActivity;
import com.bidhee.nagariknews.widget.EndlessScrollListener;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/9/16.
 */
public class SwipableFragment extends Fragment implements NewsTitlesAdapter.RecyclerPositionListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.progess)
    ProgressBar progressBar;

    ArrayList<NewsObj> newsObjs;
    NewsTitlesAdapter newsTitlesAdapter;
    SessionManager sessionManager;

    private String categoryId;
    private String categoryName;


    public static SwipableFragment createNewInstance(TabModel tab) {

        SwipableFragment swipableFragment = new SwipableFragment();
        Bundle box = new Bundle();
        box.putString(StaticStorage.NEWS_CATEGORY_ID, tab.cat_id);
        box.putString(StaticStorage.NEWS_CATEGORY, tab.cat_name);
        swipableFragment.setArguments(box);
        return swipableFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());

        categoryId = getArguments().getString(StaticStorage.NEWS_CATEGORY_ID);
        categoryName = getArguments().getString(StaticStorage.NEWS_CATEGORY);


        if (savedInstanceState != null) {
            newsObjs = savedInstanceState.getParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE);
        } else {
            newsObjs = (sessionManager.getSwitchedNewsValue() == 0) ?
                    Integer.parseInt(categoryId) == 0 ? NewsData.loadBreakingLatestNewsTesting(getActivity(), categoryName) : NewsData.getNewsRepublica(getActivity(), categoryName) :
                    Integer.parseInt(categoryId) == 0 ? NewsData.loadBreakingLatestNewsTesting(getActivity(), categoryName) : NewsData.getNewsNagarik(getActivity(), categoryName);
        }

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
        addingSwipeRefreshListener();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        newsTitlesAdapter = new NewsTitlesAdapter(Integer.parseInt(categoryId), newsObjs);
        newsTitlesAdapter.setOnRecyclerPositionListener(this);
        recyclerView.setAdapter(newsTitlesAdapter);

        if (Integer.parseInt(categoryId) != 0) {
            recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {

                    Log.i("categoryId", categoryId + " " + categoryName + "parents title" + ((CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar)).getTitle());
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ArrayList<NewsObj> moreNews = sessionManager.getSwitchedNewsValue() == 0 ?
                                    NewsData.getNewsRepublica(getContext(), categoryName) :
                                    NewsData.getNewsNagarik(getContext(), categoryName);

                            int curSize = newsTitlesAdapter.getItemCount();
                            newsObjs.addAll(moreNews);
                            newsTitlesAdapter.notifyItemRangeInserted(curSize, newsObjs.size() - 1);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 2000);

                    Log.i(TAG, "current page No " + current_page);
                }
            });
        }


    }

    private void addingSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }

    private void fetchData() {
        ToggleRefresh.showRefreshDialog(getActivity(), swipeRefreshLayout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
            }
        }, 1000);
    }


    @Override
    public void onChildItemPositionListen(int position, View view) {

        if (view.getId() == R.id.news_share_text_view) {

        } else if (view.getId() == R.id.news_show_detail_text_view) {

        } else {
            Intent newsDetailIntent = new Intent(getActivity(), NewsDetailActivity.class);
            NewsObj newsObj = newsObjs.get(position);
            newsObj.setNewsCategory(categoryName);

            newsDetailIntent.putExtra(NewsDetailActivity.NEWS_TITLE_EXTRA_STRING, newsObj);
            startActivity(newsDetailIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE, newsObjs);
    }
}
