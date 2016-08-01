package com.bajratechnologies.nagariknews.views.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.controller.server_request.ServerConfig;
import com.bajratechnologies.nagariknews.controller.server_request.WebService;
import com.bajratechnologies.nagariknews.model.Multimedias;
import com.bajratechnologies.nagariknews.widget.GalleryAdapter;
import com.bajratechnologies.nagariknews.widget.RecyclerItemClickListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 2/26/16.
 */
public class YoutubePlayerActivity extends YouTubeBaseActivity implements RecyclerItemClickListener.OnItemClickListener {
    YouTubePlayer.OnInitializedListener listener;

    @Bind(R.id.view)
    YouTubePlayerView playerView;
    private YouTubePlayer youtubePlayerStock;

    @Bind(R.id.video_title_text_view)
    TextView videoTitleTextView;
    @Bind(R.id.no_of_view_text_view)
    TextView noOfViewTextView;
    @Bind(R.id.no_of_like_text_view)
    TextView noOfLikeTextView;
    @Bind(R.id.no_of_dislike_text_view)
    TextView noOfDislikeTextView;

    @Bind(R.id.share_video)
    ImageView shareVideo;
    @Bind(R.id.video_recycler_view)
    RecyclerView videoRecyclerView;


    private ArrayList<Multimedias> multimedias;
    private GalleryAdapter galleryAdapter;
    private int position;
    Response.Listener<String> response;
    Response.ErrorListener errorListener;
    private String videoId;
    private String videoTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_player_activity);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(BaseThemeActivity.COLOR_PRIMARY_DARK);
        }

        multimedias = getIntent().getExtras().getParcelableArrayList(StaticStorage.KEY_VIDEO_BUNDLE);
        position = getIntent().getIntExtra("position", 0);

        handleSererResponse();
        videoTitle = multimedias.get(position).getTitle();
        videoId = multimedias.get(position).getMultimediaPath();
        playVideo(videoTitle, videoId);

        /**
         * Related vieos
         */
        loadRelatedPlayList();
    }

    @OnClick(R.id.share_video)
    void onShareVideoClicked() {
        if (!TextUtils.isEmpty(videoId)) {
            String sharevideoLink = "https://www.youtube.com/watch?v=" + videoId;
            BasicUtilMethods.shareLink(this, sharevideoLink);
        }
    }

    private void loadRelatedPlayList() {
        galleryAdapter = new GalleryAdapter(multimedias, StaticStorage.VIDEOS);

        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoRecyclerView.setHasFixedSize(true);
        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclerView.setAdapter(galleryAdapter);
        videoRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(YoutubePlayerActivity.this, 0, this));
    }

    private void playVideo(final String title, final String videoId) {

        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youtubePlayerStock = youTubePlayer;
                videoTitleTextView.setText(title);
                youTubePlayer.loadVideo(videoId);
                WebService.getServerData(ServerConfig.getVideoInfoUrl(videoId,getString(R.string.project_server_api_key)), response, errorListener);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        playerView.initialize(getString(R.string.project_server_api_key), listener);
    }

    private void handleSererResponse() {

        response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    JSONArray items = nodeObject.getJSONArray("items");
                    JSONObject subnode = items.getJSONObject(0);
                    JSONObject stats = subnode.getJSONObject("statistics");
                    String viewCOunt = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(stats.getString("viewCount")));
                    String likeCOunt = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(stats.getString("likeCount")));
                    String dislikeCount = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(stats.getString("dislikeCount")));

                    noOfViewTextView.setText(viewCOunt);
                    noOfLikeTextView.setText(likeCOunt);
                    noOfDislikeTextView.setText(dislikeCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
    }

    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        if (youtubePlayerStock != null) {
            youtubePlayerStock.release();
        }
        videoTitle = multimedias.get(position).getTitle();
        videoId = multimedias.get(position).getMultimediaPath();
        playVideo(videoTitle, videoId);
    }
}
