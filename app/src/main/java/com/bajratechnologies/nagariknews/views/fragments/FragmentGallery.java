package com.bajratechnologies.nagariknews.views.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.Utils.ToggleRefresh;
import com.bajratechnologies.nagariknews.controller.server_request.ServerConfig;
import com.bajratechnologies.nagariknews.controller.server_request.WebService;
import com.bajratechnologies.nagariknews.controller.sqlite.SqliteDatabase;
import com.bajratechnologies.nagariknews.model.Multimedias;
import com.bajratechnologies.nagariknews.views.activities.BaseThemeActivity;
import com.bajratechnologies.nagariknews.views.activities.Dashboard;
import com.bajratechnologies.nagariknews.views.activities.GalleryViewActivity;
import com.bajratechnologies.nagariknews.views.activities.YoutubePlayerActivity;
import com.bajratechnologies.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bajratechnologies.nagariknews.views.customviews.MySnackbar;
import com.bajratechnologies.nagariknews.widget.GalleryAdapter;
import com.bajratechnologies.nagariknews.widget.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    @Bind(R.id.content_not_found_textview)
    TextView contentNotFoundTextView;

    ControllableAppBarLayout appBarLayout;

    ArrayList<Multimedias> multimediaList = new ArrayList<>();
    GalleryAdapter galleryAdapter;

    private Response.Listener<String> serverResponse;
    private Response.ErrorListener errorListener;
    private ProgressDialog dialog;
    private int TYPE;
    private String gallery;
    private final String PHOTO = "photo";
    private final String VIDEO = "video";
    private final String CARTOON = "cartoon";

    private SqliteDatabase db;

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

        db = new SqliteDatabase(getActivity());
        db.open();

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
                gallery = VIDEO;
                if (BasicUtilMethods.isNetworkOnline(getActivity())) {
                    fetchYoutubeChannelData(ServerConfig.NAGARIK_VIDEO_CHANNEL_ID, 45);
                } else {
                    try {
                        parseResponse(db.getLocalGalleryResponse(BaseThemeActivity.CURRENT_MEDIA, gallery));
                    } catch (CursorIndexOutOfBoundsException exception) {
                        exception.printStackTrace();
                    }
                    toggleContentNotFoundLayout();
                    MySnackbar.showSnackBar(getActivity(), galleryRecyclerView, BaseThemeActivity.NO_NETWORK).show();
                }

            } else {
                if (TYPE == StaticStorage.PHOTOS) {
                    gallery = PHOTO;
                } else if (TYPE == StaticStorage.CARTOONS) {
                    gallery = CARTOON;
                }
                if (BasicUtilMethods.isNetworkOnline(getActivity())) {
                    fetchGalleryFromOwnServer();
                } else {
                    try {
                        parseResponse(db.getLocalGalleryResponse(BaseThemeActivity.CURRENT_MEDIA, gallery));
                    } catch (CursorIndexOutOfBoundsException exception) {
                        exception.printStackTrace();
                    }
                    toggleContentNotFoundLayout();
                    MySnackbar.showSnackBar(getActivity(), galleryRecyclerView, BaseThemeActivity.NO_NETWORK).show();
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

    private void fetchGalleryFromOwnServer() {
        dialog.show();
        handleServerResponse();
        WebService.getServerData(ServerConfig.getGalleryUrl(Dashboard.baseUrl, gallery), serverResponse, errorListener);
    }

    private void loadAdapter(ArrayList<Multimedias> multimediaList) {

        toggleContentNotFoundLayout();


        galleryAdapter = new GalleryAdapter(multimediaList, TYPE);

        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));
    }

    private void toggleContentNotFoundLayout() {
        /**
         * toggle the {@value contentNotFoundLayout}
         * if the {@value multimediaList} is empty make it visible else,
         * make it invisible
         */

        if (multimediaList.size() > 0) {
            contentNotFoundLayout.setVisibility(View.GONE);

        } else {
            String string = "";
            switch (gallery) {
                case VIDEO:
                    string = BaseThemeActivity.EMPTY_YOUTUBE_VIDEOS;
                    break;
                case PHOTO:
                    string = BaseThemeActivity.EMPTY_PHOTOS;
                    break;
                case CARTOON:
                    string = BaseThemeActivity.EMPTY_CARTOONS;
                    break;
            }
            contentNotFoundTextView.setText(string);
            contentNotFoundLayout.setVisibility(View.VISIBLE);
        }
    }

    private void fetchYoutubeChannelData(String channelId, int count) {
        dialog.show();
        handleServerResponse();
        String url = ServerConfig.getYoutubeChannelLinkUrl(channelId, count);
        Log.i(TAG,"YoutubeURL:"+url);
        WebService.getServerData(url, serverResponse, errorListener);

    }

    private void handleServerResponse() {

        serverResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.i(TAG, response);

                //delete response from the local cache and re-add fresh response
                db.deleteLocalGallery(BaseThemeActivity.CURRENT_MEDIA, gallery);
                db.saveGallery(BaseThemeActivity.CURRENT_MEDIA, gallery, response);

                parseResponse(response);
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing()) dialog.dismiss();
                toggleContentNotFoundLayout();
            }
        };
    }

    private void parseResponse(String response) {
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
                    publishDagte = BasicUtilMethods.getTimeAgo(publishDagte);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        if (TYPE == StaticStorage.VIDEOS) {

            Intent playerIntent = new Intent(getActivity(), YoutubePlayerActivity.class);
            playerIntent.putExtra("position", position);
            playerIntent.putParcelableArrayListExtra(StaticStorage.KEY_VIDEO_BUNDLE, multimediaList);
            startActivity(playerIntent);

/**
 try {
 Log.i(TAG,multimediaList.get(position).getMultimediaPath());
 //                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + multimediaList.get(position).getMultimediaPath()+"&list=UUxxx4M3jP9HcKLHJ0dFLe7g"));
 //                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://fu6r67rmZao&list=UUxxx4M3jP9HcKLHJ0dFLe7g"));
 //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 //                startActivity(intent);


 } catch (ActivityNotFoundException e) {

 // youtube is not installed.Will be opened in other available apps

 Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + multimediaList.get(position).getMultimediaPath()));
 startActivity(i);
 }
 **/
        } else {
            Intent epaperIntent = new Intent(getActivity(), GalleryViewActivity.class);

            epaperIntent.putExtra(StaticStorage.KEY_GALLERY_TYPE, StaticStorage.KEY_PHOTO_CARTOON);
            epaperIntent.putExtra(StaticStorage.KEY_PHOTOS_CARTOON_POSITION, position);
            epaperIntent.putExtra(StaticStorage.FOLDER_TYPE, TYPE);
            epaperIntent.putParcelableArrayListExtra(StaticStorage.KEY_PHOTO_CARTOON, multimediaList);

            startActivity(epaperIntent);
            Log.i("info", "clicked");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StaticStorage.KEY_MULTIMEDIA_SAVED_STATE, multimediaList);
    }
}
