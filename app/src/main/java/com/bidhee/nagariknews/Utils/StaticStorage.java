package com.bidhee.nagariknews.Utils;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.ExtraModel;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.model.TabModel;

import java.util.ArrayList;

/**
 * Created by ronem on 2/17/16.
 */
public class StaticStorage {

    public static String VIDEO_THUMBNAIL_PREFIX = "http://img.youtube.com/vi/";
    public static String VIDEO_THUMBNAIL_POSTFIX = "/default.jpg";

    public static int PHOTOS = 1;
    public static int CARTOONS = 2;
    public static int VIDEOS = 3;

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
            multimediaList.add(new Multimedias("id", "title", "data:image/jpeg;base64,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"));
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

        } else {
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


}
