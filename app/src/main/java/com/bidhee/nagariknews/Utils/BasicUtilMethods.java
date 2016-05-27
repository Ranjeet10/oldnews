package com.bidhee.nagariknews.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.controller.sqlite.SqliteDatabase;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ronem on 2/4/16.
 */
public class BasicUtilMethods {


    public static boolean isNetworkOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    public static String getImageNameFromImagepath(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf("/"));
    }

    public static void saveFileToGalery(Context context, String folderName, String fileName, ImageView imageView) {
        FileOutputStream fops;
        BitmapDrawable bmpDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();

        //folder
        File folder = new File(Environment.getExternalStorageDirectory(), File.separator + folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //file
        File file = new File(folder, fileName + ".PNG");


        if (!file.exists()) {
            try {
                fops = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fops);
                fops.flush();
                fops.close();
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "file already exists", Toast.LENGTH_SHORT).show();
        }

    }

    public static void collapseAppbar(ControllableAppBarLayout appBarLayout, Menu menu) {
        appBarLayout.setEnabled(false);
        appBarLayout.setActivated(false);
        appBarLayout.collapseToolbar(true);
        if (menu != null)
            menu.getItem(0).setVisible(false);
    }

    public static void expandAppbar(ControllableAppBarLayout appBarLayout, Menu menu) {
        appBarLayout.setEnabled(true);
        appBarLayout.expandToolbar(true);
        if (menu != null)
            menu.getItem(0).setVisible(false);
    }


    public static void shareLink(Context context, String link) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
        context.startActivity(Intent
                .createChooser(sharingIntent, "Share using"));
    }

    public static void loadImage(final Context context, final String url, final ImageView galleryThumbnail) {
        try {
            Picasso.with(context)
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(galleryThumbnail, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
//                            //Try again online if cache failed
                            try {
                                Picasso.with(context)
                                        .load(url)
                                        .error(R.drawable.nagariknews)
                                        .into(galleryThumbnail, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso", "Could not fetch image");
                                            }
                                        });
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }
                        }
                    });
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    public static Boolean isValidPassword(String text) {
        if (text.length() > 5)
            return true;
        return false;
    }

    public static Boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    public static String getTimeAgo(String publishDagte) {
        Log.i("publishDate", publishDagte);
        String resultTimeAgo = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date parsedDate = sdf.parse(publishDagte);
            Log.i("parsedDate", parsedDate + "");
            Date systemDate = new Date();

            long vDateMilli = parsedDate.getTime();
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

    public static void logout(Activity context) {
//        clearApplicationData(context);
        clearCache(context);
    }

    private static void clearCache(Activity context) {
        new SessionManager(context).clearEditorData();

        SqliteDatabase db = new SqliteDatabase(context);
        db.open();
        db.deleteAllNews();
//        context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE).edit().clear();

        db.close();

//        context.startActivity(new Intent(context, Dashboard.class));
        context.finish();
    }

    public static void clearApplicationData(final Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                File f = new File(appDir, s);
                if (deleteDir(f)) {
                    AppLog.i("Util", String.format("DELETED::", f.getAbsolutePath()));
                }
            }
        }
        context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE).edit().clear();

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                AppLog.i("Util", "DELETING:: " + aChildren);
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        assert dir != null;
        return dir.delete();
    }

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String registrationId = prefs.getString(SessionManager.REGISTRATION_ID_GCM, "");
        if (registrationId.isEmpty()) {
            Log.i("GCMRegistration", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(SessionManager.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = SessionManager.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("AppVersion", "App version changed.");
            return "";
        }
        return registrationId;
    }

}
