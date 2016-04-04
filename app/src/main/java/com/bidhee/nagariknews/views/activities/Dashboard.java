package com.bidhee.nagariknews.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.MyAnimation;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.AppbarListener;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.views.fragments.FragmentAllNews;
import com.bidhee.nagariknews.views.fragments.FragmentEpaper;
import com.bidhee.nagariknews.views.fragments.FragmentExtra;
import com.bidhee.nagariknews.views.fragments.FragmentGallery;
import com.bidhee.nagariknews.views.fragments.FragmentSaved;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private String TAG = getClass().getSimpleName();
    Menu menu;
    @Bind(R.id.news_type_image_logo)
    ImageView newsTypeImageLogo;
    @Bind(R.id.slider)
    SliderLayout imageSlider;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar_layout)
    ControllableAppBarLayout appBarLayout;
    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.fragment_container_layout)
    FrameLayout fragmentContainer;

    LinearLayout switchMenusLayout;
    ImageView navImageView;
    TextView switchNagarik, switchRepublica, switchSukrabar;
    Handler handler;
    Runnable runnable;

    Boolean isUser;
    public static SessionManager sessionManager;
    HashMap<String, String> userDetail;

    private MyAnimation myAnimation;
    private String currentFragmentTag;
    private String currentTitle;
    private String currentNewsType;


    String navImageUrl = "http://nagariknews.com/media/k2/items/cache/x78fd7c7087409bceafbd973478ab7045_L.jpg.pagespeed.ic.YKq77EitMY.webp";
    //    MyAnimation myAnimation;
    private Fragment replaceableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        switch (sessionManager.getSwitchedNewsValue()) {
            case 1:
                setTheme(R.style.RepublicaTheme);
                break;
            case 2:
                setTheme(R.style.NagarikTheme);
                break;
            case 3:
                setTheme(R.style.SukrabarTheme);
                break;
        }

        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        handler = new Handler();
        myAnimation = new MyAnimation();

        settingToolbar();
        setUpImageSlider();
//        setUpVIewFlipper();

        userDetail = sessionManager.getLoginDetail();

        if (sessionManager.isFirstRun(this)) {
            getSharedPreferences(SessionManager.FIRST_RUN_PREFERENCE_NAME, MODE_PRIVATE)
                    .edit()
                    .putBoolean(SessionManager.isFirstRun, false)
                    .apply();

            //set the font size 2 for the normal newsDetail to the session
            sessionManager.setTheFontSize(2);

            /**
             * argument 1 is the value for Republica News type
             */
            sessionManager.switchNewsTo(1);
            //starting loginIntent
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
            return;

        } else {
            isUser = sessionManager.isLoggedIn() ? true : false;
        }


        setUpNavigation();
        setUpNavigationMenu();
        settingCollapsingToolBarListener();

        if (savedInstanceState != null) {

            //if savedInstanceState is not null and if there are data present
            //jst fetch those data and save to the local variables
            currentFragmentTag = savedInstanceState.getString(StaticStorage.KEY_CURRENT_TAG);
            currentTitle = savedInstanceState.getString(StaticStorage.KEY_CURRENT_TITLE);
            currentNewsType = savedInstanceState.getString(StaticStorage.KEY_NEWS_TYPE);

            replaceableFragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
            attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle);

        } else {

            //if oncreate was called for the first time then initialize the very first variables

            switch (sessionManager.getSwitchedNewsValue()) {
                case 1:
                    currentTitle = getResources().getString(R.string.all_news_r);
                    break;
                case 2:
                    currentTitle = getResources().getString(R.string.all_news_n);
                    break;
                case 3:
                    currentTitle = getResources().getString(R.string.all_news_r);
                    break;
            }

            replaceableFragment = FragmentAllNews.createNewInstance();
            currentFragmentTag = replaceableFragment.getClass().getName();
            attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle);
        }

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        viewFlipper.startFlipping();
//    }

    private void setUpVIewFlipper() {
        Animation slideInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation slideOutAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(slideInAnim);
        viewFlipper.setOutAnimation(slideOutAnim);


        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 4000);
                viewFlipper.showNext();
            }
        };
        handler.post(runnable);
    }

    private void setUpImageSlider() {
        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("पुनरागमनमा विश्वस्त अस्ट्रेलिया", "http://nagariknews.com/media/k2/items/cache/x22779b96550ec2f4cb77a363acfed28d_L.jpg.pagespeed.ic.sURn1ZmJlg.jpg");
//        url_maps.put("अझै पाइएन ग्यास", "http://nagariknews.com/media/k2/items/cache/xdf2a5a6447c772e5d774c787f3f38111_L.jpg.pagespeed.ic.BFrcFCn6QJ.jpg");
//        url_maps.put("मन्त्रीज्यू, पैसा उठाइदिनुस्", "http://nagariknews.com/media/k2/items/cache/xb61ba0575ba650b6e2b511023b28b46c_L.jpg.pagespeed.ic.Oa9Qkcelap.jpg");


        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            imageSlider.addSlider(textSliderView);
        }
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        imageSlider.setDuration(4000);
        imageSlider.addOnPageChangeListener(this);
    }

    private void settingCollapsingToolBarListener() {

        //initially set the title to empty
        getSupportActionBar().setTitle("");

        appBarLayout.setOnStateChangeListener(new ControllableAppBarLayout.OnStateChangeListener() {
            @Override
            public void onStateChange(ControllableAppBarLayout.State toolbarChange) {
                switch (toolbarChange) {

                    case COLLAPSED:
                        collapsingToolbarLayout.setTitle(currentNewsType + " : " + currentTitle);
                        myAnimation.expand(newsTypeImageLogo);
                        break;

                    case EXPANDED:
                        collapsingToolbarLayout.setTitle("");
                        myAnimation.collapse(newsTypeImageLogo);
                        break;

                    case IDLE:
                        collapsingToolbarLayout.setTitle(currentNewsType + " : " + currentTitle);
                        myAnimation.collapse(newsTypeImageLogo);

                        break;
                }
            }
        });
    }


    private void settingToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_light));

    }

    private void attachFragment(Fragment fragment, String currentFragmentTag, String currentNewsType, String currentTitle) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_layout, fragment, currentFragmentTag)
                .commit();

        collapsingToolbarLayout.setTitle(currentNewsType + " : " + currentTitle);
        Log.d(TAG, currentFragmentTag);

    }


    private void setUpNavigation() {
        if (sessionManager.getSwitchedNewsValue() == 1) {

            //value 1 means saved newstype was for Republica
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_republica : R.menu.free_user_nav_menu_republica);
            currentNewsType = getResources().getString(R.string.republica);
            newsTypeImageLogo.setImageResource(R.mipmap.ic_share_black);

        } else if (sessionManager.getSwitchedNewsValue() == 2) {
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_nagarik : R.menu.free_user_nav_menu_nagarik);
            currentNewsType = getResources().getString(R.string.nagarik);
            newsTypeImageLogo.setImageResource(R.mipmap.ic_alarm_black_24dp);

        } else if (sessionManager.getSwitchedNewsValue() == 3) {
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_republica : R.menu.free_user_nav_menu_republica);
            currentNewsType = getResources().getString(R.string.sukrabar);
            newsTypeImageLogo.setImageResource(R.mipmap.ic_favorite_black);
        }

        switchMenusLayout = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.switch_menus_background_layout);
        navImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_image_view);
        switchRepublica = (TextView) navigationView.getHeaderView(0).findViewById(R.id.switch_republica);
        switchNagarik = (TextView) navigationView.getHeaderView(0).findViewById(R.id.switch_nagarik);
        switchSukrabar = (TextView) navigationView.getHeaderView(0).findViewById(R.id.switch_sukrabar);

        Picasso.with(this)
                .load(navImageUrl)
                .error(R.drawable.nagariknews)
                .placeholder(R.drawable.nagariknews)
                .into(navImageView);

        switch (sessionManager.getSwitchedNewsValue()) {
            case 1:
                switchMenusLayout.setBackgroundColor(getResources().getColor(R.color.republicaColorPrimaryDark));
                switchRepublica.setVisibility(View.GONE);
                switchNagarik.setVisibility(View.VISIBLE);
                switchSukrabar.setVisibility(View.VISIBLE);
                break;
            case 2:
                switchMenusLayout.setBackgroundColor(getResources().getColor(R.color.nagarikColorPrimaryDark));
                switchRepublica.setVisibility(View.VISIBLE);
                switchNagarik.setVisibility(View.GONE);
                switchSukrabar.setVisibility(View.VISIBLE);
                break;
            case 3:
                switchMenusLayout.setBackgroundColor(getResources().getColor(R.color.sukrabarColorPrimaryDark));
                switchRepublica.setVisibility(View.VISIBLE);
                switchNagarik.setVisibility(View.VISIBLE);
                switchSukrabar.setVisibility(View.GONE);
                break;
        }

        View.OnClickListener switchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // republica 1
                // nagarik 2
                // sukrabar 3
                switch (v.getId()) {
                    case R.id.switch_republica:
                        sessionManager.switchNewsTo(1);
                        break;
                    case R.id.switch_nagarik:
                        sessionManager.switchNewsTo(2);
                        break;
                    case R.id.switch_sukrabar:
                        sessionManager.switchNewsTo(3);
                        break;
                }
                reLaunch();
            }
        };

        switchRepublica.setOnClickListener(switchListener);
        switchNagarik.setOnClickListener(switchListener);
        switchSukrabar.setOnClickListener(switchListener);

    }

    private void reLaunch() {
//        drawerLayout.closeDrawer(GravityCompat.START);
        finish();
        startActivity(new Intent(this, Dashboard.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setUpNavigationMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        this.menu = menu;

        //show hide the menu item
        //show menu item (search) if the fragment is FragmentAllNews
        //hide menu item (search) if the fragment is other than FragmentAllNews
        if (!TextUtils.isEmpty(currentFragmentTag)) {
            if (currentFragmentTag.equals("com.bidhee.nagariknews.views.fragments.FragmentAllNews")) {
                expandAppbar();

            } else {
                collapseAppbar();
            }
        }

        return true;
    }

    private void collapseAppbar() {
        appBarLayout.setEnabled(false);
        appBarLayout.setActivated(false);
        appBarLayout.collapseToolbar(true);
        this.menu.getItem(0).setVisible(false);
    }

    private void expandAppbar() {
        appBarLayout.setEnabled(true);
        appBarLayout.expandToolbar(true);
        this.menu.getItem(0).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        currentTitle = item.toString();

        int id = item.getItemId();

        //// Expand the collapsing toolbar with animation if it is all news fragment else Collapse it ///

        if (id == R.id.nav_all_news) {
            expandAppbar();
        } else {
            collapseAppbar();
        }

        switch (id) {
            case R.id.nav_all_news:
                replaceableFragment = FragmentAllNews.createNewInstance();

                break;

            case R.id.nav_photos:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.PHOTOS);

                break;

            case R.id.nav_cartoons:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.CARTOONS);

                break;

            case R.id.nav_videos:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.VIDEOS);

                break;

            case R.id.nav_epaper:
                int epaperId = 0;
                switch (sessionManager.getSwitchedNewsValue()) {
                    case 1:
                        epaperId = StaticStorage.E_PAPER_REPUBLICA;
                        break;
                    case 2:
                        epaperId = StaticStorage.E_PAPER_NAGARIK;
                        break;
                    case 3:
                        epaperId = StaticStorage.E_PAPER_SUKRABAR;
                        break;
                }
                replaceableFragment = FragmentEpaper.createNewInstance(epaperId);
                break;

            case R.id.nav_saved:
                replaceableFragment = FragmentSaved.createNewInstance();
                break;


            case R.id.nav_extras:
                replaceableFragment = FragmentExtra.createNewInstance();

                break;

            case R.id.nav_share:
                break;
        }


        currentFragmentTag = replaceableFragment.getClass().getName();
        attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle);

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(StaticStorage.KEY_CURRENT_TAG, currentFragmentTag);
        outState.putString(StaticStorage.KEY_NEWS_TYPE, currentNewsType);
        outState.putString(StaticStorage.KEY_CURRENT_TITLE, currentTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        Log.i("onDestroy", "called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        imageSlider.stopAutoCycle();
        Log.i("onstop", "called");
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


}
