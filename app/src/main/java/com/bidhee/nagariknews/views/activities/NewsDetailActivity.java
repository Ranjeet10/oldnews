package com.bidhee.nagariknews.views.activities;

/**
 * Created by ronem on 2/10/16.
 */


import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.NewsData;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.BaseThemeActivity;
import com.bidhee.nagariknews.controller.interfaces.AlertDialogListener;
import com.bidhee.nagariknews.controller.server_request.ServerConfig;
import com.bidhee.nagariknews.controller.server_request.WebService;
import com.bidhee.nagariknews.controller.sqlite.SqliteDatabase;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.customviews.AlertDialog;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.widget.CustomLinearLayoutManager;
import com.bidhee.nagariknews.widget.NewsTitlesAdapter;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.bidhee.nagariknews.controller.Dashboard.sessionManager;

public class NewsDetailActivity extends BaseThemeActivity implements
        NewsTitlesAdapter.RecyclerPositionListener,

        RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,
        AlertDialogListener {
    @Bind(R.id.news_type_image_logo)
    ImageView newsTypeImageLogo;
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
    @Bind(R.id.vertical_line_separator)
    View verticalLineSeparator;

    NewsTitlesAdapter newsTitlesAdapter;
    WebSettings webSettings;


    private String TAG = getClass().getSimpleName();

    //    public static String NEWS_TITLE_EXTRA_STRING = "newsobject";
    private int categoryId = 2; // setting categoryId = 2 so that it will inflate the news title layout in normal way
    private int SELECTED_NEWS_POSITION = 0;
    private ArrayList<NewsObj> newsObjs;
//    private ArrayList<NewsObj> newsListToShow;
    private NewsObj selectedNews;
    String selectedNewsType = "";
    private int fabDrawable;
    private int MENU_COLOR;

    private Response.Listener<String> serverResponseNewsDetail;
    private Response.ErrorListener errorListenernewsDetail;
    private ProgressDialog progressDialog;

    SqliteDatabase db;
    private Boolean isNewsInFavouite = false;
    private int NEWS_TYPE = 1;//default set to 1

    private AlertDialog alertDialog;
    private String alertTitle;
    private String alertDescription;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * make font selector dialog
         *
         */
        super.onCreate(savedInstanceState);

        initActivityTransitions();
        NEWS_TYPE = Dashboard.sessionManager.getSwitchedNewsValue();

        setContentView(R.layout.news_detail_layout);
        ButterKnife.bind(this);

        db = new SqliteDatabase(NewsDetailActivity.this);
        db.open();

        //initialize the progressdialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);


        gettingBundle();


        webSettings = descriptionTextView.getSettings();

        if (newsObjs.size() > 0) {
            selectedNews = newsObjs.get(SELECTED_NEWS_POSITION);
            isNewsInFavouite = checkIfNewsWasAddedToFavourite(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId());

            if (isNewsInFavouite) {
                loadingDetail(db.getNewsObj(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId()));
            } else {
                getNewsDetailFromServer(selectedNews.getNewsId());
            }

            loadRelatedContent();

            relatedNewsTextView.setVisibility(View.VISIBLE);
            String related = "";
            switch (NEWS_TYPE) {
                case 1:
                    fabDrawable = R.drawable.menu_republica_corner_fab;
                    MENU_COLOR = getResources().getColor(R.color.republicaColorPrimary);    //setting menu color to current themes colorPrimary
                    related = getResources().getString(R.string.related_news);              //getting related value to show for the bottom news list
                    selectedNewsType = getResources().getString(R.string.republica);        //setting the title for the toolbar (not showing but jst geting for future use)
                    newsTypeImageLogo.setImageResource(StaticStorage.NEWS_LOGOS[0]);        //setting news logo to republica
                    relatedNewsTextView.setBackgroundResource(R.drawable.corner_republica_background);
                    alertTitle = getResources().getString(R.string.myrepublica_alert_title);
                    alertDescription = getResources().getString(R.string.eng_alert_desc);
                    break;
                case 2:
                    fabDrawable = R.drawable.menu_nagarik_corner_fab;
                    MENU_COLOR = getResources().getColor(R.color.nagarikColorPrimary);
                    selectedNewsType = getResources().getString(R.string.nagarik);
                    related = getResources().getString(R.string.sambandhit_news);
                    newsTypeImageLogo.setImageResource(StaticStorage.NEWS_LOGOS[1]);        //setting news logo to nagarik
                    relatedNewsTextView.setBackgroundResource(R.drawable.corner_nagarik_background);
                    alertTitle = getResources().getString(R.string.nagarik_alert_title);
                    alertDescription = getResources().getString(R.string.nepali_alert_desc);
                    break;
                case 3:
                    fabDrawable = R.drawable.menu_sukrabar_corner_fab;
                    MENU_COLOR = getResources().getColor(R.color.sukrabarColorPrimary);
                    selectedNewsType = getResources().getString(R.string.sukrabar);
                    newsTypeImageLogo.setImageResource(StaticStorage.NEWS_LOGOS[2]);        //setting news logo to sukrabar
                    related = getResources().getString(R.string.sambandhit_news);
                    relatedNewsTextView.setBackgroundResource(R.drawable.corner_sukrabar_background);
                    alertTitle = getResources().getString(R.string.sukrabar_alert_title);
                    alertDescription = getResources().getString(R.string.nepali_alert_desc);
                    break;
            }

            relatedNewsTextView.setText(related);

            setCallbackListenerToFabDial();
        }

        /**
         * setting {@value selectedNewsType} to the {@value toolbar}
         * but it is useless for now since we are displaying only logo to the toolbar
         * this function is overlapped
         */
        settingToolbar(selectedNewsType + " : " + selectedNews.getNewsCategoryName());


    }

    private void getNewsDetailFromServer(String newsId) {
        progressDialog.show();
        handleServerResponseForNewsDetail();
        WebService.getServerData(ServerConfig.getNewsDetailUrl(Dashboard.baseUrl, newsId), serverResponseNewsDetail, errorListenernewsDetail);
    }


    private void handleServerResponseForNewsDetail() {
        serverResponseNewsDetail = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "newsdetail:" + response);
                progressDialog.dismiss();
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    JSONObject newsDetailObject = nodeObject.getJSONObject("data");
                    String detail = newsDetailObject.getString("content");
                    Log.i("detail", detail);
                    String newsUrl = newsDetailObject.getString("url"); //semi url
//                    newsDetail = new NewsDetail(id, title, detail, featuredImage, BuildConfig.BASE_URL + newsUrl, publishDate, authorName);
//                    loadNewsDetail(newsDetail);
                    selectedNews.setDescription(detail);
                    selectedNews.setNewsUrl(Dashboard.baseUrl + newsUrl);

                    isNewsInFavouite = false;
                    loadingDetail(selectedNews);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        errorListenernewsDetail = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        };
    }


    @OnClick(R.id.news_share)
    void onShareClick() {
        Log.i("newsUrl", selectedNews.getNewsUrl());
        BasicUtilMethods.shareLink(this, selectedNews.getNewsUrl());
    }

    @OnClick(R.id.news_add_to_fav)
    void onAddToFavCilck() {
        if (Dashboard.sessionManager.isLoggedIn()) {
            if (isNewsInFavouite) {
                //if news is in persist state remove it and toggle the favourite state
                db.deleteRowFromNews(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId());
                isNewsInFavouite = false;
                toggleFavouriteState();
                Log.i(TAG, "news was present");

            } else {
                //if news is not present add the current newsObject to the sqlite database
                //set news Description and news url to the newsObject

                db.saveNews(selectedNews);
                isNewsInFavouite = true;
                toggleFavouriteState();
                Log.i(TAG, "news was not present");
            }
        } else {
            alertDialog = new AlertDialog(this, alertTitle, alertDescription);
            alertDialog.setOnAlertDialogListener(this);
            alertDialog.show();
        }
    }


    private void setCallbackListenerToFabDial() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(NewsDetailActivity.this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Increase font size")
                        .setResId(R.mipmap.ifont)
                        .setIconNormalColor(MENU_COLOR)
                        .setIconPressedColor(getResources().getColor(R.color.white))
                        .setLabelColor(getResources().getColor(R.color.light_black))
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Decrease font size")
                        .setResId(R.mipmap.df)
                        .setIconNormalColor(MENU_COLOR)
                        .setIconPressedColor(getResources().getColor(R.color.white))
                        .setLabelColor(getResources().getColor(R.color.light_black))
//                        .setLabelSizeSp(14)
//                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(getResources().getColor(R.color.light_grey), ABTextUtil.dip2px(this, 4)))
                        .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Share this news")
                        .setResId(R.mipmap.ic_share_black)
                        .setIconNormalColor(MENU_COLOR)
                        .setIconPressedColor(getResources().getColor(R.color.white))
                        .setLabelColor(getResources().getColor(R.color.light_black))
                        .setWrapper(2)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 1))
                .setIconShadowColor(getResources().getColor(R.color.grey))
                .setIconShadowDy(ABTextUtil.dip2px(this, 1))
        ;
        rfaBtn.setBackgroundResource(fabDrawable);
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

        NewsObj no = newsObjs.get(0);
        NewsData.getTrendingNews(this, NEWS_TYPE, no.getNewsCategoryId(), no.getNewsCategoryName());

    }

    private void loadingDetail(NewsObj news) {
        if (news.getImg().length() > 0)
            BasicUtilMethods.loadImage(this, news.getImg(), image);

        titleTextView.setText(news.getTitle());
        newsCategoryTextView.setText(news.getNewsCategoryName());
        newsTimeTextView.setText(news.getDate());

        String desc = news.getDescription();
        desc = "<html><body><p align=\"justify\">" + desc + "</p></body></html>";
        descriptionTextView.loadDataWithBaseURL(null, desc, "text/html", "utf-8", null);


        scrollUp();
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
            // modified
//            newsListToShow = new ArrayList<>();
            String newsId = newsObjs.get(SELECTED_NEWS_POSITION).getNewsId();
//            for (int i = 0; i < newsObjs.size(); i++) {
//                if (!newsObjs.get(i).getNewsId().equals(newsId)) {
//                    newsListToShow.add(newsObjs.get(i));
//                }
//            }
//            Log.i(TAG, newsObjs.size() + "");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        menu.getItem(0).setVisible(false);

        /**
         * applying the tint color to all the font icon
         * with color white
         */
        for (int i = 1; i <= 2; i++) {
            final Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                final Drawable wrapped = DrawableCompat.wrap(drawable);
                drawable.mutate();
                DrawableCompat.setTint(wrapped, Color.WHITE);
                menu.getItem(i).setIcon(drawable);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.news_font_control_small:
                scrollUp();
                webSettings.setDefaultFontSize(15);
                break;

            case R.id.news_font_control_large:
                scrollUp();
                webSettings.setDefaultFontSize(20);
                break;

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
        Log.i(TAG, "selected :" + selectedNews);
        getNewsDetailFromServer(selectedNews.getNewsId());

    }

    private void scrollUp() {
        //make full scroll up so that nestedscrollview's first child is visible
//        scrollView.fullScroll(View.FOCUS_UP);
        ObjectAnimator.ofInt(scrollView, "scrollY", View.FOCUS_UP).setDuration(2500).start();
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
                BasicUtilMethods.shareLink(this, selectedNews.getNewsUrl());
                break;
        }
    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        rfabHelper.toggleContent();
        setFont(i);
    }

    @Override
    public void alertPositiveButtonClicked() {

        Intent loginIntent = new Intent(NewsDetailActivity.this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);

    }

    @Override
    public void alertNegativeButtonClicked() {
        alertDialog.dismiss();
    }
}
