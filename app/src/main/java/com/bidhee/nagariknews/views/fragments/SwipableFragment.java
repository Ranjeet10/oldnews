package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.controller.server_request.ServerConfig;
import com.bidhee.nagariknews.controller.server_request.WebService;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.activities.NewsDetailActivity;
import com.bidhee.nagariknews.widget.EndlessScrollListener;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
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

        newsListToShow = new ArrayList<>();
        categoryId = getArguments().getString(StaticStorage.NEWS_CATEGORY_ID);
        categoryName = getArguments().getString(StaticStorage.NEWS_CATEGORY);

    }

    //========================================================================
    //========================== HANDLE RESPONSES ============================
    //========================================================================
    private void handleServerResponseForBreakingAndLatestNews() {
        serverResponseNewsTitle = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingBar.setVisibility(View.GONE);
                Log.i(TAG, response);
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    if (nodeObject.has("success")) {
                        JSONObject dataObject = nodeObject.getJSONObject("data");

                        /**
                         * listing all the keys for JSONArray from the JSONObject {@link nodeObject}
                         */
                        Iterator keys = dataObject.keys();
                        while (keys.hasNext()) {

                            /**
                             * looping through out the no. of keys
                             * to get the list of {@link newsObjs} and adding to the {@link newsListToShow}
                             */
                            newsListToShow.addAll(getArrayList(dataObject, (String) keys.next()));
                        }

                        /**
                         * Method to notify the inserted data item to the adapter{@link newsTitlesAdapter}
                         */
                        notifyDataSetChanged(newsListToShow);


                    } else {
                        //To do if its error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListenerNewsTitle = new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                showContentNotFoundLayoutIfNeeded();
            }
        }

        ;

    }

    private void handleServerResponseForNewsTitle() {
        serverResponseNewsTitle = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListenerNewsTitle = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                showContentNotFoundLayoutIfNeeded();
            }
        }

        ;
    }

    private void getNewsTitles(String baseUrl, int pageIndex, String categoryId) {
        loadingBar.setVisibility(View.VISIBLE);
        handleServerResponseForBreakingAndLatestNews();

        /**
         * category id 0 means
         * the first index of tab is for Latest news
         */
        if (categoryId.equals("0")) {
            Log.i(TAG, "categoryId was 0");
            WebService.getServerData(ServerConfig.getLatestBreakingNewsUrl(baseUrl), serverResponseNewsTitle, errorListenerNewsTitle);
        } else {
            if (pageIndex == 1)
                loadingBar.setVisibility(View.VISIBLE);
            handleServerResponseForNewsTitle();
            //load news titles
            WebService.getServerData(ServerConfig.getNewsTitleUrl(baseUrl, pageIndex, categoryId), serverResponseNewsTitle, errorListenerNewsTitle);
        }


    }

    private Collection<? extends NewsObj> getArrayList(JSONObject dataObject, String newsArray) {
        newsObjs = new ArrayList<>();
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
                    img = "https://lh3.googleusercontent.com/2BGOr1KnAekO9NmaU4VZg2ZLRs_-60aaA7p4ABYlZXnqsLrItMi4uhmA62pGQDx9NwU=s630-fcrop64=1,0723098ffae5f834";
                }

                NewsObj newsObj = new NewsObj(String.valueOf(newsType), categoryId, newsId, newsArray, img, newsTile, publishedBy, publishDate, introText, "", "");

                //if the object is at first position make the title visible; for this we have to set the boolean value to the 0'th newsObj
                if (i == 0) {
                    newsObj.setIsTOShow(true);
                } else {
                    newsObj.setIsTOShow(false);
                }
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
            contentNotFoundLayout.setVisibility(View.INVISIBLE);
        } else {
            contentNotFoundLayout.setVisibility(View.VISIBLE);
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

            newsListToShow = savedInstanceState.getParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE);

        } else {
            newsType = Dashboard.sessionManager.getSwitchedNewsValue();

            getNewsTitles(Dashboard.baseUrl, 1, categoryId);


        }

        newsTitlesAdapter = new NewsTitlesAdapter(false, Integer.parseInt(categoryId), newsListToShow);
        ScaleInAnimationAdapter bottomAnimationAdapter = new ScaleInAnimationAdapter(newsTitlesAdapter);
        bottomAnimationAdapter.setDuration(200);
        recyclerView.setAdapter(bottomAnimationAdapter);

        newsTitlesAdapter.setOnRecyclerPositionListener(this);


        loadMoreListener(linearLayoutManager);

    }

    private void loadMoreListener(LinearLayoutManager linearLayoutManager) {
        if (Integer.parseInt(categoryId) != 0) {
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
            NewsObj newsObj = newsListToShow.get(position);
            newsObj.setNewsCategoryName(categoryName);

            newsDetailIntent.putParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST, newsListToShow);
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
        outState.putParcelableArrayList(StaticStorage.KEY_NEWS_SAVED_STATE, newsListToShow);
    }
}
