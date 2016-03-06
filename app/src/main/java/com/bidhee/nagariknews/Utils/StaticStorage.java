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

    //
    //files and folder
    //
    public static String FOLDER_ROOT = "Nagarik";
    public static String FOLDER_PHOTO = "Photos";
    public static String FOLDER_CARTOON = "Cartoons";
    public static String FOLDER_VIDEOS = "Videos";


    public static String VIDEO_THUMBNAIL_PREFIX = "http://img.youtube.com/vi/";
    public static String VIDEO_THUMBNAIL_POSTFIX = "/default.jpg";

    public static int PHOTOS = 1;
    public static int CARTOONS = 2;
    public static int VIDEOS = 3;

    public static int E_PAPER_REPUBLICA = 4;
    public static int E_PAPER_NAGARIK = 5;


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

        switch (which) {
            case 0:
                tabs.add(new TabModel("0", "Breaking And Latest News"));
                tabs.add(new TabModel("1", "Politics"));
                tabs.add(new TabModel("2", "Economics"));
                tabs.add(new TabModel("3", "Society"));
                tabs.add(new TabModel("4", "Sports"));
                tabs.add(new TabModel("5", "Health"));
                tabs.add(new TabModel("6", "Art"));
                tabs.add(new TabModel("7", "Technology"));
                break;
            case 1:
                tabs.add(new TabModel("0", "मुख्य तथा ताजा समाचार"));
                tabs.add(new TabModel("1", "राजनीति"));
                tabs.add(new TabModel("2", "आर्थीक्"));
                tabs.add(new TabModel("3", "समाजिक्"));
                tabs.add(new TabModel("4", "खेल्कुद्"));
                tabs.add(new TabModel("5", "स्वास्थ्य"));
                tabs.add(new TabModel("6", "कला"));
                tabs.add(new TabModel("7", "बिज्ञान"));
                break;
        }

        return tabs;
    }

    // /////////////////  argument keys  //////////////////

    public static String NEWS_CATEGORY_ID = "news_category_id";
    public static String NEWS_CATEGORY = "newscategory";
    public static String KEY_CURRENT_TAG = "current_fragment_tag";
    public static String KEY_NEWS_TYPE = "news_type";
    public static String KEY_CURRENT_TITLE = "current_title";
    public static String KEY_NEWS_SAVED_STATE = "news_saved_state";
    public static String KEY_GALLERY_TYPE = "galery_type";
    public static String KEY_VIDEO_BUNDLE = "video_bundle";
    public static String KEY_EPAPER_TYPE = "epaper_type";
    public static String KEY_EPAPER = "epaper";
    public static String KEY_EPAPER_PAGE = "epaper_page";


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
        if (type == PHOTOS) {
            multimediaList.add(new Multimedias("id", "title", "http://nagariknews.com/media/k2/items/cache/xaafbf109d9cc513c903b1a05e07fc919_L.jpg.pagespeed.ic.T8f9vg-kZj.webp"));
            multimediaList.add(new Multimedias("id", "title", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQg8Anx_UrtCibPNGVqVhRVmv0DIIVthNCr9ClHt0XtRE3CSUwE"));
            multimediaList.add(new Multimedias("id", "title", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcQ3W9nImucyhYWc0mh3c9_YNmSwYPJ96IyCAEUylrRyX6RTOr"));
            multimediaList.add(new Multimedias("id", "title", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTYm3vnXWhmTxNrra_jtqvqZBjnFZNxi8PTtkBkTTFQLSHKwX93"));
            multimediaList.add(new Multimedias("id", "title", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQQ31ZjpY-62o-Buk7kIdLHqRft9Bv71tnMI2cPqMhVgKTk3nKNAg"));
            multimediaList.add(new Multimedias("id", "title", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFyveauO6PNwN1wAMdTjkxUvUC3hTK5uaG7wgXoInPZLymWyFx"));
            multimediaList.add(new Multimedias("id", "title", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcR_uvGQvbqhAUfsqpzowejAb-fD_C6swvo3FSDIUFAf6QKa-Vn6"));
            multimediaList.add(new Multimedias("id", "title", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQg8Anx_UrtCibPNGVqVhRVmv0DIIVthNCr9ClHt0XtRE3CSUwE"));
            multimediaList.add(new Multimedias("id", "title", "http://nagariknews.com/media/k2/items/cache/xaafbf109d9cc513c903b1a05e07fc919_L.jpg.pagespeed.ic.T8f9vg-kZj.webp"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs.dreamstime.com/t/cartoon-dump-truck-29199727.jpg"));
        } else if (type == CARTOONS) {

            multimediaList.add(new Multimedias("id", "title", "http://thumbs9.dreamstime.com/t/cartoon-fish-16120643.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs4.dreamstime.com/t/illustration-blue-bird-cartoon-singing-30204548.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs9.dreamstime.com/t/happy-elephant-cartoon-illustration-isolated-white-49398584.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs1.dreamstime.com/t/cute-snake-cartoon-illustration-55471879.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs6.dreamstime.com/t/cartoon-cow-friendly-expression-59284628.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs.dreamstime.com/t/reindeer-cartoon-christmas-set-collection-six-funny-characters-different-positions-expressions-isolated-white-35098489.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs.dreamstime.com/x/group-cartoon-pirates-funny-animals-43401148.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs.dreamstime.com/t/cartoon-forest-landscape-endless-vector-nature-background-computer-games-tree-outdoor-plant-green-natural-environment-58460655.jpg"));
            multimediaList.add(new Multimedias("id", "title", "http://thumbs.dreamstime.com/t/cartoon-dump-truck-29199727.jpg"));

        } else if (type == VIDEOS) {
            multimediaList.add(new Multimedias("id", "Video shows terrified tourists as the temple collapses - BBC News", "Yyhh98NDLNs"));
            multimediaList.add(new Multimedias("id", "New Nepali Movie PARDESHI Song Kura Khatti Ho || Official Full Video HD", "77LB0TP57JA"));
            multimediaList.add(new Multimedias("id", "U & Me - Samrakshan Devkota Ft. Girish | New Nepali R&B Pop Song 2015", "yPKGByxyq-g"));
            multimediaList.add(new Multimedias("id", "Sustari - Rozan Adhikari Ft. Nawaz Ansari | New Nepali Pop Song 2015", "AKeaQfdwduY"));
            multimediaList.add(new Multimedias("id", "Jet Udauchu- Nepali new song 2016 ~ Nicky Karki", "Wbj34qhlJFI"));
            multimediaList.add(new Multimedias("id", "Eruption Warning, GMO Update | S0 News April 28, 2015", "fEnB3ePsOow"));
            multimediaList.add(new Multimedias("id", "Magnetic Pole Quake, Alerts, App Q&A | S0 News Feb.24.2016", "B4TgP0QPzSg"));
            multimediaList.add(new Multimedias("id", "Negative Fox News Article Removal Causes Mickey Kaus To Quit", "e-UpPLv98ro"));
            multimediaList.add(new Multimedias("id", "Why India and Pakistan Hate Each Other", "yReaR1p-PV0"));
            multimediaList.add(new Multimedias("id", "Over 350 cadres of UML, NC and RPP join Maoists", "HGweIuYDpO4&list=PL1C1A7F4C69853BB8&index=2"));
            multimediaList.add(new Multimedias("id", "Maoist Artists, JUne 28", "DDVgVopOACc&index=3&list=PL1C1A7F4C69853BB8"));
            multimediaList.add(new Multimedias("id", "mahila sanchar samuha", "9SEwskS1gm4&list=PL1C1A7F4C69853BB8&index=5"));
            multimediaList.add(new Multimedias("id", "Deuba angry over hooting in joint mass meeting", "EDfCpDQBkgU&list=PL1C1A7F4C69853BB8&index=4"));
            multimediaList.add(new Multimedias("id", "Likhe Jo Khat Tujhe Song - Mohammed Rafi - Kanyadan Hindi", "3dwzth2CJOQ&index=6&list=RDfBI1VKnAZdc"));

        }

        return multimediaList;
    }

    /////////////////////   epaper   ///////////////////////
    public static EpaperBundle getEpaperBundle(int type) {
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(new Page("1", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_01.jpg"));
        pages.add(new Page("2", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_02.jpg"));
        pages.add(new Page("3", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg"));
        pages.add(new Page("4", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_04.jpg"));
        pages.add(new Page("4", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_05.jpg"));
        pages.add(new Page("4", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_06.jpg"));
        pages.add(new Page("4", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_07.jpg"));


        Epaper epaper1 = new Epaper("2016-12-22", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_01.jpg", pages);
        Epaper epaper2 = new Epaper("2016-12-23", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_02.jpg", pages);
        Epaper epaper3 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);
        Epaper epaper4 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);
        Epaper epaper5 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);
        Epaper epaper6 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);
        Epaper epaper7 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);
        Epaper epaper8 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);
        Epaper epaper9 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);
        Epaper epaper10 = new Epaper("2016-12-24", "http://nagarikplus.nagariknews.com/images/flippingbook/2016_feb_29/nagarik/ng_zoom_03.jpg", pages);

        List<Epaper> epapers = new ArrayList<>();
        epapers.add(epaper1);
        epapers.add(epaper2);
        epapers.add(epaper3);
        epapers.add(epaper4);
        epapers.add(epaper5);
        epapers.add(epaper6);
        epapers.add(epaper7);
        epapers.add(epaper8);
        epapers.add(epaper9);
        epapers.add(epaper10);

        EpaperBundle epaperBundle = new EpaperBundle("1", epapers);

        return epaperBundle;


    }


}
