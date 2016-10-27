package com.bajratechnologies.nagariknews.gcm;

/**
 * Created by ram on 1/28/16.
 **/

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.controller.SessionManager;
import com.bajratechnologies.nagariknews.model.NewsObj;
import com.bajratechnologies.nagariknews.views.activities.NewsDetailActivity;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MyGcmListenerService extends GcmListenerService {

    String newsType = "";
    String newsCategoryId = "";
    String newsId = "";
    String newsCategoryName = "";
    String title = "";
    String introText = "";
    String description = "";
    String newsUrl = "";
    String date = "";
    String img = "";
    String reportedBy = "";
    int isSaved = 0;

    private int SWITCHED_TO = 1;

    private String notificationTitle;

    private static final String TAG = "MyGcmListenerService";


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, "message_received : " + data.toString());

        String _message = data.getString("message");

        Log.i(TAG, "Message:" + _message);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean show = pref.getBoolean(getString(R.string.preference_checkbox_key), false);
//
        if (show) {
            Log.i(TAG, "Was checked");
            showNotification(_message);
        } else {
            Log.i(TAG, "Was not checked");
        }


        // [END_EXCLUDE]
    }

    private void showNotification(String _message) {
        if (BasicUtilMethods.isValidJSON(_message)) {
            try {
                JSONObject jsonObject = new JSONObject(_message);
                newsId = jsonObject.getString("id");
                newsType = jsonObject.getString("media");
                title = jsonObject.getString("title");
                newsUrl = jsonObject.getString("url");
                description = jsonObject.getString("description");
                img = jsonObject.getString("featured_image");
                newsCategoryName = jsonObject.getString("category_name");
                newsCategoryId = jsonObject.getString("category_id");

                SessionManager sessionManager = new SessionManager(this);

                if (newsType.equals("republica")) {
                    notificationTitle = getResources().getString(R.string.republica);
//                    sessionManager.switchNewsTo(1);
//                    Log.i(TAG, "CHANGED::newsType to 1(republica)");

                    //Setting 1 for republica
                    SWITCHED_TO = 1;

                } else if (newsType.equals("nagarik")) {
                    notificationTitle = getResources().getString(R.string.nagarik);
//                    sessionManager.switchNewsTo(2);
//                    Log.i(TAG, "CHANGED::newsType to 2(nagarik)");

                    //Setting 1 for nagarik
                    SWITCHED_TO = 2;

                } else {

//                    sessionManager.switchNewsTo(3);
                    notificationTitle = getResources().getString(R.string.sukrabar);
//                    Log.i(TAG, "CHANGED::newsType to 3(shukrabar)");

                    //Setting 1 for shukrabar
                    SWITCHED_TO = 3;

                }


                NewsObj newsObj = new NewsObj(newsType, newsCategoryId, newsId, newsCategoryName, img, title, reportedBy, date, introText, description, newsUrl, isSaved);

                Log.i(TAG, "news:" + newsObj.toString());

//                Bitmap newsImage = null;
//
//                if (!TextUtils.isEmpty(img)) {
//                    try {
//
//                        InputStream inputStream = (InputStream) new URL(img).getContent();
//                        newsImage = BitmapFactory.decodeStream(inputStream);
//
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

                sendNotification(/*newsImage,*/ newsObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // [END receive_message]


    private void sendNotification(/*Bitmap newsImage, */NewsObj newsObj) {

        int num;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        num = sp.getInt("notification_id", 1);

        num = num + 1;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("notification_id", num).apply();

        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(StaticStorage.KEY_NEWS_LIST, newsObj);
        Log.i(TAG, "PARCELABLE::" + newsObj.toString());

        intent.putExtra(StaticStorage.KEY_NEWS_TYPE, SWITCHED_TO);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, num /*0 Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        Bitmap large = BitmapFactory.decodeResource(getResources(), R.drawable.nagariknews);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(newsImage != null ? newsImage : large)
                .setContentTitle(notificationTitle)
                .setContentText(newsObj.getTitle())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(num /* ID of notification */, notificationBuilder.build());
    }

}
