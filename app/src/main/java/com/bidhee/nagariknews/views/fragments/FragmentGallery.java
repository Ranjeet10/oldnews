package com.bidhee.nagariknews.views.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.controller.AppbarListener;
import com.bidhee.nagariknews.controller.server_request.ServerConfig;
import com.bidhee.nagariknews.controller.server_request.WebService;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.activities.YoutubePlayerActivity;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.views.customviews.ImageSliderDialog;
import com.bidhee.nagariknews.widget.GalleryAdapter;
import com.bidhee.nagariknews.widget.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/17/16.
 */
public class FragmentGallery extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private String TAG = getClass().getSimpleName();

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gallery_recycler_view)
    RecyclerView galleryRecyclerView;
    @Bind(R.id.content_not_found_parent_layout)
    LinearLayout contentNotFoundLayout;

    ControllableAppBarLayout appBarLayout;

    ArrayList<Multimedias> multimediaList;
    GalleryAdapter galleryAdapter;
    ImageSliderDialog imageSliderDialog;

    private Response.Listener<String> serverResponse;
    private Response.ErrorListener errorListener;
    private ProgressDialog dialog;
    private int TYPE;


    public static FragmentGallery createNewInstance(int type) {
        FragmentGallery frag = new FragmentGallery();
        Bundle box = new Bundle();
        box.putInt(StaticStorage.KEY_GALLERY_TYPE, type);
        frag.setArguments(box);

        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        Bundle args = getArguments();
        if (args != null)

        {
            TYPE = args.getInt(StaticStorage.KEY_GALLERY_TYPE);
        }


        imageSliderDialog = new ImageSliderDialog();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * accessing the views of the parent activity {@link Dashboard}
         */
        appBarLayout = (ControllableAppBarLayout) (getActivity().findViewById(R.id.app_bar_layout));
        (getActivity().findViewById(R.id.slide_image_view)).setVisibility(View.GONE);
        GridLayoutManager gridLayoutManager = null;

        if (TYPE == StaticStorage.VIDEOS) {
            /**
             * if the give type is of Video
             * only make single grid per row
             */
            gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        } else {
            gridLayoutManager = (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                    new GridLayoutManager(getActivity(), 2) :
                    new GridLayoutManager(getActivity(), 4);
        }

        galleryRecyclerView.setLayoutManager(gridLayoutManager);
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setItemAnimator(new DefaultItemAnimator());


        if (savedInstanceState != null) {
            multimediaList = savedInstanceState.getParcelableArrayList(StaticStorage.KEY_MULTIMEDIA_SAVED_STATE);
            loadAdapter(multimediaList);
        } else {
            if (TYPE == StaticStorage.VIDEOS) {

                if (Dashboard.sessionManager.getSwitchedNewsValue() == 1) {
                    fetchYoutubeChannelData(ServerConfig.NAGARIK_VIDEO_CHANNEL_ID, 45);
                } else if (Dashboard.sessionManager.getSwitchedNewsValue() == 2) {
                    fetchYoutubeChannelData(ServerConfig.NAGARIK_VIDEO_CHANNEL_ID, 45);
                } else {
                    fetchYoutubeChannelData(ServerConfig.NAGARIK_VIDEO_CHANNEL_ID, 45);
                }

            } else if (TYPE == StaticStorage.PHOTOS) {

                fetchGalleryFromOwnServer(TYPE);

            } else if (TYPE == StaticStorage.CARTOONS) {

                if (Dashboard.sessionManager.getSwitchedNewsValue() == 1) {
                    multimediaList = StaticStorage.getGalleryList(TYPE);
                    loadAdapter(multimediaList);
                } else if (Dashboard.sessionManager.getSwitchedNewsValue() == 2) {
                    multimediaList = StaticStorage.getGalleryList(TYPE);
                    loadAdapter(multimediaList);
                } else {
                    multimediaList = StaticStorage.getGalleryList(TYPE);
                    loadAdapter(multimediaList);
                }
            }
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                    @Override
                                                    public void onRefresh() {
                                                        ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
                                                        BasicUtilMethods.collapseAppbar(appBarLayout, null);
                                                    }
                                                }

        );
    }

    private void fetchGalleryFromOwnServer(int galleryType) {
        dialog.show();
        handleServerResponse();
        WebService.getServerData(ServerConfig.getGalleryUrl(Dashboard.baseUrl, galleryType), serverResponse, errorListener);
    }

    private void loadAdapter(ArrayList<Multimedias> multimediaList) {

        /**
         * toggle the {@value contentNotFoundLayout}
         * if the {@value multimediaList} is empty make it visible else,
         * make it invisible
         */

        if (multimediaList.size() > 0) {
            contentNotFoundLayout.setVisibility(View.INVISIBLE);

        } else {
            contentNotFoundLayout.setVisibility(View.VISIBLE);
        }


        galleryAdapter = new GalleryAdapter(multimediaList, TYPE);

        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));
    }

    private void fetchYoutubeChannelData(String channelId, int count) {
        dialog.show();
        handleServerResponse();
        WebService.getServerData(ServerConfig.getYoutubeChannelLinkUrl(channelId, count), serverResponse, errorListener);
    }

    private void handleServerResponse() {

        serverResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.i(TAG, response);
                multimediaList = new ArrayList<>();
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    if (TYPE == StaticStorage.VIDEOS) {
                        JSONArray itemArray = nodeObject.getJSONArray("items");
                        for (int i = 0; i < itemArray.length(); i++) {
                            JSONObject itemObject = itemArray.getJSONObject(i);
                            JSONObject idObject = itemObject.getJSONObject("id");
                            String id = idObject.getString("videoId");

                            JSONObject snippet = itemObject.getJSONObject("snippet");

                            String publishDagte = snippet.getString("publishedAt");
                            publishDagte = publishDagte.substring(0, publishDagte.lastIndexOf("."));
                            publishDagte = publishDagte.replace("T", " ");
                            publishDagte = getTimeAgo(publishDagte);

                            String title = snippet.getString("title");
                            multimediaList.add(new Multimedias("", title, id, "", publishDagte));
                        }

                    } else if (TYPE == StaticStorage.PHOTOS || TYPE == StaticStorage.CARTOONS) {
                        if (nodeObject.has("status")) {
                            String status = nodeObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray galleryArray = nodeObject.getJSONArray("data");
                                for (int i = 0; i < galleryArray.length(); i++) {
                                    JSONObject data = galleryArray.getJSONObject(i);
                                    String id = data.getString("id");
                                    String title = data.getString("title");
                                    String image = data.getString("featuredImage");
                                    String date = data.getString("publishOn");
                                    multimediaList.add(new Multimedias(id, title, image, "", date));
                                }
                            }
                        }
                    }

                    loadAdapter(multimediaList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing()) dialog.dismiss();
            }
        };
    }

    private String getTimeAgo(String publishDagte) {
        Log.i("publishDate", publishDagte);
        String resultTimeAgo = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date videoDate = sdf.parse(publishDagte);
            Log.i("videoDate", videoDate + "");
            Date systemDate = new Date();

            long vDateMilli = videoDate.getTime();
            Log.i("videoDMilli", vDateMilli + "");
            long systemDateMilli = systemDate.getTime();

            long resultInMilli = systemDateMilli - vDateMilli;


            long seconds, minutes, hour, day, week, month, year;

            seconds = resultInMilli / 1000;

            minutes = seconds / 60;
            if (minutes >= 60) {
                hour = minutes / 60;
                if (hour >= 24) {
                    day = hour / 24;
                    if (day >= 7) {
                        week = day / 7;
                        if (week >= 5) {
                            month = week / 5;
                            if (month >= 12) {
                                year = month / 12;
                                resultTimeAgo = String.valueOf(year) + "year ago";
                            } else {
                                resultTimeAgo = String.valueOf(month) + "months ago";
                            }
                        } else {
                            resultTimeAgo = String.valueOf(week) + "weeks ago";
                        }
                    } else {
                        resultTimeAgo = String.valueOf(day) + "days ago";
                    }
                } else {
                    resultTimeAgo = String.valueOf(hour) + "hours ago";
                }
            } else {
                resultTimeAgo = String.valueOf(minutes) + "minutes ago";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultTimeAgo;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        if (TYPE == StaticStorage.VIDEOS) {

//            Intent playerIntent = new Intent(getActivity(), YoutubePlayerActivity.class);
//
//            playerIntent.putExtra(StaticStorage.KEY_VIDEO_BUNDLE, multimediaList.get(position));
//            startActivity(playerIntent);


            try {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + multimediaList.get(position).getMultimediaPath()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } catch (ActivityNotFoundException e) {

                // youtube is not installed.Will be opened in other available apps

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + multimediaList.get(position).getMultimediaPath()));
                startActivity(i);
            }
        } else {
            imageSliderDialog.showDialog(getActivity(), multimediaList, position, TYPE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StaticStorage.KEY_MULTIMEDIA_SAVED_STATE, multimediaList);
    }
}
