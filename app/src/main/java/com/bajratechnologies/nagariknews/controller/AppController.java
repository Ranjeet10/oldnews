package com.bajratechnologies.nagariknews.controller;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.bajratechnologies.nagariknews.R;
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

    private String TAG = getClass().getSimpleName();

    private RequestQueue requestQueue;
    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Volley initialization
         */

        instance = this;


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
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_consumer_api_key), getResources().getString(R.string.twitter_consumer_secrete_key));
        Fabric.with(this, new Twitter(authConfig));

    }

    /**
     *
     * @return instance of the {@link AppController}
     */
    public static synchronized AppController getInstance() {
        return instance;
    }

    /**
     *
     * @return requestQueue
     * initialize only once throughout the whole application
     *
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }


    /**
     * Method to add the {@link Volley requests} to the #requestQueue
     * @param req
     * @param <T>
     *
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        int socketTimeOut = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        getRequestQueue().add(req);
    }


}
