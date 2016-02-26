package com.bidhee.nagariknews.Utils;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.ExtraModel;
import com.bidhee.nagariknews.model.TabModel;

import java.util.ArrayList;

/**
 * Created by ronem on 2/17/16.
 */
public class StaticStorage {

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


    public static String NEWS_CATEGORY_ID = "news_category_id";
    public static String NEWS_CATEGORY = "newscategory";
    public static String KEY_CURRENT_TAG = "current_fragment_tag";
    public static String KEY_NEWS_TYPE = "news_type";
    public static String KEY_CURRENT_TITLE = "current_title";

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
}
