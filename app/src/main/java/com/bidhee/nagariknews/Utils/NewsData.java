package com.bidhee.nagariknews.Utils;

import android.content.Context;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.BreakingAndLatestNews;
import com.bidhee.nagariknews.model.BreakingAndLatestNewsListModel;
import com.bidhee.nagariknews.model.ExtraModel;
import com.bidhee.nagariknews.model.NewsObj;

import java.util.ArrayList;

/**
 * Created by ronem on 2/14/16.
 */
public class NewsData {
    private static String img = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnqivhS7umB9iog3r5wL5k5pNZPF_jOtvEIe6dii7csdFmmPwN";

    public static ArrayList<NewsObj> getNewsRepublica(Context context) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsObjs.add(new NewsObj("12",
                    "",
                    img,
                    context.getResources().getString(R.string.news_title_republica),
                    "Andy Rubin",
                    "Jan 20 2016",
                    context.getResources().getString(R.string.news_description_republica)));
        }
        return newsObjs;

    }

    public static ArrayList<NewsObj> getNewsNagarik(Context context, String categoryName) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsObjs.add(new NewsObj("12",
                    "",
                    img,
                    categoryName + " " + context.getResources().getString(R.string.news_title_nagarik),
                    "कल्पना पौडेल",
                    "आइतबार २ फागुन, २०७२",
                    context.getResources().getString(R.string.news_description_nagarik)));
        }
        return newsObjs;

    }


    public static ArrayList<BreakingAndLatestNews> loadBreakingLatestNews(Context context, String categoryName) {
//        public NewsObj(String id, String newsCategory, String img, String title, String reportedBy, String date, String desc)


        //news list one
        ArrayList<NewsObj> listModels1 = new ArrayList<>();
        listModels1.add(new NewsObj("", "", img,
                "p1c1" + categoryName + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));
        listModels1.add(new NewsObj("", "", img,
                "p1c2" + categoryName + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));
        listModels1.add(new NewsObj("", "", img,
                "p1c3" + categoryName + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));


        //news list two
        ArrayList<NewsObj> listModels2 = new ArrayList<>();
        listModels2.add(new NewsObj("", "", img,
                "p2c1" + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c2" + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c3" + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c4" + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c5" + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                context.getResources().getString(R.string.news_description_republica)
        ));


        ArrayList<BreakingAndLatestNews> breakingAndLatestNewses = new ArrayList<>();
        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.breaking_news), listModels1));
        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.latest_news), listModels2));

        return breakingAndLatestNewses;
    }

    public static ArrayList<BreakingAndLatestNews> loadMukhyaTathaTajaSamaarchar(Context context, String categoryName) {
//        public NewsObj(String id, String newsCategory, String img, String title, String reportedBy, String date, String desc)


        //news list one
        ArrayList<NewsObj> listModels1 = new ArrayList<>();
        listModels1.add(new NewsObj("", "", img,
                "p1c1" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));
        listModels1.add(new NewsObj("", "", img,
                "p1c2" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));
        listModels1.add(new NewsObj("", "", img,
                "p1c3" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));


        //news list two
        ArrayList<NewsObj> listModels2 = new ArrayList<>();
        listModels2.add(new NewsObj("", "", img,
                "p2c1" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c2" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c3" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c4" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));
        listModels2.add(new NewsObj("", "", img,
                "p2c5" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                context.getResources().getString(R.string.news_description_nagarik)
        ));


        ArrayList<BreakingAndLatestNews> breakingAndLatestNewses = new ArrayList<>();
        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.mukhya_samachar), listModels1));
        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.taja_samachar), listModels2));

        return breakingAndLatestNewses;
    }


}
