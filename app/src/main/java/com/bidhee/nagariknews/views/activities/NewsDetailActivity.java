package com.bidhee.nagariknews.views.activities;

/**
 * Created by ronem on 2/10/16.
 */


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
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
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.michaldrabik.tapbarmenulib.TapBarMenu;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

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

    public static String NEWS_TITLE_EXTRA_STRING = "newsobject";
    private NewsObj news;
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
        settingToolbar();
        loadingDetail();


    }

    private void loadingDetail() {

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

    private void settingToolbar() {
        ViewCompat.setTransitionName(appBarLayout, news.getImg());
        supportPostponeEnterTransition();

        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(news.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_light));


    }

    private void gettingBundle() {
        news = getIntent().getExtras().getParcelable(NEWS_TITLE_EXTRA_STRING);
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
}
