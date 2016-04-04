package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.controller.sqlite.SqliteDatabase;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.activities.NewsDetailActivity;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/23/16.
 */
public class FragmentSaved extends Fragment implements NewsTitlesAdapter.RecyclerPositionListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<NewsObj> newsObjs;
    NewsTitlesAdapter newsTitlesAdapter;
    SqliteDatabase db;


    public static FragmentSaved createNewInstance() {
        return new FragmentSaved();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SqliteDatabase(getActivity());
        db.open();
        newsObjs = new ArrayList<>();

        /**
         * get all news related to the newstype from the SQLite
         */
        newsObjs = (ArrayList<NewsObj>) db.getNewsList(String.valueOf(Dashboard.sessionManager.getSwitchedNewsValue()));

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        newsTitlesAdapter = new NewsTitlesAdapter(false, 2, newsObjs);
        newsTitlesAdapter.setOnRecyclerPositionListener(this);
        recyclerView.setAdapter(newsTitlesAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
            }
        });
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

            newsDetailIntent.putExtra(NewsDetailActivity.NEWS_TITLE_EXTRA_STRING, newsObj);
            newsDetailIntent.putParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST, newsObjs);
            newsDetailIntent.putExtra(StaticStorage.KEY_NEWS_POSITION, position);
            startActivity(newsDetailIntent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        db.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE, newsObjs);
    }
}
