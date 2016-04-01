package com.bidhee.nagariknews.views.activities;

/**
 * Created by ronem on 2/10/16.
 */


import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.controller.interfaces.FontSizeListener;
import com.bidhee.nagariknews.controller.sqlite.SqliteDatabase;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.views.customviews.FontDialog;
import com.bidhee.nagariknews.widget.CustomLinearLayoutManager;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;
import com.squareup.picasso.Picasso;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity implements NewsTitlesAdapter.RecyclerPositionListener, FontSizeListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar_layout)
    ControllableAppBarLayout appBarLayout;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.activity_main_rfal)
    RapidFloatingActionLayout rfaLayout;
    @Bind(R.id.activity_main_rfab)
    RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;

    @Bind(R.id.title)
    TextView titleTextView;
    @Bind(R.id.news_from_category_text_view)
    TextView newsCategoryTextView;
    @Bind(R.id.news_time_text_view)
    TextView newsTimeTextView;
    @Bind(R.id.description)
    WebView descriptionTextView;
    @Bind(R.id.related_news_badge)
    TextView relatedNewsTextView;
    @Bind(R.id.scroll)
    NestedScrollView scrollView;
    @Bind(R.id.related_recycler_view)
    RecyclerView recyclerVIew;
    @Bind(R.id.news_share)
    ImageView newsShare;
    @Bind(R.id.news_add_to_fav)
    ImageView newsAddToFav;

    NewsTitlesAdapter newsTitlesAdapter;
    WebSettings webSettings;
    FontDialog fontDialog;


    private String TAG = getClass().getSimpleName();

    public static String NEWS_TITLE_EXTRA_STRING = "newsobject";
    private int categoryId = 2; // setting categoryId = 2 so that it will inflate the news title layout in normal way
    private int SELECTED_NEWS_POSITION = 0;
    private ArrayList<NewsObj> newsObjs;
    private NewsObj selectedNews;
    SessionManager sessionManager;
    String selectedNewsType = "";

    SqliteDatabase db;
    private Boolean isNewsInFavouite = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * make font selector dialog
         *
         */
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.news_detail_layout);

        ButterKnife.bind(this);
        db = new SqliteDatabase(NewsDetailActivity.this);
        db.open();

        sessionManager = new SessionManager(this);
        fontDialog = new FontDialog();
        fontDialog.setOnFontSizeListener(NewsDetailActivity.this);

        gettingBundle();
        webSettings = descriptionTextView.getSettings();

        if (newsObjs.size() > 0) {
            selectedNews = newsObjs.get(SELECTED_NEWS_POSITION);
            loadingDetail(selectedNews);
            loadRelatedContent();

            relatedNewsTextView.setVisibility(View.VISIBLE);
            String related = "";
            switch (sessionManager.getSwitchedNewsValue()) {
                case 1:
                    related = getResources().getString(R.string.related_news);
                    selectedNewsType = getResources().getString(R.string.republica);
                    break;
                case 2:
                    selectedNewsType = getResources().getString(R.string.nagarik);
                    related = getResources().getString(R.string.sambandhit_news);
                    break;
                case 3:
                    selectedNewsType = getResources().getString(R.string.sukrabar);
                    related = getResources().getString(R.string.related_news);
                    break;
            }

            relatedNewsTextView.setText(related);
            setCallbackListenerToFabDial();
        }
        settingToolbar(selectedNewsType + " : " + selectedNews.getNewsCategoryName());


    }

    @OnClick(R.id.news_share)
    void onShareClick() {
        Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.news_add_to_fav)
    void onAddToFavCilck() {
        if (isNewsInFavouite) {
            //if news is in persist state remove it and toggle the favourite state
            db.deleteRowFromNews(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId());
            isNewsInFavouite = false;
            toggleFavouriteState();
            Log.i(TAG, "news was present");

        } else {
            //if news is not present add the current newsObject to the sqlite database
            db.saveNews(selectedNews);
            isNewsInFavouite = true;
            toggleFavouriteState();
            Log.i(TAG, "news was not present");
        }
    }


    private void setCallbackListenerToFabDial() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(NewsDetailActivity.this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Large Font")
                        .setResId(R.mipmap.ic_format_size_black_24dp)
                        .setIconNormalColor(getResources().getColor(R.color.grid_3))
                        .setIconPressedColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setLabelColor(getResources().getColor(R.color.light_black))
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Normal Font")
                        .setResId(R.mipmap.ic_format_size_black_24dp)
                        .setIconNormalColor(getResources().getColor(R.color.grid_3))
                        .setIconPressedColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setLabelColor(getResources().getColor(R.color.light_black))
//                        .setLabelSizeSp(14)
//                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(getResources().getColor(R.color.light_grey), ABTextUtil.dip2px(this, 4)))
                        .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Small Font")
                        .setResId(R.mipmap.ic_format_size_black_24dp)
                        .setIconNormalColor(getResources().getColor(R.color.grid_3))
                        .setIconPressedColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setLabelColor(getResources().getColor(R.color.light_black))
                        .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Share this news")
                        .setResId(R.mipmap.ic_share_black)
                        .setIconNormalColor(getResources().getColor(R.color.grid_3))
                        .setIconPressedColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setLabelColor(getResources().getColor(R.color.light_black))
                        .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(getResources().getColor(R.color.grey))
                .setIconShadowDy(ABTextUtil.dip2px(this, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
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
            Picasso.with(this).
                    load(news.getImg()).
                    placeholder(R.drawable.nagariknews).
                    into(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        titleTextView.setText(news.getTitle());
        newsCategoryTextView.setText(news.getNewsCategoryName());
        newsTimeTextView.setText(news.getDate());

        String desc = news.getDesc();
        desc = "<html><body><p align=\"justify\">" + news.getTitle() + "<br><br>" + desc + "</p></body></html>";
        descriptionTextView.loadDataWithBaseURL(null, desc, "text/html", "utf-8", null);

        /**
         * check if news is present in SQLite
         */
        isNewsInFavouite = checkIfNewsWasAddedToFavourite(news.getNewsType(), news.getNewsCategoryId(), news.getNewsId());

        /**
         * toggle the favourite icon color for saved/empty respectively
         */
        toggleFavouriteState();


    }

    private void toggleFavouriteState() {
        //if news already exist in favourite, change the color of fav icon to color that should be shown.
        //and versa
        if (isNewsInFavouite) {
            newsAddToFav.setColorFilter(getResources().getColor(R.color.grid_3));
        } else {
            newsAddToFav.setColorFilter(getResources().getColor(R.color.light_grey));
        }
    }

    private Boolean checkIfNewsWasAddedToFavourite(String newsType, String newsCategoryId, String newsId) {
        isNewsInFavouite = db.isNewsPresent(newsType, newsCategoryId, newsId);
        return isNewsInFavouite;
    }


    private void settingToolbar(final String title) {

        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initially set the title to empty
        getSupportActionBar().setTitle("");

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_light));

        appBarLayout.setOnStateChangeListener(new ControllableAppBarLayout.OnStateChangeListener() {
            @Override
            public void onStateChange(ControllableAppBarLayout.State toolbarChange) {
                switch (toolbarChange) {

                    case COLLAPSED:
                        collapsingToolbarLayout.setTitle(title);
                        break;

                    case EXPANDED:
                        collapsingToolbarLayout.setTitle("");
                        break;

                    case IDLE:
                        collapsingToolbarLayout.setTitle("");
                        break;
                }
            }
        });

    }


    private void gettingBundle() {
        SELECTED_NEWS_POSITION = getIntent().getExtras().getInt(StaticStorage.KEY_NEWS_POSITION, 0);
        try {
            newsObjs = getIntent().getParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST);
            Log.i(TAG, newsObjs.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        db.close();
    }

    @Override
    public void onChildItemPositionListen(int position, View view, Boolean isShown) {
        SELECTED_NEWS_POSITION = position;
        selectedNews = newsObjs.get(SELECTED_NEWS_POSITION);
        loadingDetail(selectedNews);

        //make full scroll up so that nestedscrollview's first child is visible
//        scrollView.fullScroll(View.FOCUS_UP);
        ObjectAnimator.ofInt(scrollView, "scrollY", View.FOCUS_UP).setDuration(2500).start();
    }

    @Override
    public void onFontChanged(int fontSize) {
        webSettings.setDefaultFontSize(fontSize);
    }

    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {
        rfabHelper.toggleContent();
        setFont(i);
    }

    private void setFont(int i) {
        switch (i) {
            case 0:
                webSettings.setDefaultFontSize(20);
                break;
            case 1:
                webSettings.setDefaultFontSize(15);
                break;
            case 2:
                webSettings.setDefaultFontSize(10);
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        rfabHelper.toggleContent();
        setFont(i);
    }

}
