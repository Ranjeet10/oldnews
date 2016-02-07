package com.bidhee.nagariknews.controller;

/**
 * Created by ronem on 2/7/16.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by ram mandal on 3/8/15.
 */
public class SessionManager {
    public String TAG = "Sessionmanager";
    SharedPreferences LOGIN_PREFERENCE;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREFERENCE_NAME = "nagarikPrefs";
    private static final String IS_LOGIN = "isLoggedIn";

    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_IMAGE = "userImage";

    public SessionManager(Context context) {
        this.context = context;
        LOGIN_PREFERENCE = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = LOGIN_PREFERENCE.edit();
    }

    public void createLoginSession(String user_name, String user_image) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_NAME, user_name);
        editor.putString(KEY_USER_IMAGE, user_image);
        editor.commit();
    }


    public boolean isLoggedIn() {
        return LOGIN_PREFERENCE.getBoolean(IS_LOGIN, false);
    }


    public HashMap<String, String> getLoginDetail() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_NAME, LOGIN_PREFERENCE.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_NAME, LOGIN_PREFERENCE.getString(KEY_USER_IMAGE, null));
        return user;
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}