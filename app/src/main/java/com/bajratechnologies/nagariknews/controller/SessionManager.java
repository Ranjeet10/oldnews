package com.bajratechnologies.nagariknews.controller;

/**
 * Created by ronem on 2/7/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by ram mandal on 3/8/15.
 */
public class SessionManager {
    public String TAG = "Sessionmanager";
    SharedPreferences LOGIN_PREFERENCE, SWITCHED_TO_PREFERENCE, FONT_CONTROLLER_PREFERENCE;

    SharedPreferences.Editor loginEditor, switchedToEditor, fontControllerEditor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREFERENCE_NAME = "nagarikPrefs";
    public static final String FIRST_RUN_PREFERENCE_NAME = "firstRunPrefs";
    public static final String SWITCHED_TO_PREFERENCE_NAME = "switchedToPreference";
    public static final String FONT_CONTROLLER_PREFERENCE_Name = "fontControllerPreference";


    public static final String isFirstRun = "isFirstRun";

    private static final String IS_LOGIN = "isLoggedIn";
    private static final String KEY_LOGIN_TYPE = "loginType";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_TOKEN = "userToken";

    //gcm keys
    public static final String KEY_SENT_TOKEN_TO_SERVER = "sentToken";
    public static final String REGISTRATION_ID_GCM="registrationId";
    public static final String PROPERTY_APP_VERSION = "appVersion";

    //notification keys
    public static final String NOTIFICATION_TITLE="notificationTitle";
    public static final String NOTIFICATION_DESCRIPTION="notificationDescription";


    public static final String KEY_NEWS_SWITCHED_TO = "newsSwitchedTo";

    public static final String KEY_NEWS_FONT_SIZE = "fontSize";


    public SessionManager(Context context) {
        this.context = context;
        LOGIN_PREFERENCE = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        SWITCHED_TO_PREFERENCE = context.getSharedPreferences(SWITCHED_TO_PREFERENCE_NAME, PRIVATE_MODE);
        FONT_CONTROLLER_PREFERENCE = context.getSharedPreferences(FONT_CONTROLLER_PREFERENCE_Name, PRIVATE_MODE);

        //initializing the editors for update operation
        loginEditor = LOGIN_PREFERENCE.edit();
        switchedToEditor = SWITCHED_TO_PREFERENCE.edit();
        fontControllerEditor = FONT_CONTROLLER_PREFERENCE.edit();
    }

    public void clearEditorData() {
        loginEditor.clear().commit();
        switchedToEditor.clear().commit();
        switchedToEditor.clear().commit();
    }

    //creating new session for the  user
    public void createLoginSession(int loginType, String user_name, String user_email, String user_image, String token) {
        loginEditor.putBoolean(IS_LOGIN, true);
        loginEditor.putInt(KEY_LOGIN_TYPE, loginType);
        loginEditor.putString(KEY_USER_NAME, user_name);
        loginEditor.putString(KEY_USER_EMAIL, user_email);
        loginEditor.putString(KEY_USER_IMAGE, user_image);
        loginEditor.putString(KEY_USER_TOKEN, token);
        loginEditor.commit();
    }


    public boolean isLoggedIn() {
        return LOGIN_PREFERENCE.getBoolean(IS_LOGIN, false);
    }

    public int getLoginType() {
        return LOGIN_PREFERENCE.getInt(KEY_LOGIN_TYPE, 0);
    }

    public String getToken() {
        return LOGIN_PREFERENCE.getString(KEY_USER_TOKEN, "");
    }

    public void clearSession() {
        loginEditor.putBoolean(IS_LOGIN, false);
        loginEditor.putInt(KEY_LOGIN_TYPE, 0);
        loginEditor.putString(KEY_USER_NAME, "");
        loginEditor.putString(KEY_USER_EMAIL, "");
        loginEditor.putString(KEY_USER_IMAGE, "");
        loginEditor.putString(KEY_USER_TOKEN, "");
        loginEditor.commit();
    }


    public HashMap<String, String> getLoginDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USER_NAME, LOGIN_PREFERENCE.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_EMAIL, LOGIN_PREFERENCE.getString(KEY_USER_EMAIL, null));
        user.put(KEY_USER_IMAGE, LOGIN_PREFERENCE.getString(KEY_USER_IMAGE, null));

        if (!TextUtils.isEmpty(user.get(KEY_USER_NAME)) || !TextUtils.isEmpty(user.get(KEY_USER_EMAIL)) || !TextUtils.isEmpty(user.get(KEY_USER_IMAGE))) {
            return user;
        }
        return null;
    }

    //first run app ?
    public Boolean isFirstRun(Context context) {
        return context.getSharedPreferences(FIRST_RUN_PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(isFirstRun, true);
    }

    //news switched to methods
    public void switchNewsTo(int switchedValue) {
        switchedToEditor.putInt(KEY_NEWS_SWITCHED_TO, switchedValue);
        switchedToEditor.commit();
    }

    public int getSwitchedNewsValue() {
        return SWITCHED_TO_PREFERENCE.getInt(KEY_NEWS_SWITCHED_TO, 0);
    }

    //font controller methods
    public void setTheFontSize(int size) {
        fontControllerEditor.putInt(KEY_NEWS_FONT_SIZE, size);
        fontControllerEditor.commit();

    }

    //get the current font size
    public int getCurrentFontSize() {
        int currentFontSize = FONT_CONTROLLER_PREFERENCE.getInt(KEY_NEWS_FONT_SIZE, 1);
        return currentFontSize;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     *
     * @return
     * true if app on device was registered for gcm
     * false if app on device was not registered
     */
    public static boolean isRegisterd(Context context) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(SessionManager.KEY_SENT_TOKEN_TO_SERVER, false);
    }

}