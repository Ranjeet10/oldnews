package com.bidhee.nagariknews.Utils;

import android.content.Context;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.BreakingAndLatestNews;
import com.bidhee.nagariknews.model.NewsObj;

import java.util.ArrayList;

/**
 * Created by ronem on 2/14/16.
 */
public class NewsData {
    //    private static String img = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnqivhS7umB9iog3r5wL5k5pNZPF_jOtvEIe6dii7csdFmmPwN";
    private static String[] imgArray = new String[]{
            "http://nagariknews.com/images/2016/Holi_Ritesh/940x652xBirjung_Holi_1.JPG.pagespeed.ic.7t-il8S0AV.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnqivhS7umB9iog3r5wL5k5pNZPF_jOtvEIe6dii7csdFmmPwN",
            "ff"
    };

    public static ArrayList<NewsObj> getNewsRepublica(Context context, int newsType, String categoryId, String categoryName) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NewsObj newsObj = new NewsObj(String.valueOf(newsType),
                    categoryId,
                    newsType + categoryId + i,
                    categoryName,
                    (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                    categoryName + " " + context.getResources().getString(R.string.news_title_republica),
                    "Andy Rubin",
                    "Jan 20 2016",
                    context.getResources().getString(R.string.news_description_republica));
            newsObjs.add(newsObj);

        }
        return newsObjs;

    }

    public static ArrayList<NewsObj> getSukrabar(Context context, int newsType, String categoryId, String categoryName) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NewsObj newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                    newsType + categoryId + i,
                    categoryName,
                    (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                    categoryName + " " + context.getResources().getString(R.string.news_title_republica),
                    "Andy Rubin",
                    "Jan 20 2016",
                    context.getResources().getString(R.string.news_description_republica));
            newsObjs.add(newsObj);

        }
        return newsObjs;

    }

    public static ArrayList<NewsObj> getNewsNagarik(Context context, int newsType, String categoryId, String categoryName) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsObjs.add(new NewsObj(String.valueOf(newsType), categoryId,
                    newsType + categoryId + i,
                    "",
                    (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                    categoryName + " " + context.getResources().getString(R.string.news_title_nagarik),
                    "कल्पना पौडेल",
                    "आइतबार २ फागुन, २०७२",
                    context.getResources().getString(R.string.news_description_nagarik)));
        }
        return newsObjs;

    }

//
//    public static ArrayList<BreakingAndLatestNews> loadBreakingLatestNews(Context context, String categoryName) {
////        public NewsObj(String id, String newsCategory, String img, String title, String reportedBy, String date, String desc)
//
//
//        //news list one
//        ArrayList<NewsObj> listModels1 = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            listModels1.add(new NewsObj("", "", context.getResources().getString(R.string.breaking_news), (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
//                    "p1c1" + categoryName + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
//                    context.getResources().getString(R.string.news_description_republica)
//            ));
//        }
//
//
//        //news list two
//        ArrayList<NewsObj> listModels2 = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            listModels2.add(new NewsObj("", "", context.getResources().getString(R.string.latest_news), (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
//                    "p2c1" + context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
//                    context.getResources().getString(R.string.news_description_republica)
//            ));
//        }
//
//
//        ArrayList<BreakingAndLatestNews> breakingAndLatestNewses = new ArrayList<>();
//        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.breaking_news), listModels1));
//        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.latest_news), listModels2));
//
//        return breakingAndLatestNewses;
//    }
//
//    public static ArrayList<BreakingAndLatestNews> loadMukhyaTathaTajaSamaarchar(Context context, String categoryName) {
////        public NewsObj(String id, String newsCategory, String img, String title, String reportedBy, String date, String desc)
//
//
//        //news list one
//        ArrayList<NewsObj> listModels1 = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            listModels1.add(new NewsObj("", "", "", (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
//                    "p1c1" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
//                    context.getResources().getString(R.string.news_description_nagarik)
//            ));
//        }
//
//
//        //news list two
//        ArrayList<NewsObj> listModels2 = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            listModels2.add(new NewsObj("", "", "", (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
//                    "p2c1" + categoryName + context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
//                    context.getResources().getString(R.string.news_description_nagarik)
//            ));
//        }
//
//
//        ArrayList<BreakingAndLatestNews> breakingAndLatestNewses = new ArrayList<>();
//        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.mukhya_samachar), listModels1));
//        breakingAndLatestNewses.add(new BreakingAndLatestNews(context.getResources().getString(R.string.taja_samachar), listModels2));
//
//        return breakingAndLatestNewses;
//    }


    //testing
    public static ArrayList<NewsObj> loadBreakingLatestNewsTesting(Context context, int newsType, String categoryId) {
//        public NewsObj(String id, String newsCategory, String img, String title, String reportedBy, String date, String desc)

        ArrayList<NewsObj> listModels1 = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            NewsObj newsObj = null;

            switch (newsType) {
                case 1:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 1 + i, context.getResources().getString(R.string.breaking_news), (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica));
                    break;
                case 2:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 1 + i, context.getResources().getString(R.string.mukhya_samachar), (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                            context.getResources().getString(R.string.news_description_nagarik));

                    break;
                case 3:

                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 1 + i, context.getResources().getString(R.string.breaking_news), (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica));
                    break;
            }

            if (i == 0) {
                newsObj.setIsTOShow(true);
            } else {
                newsObj.setIsTOShow(false);
            }
            listModels1.add(newsObj);
        }

        for (int i = 0; i < 5; i++) {
            NewsObj newsObj = null;

            switch (newsType) {
                case 1:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 2 + i, context.getResources().getString(R.string.latest_news), (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica));
                    break;
                case 2:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 2 + i, context.getResources().getString(R.string.taja_samachar), (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल", "आइतबार २ फागुन, २०७२",
                            context.getResources().getString(R.string.news_description_nagarik));
                    break;
                case 3:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 2 + i, context.getResources().getString(R.string.latest_news), (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin", "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica));
                    break;
            }
            listModels1.add(newsObj);

            if (i == 0) {
                newsObj.setIsTOShow(true);
            } else {
                newsObj.setIsTOShow(false);
            }
        }


        return listModels1;
    }


}
