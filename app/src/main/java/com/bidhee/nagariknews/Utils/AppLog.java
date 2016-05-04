package com.bidhee.nagariknews.Utils;

import android.util.Log;

import com.bidhee.nagariknews.BuildConfig;

/**
 * Created by ronem on 5/4/16.
 */
public class AppLog {

    private static String TAG = BuildConfig.APPLICATION_ID;

    public static int i(Object object, String message) {
        if (BuildConfig.DEBUG) {
            return Log.i(object.getClass().getSimpleName(), message);
        }
        return 0;
    }

    public static int v(Object object, String message) {
        if (BuildConfig.DEBUG) {
            return Log.v(object.getClass().getSimpleName(), message);
        }
        return 0;
    }

    public static int e(Object object, String message) {
        if (BuildConfig.DEBUG) {
            return Log.e(object.getClass().getSimpleName(), message);
        }
        return 0;
    }
}