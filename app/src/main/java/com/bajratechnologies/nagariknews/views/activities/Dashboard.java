package com.bajratechnologies.nagariknews.views.activities;

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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.controller.SessionManager;
import com.bajratechnologies.nagariknews.controller.interfaces.AlertDialogListener;
import com.bajratechnologies.nagariknews.controller.interfaces.ListPositionListener;
import com.bajratechnologies.nagariknews.controller.sqlite.SqliteDatabase;
import com.bajratechnologies.nagariknews.gcm.RegistrationIntentService;
import com.bajratechnologies.nagariknews.model.Multimedias;
import com.bajratechnologies.nagariknews.views.customviews.AlertDialog;
import com.bajratechnologies.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bajratechnologies.nagariknews.views.customviews.EpaperOptionMenu;
import com.bajratechnologies.nagariknews.views.fragments.FragmentAllNews;
import com.bajratechnologies.nagariknews.views.fragments.FragmentEpaper;
import com.bajratechnologies.nagariknews.views.fragments.FragmentGallery;
import com.bajratechnologies.nagariknews.views.fragments.FragmentSaved;
import com.bajratechnologies.nagariknews.widget.PhotosCartoonPagerAdapter;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class Dashboard extends BaseThemeActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, AlertDialogListener, ListPositionListener {

    public static Dashboard instance = null;

    public static Dashboard getInstance() {
        return instance;
    }

    private String TAG = getClass().getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    Menu menu;
    @Bind(R.id.news_type_image_logo)
    ImageView newsTypeImageLogo;
    @Bind(R.id.btn_epaper_option)
    ImageView epaperOptionMenu;
    @Bind(R.id.banner_viewpager)
    ViewPager bannerViewpager;
    @Bind(R.id.indicator)
    CircleIndicator indicator;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar_layout)
    ControllableAppBarLayout appBarLayout;
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
    Handler handler, viewpagerHandler;
    Runnable runnable, viewpagerRunnable;
    int NUM_PAGES = 0;
    int currentPage = 0;
    Timer swipeTimer;

    private Boolean isUser;
    private Boolean shouldReplaceFragment = false;
    private Boolean isProfileImageClicked = false;
    private AlertDialog alertDialog;
    private EpaperOptionMenu epaperOptionMenuDialog;

    HashMap<String, String> userDetail;
    public static String userName = "";
    public static String userEmail = "";
    public static String userImage = "";

    private String currentFragmentTag;
    public static String currentTitle;
    public static String currentNewsType;
    private Boolean collapseToolbar = false;
    public static int logoImage;
    private PhotosCartoonPagerAdapter adapter;

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
        printHashKey();
        settingToolbar();


        userDetail = sessionManager.getLoginDetail();

        if (sessionManager.isFirstRun(this)) {
            getSharedPreferences(SessionManager.FIRST_RUN_PREFERENCE_NAME, MODE_PRIVATE)
                    .edit()
                    .putBoolean(SessionManager.isFirstRun, false)
                    .apply();

            //set the font size 2 for the normal newsDetail to the session
            sessionManager.setTheFontSize(2);

            /**
             * {@link SessionManager#switchNewsTo(int)}
             * argument 1 is the value for Republica
             * argument 2 is the value for Nagarik
             * argument 3 is the value for Sukrabar
             */
            sessionManager.switchNewsTo(2);
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
                Log.i(TAG, "AccessToken:" + sessionManager.getToken());
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
                    logoImage = StaticStorage.NEWS_LOGOS[0];

                    break;
                case 2:
                    logoImage = StaticStorage.NEWS_LOGOS[1];
                    break;
                case 3:
                    logoImage = StaticStorage.NEWS_LOGOS[2];
                    break;
            }

            newsTypeImageLogo.setImageResource(logoImage);
            replaceableFragment = FragmentAllNews.createNewInstance();
            currentFragmentTag = replaceableFragment.getClass().getName();
            attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle, false);
        }

        startGcmCheck();
    }


    //=================== methods for the gcm and play===================
    private void startGcmCheck() {

        if (checkPlayServices()) {
            String regId = BasicUtilMethods.getRegistrationId(this);
            if (regId.isEmpty() || !SessionManager.isRegisterd(this)) {

                Log.i(TAG, "USER NOT REGISTERED TO SERVER");
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            } else {
                Log.i(TAG, "USER REGISTERED ALREADY");
            }

        } else {
            Log.i(TAG, "NO PLAY SERVICES");
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    //[gcm methods ended]

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


    public void setBannerViewpager(ArrayList<Multimedias> list) {
//        if (adapter.getCount() < 0) {
            adapter = new PhotosCartoonPagerAdapter(getSupportFragmentManager(), list, 100, true);
            bannerViewpager.setAdapter(adapter);
            indicator.setViewPager(bannerViewpager);
            NUM_PAGES = list.size();
            swipePager();
//        }
    }

    @OnClick(R.id.btn_epaper_option)
    void onEpaperOptionClicked() {
        epaperOptionMenuDialog = new EpaperOptionMenu(this);
        epaperOptionMenuDialog.setListPositionListener(this);

        Window window = epaperOptionMenuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;

        wlp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        wlp.y = 50;
        window.setAttributes(wlp);


        epaperOptionMenuDialog.show();
    }

    private void swipePager() {

        viewpagerHandler = new Handler();
        viewpagerRunnable = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                try {
                    bannerViewpager.setCurrentItem(currentPage++, true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(viewpagerRunnable);
            }
        }, 500, 3000);
    }

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
            epaperOptionMenu.setVisibility(View.GONE);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, BaseThemeActivity.BACK_PRESSED_MESSAGE, Toast.LENGTH_LONG).show();

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
                epaperOptionMenu.setVisibility(View.GONE);
                break;

            case R.id.nav_photos:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.PHOTOS);
                shouldReplaceFragment = true;
                epaperOptionMenu.setVisibility(View.GONE);
                break;

            case R.id.nav_cartoons:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.CARTOONS);
                shouldReplaceFragment = true;
                epaperOptionMenu.setVisibility(View.GONE);
                break;

            case R.id.nav_videos:
                replaceableFragment = FragmentGallery.createNewInstance(StaticStorage.VIDEOS);
                shouldReplaceFragment = true;
                epaperOptionMenu.setVisibility(View.GONE);
                break;

            case R.id.nav_epaper:
                int epaperId = 0;
                switch (sessionManager.getSwitchedNewsValue()) {
                    case 1:
                        epaperId = StaticStorage.E_PAPER_REPUBLICA;
                        epaperOptionMenu.setVisibility(View.GONE);
                        break;
                    case 2:
                        epaperId = StaticStorage.E_PAPER_NAGARIK;
                        epaperOptionMenu.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        epaperId = StaticStorage.E_PAPER_SUKRABAR;
                        epaperOptionMenu.setVisibility(View.GONE);
                        break;
                }
                replaceableFragment = FragmentEpaper.createNewInstance(epaperId);
                shouldReplaceFragment = true;
                break;

            case R.id.nav_saved:
                replaceableFragment = new FragmentSaved();
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
        try {
            if (runnable != null)
                handler.removeCallbacks(runnable);
            if (viewpagerRunnable != null)
                viewpagerHandler.removeCallbacks(viewpagerRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("onstop", "called");
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
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }

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
        finish();
    }

    @Override
    public void alertNegativeButtonClicked() {
        alertDialog.dismiss();
    }


    @Override
    public void tappedPosition(int position) {
        if (replaceableFragment instanceof FragmentEpaper) {
            ((FragmentEpaper) replaceableFragment).getSelectedEpaperFor(position);
        }
    }
}
