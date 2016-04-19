package com.bidhee.nagariknews.controller;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


/**
 * Created by ronem on 2/4/16.
 */
public class AppController extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "C6WHEGzaCeOWYryxelmJ7hYH0";
    private static final String TWITTER_SECRET = "0nHYouAd0NypXEE036wQ50ZCoMNYsOhIgR5XCPJs51risc7Orw ";


    @Override
    public void onCreate() {
        super.onCreate();


        /**
         * Set Picasso SingletonInstance
         * for disk cache
         */
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        /**
         * Initialize Facebook sdk and set the AppEventsLogger
         */
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        /**
         * Initialize Twitter sdk with consumer api key and consumer secrete key
         */
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

    }


}
