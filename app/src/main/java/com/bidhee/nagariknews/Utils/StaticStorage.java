package com.bidhee.nagariknews.Utils;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.ExtraModel;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.model.TabModel;
import com.bidhee.nagariknews.model.epaper.Epaper;
import com.bidhee.nagariknews.model.epaper.EpaperBundle;
import com.bidhee.nagariknews.model.epaper.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronem on 2/17/16.
 */
public class StaticStorage {

    //upgrading adb from 1.0.31 to 1.0.32
    //go to the ~Android/sdk/platform-tools
    //wget -O - https://skia.googlesource.com/skia/+archive/cd048d18e0b81338c1a04b9749a00444597df394/platform_tools/android/bin/linux.tar.gz | tar -zxvf - adb
    // sudo mv adb /usr/bin/adb
    //

    public static String DEFAULT_IMAGE = "https://lh3.googleusercontent.com/2BGOr1KnAekO9NmaU4VZg2ZLRs_-60aaA7p4ABYlZXnqsLrItMi4uhmA62pGQDx9NwU=s630-fcrop64=1,0723098ffae5f834";
    //getting height of each row of the news item in recyclerview
    public static int ROW_HEIGHT = 0;

    public static String ALERT_TITLE_LOGIN = "Login";
    public static String ALERT_TITLE_LOGOUT = "Logout";
    public static String LOGIN_INFO = "Application says you have to login to get extra features";
    public static String LOGOUT_INFO = "Are you sure you want to log-out as ";
    public static String SOMETHING_WENT_WRONG = "something went wrong";

    //
    //files and folder
    //
    public static String FOLDER_ROOT = "Nagarik";
    public static String FOLDER_PHOTO = "Photos";
    public static String FOLDER_CARTOON = "Cartoons";
    public static String FOLDER_EPAPER = "Epapers";

    public static String FOLDER_TYPE = "folder_type";


    public static String VIDEO_THUMBNAIL_PREFIX = "http://img.youtube.com/vi/";
    public static String VIDEO_THUMBNAIL_POSTFIX = "/default.jpg";

    public static int PHOTOS = 1;
    public static int CARTOONS = 2;
    public static int EPAPER = 3;
    public static int VIDEOS = 4;

    public static int E_PAPER_REPUBLICA = 31;
    public static int E_PAPER_NAGARIK = 32;
    public static int E_PAPER_SUKRABAR = 33;


    /////////////////  tabs in array  /////////////////////////
    public static String[] republicaTab = {
            "Breaking News",
            "Politics",
            "Economics",
            "Society",
            "Sports",
            "Health",
            "Art",
            "Technology"};

    public static String[] nagarikTab = {
            "मुख्य तथा ताजा समाचार",
            "राजनीति",
            "आर्थीक्",
            "समाजिक्",
            "खेल्कुद्",
            "स्वास्थ्य",
            "कल",
            "बिज्ञान"};


    /////////////////  tabs in list  /////////////////////////
    public static ArrayList<TabModel> getTabData(int which) {
        ArrayList<TabModel> tabs = new ArrayList<>();
        //republica
        switch (which) {
            case 0:
                tabs.add(new TabModel("-1", "Latest News"));
                tabs.add(new TabModel("0", "Mero Ruchi"));
                tabs.add(new TabModel("23", "Politics"));
                tabs.add(new TabModel("22", "Economy"));
                tabs.add(new TabModel("24", "Society"));
                tabs.add(new TabModel("26", "Sports"));
                tabs.add(new TabModel("27", "World"));
                tabs.add(new TabModel("81", "Opinion"));
                tabs.add(new TabModel("35", "Life Style"));
                tabs.add(new TabModel("117", "The Week"));
                break;
            //nagarik
            case 1:
                tabs.add(new TabModel("-1", "ताजा समाचार"));
                tabs.add(new TabModel("0", "मेरो रुची"));
                tabs.add(new TabModel("21", "राजनीति"));
                tabs.add(new TabModel("22", "आर्थीक्"));
                tabs.add(new TabModel("24", "समाजिक्"));
                tabs.add(new TabModel("25", "कला"));
                tabs.add(new TabModel("26", "खेल्कुद्"));
                tabs.add(new TabModel("27", "विश्व"));
                tabs.add(new TabModel("28", "प्रवास"));
                tabs.add(new TabModel("33", "प्रविधि"));
                tabs.add(new TabModel("31", "स्वास्थ्य"));
                tabs.add(new TabModel("81", "विचार"));
                tabs.add(new TabModel("82", "अन्तर्वार्ता"));
                tabs.add(new TabModel("37", "ब्लग"));
                break;
            //sukrabar
            case 2:
                tabs.add(new TabModel("-1", "ताजा समाचार"));
                tabs.add(new TabModel("0", "मेरो रुची"));
                tabs.add(new TabModel("1", "कभर स्टोरी"));
                tabs.add(new TabModel("2", "विविध"));
                tabs.add(new TabModel("3", "मव सम्मत"));
                tabs.add(new TabModel("4", "प्रविधि"));
                tabs.add(new TabModel("5", "संगाीत"));
                tabs.add(new TabModel("22", "टिप्स"));
                break;
        }

        return tabs;
    }

    public static int[] NEWS_LOGOS = new int[]{
            R.mipmap.republica,
            R.mipmap.nagarik,
            R.mipmap.sukrabar};

    // /////////////////  argument keys  //////////////////

    public static String NEWS_CATEGORY_ID = "news_category_id";
    public static String NEWS_CATEGORY = "newscategory";
    public static String KEY_CURRENT_TAG = "current_fragment_tag";
    public static String KEY_NEWS_TYPE = "news_type";
    public static String KEY_NEWS_LIST = "news_list";
    public static String KEY_NEWS_POSITION = "news_position";
    public static String KEY_CURRENT_TITLE = "current_title";
    public static String KEY_CURRENT_LOGO = "current_logo";
    public static String KEY_NEWS_SAVED_STATE = "news_saved_state";
    public static String KEY_MULTIMEDIA_SAVED_STATE = "multimedia_saved_state";
    public static String KEY_GALLERY_TYPE = "galery_type";
    public static String KEY_VIDEO_BUNDLE = "video_bundle";
    public static String KEY_EPAPER_TYPE = "epaper_type";
    public static String KEY_EPAPER_PAGE = "epaper_page";
    public static String KEY_IS_FROM_FAV = "favourite";
    public static String KEY_PHOTOS_CARTOON_POSITION = "position";

    public static String KEY_PHOTO_CARTOON = "photos_cartoons";
    public static String KEY_EPAPER = "epaper";

    /////////////////// login type ///////////////////
    public static int LOGIN_TYPE_FORM = 1;
    public static int LOGIN_TYPE_FACEBOOK = 2;
    public static int LOGIN_TYPE_TWITTER = 3;
    public static int LOGIN_TYPE_GOOGLE = 4;


    public static ArrayList<ExtraModel> getExtraList() {

        ArrayList<ExtraModel> list = new ArrayList<>();

        list.add(new ExtraModel("1", R.mipmap.ic_gesture_black_24dp, "Horroscope", "Libra"));
        list.add(new ExtraModel("2", R.mipmap.ic_lightbulb_outline_black_24dp, "Loadsheding", "group 6"));
        list.add(new ExtraModel("3", R.mipmap.ic_invert_colors_black_24dp, "Bullion", "petrol/diesel"));
        list.add(new ExtraModel("4", R.mipmap.ic_euro_symbol_black_24dp, "Exchange Rate", "$1-Rs.104"));
        list.add(new ExtraModel("5", R.mipmap.ic_trending_up_black_24dp, "Stock", ""));
        list.add(new ExtraModel("6", R.mipmap.ic_date_range_black_24dp, "Today's Significane", ""));
        return list;
    }

    public static ArrayList<ExtraModel> getAnyaList() {

        ArrayList<ExtraModel> list = new ArrayList<>();

        list.add(new ExtraModel("1", R.mipmap.ic_gesture_black_24dp, "राशिफल", "तुला राशि"));
        list.add(new ExtraModel("2", R.mipmap.ic_lightbulb_outline_black_24dp, "लोड्सेडिङ्ग्", "समुह ६"));
        list.add(new ExtraModel("3", R.mipmap.ic_invert_colors_black_24dp, "बुलियन", "पेट्रोल डिजेल "));
        list.add(new ExtraModel("4", R.mipmap.ic_euro_symbol_black_24dp, "बिनिमय दर", "$१=रु १०४"));
        list.add(new ExtraModel("5", R.mipmap.ic_trending_up_black_24dp, "स्टक", ""));
        list.add(new ExtraModel("6", R.mipmap.ic_date_range_black_24dp, "आजकोदिन", ""));
        return list;
    }


    //////////////////    gallery methods   /////////////
    public static ArrayList<Multimedias> getGalleryList(int type) {
        ArrayList<Multimedias> multimediaList = new ArrayList<>();
        if (type == CARTOONS) {

            multimediaList.add(new Multimedias("id", "मूहुर्त", "http://nagarik.bidheegroup.com/uploads/media//cartoon/274da0858cfef8a4aea36fceeb569c2f_L.jpg", "", ""));
            multimediaList.add(new Multimedias("id", "मूहुर्त", "http://nagarik.bidheegroup.com/uploads/media//2016/may/1-10/cartoon.jpg", "", ""));

        }

        return multimediaList;
    }


}
