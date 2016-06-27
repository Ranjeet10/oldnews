package com.bajratechnologies.nagariknews.views.activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.WindowManager;

import com.bajratechnologies.nagariknews.BuildConfig;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.controller.SessionManager;

/**
 * Created by ronem on 5/8/16.
 */
public abstract class BaseThemeActivity extends AppCompatActivity {
    public static String CURRENT_MEDIA;
    public static String NAGARIK = "nagarik";
    public static String PURBELI = "purbeli";
    public static String PASCHIMELI = "paschimeli";
    public static SessionManager sessionManager;
    public static String baseUrl = "";
    private int currentTheme;
    public static String CURRENT_NEWS_TITLE;
    public static int COLOR_PRIMARY_DARK;
    public static int ALERT_BUTTON_THEME_STYLE;

    String meroRuchi = "कृपया तपाइको रुचिअनुसारको समाचार पाउन लग इन गर्नुहोस ।";
    String meroRuchiNotSelected = "कृपया तपाइको रुचिअनुसारको समाचार पाउन बिधा छनोट गर्नुहोस ।";
    String noNews = "माफ गर्नुहोला तपाईंले खोजेको समाचार प्राप्त गर्न सकिएन । ";

    public static String MERO_RUCHI_EMPTY_BECAUSE_OF_NOT_LOGGED_IN;
    public static String MERO_RUCHI_EMPTY_BECAUSE_OF_NO_CATEGORY_SELECTED;

    public static String EMPTY_NEWS;
    public static String EMPTY_YOUTUBE_VIDEOS;
    public static String EMPTY_PHOTOS;
    public static String EMPTY_CARTOONS;
    public static String SELECT_CATEGORY_TITLE;
    public static String BACK_PRESSED_MESSAGE;
    public static String EMPTY_SAVED_NEWS;
    public static String ALERT_LOGIN_TO_SAVE;

    public static String NO_NETWORK;
    public static String PLEASE_WAIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * setting the app theme according to the specific news type selected
         */
        sessionManager = new SessionManager(this);
        switch (sessionManager.getSwitchedNewsValue()) {
            case 1:
                CURRENT_MEDIA = "my-republica";
                CURRENT_NEWS_TITLE = getString(R.string.republica);
                baseUrl = BuildConfig.BASE_URL_REPUBLICA;

                currentTheme = R.style.RepublicaTheme;
                ALERT_BUTTON_THEME_STYLE = R.drawable.alert_button_republica;
                MERO_RUCHI_EMPTY_BECAUSE_OF_NOT_LOGGED_IN = "Sorry you haven't selected any categories yet.\n" +
                        "Please select one to see the interested categories news";
                MERO_RUCHI_EMPTY_BECAUSE_OF_NO_CATEGORY_SELECTED = "Please select Category to get interested news";
                EMPTY_NEWS = "Sorry ! could not find any news ";
                EMPTY_SAVED_NEWS = "Sorry ! you didn't saved any news yet";
                EMPTY_YOUTUBE_VIDEOS = "Sorry ! could not find any videos";
                EMPTY_PHOTOS = "Sorry ! could not find any photos";
                EMPTY_CARTOONS = "Sorry ! could not find any cartoons";
                SELECT_CATEGORY_TITLE = "Please select your interested news categories";
                BACK_PRESSED_MESSAGE = "Press back again to close application";
                MERO_RUCHI_EMPTY_BECAUSE_OF_NOT_LOGGED_IN = "Please select the your interested news category";
                NO_NETWORK = "Please make sure your device has network access";
                ALERT_LOGIN_TO_SAVE = "You need to login first to save this news.";
                PLEASE_WAIT = "please wait your request is implementing";
                break;
            case 2:
                CURRENT_MEDIA = "nagarik";
                CURRENT_NEWS_TITLE = getString(R.string.nagarik);
                baseUrl = BuildConfig.BASE_URL_NAGARIK;
                currentTheme = R.style.NagarikTheme;
                ALERT_BUTTON_THEME_STYLE = R.drawable.alert_button_nagarik;
                MERO_RUCHI_EMPTY_BECAUSE_OF_NOT_LOGGED_IN = meroRuchi;
                MERO_RUCHI_EMPTY_BECAUSE_OF_NO_CATEGORY_SELECTED = meroRuchiNotSelected;
                EMPTY_NEWS = noNews;
                EMPTY_SAVED_NEWS = "माफगर्नुहोला तपाईंले कुनै पनि समाचार सेभ गर्नु भएको छैन !";
                EMPTY_YOUTUBE_VIDEOS = "माफ गर्नुहोला कुनै पनि भिडोज प्राप्त गर्न सकिएन । ";
                EMPTY_PHOTOS = "माफ गर्नुहोला कुनै पनि फोटो  प्राप्त गर्न सकिएन । ";
                EMPTY_CARTOONS = "माफ गर्नुहोला कुनै पनि कार्टून प्राप्त गर्न सकिएन । ";
                SELECT_CATEGORY_TITLE = "कृपया तपाईको रूचिको समाचार विधा छनोट गर्नुहोस ।";
                BACK_PRESSED_MESSAGE = "कृपया एप्स बन्द गर्न ब्याक बटन पुन: थिच्नुहोस ";
                NO_NETWORK = "कृपया तपाइको दिभाइस्मा इन्टरनेट कनेक्ट गर्नुहोस ।";
                ALERT_LOGIN_TO_SAVE = "यो सुबिधा शक्षम गर्न लग-इन गर्नुहोस । ";
                PLEASE_WAIT = "कृपया प्रतीक्षा गर्नुहोस तपाइको अनुरोध कार्यान्वयन हुँदैछ ।";
                break;
            case 3:
                CURRENT_MEDIA = "sukrabar";
                CURRENT_NEWS_TITLE = getString(R.string.sukrabar);
                baseUrl = BuildConfig.BASE_URL_SUKRABAR;
                currentTheme = R.style.SukrabarTheme;
                ALERT_BUTTON_THEME_STYLE = R.drawable.alert_button_sukrabar;
                MERO_RUCHI_EMPTY_BECAUSE_OF_NOT_LOGGED_IN = meroRuchi;
                MERO_RUCHI_EMPTY_BECAUSE_OF_NO_CATEGORY_SELECTED = meroRuchiNotSelected;
                EMPTY_NEWS = noNews;
                EMPTY_SAVED_NEWS = "माफगर्नुहोला तपाईंले कुनै पनि समाचार सेभ गर्नु भएको छैन !";
                EMPTY_PHOTOS = "माफ गर्नुहोला कुनै पनि फोटो  प्राप्त गर्न सकिएन । ";
                EMPTY_CARTOONS = "माफ गर्नुहोला कुनै पनि कार्टून प्राप्त गर्न सकिएन । ";
                EMPTY_CARTOONS = "माफ गर्नुहोला कुनै पनि भिडोज प्राप्त गर्न सकिएन । ";
                SELECT_CATEGORY_TITLE = "कृपया तपाईको रूचिको समाचार विधा छनोट गर्नुहोस ।";
                BACK_PRESSED_MESSAGE = "कृपया एप्स बन्द गर्न ब्याक बटन पुन: थिच्नुहोस ";
                NO_NETWORK = "कृपया तपाइको दिभाइस्मा इन्टरनेट कनेक्ट गर्नुहोस ।";
                ALERT_LOGIN_TO_SAVE = "यो सुबिधा शक्षम गर्न लग-इन गर्नुहोस । ";
                PLEASE_WAIT = "कृपया प्रतीक्षा गर्नुहोस तपाइको अनुरोध कार्यान्वयन हुँदैछ ।";
                break;

        }
        setTheme(currentTheme);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        COLOR_PRIMARY_DARK = typedValue.data;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(COLOR_PRIMARY_DARK);
        }
    }

}
