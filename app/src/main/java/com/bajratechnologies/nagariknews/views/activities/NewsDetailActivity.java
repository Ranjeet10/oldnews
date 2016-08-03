package com.bajratechnologies.nagariknews.views.activities;

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
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.controller.interfaces.AlertDialogListener;
import com.bajratechnologies.nagariknews.controller.server_request.ServerConfig;
import com.bajratechnologies.nagariknews.controller.server_request.WebService;
import com.bajratechnologies.nagariknews.controller.sqlite.SqliteDatabase;
import com.bajratechnologies.nagariknews.model.NewsObj;
import com.bajratechnologies.nagariknews.views.customviews.AlertDialog;
import com.bajratechnologies.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bajratechnologies.nagariknews.views.customviews.MySnackbar;
import com.bajratechnologies.nagariknews.widget.CustomLinearLayoutManager;
import com.bajratechnologies.nagariknews.widget.RelatedNewsTitleAdapter;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.bidhee.nagariknews.controller.Dashboard.sessionManager;

public class NewsDetailActivity extends BaseThemeActivity implements
        RelatedNewsTitleAdapter.RecyclerPositionListener,

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
    @Bind(R.id.news_description_and_related_layout)
    LinearLayout mainLayout;
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

    RelatedNewsTitleAdapter newsTitlesAdapter;
    WebSettings webSettings;


    private String TAG = getClass().getSimpleName();

    private String newsType;
    private String categoryName;
    private int categoryId = 2; // setting categoryId = 2 so that it will inflate the news title layout in normal way
    private int SELECTED_NEWS_POSITION = 0;
    private ArrayList<NewsObj> newsObjs;
    private NewsObj selectedNews;
    String selectedNewsType = "";
    private int fabDrawable;
    private int MENU_COLOR;

    private Response.Listener<String> serverResponseNewsDetail, saveResponse, unSaveResponse;
    private Response.ErrorListener errorListenernewsDetail, saveErrorListener, unsaveErrorListener;
    private ProgressDialog progressDialog;

    SqliteDatabase db;
    //    private Boolean isNewsInFavouite = false;
    private Boolean isNewsDetailPresent = false;
    private int SAVED_COLOR;
    private int UN_SAVED_COLOR;
    private int NEWS_TYPE = 1;//default set to 1

    private AlertDialog alertDialog;
    private String alertTitle;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivityTransitions();
//        NEWS_TYPE = Dashboard.sessionManager.getSwitchedNewsValue();
        NEWS_TYPE = BaseThemeActivity.sessionManager.getSwitchedNewsValue();
        SAVED_COLOR = getResources().getColor(R.color.grid_2);
        UN_SAVED_COLOR = getResources().getColor(R.color.light_grey);

        setContentView(R.layout.news_detail_layout);
        ButterKnife.bind(this);

        db = new SqliteDatabase(NewsDetailActivity.this);
        db.open();

        //initialize the progressdialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        mainLayout.setVisibility(View.GONE);

        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(this);
        recyclerVIew.setLayoutManager(linearLayoutManager);
        recyclerVIew.setHasFixedSize(true);
        recyclerVIew.setItemAnimator(new DefaultItemAnimator());
        recyclerVIew.invalidate();


        gettingBundle();


        webSettings = descriptionTextView.getSettings();

        if (newsObjs.size() > 0) {
            selectedNews = newsObjs.get(SELECTED_NEWS_POSITION);

            if (BasicUtilMethods.isNetworkOnline(this)) {
                getNewsDetailFromServer(selectedNews.getNewsId());
                loadAdapter();
            } else {
                relatedNewsTextView.setVisibility(View.GONE);
                MySnackbar.showSnackBar(this, recyclerVIew, BaseThemeActivity.NO_NETWORK).show();
                isNewsDetailPresent = db.isNewsDetailPresent(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId());
                if (isNewsDetailPresent) {
                    loadingDetail(db.getNewsObj(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId()));
                }
            }
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
                    break;
                case 2:
                    fabDrawable = R.drawable.menu_nagarik_corner_fab;
                    MENU_COLOR = getResources().getColor(R.color.nagarikColorPrimary);
                    selectedNewsType = getResources().getString(R.string.nagarik);
                    related = getResources().getString(R.string.sambandhit_news);
                    newsTypeImageLogo.setImageResource(StaticStorage.NEWS_LOGOS[1]);        //setting news logo to nagarik
                    relatedNewsTextView.setBackgroundResource(R.drawable.corner_nagarik_background);
                    alertTitle = getResources().getString(R.string.nagarik_alert_title);
                    break;
                case 3:
                    fabDrawable = R.drawable.menu_sukrabar_corner_fab;
                    MENU_COLOR = getResources().getColor(R.color.sukrabarColorPrimary);
                    selectedNewsType = getResources().getString(R.string.sukrabar);
                    newsTypeImageLogo.setImageResource(StaticStorage.NEWS_LOGOS[2]);        //setting news logo to sukrabar
                    related = getResources().getString(R.string.sambandhit_news);
                    relatedNewsTextView.setBackgroundResource(R.drawable.corner_sukrabar_background);
                    alertTitle = getResources().getString(R.string.sukrabar_alert_title);
                    break;
            }

            relatedNewsTextView.setText(related);
            setCallbackListenerToFabDial();
        } else {

        }

        /**
         * setting {@value selectedNewsType} to the {@value toolbar}
         * but it is useless for now since we are displaying only logo to the toolbar
         * this function is overlapped
         */
        settingToolbar(selectedNewsType + " : " + selectedNews.getNewsCategoryName());


    }

    private void getNewsDetailFromServer(String newsId) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        handleServerResponseForNewsDetail();
        Log.i(TAG, "newsDetailUrl:" + ServerConfig.getNewsDetailUrl(Dashboard.baseUrl, newsId));
        Log.i(TAG, "token:" + sessionManager.getToken());
        if (BasicUtilMethods.isNetworkOnline(this)) {
            WebService.getServerDataWithHeader(ServerConfig.getNewsDetailUrl(Dashboard.baseUrl, newsId), sessionManager.getToken(), serverResponseNewsDetail, errorListenernewsDetail);
        } else {
            MySnackbar.showSnackBar(this, scrollView, Dashboard.NO_NETWORK).show();
        }
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
                    Boolean isSaved = newsDetailObject.getBoolean("saved");
                    Log.i("detail", detail);
                    String newsUrl = newsDetailObject.getString("url");
                    selectedNews.setIsSaved(isSaved ? 1 : 0);
                    selectedNews.setDescription(detail);
                    selectedNews.setNewsUrl(Dashboard.baseUrl + newsUrl);

                    /**
                     * update sqlite table
                     */
                    db.updateNewsDetail(selectedNews);
                    loadingDetail(selectedNews);
                    Log.i(TAG, "saveddata" + selectedNews.toString());

                    if (nodeObject.has("relatedCatNews")) {
                        try {
                            JSONArray jArray = nodeObject.getJSONArray("relatedCatNews");
                            newsObjs.clear();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject obj = jArray.getJSONObject(i);
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


                                Log.i(TAG, "list cleared");
                                NewsObj newsObj = new NewsObj(newsType, String.valueOf(categoryId), newsId, categoryName, img, newsTile, publishedBy, publishDate, introText, "", "", 1, 0);
                                newsObjs.add(newsObj);

                                loadAdapter();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

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
//        if (Dashboard.sessionManager.isLoggedIn()) {
        if (BaseThemeActivity.sessionManager.isLoggedIn()) {
            if (BasicUtilMethods.isNetworkOnline(this)) {
                switch (selectedNews.getIsSaved()) {
                    case 0:
                        //save news
                        saveNewsToMyFavourite();
                        break;
                    case 1:
                        //delete news
                        deleteNewsFromMyFavourite();
                        break;
                }
            } else {
                MySnackbar.showSnackBar(this, newsAddToFav, BaseThemeActivity.NO_NETWORK).show();
            }
        } else {
            alertDialog = new AlertDialog(this, alertTitle, BaseThemeActivity.ALERT_LOGIN_TO_SAVE);
            alertDialog.setOnAlertDialogListener(this);
            alertDialog.show();

        }
    }

    private void saveNewsToMyFavourite() {
        saveResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    String status = nodeObject.getString("status");
                    if (status.equals("success")) {
                        selectedNews.setIsSaved(1);
                        db.updateNewsDetail(selectedNews);
                        Log.i(TAG, "saved:" + selectedNews.toString());
                        newsAddToFav.setColorFilter(SAVED_COLOR);
                    } else {
                        MySnackbar.showSnackBar(NewsDetailActivity.this, newsAddToFav, "Something went wrong").show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        saveErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MySnackbar.showSnackBar(NewsDetailActivity.this, newsAddToFav, "Something went wrong").show();
            }
        };

        progressDialog.setMessage(BaseThemeActivity.PLEASE_WAIT);
        progressDialog.setCancelable(true);
        progressDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("consumer_news[newsId]", selectedNews.getNewsId());
        params.put("consumer_news[media]", BaseThemeActivity.CURRENT_MEDIA);

        Log.i(TAG, "token:" + sessionManager.getToken());
        Log.i(TAG, "consumer_news[newsId]:" + selectedNews.getNewsId());
        Log.i(TAG, "consumer_news[media]:" + BaseThemeActivity.CURRENT_MEDIA);

        WebService.hitServerWithHeaderAndParams(ServerConfig.SAVE_NEWS_URL, sessionManager.getToken(), params, saveResponse, saveErrorListener);
    }

    private void deleteNewsFromMyFavourite() {
        unSaveResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    String status = nodeObject.getString("status");
                    String message = nodeObject.getString("message");
                    if (status.equals("success")) {
                        selectedNews.setIsSaved(0);
                        db.updateNewsDetail(selectedNews);
                        Log.i(TAG, "saved:" + selectedNews.toString());
                        newsAddToFav.setColorFilter(UN_SAVED_COLOR);
                    } else {
                        MySnackbar.showSnackBar(NewsDetailActivity.this, newsAddToFav, message).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        unsaveErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MySnackbar.showSnackBar(NewsDetailActivity.this, newsAddToFav, "Something went wrong from volley error").show();
            }
        };

        progressDialog.setMessage(BaseThemeActivity.PLEASE_WAIT);
        progressDialog.setCancelable(true);
        progressDialog.show();


        String url = ServerConfig.getUnsaveNewsUrl(selectedNews.getNewsId(), BaseThemeActivity.CURRENT_MEDIA);
        Log.i(TAG, "deleteurl" + url);
        WebService.deleteNewsFromFavourite(url, sessionManager.getToken(), unSaveResponse, unsaveErrorListener);
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


    private void loadAdapter() {
        if (newsObjs.size() > 1)
            relatedNewsTextView.setVisibility(View.VISIBLE);
        newsTitlesAdapter = new RelatedNewsTitleAdapter(true, categoryId, newsObjs);
        newsTitlesAdapter.setOnRecyclerPositionListener(this);
        recyclerVIew.setAdapter(newsTitlesAdapter);

        //setting newstedscrollingEnabled to false so that app bar collapse only when recyclerview is scrolled
        recyclerVIew.setNestedScrollingEnabled(false);

    }

    private void loadingDetail(NewsObj news) {
        if (news.getImg().length() > 0)
            BasicUtilMethods.loadImage(this, news.getImg(), image);

        if (news.getIsSaved() == 1) {
            newsAddToFav.setColorFilter(SAVED_COLOR);
        } else {
            newsAddToFav.setColorFilter(UN_SAVED_COLOR);
        }
        mainLayout.setVisibility(View.VISIBLE);
        titleTextView.setText(news.getTitle());
        newsCategoryTextView.setText(news.getNewsCategoryName());
        newsTimeTextView.setText(news.getDate());

        String desc = news.getDescription();
        desc = "<html><body><p align=\"justify\">" + desc + "</p></body></html>";

        setNormalFont();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

//        String head = "<head><style>img{display: inline; height: auto; max-width: 100%;}</style></head>";

        descriptionTextView.loadDataWithBaseURL(null, desc, "text/html", "utf-8", null);

        //disabling the text selection=================================================================================================================
        descriptionTextView.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                Log.i(TAG,"long pressed");
                return true;
            }
        });

        /**
         * toggle the favourite icon color for saved/empty respectively
         */


        Log.i("selectednewsduringSave:", news.toString());
    }

    private void setNormalFont() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setDefaultFontSize(50);
        } else {
            webSettings.setDefaultFontSize(15);
        }

    }

    private void setLargeFont() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setDefaultFontSize(60);
        } else {
            webSettings.setDefaultFontSize(20);
        }
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
        newsObjs = getIntent().getParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST);
        newsType = newsObjs.get(0).getNewsType();
        categoryName = newsObjs.get(0).getNewsCategoryName();
        categoryId = Integer.parseInt(newsObjs.get(0).getNewsCategoryId());
        for (int i = 0; i < newsObjs.size(); i++) {
            Log.i("ischecked", newsObjs.get(i).getIsTOShow() + "");
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
                setNormalFont();
                break;

            case R.id.news_font_control_large:
                scrollUp();
                setLargeFont();
                break;

            case android.R.id.home:
                goback();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void goback() {
        if (Dashboard.getInstance() != null) {
            this.finish();
        } else {
            Intent intent = new Intent(NewsDetailActivity.this, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        db.close();
    }

    @Override
    public void onBackPressed() {
        goback();
    }

    @Override
    public void onChildItemPositionListen(int position, View view) {
        SELECTED_NEWS_POSITION = position;
        selectedNews = newsObjs.get(SELECTED_NEWS_POSITION);
        Log.i(TAG, "selected :" + selectedNews.toString());
        for (int i = 0; i < newsObjs.size(); i++) {
            newsObjs.get(i).setIsTOShow(1);
        }
        newsObjs.get(position).setIsTOShow(0);
        if (BasicUtilMethods.isNetworkOnline(this)) {
            getNewsDetailFromServer(selectedNews.getNewsId());
            scrollUp();
        } else {
            if (db.isNewsDetailPresent(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId())) {
                scrollUp();
                loadingDetail(db.getNewsObj(selectedNews.getNewsType(), selectedNews.getNewsCategoryId(), selectedNews.getNewsId()));
            }
            MySnackbar.showSnackBar(this, recyclerVIew, BaseThemeActivity.NO_NETWORK).show();
        }

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
