package com.bajratechnologies.nagariknews.views.activities;

import android.app.ProgressDialog;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.controller.server_request.ServerConfig;
import com.bajratechnologies.nagariknews.controller.server_request.WebService;
import com.bajratechnologies.nagariknews.controller.sqlite.SqliteDatabase;
import com.bajratechnologies.nagariknews.model.Multimedias;
import com.bajratechnologies.nagariknews.model.epaper.Page;
import com.bajratechnologies.nagariknews.views.customviews.MySnackbar;
import com.bajratechnologies.nagariknews.widget.EpaperPagerAdapter;
import com.bajratechnologies.nagariknews.widget.PhotosCartoonPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/29/16.
 */
public class GalleryViewActivity extends BaseThemeActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_header_layout)
    RelativeLayout toolbarHeaderLayout;
    @Bind(R.id.epaper_viewpager)
    ViewPager epaperViewpager;
    private ProgressDialog dialog;

    String galleryType;
    int TYPE;

    EpaperPagerAdapter epaperPagerAdapter;
    private int epaperId;
    private String date;
    Response.Listener<String> response;
    Response.ErrorListener errorListener;
    ArrayList<Page> epaperPages;

    ArrayList<Multimedias> multimedias;
    PhotosCartoonPagerAdapter photosCartoonPagerAdapter;
    private int POSITION;
    private SqliteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * main content view for the {@link GalleryViewActivity}
         */
        setContentView(R.layout.epaper_activity_layout);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        galleryType = getIntent().getStringExtra(StaticStorage.KEY_GALLERY_TYPE);

        TYPE = getIntent().getIntExtra(StaticStorage.FOLDER_TYPE, 1);
        if (galleryType.equals(StaticStorage.KEY_PHOTO_CARTOON)) {
            multimedias = getIntent().getExtras().getParcelableArrayList(StaticStorage.KEY_PHOTO_CARTOON);
            POSITION = getIntent().getIntExtra(StaticStorage.KEY_PHOTOS_CARTOON_POSITION, 0);

        } else if (galleryType.equals(StaticStorage.KEY_EPAPER)) {
            epaperId = getIntent().getExtras().getInt("id");
            date = getIntent().getExtras().getString("date");

        }
        db = new SqliteDatabase(this);
        db.open();
        epaperPages = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarHeaderLayout.setVisibility(View.GONE);
        setViewPagerData();

    }

    private void setViewPagerData() {
        if (galleryType.equals(StaticStorage.KEY_EPAPER)) {
            handleServerResponse();
            fetchEpapers();
        } else {
            photosCartoonPagerAdapter = new PhotosCartoonPagerAdapter(getSupportFragmentManager(), multimedias, TYPE, false);
            epaperViewpager.setAdapter(photosCartoonPagerAdapter);
            epaperViewpager.setCurrentItem(POSITION);
        }

        epaperViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setPageTitle(position + 1);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void fetchEpapers() {

        if (BasicUtilMethods.isNetworkOnline(this)) {
            dialog.show();
            String url = ServerConfig.getEpaperPages(epaperId);
            Log.i("EPAPER", url);
            WebService.getServerData(url, response, errorListener);
        } else {
            loadingFromCache();
            MySnackbar.showSnackBar(this, epaperViewpager, BaseThemeActivity.NO_NETWORK);
        }
    }

    private void handleServerResponse() {
        response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    db.deleteEpaperPages(BaseThemeActivity.CURRENT_MEDIA, String.valueOf(epaperId));
                    db.saveEpaperPages(BaseThemeActivity.CURRENT_MEDIA, String.valueOf(epaperId), response);
                } catch (CursorIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                parseResponse(response);
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                loadingFromCache();
            }
        };
    }

    private void loadingFromCache() {
        String cacheResponse;
        try {
            cacheResponse = db.getEpaperPages(BaseThemeActivity.CURRENT_MEDIA, String.valueOf(epaperId));
            if (!TextUtils.isEmpty(cacheResponse))
                parseResponse(cacheResponse);
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void parseResponse(String response) {
        try {
            JSONObject nodeObject = new JSONObject(response);
            String status = nodeObject.getString("status");
            if (status.equalsIgnoreCase("success")) {
                JSONObject dataObject = nodeObject.getJSONObject("data");
                JSONArray images = dataObject.getJSONArray("images");
                for (int i = 0; i < images.length(); i++) {
                    JSONObject imageObject = images.getJSONObject(i);
//                    int id = imageObject.getInt("id");
                    int id = 1;
                    String imageUrl = imageObject.getString("imageUrl");
                    epaperPages.add(new Page(id, imageUrl));
                }
                /**
                 * sorting the pages in ascending order
                 */

                Comparator<Page> pageComparator = new Comparator<Page>() {
                    @Override
                    public int compare(Page lhs, Page rhs) {
                        return lhs.getPageUrl().compareTo(rhs.getPageUrl());
                    }
                };
                Collections.sort(epaperPages,pageComparator);

                /**
                 * setting the pages to the adapter
                 */
                epaperPagerAdapter = new EpaperPagerAdapter(getSupportFragmentManager(), epaperPages);
                epaperViewpager.setAdapter(epaperPagerAdapter);
            } else {
                MySnackbar.showSnackBar(GalleryViewActivity.this, epaperViewpager, StaticStorage.SOMETHING_WENT_WRONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPageTitle(int currentPageNumber) {
        if (galleryType.equals(StaticStorage.KEY_EPAPER)) {
            getSupportActionBar().setTitle(Dashboard.currentNewsType + " (" + date + ") " + currentPageNumber + "/" + epaperPages.size());
        } else {
            getSupportActionBar().setTitle(Dashboard.currentNewsType);
        }
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
