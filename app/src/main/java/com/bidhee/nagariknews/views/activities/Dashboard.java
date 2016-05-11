package com.bidhee.nagariknews.views.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.MyAnimation;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.BaseThemeActivity;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.controller.interfaces.AlertDialogListener;
import com.bidhee.nagariknews.controller.sqlite.SqliteDatabase;
import com.bidhee.nagariknews.views.customviews.AlertDialog;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.views.fragments.FragmentAllNews;
import com.bidhee.nagariknews.views.fragments.FragmentEpaper;
import com.bidhee.nagariknews.views.fragments.FragmentGallery;
import com.bidhee.nagariknews.views.fragments.FragmentSaved;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Dashboard extends BaseThemeActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,
        GoogleApiClient.OnConnectionFailedListener, AlertDialogListener {

    public static Dashboard instance = null;

    public static Dashboard getInstance() {
        return instance;
    }

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

    GoogleApiClient googleApiClient;

    private ControllableAppBarLayout.LayoutParams params;
    LinearLayout switchMenusLayout;
    ImageView navImageView, userImageView;
    TextView userNameTextView, userEmailTextView;
    ImageView switchNagarik, switchRepublica, switchSukrabar;
    Handler handler;
    Runnable runnable;

    private Boolean isUser;
    private Boolean shouldReplaceFragment = false;
    private Boolean isProfileImageClicked = false;
    private AlertDialog alertDialog;

    //    public static SessionManager sessionManager;
    HashMap<String, String> userDetail;
    public static String userName = "";
    public static String userEmail = "";
    public static String userImage = "";

    private MyAnimation myAnimation;
    private String currentFragmentTag;
    //    public static int currentTheme;
    public static String currentTitle;
    public static String currentNewsType;
    private Boolean collapseToolbar = false;
    public static int logoImage;

//    public static String baseUrl = "";


    String navImageUrl = "https://s3.amazonaws.com/uploads.hipchat.com/509974/3391264/KWM6fpLrshlYjh0/profile.jpg";
    //    MyAnimation myAnimation;
    private Fragment replaceableFragment;
    private FragmentManager fragmentManager;

    Boolean doubleBackToExitPressedOnce = false;

    private SqliteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        db = new SqliteDatabase(this);
        db.open();


        /**
         * main content view
         */
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        handler = new Handler();
        myAnimation = new MyAnimation();
        printHashKey();
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
            if (isUser) {
                userName = userDetail.get(SessionManager.KEY_USER_NAME);
                userImage = userDetail.get(SessionManager.KEY_USER_IMAGE);
                userEmail = userDetail.get(SessionManager.KEY_USER_EMAIL);
            }

            googleClientConfigure();

        }


        setUpNavigation();
        setUpNavigationMenu();
        /**
         * accessing the params of {@value collapsingToolbarLayout}
         * to modify its attributes during navigation item selection
         */
        params = (ControllableAppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();

        settingCollapsingToolBarListener();

        if (savedInstanceState != null) {

            //if savedInstanceState is not null and if there are data present
            //jst fetch those data and save to the local variables
            currentFragmentTag = savedInstanceState.getString(StaticStorage.KEY_CURRENT_TAG);
            currentTitle = savedInstanceState.getString(StaticStorage.KEY_CURRENT_TITLE);
            currentNewsType = savedInstanceState.getString(StaticStorage.KEY_NEWS_TYPE);
            logoImage = savedInstanceState.getInt(StaticStorage.KEY_CURRENT_LOGO);

            newsTypeImageLogo.setImageResource(logoImage);
            replaceableFragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
            attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle, false);

        } else {

            //if oncreate was called for the first time then initialize the very first variables

            switch (sessionManager.getSwitchedNewsValue()) {
                case 1:
//                    currentTitle = getResources().getString(R.string.all_news_r);
                    logoImage = StaticStorage.NEWS_LOGOS[0];

                    break;
                case 2:
//                    currentTitle = getResources().getString(R.string.all_news_n);
                    logoImage = StaticStorage.NEWS_LOGOS[1];
                    break;
                case 3:
//                    currentTitle = getResources().getString(R.string.all_news_r);
                    logoImage = StaticStorage.NEWS_LOGOS[2];
                    break;
            }

            newsTypeImageLogo.setImageResource(logoImage);
            replaceableFragment = FragmentAllNews.createNewInstance();
            currentFragmentTag = replaceableFragment.getClass().getName();
            attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle, false);
        }


    }

    private void googleClientConfigure() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API
        // and the options specified by gGoogleSignInOptions.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        viewFlipper.startFlipping();
//    }


    private void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.bidhee.nagariknews",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

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
                        break;

                    case EXPANDED:
                        if (collapseToolbar)
                            BasicUtilMethods.collapseAppbar(appBarLayout, null);
                        break;

                    case IDLE:
                        /**
                         * IDLE is listened when collapsing toolbar is on motion
                         */
                        if (collapseToolbar)
                            BasicUtilMethods.collapseAppbar(appBarLayout, null);

                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    private void settingToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_light));

    }

    private void attachFragment(Fragment fragment, String currentFragmentTag, String currentNewsType, String currentTitle, Boolean addToBackStack) {
        if (addToBackStack) {
            fragmentManager.popBackStack();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_layout, fragment, currentFragmentTag)
                    .addToBackStack(null)
                    .commit();

//        collapsingToolbarLayout.setTitle(currentNewsType + " : " + currentTitle);
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_layout, fragment, currentFragmentTag)
                    .commit();

//        collapsingToolbarLayout.setTitle(currentNewsType + " : " + currentTitle);
        }

        Log.d(TAG, currentFragmentTag);

    }


    private void setUpNavigation() {

        if (sessionManager.getSwitchedNewsValue() == 1) {

            //value 1 means saved newstype was for Republica
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_republica : R.menu.free_user_nav_menu_republica);
            currentNewsType = getResources().getString(R.string.republica);

        } else if (sessionManager.getSwitchedNewsValue() == 2) {
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_nagarik : R.menu.free_user_nav_menu_nagarik);
            currentNewsType = getResources().getString(R.string.nagarik);

        } else if (sessionManager.getSwitchedNewsValue() == 3) {
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_sukrabar : R.menu.free_user_nav_menu_sukrabar);
            currentNewsType = getResources().getString(R.string.sukrabar);
        }

        //hide navigation scroll bar
        BasicUtilMethods.disableNavigationViewScrollbars(navigationView);

        switchMenusLayout = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.switch_menus_background_layout);
        navImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_image_view);
        userImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        userNameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.logged_in_user_name);
        userEmailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.logged_in_user_email);

        switchRepublica = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.switch_republica);
        switchNagarik = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.switch_nagarik);
        switchSukrabar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.switch_sukrabar);


        BasicUtilMethods.loadImage(this, navImageUrl, navImageView);

        if (userDetail != null) {
            Log.i(TAG, "user detail was null");
            BasicUtilMethods.loadImage(this, userImage, userImageView);
            userNameTextView.setText(userName);
            userEmailTextView.setText(userEmail);
        }

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
                        isProfileImageClicked = false;
                        sessionManager.switchNewsTo(1);
                        break;
                    case R.id.switch_nagarik:
                        isProfileImageClicked = false;
                        sessionManager.switchNewsTo(2);
                        break;
                    case R.id.switch_sukrabar:
                        isProfileImageClicked = false;
                        sessionManager.switchNewsTo(3);
                        break;
                    case R.id.profile_image:
                        isProfileImageClicked = true;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        if (sessionManager.isLoggedIn()) {
                            alertDialog = new AlertDialog(getInstance(), StaticStorage.ALERT_TITLE_LOGOUT, StaticStorage.LOGOUT_INFO + userName + " ?");
                        } else {
                            alertDialog = new AlertDialog(getInstance(), StaticStorage.ALERT_TITLE_LOGIN, StaticStorage.LOGIN_INFO);
                        }
                        alertDialog.setOnAlertDialogListener(getInstance());
                        alertDialog.show();
                        break;
                }
                if (!isProfileImageClicked)
                    reLaunch();
            }
        };

        switchRepublica.setOnClickListener(switchListener);
        switchNagarik.setOnClickListener(switchListener);
        switchSukrabar.setOnClickListener(switchListener);
        userImageView.setOnClickListener(switchListener);


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
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            collapseToolbar = false;
            fragmentManager.popBackStack();
            navigationView.setCheckedItem(R.id.nav_all_news);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Tap back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        this.menu = menu;
        this.menu.getItem(1).setVisible(false);
        this.menu.getItem(2).setVisible(false);

//        final Drawable drawable = menu.getItem(0).getIcon();
//        if (drawable != null) {
//            final Drawable wrapped = DrawableCompat.wrap(drawable);
//            drawable.mutate();
//            DrawableCompat.setTint(wrapped, Color.WHITE);
//            menu.getItem(0).setIcon(drawable);
//        }


        //show hide the menu item
        //show menu item (search) if the fragment is FragmentAllNews
        //hide menu item (search) if the fragment is other than FragmentAllNews
        if (!TextUtils.isEmpty(currentFragmentTag)) {
            if (currentFragmentTag.equals("com.bidhee.nagariknews.views.fragments.FragmentAllNews")) {
                BasicUtilMethods.expandAppbar(appBarLayout, this.menu);
            } else {
                BasicUtilMethods.collapseAppbar(appBarLayout, this.menu);
            }
        }

        return true;
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
//        scroll|exitUntilCollapsed


        //// Expand the collapsing toolbar with animation if it is all news fragment else Collapse it ///
        if (id == R.id.nav_all_news) {
            collapseToolbar = false;
            BasicUtilMethods.expandAppbar(appBarLayout, this.menu);
            /**
             * if the selected item is all news, set the following attributes to the {@link collapsingToolbarLayout}
             * set scroll flag {@link collapsingToolbarLayout} as
             * SCROLL_FLAG_SCROLL,
             * SCROLL_FLAG_ENTER_ALWAYS &
             * SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
             * so that image and toolbar collapses on scroll up
             */
            params.setScrollFlags(ControllableAppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | ControllableAppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);

        } else {
            collapseToolbar = true;
            BasicUtilMethods.collapseAppbar(appBarLayout, this.menu);
            /**
             * if the selected item is all news, set the following attributes to the {@link collapsingToolbarLayout}
             * set scroll flag {@link collapsingToolbarLayout} as
             * SCROLL_FLAG_SCROLL &
             * SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
             * so that image collapses but toolbar remains sticked on scroll up
             */
            params.setScrollFlags(ControllableAppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | ControllableAppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        }

        switch (id) {
            case R.id.nav_all_news:
                replaceableFragment = FragmentAllNews.createNewInstance();
                shouldReplaceFragment = true;
                break;

            case R.id.nav_photos:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.PHOTOS);
                shouldReplaceFragment = true;
                break;

            case R.id.nav_cartoons:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.CARTOONS);
                shouldReplaceFragment = true;
                break;

            case R.id.nav_videos:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.VIDEOS);
                shouldReplaceFragment = true;
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
                shouldReplaceFragment = true;
                break;

            case R.id.nav_saved:
                replaceableFragment = FragmentSaved.createNewInstance();
                shouldReplaceFragment = true;
                break;


//            case R.id.nav_extras:
//                replaceableFragment = FragmentExtra.createNewInstance();
//                shouldReplaceFragment = true;
//                break;

            case R.id.nav_select_categorylist:
                shouldReplaceFragment = false;
                startActivity(new Intent(this, SelectCategoryActivity.class));

                break;

            case R.id.nav_login:
                startActivity(new Intent(Dashboard.this, LoginActivity.class));
                shouldReplaceFragment = false;

                break;

            case R.id.nav_settings:
                startActivity(new Intent(Dashboard.this, SettingActivity.class));
                shouldReplaceFragment = false;

                break;

        }

        if (shouldReplaceFragment) {
            currentFragmentTag = replaceableFragment.getClass().getName();
            attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle, true);
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(StaticStorage.KEY_CURRENT_TAG, currentFragmentTag);
        outState.putString(StaticStorage.KEY_NEWS_TYPE, currentNewsType);
        outState.putString(StaticStorage.KEY_CURRENT_TITLE, currentTitle);
        outState.putInt(StaticStorage.KEY_CURRENT_LOGO, logoImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        db.close();
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


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void alertPositiveButtonClicked() {
        alertDialog.dismiss();
        if (sessionManager.isLoggedIn()) {
            logout();
        } else {
            Intent loginIntent = new Intent(getInstance(), LoginActivity.class);
            startActivity(loginIntent);
        }

    }

    private void logout() {
        String wasFrom = "";
        switch (sessionManager.getLoginType()) {
            case 1:
                //login type is simple form
                wasFrom = "simple login";
                break;
            case 2:
                //login type is facebook
                LoginManager.getInstance().logOut();
                wasFrom = "facebook";
                break;
            case 3:
                wasFrom = "twitter";
                try {
                    CookieSyncManager.createInstance(this);
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeSessionCookie();
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 4:
                wasFrom = "google";

                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
                googleApiClient.connect();
                break;
        }
        Log.i(TAG, wasFrom);
        sessionManager.clearSession();
        db.deleteAllNews();
        startActivity(new Intent(Dashboard.this, Dashboard.class));
//                shouldReplaceFragment = false;
        finish();
    }

    @Override
    public void alertNegativeButtonClicked() {
        alertDialog.dismiss();
    }
}
