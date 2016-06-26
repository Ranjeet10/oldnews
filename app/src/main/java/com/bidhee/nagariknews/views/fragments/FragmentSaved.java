package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.controller.interfaces.AlertDialogListener;
import com.bidhee.nagariknews.controller.sqlite.SqliteDatabase;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.views.activities.BaseThemeActivity;
import com.bidhee.nagariknews.controller.server_request.ServerConfig;
import com.bidhee.nagariknews.controller.server_request.WebService;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.activities.LoginActivity;
import com.bidhee.nagariknews.views.activities.NewsDetailActivity;
import com.bidhee.nagariknews.views.activities.SelectCategoryActivity;
import com.bidhee.nagariknews.views.customviews.AlertDialog;
import com.bidhee.nagariknews.views.customviews.MySnackbar;
import com.bidhee.nagariknews.widget.EndlessScrollListener;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;
import com.bidhee.nagariknews.widget.PhotosCartoonPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by ronem on 2/9/16.
 */
public class FragmentSaved extends Fragment implements NewsTitlesAdapter.RecyclerPositionListener {

    private String TAG = getClass().getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.progess)
    ProgressBar progressBar;

    //initial news loading progress bar
    @Bind(R.id.loading_bar)
    ProgressBar loadingBar;

    @Bind(R.id.content_not_found_parent_layout)
    LinearLayout contentNotFoundLayout;
    @Bind(R.id.content_not_found_textview)
    TextView contentNotFoundTextView;
    @Bind(R.id.warning_image)
    ImageView warningImage;

    ArrayList<NewsObj> newsListToShow;
    NewsTitlesAdapter newsTitlesAdapter;


    private int newsType;
    SqliteDatabase db;

    /**
     * response for the {@link com.android.volley.toolbox.Volley Request}
     */
    private Response.Listener<String> serverResponseNewsTitle;
    private Response.ErrorListener errorListenerNewsTitle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "oncreate called");

        /**
         * construct and open database
         */
        db = new SqliteDatabase(getActivity());
        db.open();

        newsListToShow = new ArrayList<>();

    }

    //========================================================================
    //========================== HANDLE RESPONSES ============================
    //========================================================================
    private void handleServerResponseForNewsTitle() {
        serverResponseNewsTitle = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
                loadingBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject nodeObject = new JSONObject(response);

                    /**
                     * pass the base object and JSONArray value name to the
                     * method { #getArrayList(nodeObject,"data")}
                     * to get the list of newsObjs
                     */
                    newsListToShow.addAll(getArrayList(nodeObject, "data"));
                    setAdapterData(newsListToShow);
                    Log.i(TAG, "server response");

                    /**
                     * Adding to cache
                     */
                    if (newsListToShow != null) {
                        /**
                         * Deleting the news from sqlite if present
                         */
                        try {
                            db.deleteAllSavedNews();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /**
                         * Adding the news fetched from the remote server to sqlite db
                         */
                        for (int i = 0; i < newsListToShow.size(); i++) {
                            try {
                                db.saveNewstoSaved(newsListToShow.get(i));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListenerNewsTitle = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                try {
//                    newsListToShow = (ArrayList<NewsObj>) db.getNewsList(String.valueOf(newsType), categoryId);
//                    loadDatas(newsListToShow);
//                    loadBannerViewPager(newsListToShow);
//                } catch (NullPointerException npe) {
//                    npe.printStackTrace();
//                }

//                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
//                loadingBar.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//                showContentNotFoundLayoutIfNeeded();
            }
        }

        ;
    }


    private void getNewsTitles(String baseUrl) {
        handleServerResponseForNewsTitle();
        loadingBar.setVisibility(View.VISIBLE);
        Log.i(TAG, "url:" + ServerConfig.getSaveNewsTitleUrl(baseUrl) + "\ntoken:" + Dashboard.sessionManager.getToken());
        WebService.getServerDataWithHeader(ServerConfig.getSaveNewsTitleUrl(baseUrl), Dashboard.sessionManager.getToken(), serverResponseNewsTitle, errorListenerNewsTitle);
    }

    private Collection<? extends NewsObj> getArrayList(JSONObject dataObject, String newsArray) {
        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        String arrayName;
        try {
            JSONArray jArray = dataObject.getJSONArray(newsArray);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                String categoryId = obj.getString("category_id");
                String newsId = obj.getString("id");
                String newsTile = obj.getString("title");
                String introText = obj.getString("introText");
                String publishDate;
                if (obj.has("nepaliDate")) {
                    publishDate = obj.getString("nepaliDate");
                } else {
                    publishDate = obj.getString("publishOnDate");
                }

                String publishedBy = "";
                String img = obj.getString("featuredImage");

                if (TextUtils.isEmpty(img)) {
                    img = StaticStorage.DEFAULT_IMAGE;
                }

                //its the category name
                arrayName = "saved news";

                NewsObj newsObj = new NewsObj(String.valueOf(newsType), categoryId, newsId, arrayName, img, newsTile, publishedBy, publishDate, introText, "", "", 1, 0);
                newsObjs.add(newsObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsObjs;
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

        if (savedInstanceState != null) {
            /**
             * called when screens orientation is changed and
             * when the fragment is re-created during viewpager swipe
             */
            newsListToShow = savedInstanceState.getParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE);
            setAdapterData(newsListToShow);
        } else {
            newsType = Dashboard.sessionManager.getSwitchedNewsValue();
            if (newsListToShow.size() <= 0) {
                if (BasicUtilMethods.isNetworkOnline(getActivity())) {
                    getNewsTitles(Dashboard.baseUrl);
                } else {
                    try {
                        newsListToShow = (ArrayList<NewsObj>) db.getNewsList(String.valueOf(newsType), "", true);
                        setAdapterData(newsListToShow);
                    } catch (CursorIndexOutOfBoundsException cie) {
                        cie.printStackTrace();
                    }
                    MySnackbar.showSnackBar(getActivity(), loadingBar, BaseThemeActivity.NO_NETWORK).show();
                }
            }

        }
    }

    private void setAdapterData(ArrayList<NewsObj> newsListToShow) {
        newsTitlesAdapter = new NewsTitlesAdapter(false, -2, newsListToShow);
        ScaleInAnimationAdapter bottomAnimationAdapter = new ScaleInAnimationAdapter(newsTitlesAdapter);
        bottomAnimationAdapter.setDuration(200);
        recyclerView.setAdapter(bottomAnimationAdapter);

        newsTitlesAdapter.setOnRecyclerPositionListener(this);
    }


    @Override
    public void onChildItemPositionListen(int position, View view) {
        NewsObj newsObj = newsListToShow.get(position);
        if (view.getId() == R.id.news_share_text_view) {

        } else if (view.getId() == R.id.news_show_detail_text_view) {

        } else {

            if (BasicUtilMethods.isNetworkOnline(getActivity()) || db.isNewsDetailPresent(newsObj.getNewsType(), newsObj.getNewsCategoryId(), newsObj.getNewsId())) {
                Intent newsDetailIntent = new Intent(getActivity(), NewsDetailActivity.class);
                newsListToShow.get(position).setIsTOShow(0);
                for (int i = 0; i < newsListToShow.size(); i++) {
                    newsListToShow.get(i).setIsTOShow(1);
                }
                newsListToShow.get(position).setIsTOShow(0);
                newsDetailIntent.putParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST, newsListToShow);

                newsDetailIntent.putExtra(StaticStorage.KEY_NEWS_POSITION, position);
                startActivity(newsDetailIntent);
            } else {
                MySnackbar.showSnackBar(getActivity(), recyclerView, BaseThemeActivity.NO_NETWORK).show();

            }

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE, newsListToShow);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
