package com.bidhee.nagariknews.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bidhee.nagariknews.BuildConfig;
import com.bidhee.nagariknews.R;

/**
 * Created by ronem on 5/8/16.
 */
public abstract class BaseThemeActivity extends AppCompatActivity {
    public static SessionManager sessionManager;
    public static String baseUrl = "";
    private int currentTheme;
    public static int ALERT_BUTTON_THEME_STYLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * setting the app theme according to the specific news type selected
         */
        sessionManager = new SessionManager(this);
        switch (sessionManager.getSwitchedNewsValue()) {
            case 1:
                baseUrl = BuildConfig.BASE_URL_REPUBLICA;
                currentTheme = R.style.RepublicaTheme;
                ALERT_BUTTON_THEME_STYLE = R.drawable.alert_button_republica;
                break;
            case 2:
                baseUrl = BuildConfig.BASE_URL_NAGARIK;
                currentTheme = R.style.NagarikTheme;
                ALERT_BUTTON_THEME_STYLE = R.drawable.alert_button_nagarik;
                break;
            case 3:
                baseUrl = BuildConfig.BASE_URL_SUKRABAR;
                currentTheme = R.style.SukrabarTheme;
                ALERT_BUTTON_THEME_STYLE = R.drawable.alert_button_sukrabar;
                break;

        }
        setTheme(currentTheme);
    }

}
