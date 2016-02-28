package com.bidhee.nagariknews.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.Multimedias;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/26/16.
 */
public class YoutubePlayerActivity extends YouTubeBaseActivity {
    YouTubePlayer.OnInitializedListener listener;

    @Bind(R.id.view)
    YouTubePlayerView playerView;
    @Bind(R.id.video_title_text_view)
    TextView videoTitleTextView;


    private Multimedias multimedias;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_player_activity);
        ButterKnife.bind(this);

        multimedias = getIntent().getExtras().getParcelable(StaticStorage.KEY_VIDEO_BUNDLE);

        videoTitleTextView.setText(multimedias.getTitle());

        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(multimedias.getMultimediaPath());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };


        playerView.initialize(getString(R.string.apikey), listener);


    }
}
