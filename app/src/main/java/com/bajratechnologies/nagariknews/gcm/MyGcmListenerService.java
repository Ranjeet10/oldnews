package com.bajratechnologies.nagariknews.gcm;

/**
 * Created by ram on 1/28/16.
 **/

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
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

public class MyGcmListenerService extends GcmListenerService {

    int isTOShow = 0;
    String newsType = "";
    String newsCategoryId = "2";
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

        if (BasicUtilMethods.isValidJSON(_message)) {
            try {
                JSONObject jsonObject = new JSONObject(_message);
                newsId = jsonObject.getString("id");
                newsType = jsonObject.getString("media");
                title = jsonObject.getString("title");
                newsUrl = jsonObject.getString("url");
                description = jsonObject.getString("description");

                SessionManager sessionManager = new SessionManager(this);

                if (newsType.equals("republica")) {

                    sessionManager.switchNewsTo(1);

                } else if (newsType.equals("nagarik")) {

                    sessionManager.switchNewsTo(2);

                } else {

                    sessionManager.switchNewsTo(3);

                }


                ArrayList<NewsObj> newsObjs = new ArrayList<>();
                NewsObj newsObj = new NewsObj(newsType, newsCategoryId, newsId, newsCategoryName, img, title, reportedBy, date, introText, description, newsUrl, isTOShow, isSaved);

                Log.i(TAG, "news:" + newsObj.toString());

                newsObjs.add(newsObj);

                sendNotification(newsObjs);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // [END_EXCLUDE]
    }
    // [END receive_message]


    private void sendNotification(ArrayList<NewsObj> newsObjs) {

        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putParcelableArrayListExtra(StaticStorage.KEY_NEWS_LIST, newsObjs);
        intent.putExtra(StaticStorage.KEY_NEWS_POSITION, 0);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(newsObjs.get(0).getTitle())
                .setContentText(newsObjs.get(0).getIntroText())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
