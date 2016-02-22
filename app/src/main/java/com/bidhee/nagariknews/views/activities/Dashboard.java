package com.bidhee.nagariknews.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.MyAnimation;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.views.fragments.FragmentAllNews;
import com.bidhee.nagariknews.views.fragments.FragmentExtra;
import com.bidhee.nagariknews.views.fragments.FragmentGallery;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.fragment_container_layout)
    FrameLayout fragmentContainer;

    ImageView navImageView;
    TextView navBtnExpand;

    Boolean isUser;
    SessionManager sessionManager;
    HashMap<String, String> userDetail;

    private String currentFragmentTag;
    private String currentTitle;
    private String currentNewsType;

    String navImageUrl = "http://nagariknews.com/media/k2/items/cache/x78fd7c7087409bceafbd973478ab7045_L.jpg.pagespeed.ic.YKq77EitMY.webp";
    MyAnimation myAnimation;
    private Fragment replaceableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        myAnimation = new MyAnimation();
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        userDetail = sessionManager.getLoginDetail();

        if (sessionManager.isFirstRun(this)) {
            getSharedPreferences(SessionManager.FIRST_RUN_PREFERENCE_NAME, MODE_PRIVATE)
                    .edit()
                    .putBoolean(SessionManager.isFirstRun, false)
                    .apply();

            //set the font size 2 for the normal newsDetail to the session
            sessionManager.setTheFontSize(2);

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

            currentTitle = sessionManager.getSwitchedNewsValue() == 0 ?
                    getResources().getString(R.string.all_news_r) :
                    getResources().getString(R.string.all_news_n);

            replaceableFragment = FragmentAllNews.createNewInstance();
            currentFragmentTag = replaceableFragment.getClass().getName();
            attachFragment(replaceableFragment, currentFragmentTag, currentNewsType, currentTitle);
        }

    }

    private void attachFragment(Fragment fragment, String currentFragmentTag, String currentNewsType, String currentTitle) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_layout, fragment, currentFragmentTag)
                .commit();
        getSupportActionBar().setTitle(currentNewsType + " : " + currentTitle);
        Log.d("title", currentTitle);

    }


    private void setUpNavigation() {
        if (sessionManager.getSwitchedNewsValue() == 0) {

            //value 0 means saved newstype was for Republica

            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_republica : R.menu.free_user_nav_menu_republica);
            currentNewsType = getResources().getString(R.string.republica);

        } else if (sessionManager.getSwitchedNewsValue() == 1) {
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_nagarik : R.menu.free_user_nav_menu_nagarik);
            currentNewsType = getResources().getString(R.string.nagarik);

        }

        navImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_image_view);
        navBtnExpand = (TextView) navigationView.getHeaderView(0).findViewById(R.id.expand_btn);

        Picasso.with(this)
                .load(navImageUrl)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.logo)
                .into(navImageView);

        //check to what it was switched to previously
        navBtnExpand.setText((sessionManager.getSwitchedNewsValue() == 0 ?
                getResources().getString(R.string.nagarik) :
                getResources().getString(R.string.republica)));

        navBtnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int switchedTo = sessionManager.getSwitchedNewsValue();
                switch (switchedTo) {
                    case 0:
                        sessionManager.switchNewsTo(1);
                        break;
                    case 1:
                        sessionManager.switchNewsTo(0);
                        break;
                }
                //re-launch the application
                reLaunch();

            }
        });

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

        switch (id) {
            case R.id.nav_all_news:
                replaceableFragment = FragmentAllNews.createNewInstance();

                break;

            case R.id.nav_photos:
                replaceableFragment = FragmentGallery.createNewInstance();

                break;

            case R.id.nav_videos:
                replaceableFragment = FragmentGallery.createNewInstance();

                break;

            case R.id.nav_cartoons:
                replaceableFragment = FragmentGallery.createNewInstance();

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
    }
}
