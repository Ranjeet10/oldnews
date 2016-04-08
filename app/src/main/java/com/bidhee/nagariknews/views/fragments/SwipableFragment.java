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
import com.bidhee.nagariknews.bus.EventBus;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.activities.NewsDetailActivity;
import com.bidhee.nagariknews.widget.EndlessScrollListener;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

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

    private int newsType;
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
        EventBus.register(this);

        categoryId = getArguments().getString(StaticStorage.NEWS_CATEGORY_ID);
        categoryName = getArguments().getString(StaticStorage.NEWS_CATEGORY);


        if (savedInstanceState != null) {
            newsObjs = savedInstanceState.getParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE);
        } else {

            switch (Dashboard.sessionManager.getSwitchedNewsValue()) {
                // case
                // 1 is for republica
                // 2 for nagarik
                // 3 for sukrabar
                case 1:
                    //categoryId
                    // 1 means its for breaking and latest news
                    // >1 means its for normal news
                    newsType = 1;
                    newsObjs = Integer.parseInt(categoryId) == 1 ?
                            NewsData.loadBreakingLatestNewsTesting(getActivity(), newsType, categoryId) :
                            NewsData.getNewsRepublica(getActivity(), newsType, categoryId, categoryName);
                    break;
                case 2:
                    newsType = 2;
                    newsObjs = Integer.parseInt(categoryId) == 1 ?
                            NewsData.loadBreakingLatestNewsTesting(getActivity(), newsType, categoryId) :
                            NewsData.getNewsNagarik(getActivity(), newsType, categoryId, categoryName);
                    break;
                case 3:
                    newsType = 3;
                    newsObjs = Integer.parseInt(categoryId) == 1 ?
                            NewsData.loadBreakingLatestNewsTesting(getActivity(), newsType, categoryId) :
                            NewsData.getSukrabar(getActivity(), newsType, categoryId, categoryName);
                    break;
            }
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

//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(500);
        recyclerView.setItemAnimator(animator);


        newsTitlesAdapter = new NewsTitlesAdapter(false, Integer.parseInt(categoryId), newsObjs);
        newsTitlesAdapter.setOnRecyclerPositionListener(this);
        recyclerView.setAdapter(newsTitlesAdapter);


        if (Integer.parseInt(categoryId) != 1) {
            recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {

                    Log.i("categoryId", categoryId + " " + categoryName + "parents title" + ((CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar)).getTitle());
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ArrayList<NewsObj> moreNews = Dashboard.sessionManager.getSwitchedNewsValue() == 1 ?
                                    NewsData.getNewsRepublica(getContext(), newsType, categoryId, categoryName) :
                                    NewsData.getNewsNagarik(getContext(), newsType, categoryId, categoryName);

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
    public void onChildItemPositionListen(int position, View view, Boolean isShown) {

        View v = ((ViewGroup) view).getChildAt(0);

        //calulate the exact height of the row which is used to
        //set params in newsdetails related news recyclerview
        if (isShown) {                      //if badge is shown calculate the height of badge too
            StaticStorage.ROW_HEIGHT = (view.getHeight() - v.getHeight() - 2 * (int) getActivity().getResources().getDimension(R.dimen.screen_padding_lr));

        } else {                            //else get only height of the cardview

            StaticStorage.ROW_HEIGHT = view.getHeight();
        }


        if (view.getId() == R.id.news_share_text_view) {

        } else if (view.getId() == R.id.news_show_detail_text_view) {

        } else {
            Intent newsDetailIntent = new Intent(getActivity(), NewsDetailActivity.class);
            NewsObj newsObj = newsObjs.get(position);
            newsObj.setNewsCategoryName(categoryName);

            newsDetailIntent.putParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST, newsObjs);
            newsDetailIntent.putExtra(StaticStorage.KEY_NEWS_POSITION, position);
            startActivity(newsDetailIntent);


//            /**
//             * send only five relatedNews to the {@link NewsDetailActivity}
//             */
//            ArrayList<NewsObj> relatedNewsList = new ArrayList<>();
//
//            /**
//             * First add five {@value newsObj} to the {@value relatedNewsList} and after that
//             *
//             */
//            for (int i = 0; i < 5; i++) {
//                relatedNewsList.add(newsObjs.get(i));
//            }
//
//            /**
//             * check if the selected {@value newsObj} lies in relatedNewsList,
//             * if selected {@value newsObj} not present in {@value relatedNewsList} remove the last index of {@value relatedNewsList} and
//             * add the selected {@value newsObj} to {@value relatedNewsList},
//             * if present leave as it is
//             */
//            Intent newsDetailIntent = new Intent(getActivity(), NewsDetailActivity.class);
//            NewsObj newsObj = newsObjs.get(position);
//            newsObj.setNewsCategoryName(categoryName);
//
//
//            if (!relatedNewsList.contains(newsObj)) {
//                relatedNewsList.remove(4);
//                relatedNewsList.add(newsObj);
//
//                /**
//                 *change the position to 4, as we added the selected {@value newsObj} at index 4
//                 *if not done this we will get {@link ArrayIndexOutOfBoundsException}
//                 **/
//                position = 4;
//            }
//            newsDetailIntent.putParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST, relatedNewsList);
//            newsDetailIntent.putExtra(StaticStorage.KEY_NEWS_POSITION, position);
//            startActivity(newsDetailIntent);


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE, newsObjs);
    }
}
