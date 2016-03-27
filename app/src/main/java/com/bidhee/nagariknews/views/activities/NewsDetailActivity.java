package com.bidhee.nagariknews.views.activities;

/**
 * Created by ronem on 2/10/16.
 */


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.widget.CustomLinearLayoutManager;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;
import com.michaldrabik.tapbarmenulib.TapBarMenu;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity implements NewsTitlesAdapter.RecyclerPositionListener {

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar_layout)
    ControllableAppBarLayout appBarLayout;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.tapBarMenu)
    TapBarMenu tapBarMenu;
    @Bind(R.id.title)
    TextView titleTextView;
    @Bind(R.id.news_from_category_text_view)
    TextView newsCategoryTextView;
    @Bind(R.id.publish_text_view)
    TextView publishTextView;
    @Bind(R.id.news_time_text_view)
    TextView newsTimeTextView;
    @Bind(R.id.description)
    TextView descriptionTextView;
    @Bind(R.id.related_news_badge)
    TextView relatedNewsTextView;
    @Bind(R.id.scroll)
    NestedScrollView scrollView;

    @Bind(R.id.related_recycler_view)
    RecyclerView recyclerVIew;
    NewsTitlesAdapter newsTitlesAdapter;


    private String TAG = getClass().getSimpleName();

    public static String NEWS_TITLE_EXTRA_STRING = "newsobject";
    private int categoryId = 2; // setting categoryId = 2 so that it will inflate the news title layout in normal way
    private int SELECTED_NEWS_POSITION = 0;
    private ArrayList<NewsObj> newsObjs;
    SessionManager sessionManager;
    private HashMap<String, Float> fontMap;


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.news_detail_layout);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);

        //storing different font size for different keys
        fontMap = new HashMap<>();
        fontMap.put("1", getResources().getDimension(R.dimen.news_detail_text_size_1));
        fontMap.put("2", getResources().getDimension(R.dimen.news_detail_text_size_2));
        fontMap.put("3", getResources().getDimension(R.dimen.news_detail_text_size_3));

        gettingBundle();
        settingToolbar(newsObjs.get(SELECTED_NEWS_POSITION).getTitle());
        loadingDetail(newsObjs.get(SELECTED_NEWS_POSITION));
        loadRelatedContent();

        if (newsObjs.size() > 0) {
            relatedNewsTextView.setVisibility(View.VISIBLE);

            relatedNewsTextView.setText(
                    sessionManager.getSwitchedNewsValue() == 0 ?
                            getResources().getString(R.string.related_news) :
                            getResources().getString(R.string.sambandhit_news));
        }


    }


    private void loadRelatedContent() {
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(this);
        recyclerVIew.setLayoutManager(linearLayoutManager);
        recyclerVIew.setHasFixedSize(true);
        recyclerVIew.setItemAnimator(new DefaultItemAnimator());


        newsTitlesAdapter = new NewsTitlesAdapter(true, categoryId, newsObjs);
        newsTitlesAdapter.setOnRecyclerPositionListener(this);
        recyclerVIew.setAdapter(newsTitlesAdapter);

        //setting newstedscrollingEnabled to false so that app bar collapse only when recyclerview is scrolled
        recyclerVIew.setNestedScrollingEnabled(false);

    }

    private void loadingDetail(NewsObj news) {

        try {
            Picasso.with(this).load(news.getImg()).placeholder(R.drawable.nagariknews).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            applyPalette(palette);
                        }
                    });
                }

                @Override
                public void onError() {
                    Log.e("picasso", "error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        titleTextView.setText(news.getTitle());
        newsCategoryTextView.setText(news.getNewsCategory());
        publishTextView.setText(news.getReportedBy());
        newsTimeTextView.setText(news.getDate());
        descriptionTextView.setText(news.getDesc());
        descriptionTextView.setTextSize(fontMap.get((sessionManager.getCurrentFontSize() + "")));
    }

    private void settingToolbar(String title) {

        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_light));


    }

    private void gettingBundle() {
        SELECTED_NEWS_POSITION = getIntent().getExtras().getInt(StaticStorage.KEY_NEWS_POSITION, 0);
//        categoryId = getIntent().getExtras().getString(StaticStorage.NEWS_CATEGORY_ID);
        try {
            newsObjs = getIntent().getParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST);
            Log.i(TAG, newsObjs.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tapBarMenu)
    void onTabBarClick() {
        tapBarMenu.toggle();
    }

    @OnClick({R.id.news_share_image_view, R.id.news_font_size_change_image_view, R.id.news_add_favourite_image_view})
    public void onMenuItemClick(View view) {
        tapBarMenu.close();
        switch (view.getId()) {
            case R.id.news_share_image_view:
                Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
                appBarLayout.collapseToolbar(true);
                break;
            case R.id.news_font_size_change_image_view:
                updateNewsDetailFontSize();
                descriptionTextView.setTextSize(fontMap.get((sessionManager.getCurrentFontSize() + "")));
                break;
            case R.id.news_add_favourite_image_view:
                Toast.makeText(getApplicationContext(), "Favourite", Toast.LENGTH_SHORT).show();
                appBarLayout.expandToolbar(true);
                break;
        }
    }

    private void updateNewsDetailFontSize() {
        int currentFontSize = sessionManager.getCurrentFontSize();
        currentFontSize = currentFontSize + 1;
        if (currentFontSize > 3) {
            currentFontSize = 1;
        }
        sessionManager.setTheFontSize(currentFontSize);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimary);
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        supportStartPostponedEnterTransition();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onChildItemPositionListen(int position, View view) {
        loadingDetail(newsObjs.get(position));

        //make full scroll up so that nestedscrollview's first child is visible
        scrollView.fullScroll(View.FOCUS_UP);
    }
}
