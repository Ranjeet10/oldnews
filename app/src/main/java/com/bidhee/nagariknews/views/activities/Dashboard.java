package com.bidhee.nagariknews.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.MyAnimation;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.views.fragments.FragmentAllNews;
import com.bidhee.nagariknews.views.fragments.FragmentTest;
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
    private String currentFragmentTag = "";
    String navImageUrl = "http://nagariknews.com/media/k2/items/cache/x78fd7c7087409bceafbd973478ab7045_L.jpg.pagespeed.ic.YKq77EitMY.webp";
    MyAnimation myAnimation;
    public static String selectedNewsCategory = "";

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


        attachFragment(FragmentAllNews.getInstance());

    }

    private void attachFragment(Fragment fragment) {

        currentFragmentTag = fragment.getClass().getName();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_layout, fragment)
                .commit();

    }

    private void setUpNavigation() {
        if (sessionManager.getSwitchedNewsValue() == 0) {
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_republica : R.menu.free_user_nav_menu_republica);
            getSupportActionBar().setTitle(getResources().getString(R.string.republica));

            //set selected newsCategory to republica at initial state
            //since viewpager's first item is selected by default
            //we use this for the news detail
            selectedNewsCategory = "Breaking News";
        } else if (sessionManager.getSwitchedNewsValue() == 1) {
            navigationView.inflateMenu(isUser ? R.menu.user_logged_in_nav_menu_nagarik : R.menu.free_user_nav_menu_nagarik);
            getSupportActionBar().setTitle(getResources().getString(R.string.nagarik));

            //set selected newsCategory to republica at initial state
            //since viewpager's first item is selected by default
            //we use this for the news detail
            selectedNewsCategory = "मुख्य तथा ताजा समाचार";
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
        finish();
        startActivity(new Intent(this, Dashboard.class));
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_all_news:
                if (!currentFragmentTag.equalsIgnoreCase(FragmentAllNews.getInstance().getClass().getName()))
                    attachFragment(FragmentAllNews.getInstance());
                break;
            case R.id.nav_videos:
                attachFragment(FragmentTest.getInstance());
                break;
            case R.id.nav_photos:
                break;
            case R.id.nav_share:
                break;
        }


        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ButterKnife.unbind(this);
    }
}
