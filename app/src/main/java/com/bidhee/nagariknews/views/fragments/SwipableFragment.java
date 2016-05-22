package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
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
public class SwipableFragment extends Fragment implements NewsTitlesAdapter.RecyclerPositionListener {

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

    ArrayList<NewsObj> newsObjs;
    ArrayList<NewsObj> newsListToShow;
    NewsTitlesAdapter newsTitlesAdapter;


    private int newsType;
    private String categoryId;
    private String categoryName;

    /**
     * response for the {@link com.android.volley.toolbox.Volley Request}
     */
    private Response.Listener<String> serverResponseNewsTitle;
    private Response.ErrorListener errorListenerNewsTitle;


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
        Log.i(TAG, "oncreate called");

        newsListToShow = new ArrayList<>();
        categoryId = getArguments().getString(StaticStorage.NEWS_CATEGORY_ID);
        categoryName = getArguments().getString(StaticStorage.NEWS_CATEGORY);
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
                    newsObjs = new ArrayList<>();
                    JSONObject nodeObject = new JSONObject(response);

                    /**
                     * pass the base object and JSONArray value name to the
                     * method { #getArrayList(nodeObject,"data")}
                     * to get the list of newsObjs
                     */
                    newsListToShow.addAll(getArrayList(nodeObject, "data"));
                    notifyDataSetChanged(newsListToShow);

                    loadBannerViewPager(newsListToShow);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListenerNewsTitle = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
                loadingBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                showContentNotFoundLayoutIfNeeded();
            }
        }

        ;
    }

    private void loadBannerViewPager(ArrayList<NewsObj> newsListToShow) {
        if (categoryId.equals("-1")) {
            ArrayList<Multimedias> list = new ArrayList<>();
            for (int i = 0; i < newsListToShow.size(); i++) {
                Log.i(TAG, "size: " + newsListToShow.get(i).getImg());
                if (!newsListToShow.get(i).getImg().equals(StaticStorage.DEFAULT_IMAGE))
                    list.add(new Multimedias("", "", newsListToShow.get(i).getImg(), "", ""));
            }
            try {
                ((Dashboard) getActivity()).setBannerViewpager(list);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }

        }
    }

    private void getNewsTitles(String baseUrl, int pageIndex, String categoryId) {
        loadingBar.setVisibility(View.VISIBLE);

        if (!BasicUtilMethods.isNetworkOnline(getActivity())) {
            MySnackbar.showSnackBar(getActivity(), loadingBar, BaseThemeActivity.NO_NETWORK).show();
        }
        /**
         * category id -1 means
         * the first index of tab is for Latest news
         */
        handleServerResponseForNewsTitle();
        if (categoryId.equals("-1")) {
            Log.i(TAG, "categoryId was -1");
            WebService.getServerData(ServerConfig.getLatestBreakingNewsUrl(baseUrl), serverResponseNewsTitle, errorListenerNewsTitle);

        } else if (categoryId.equals("0")) {
            WebService.getServerDataWithHeader(ServerConfig.getMeroRuchiUrl(Dashboard.sessionManager.getSwitchedNewsValue()), Dashboard.sessionManager.getToken(), serverResponseNewsTitle, errorListenerNewsTitle);

        } else {
            if (pageIndex == 1)
                loadingBar.setVisibility(View.VISIBLE);
            WebService.getServerData(ServerConfig.getNewsTitleUrl(baseUrl, pageIndex, categoryId), serverResponseNewsTitle, errorListenerNewsTitle);
        }

    }

    private Collection<? extends NewsObj> getArrayList(JSONObject dataObject, String newsArray) {
        newsObjs = new ArrayList<>();
        String arrayName;
        try {
            JSONArray jArray = dataObject.getJSONArray(newsArray);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                String newsId = obj.getString("id");
                String newsTile = obj.getString("title");
                String introText = obj.getString("introText");

                String publishDate = obj.getString("publishOn");
                try {
                    publishDate = publishDate.substring(0, publishDate.lastIndexOf("+"));
                    publishDate = publishDate.replace("T", " ");
                    publishDate = BasicUtilMethods.getTimeAgo(publishDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String publishedBy = "";
                String img = obj.getString("featuredImage");

                if (TextUtils.isEmpty(img)) {
                    img = StaticStorage.DEFAULT_IMAGE;
                }

                //its the category name
                arrayName = categoryName;

                NewsObj newsObj = new NewsObj(String.valueOf(newsType), categoryId, newsId, arrayName, img, newsTile, publishedBy, publishDate, introText, "", "", 1);
                newsObjs.add(newsObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsObjs;
    }

    private void notifyDataSetChanged(ArrayList<NewsObj> newsListToShow) {
        /**
         * get the previous items count and append the new items after the item which matches the curSize
         */
        int curSize = newsTitlesAdapter.getItemCount();
        newsTitlesAdapter.notifyItemRangeInserted(curSize, newsListToShow.size() - 1);
        newsTitlesAdapter.notifyDataSetChanged();

        showContentNotFoundLayoutIfNeeded();
        Log.i("newsListSize", newsListToShow.size() + "");
    }


    private void showContentNotFoundLayoutIfNeeded() {
        /**
         * toggle the #contentNotFoundLayout
         * if the  #newsObjs is empty make it visible else,
         * make it invisible
         */
        if (newsListToShow.size() > 0) {
            contentNotFoundLayout.setVisibility(View.GONE);
        } else {
            if (categoryId.equals("0")) {
                if (Dashboard.sessionManager.isLoggedIn()) {
                    contentNotFoundTextView.setText(BaseThemeActivity.MERO_RUCHI_EMPTY_BECAUSE_OF_NO_CATEGORY_SELECTED);
                } else {
                    contentNotFoundTextView.setText(BaseThemeActivity.MERO_RUCHI_EMPTY_BECAUSE_OF_NOT_LOGGED_IN);
                }

            } else {
                contentNotFoundTextView.setText(BaseThemeActivity.EMPTY_NEWS);
            }
            warningImage.setImageResource(R.mipmap.ic_warning_white_48dp);
            contentNotFoundLayout.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.content_not_found_parent_layout)
    void onContentNotFoundTextViewClicked() {
        String cnft = contentNotFoundTextView.getText().toString();
        if (cnft.equals(BaseThemeActivity.MERO_RUCHI_EMPTY_BECAUSE_OF_NO_CATEGORY_SELECTED) ||
                cnft.equals(BaseThemeActivity.MERO_RUCHI_EMPTY_BECAUSE_OF_NOT_LOGGED_IN)) {

            if (Dashboard.sessionManager.isLoggedIn()) {
                startActivity(new Intent(getActivity(), SelectCategoryActivity.class));
            } else {
                final AlertDialog alertDialog = new AlertDialog(getActivity(), StaticStorage.ALERT_TITLE_LOGIN, StaticStorage.LOGIN_INFO);
                alertDialog.setOnAlertDialogListener(new AlertDialogListener() {
                    @Override
                    public void alertPositiveButtonClicked() {
                        alertDialog.dismiss();
                        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginIntent);

                    }

                    @Override
                    public void alertNegativeButtonClicked() {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

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


        if (savedInstanceState != null) {
            Log.i(TAG, "saved instance state");
            newsListToShow = savedInstanceState.getParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE);

        } else {
            newsType = Dashboard.sessionManager.getSwitchedNewsValue();
            Log.i(TAG, "not saved instance state");
            if (newsListToShow.size() <= 0)
                getNewsTitles(Dashboard.baseUrl, 1, categoryId);


        }
        resetNewsAdapter();
        loadMoreListener(linearLayoutManager);

    }

    private void resetNewsAdapter() {
        newsTitlesAdapter = new NewsTitlesAdapter(false, Integer.parseInt(categoryId), newsListToShow);
        ScaleInAnimationAdapter bottomAnimationAdapter = new ScaleInAnimationAdapter(newsTitlesAdapter);
        bottomAnimationAdapter.setDuration(200);
        recyclerView.setAdapter(bottomAnimationAdapter);

        newsTitlesAdapter.setOnRecyclerPositionListener(this);
    }

    private void loadMoreListener(LinearLayoutManager linearLayoutManager) {
        if (Integer.parseInt(categoryId) != -1 && Integer.parseInt(categoryId) != 0) {
            recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {

                    Log.i("categoryId", categoryId + " " + categoryName + "parents title" + ((CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar)).getTitle());

                    progressBar.setVisibility(View.VISIBLE);

                    getNewsTitles(Dashboard.baseUrl, current_page, categoryId);


                    Log.i(TAG, "current page No " + current_page);
                }
            });
        }
    }

    private void addingSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsListToShow = new ArrayList<>();
                resetNewsAdapter();
                getNewsTitles(Dashboard.baseUrl, 1, categoryId);
            }
        });
    }


    @Override
    public void onChildItemPositionListen(int position, View view) {

        if (view.getId() == R.id.news_share_text_view) {

        } else if (view.getId() == R.id.news_show_detail_text_view) {

        } else {

            if (BasicUtilMethods.isNetworkOnline(getActivity())) {
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
                MySnackbar.showSnackBar(getActivity(), recyclerView, BaseThemeActivity.NO_NETWORK);
            }

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
        outState.putParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE, newsListToShow);
    }

}
