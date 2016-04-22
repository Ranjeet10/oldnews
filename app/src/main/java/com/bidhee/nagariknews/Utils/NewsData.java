package com.bidhee.nagariknews.Utils;

import android.content.Context;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.NewsObj;

import java.util.ArrayList;

/**
 * Created by ronem on 2/14/16.
 */
public class NewsData {
    private static String newsUrl = "http://nagariknews.com/main-story/story/65167.html";
    //    private static String img = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnqivhS7umB9iog3r5wL5k5pNZPF_jOtvEIe6dii7csdFmmPwN";
    private static String[] imgArray = new String[]{
            "http://nagariknews.com/images/2016/Holi_Ritesh/940x652xBirjung_Holi_1.JPG.pagespeed.ic.7t-il8S0AV.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnqivhS7umB9iog3r5wL5k5pNZPF_jOtvEIe6dii7csdFmmPwN",
            "ff"
    };

    public static synchronized ArrayList<NewsObj> getNewsRepublica(Context context, int newsType, String categoryId, String categoryName) {

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
                    "intro text",
                    context.getResources().getString(R.string.news_description_republica), newsUrl);
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
                    "intro text",
                    context.getResources().getString(R.string.news_description_republica), newsUrl);
            newsObjs.add(newsObj);

        }
        return newsObjs;

    }

    public static synchronized ArrayList<NewsObj> getNewsNagarik(Context context, int newsType, String categoryId, String categoryName) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsObjs.add(new NewsObj(String.valueOf(newsType), categoryId,
                    newsType + categoryId + i,
                    "",
                    (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                    categoryName + " " + context.getResources().getString(R.string.news_title_nagarik),
                    "कल्पना पौडेल",
                    "आइतबार २ फागुन, २०७२",
                    "आइतबार २ फागुन, २०७२",
                    context.getResources().getString(R.string.news_description_nagarik), newsUrl));
        }
        return newsObjs;

    }


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
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin",
                            "Thu, 11-11-2015",
                            "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica), newsUrl);
                    break;
                case 2:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 1 + i, context.getResources().getString(R.string.mukhya_samachar), (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल",
                            "आइतबार २ फागुन, २०७२",
                            "आइतबार २ फागुन, २०७२",
                            context.getResources().getString(R.string.news_description_nagarik), newsUrl);

                    break;
                case 3:

                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 1 + i, context.getResources().getString(R.string.breaking_news), (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin",
                            "Thu, 11-11-2015",
                            "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica), newsUrl);
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
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin",
                            "Thu, 11-11-2015",
                            "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica), newsUrl);
                    break;
                case 2:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 2 + i, context.getResources().getString(R.string.taja_samachar), (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_nagarik), "कल्पना पौडेल",
                            "आइतबार २ फागुन, २०७२",
                            "आइतबार २ फागुन, २०७२",
                            context.getResources().getString(R.string.news_description_nagarik), newsUrl);
                    break;
                case 3:
                    newsObj = new NewsObj(String.valueOf(newsType), categoryId,
                            newsType + categoryId + 2 + i, context.getResources().getString(R.string.latest_news), (i % 2 == 0) ? imgArray[0] : (i == 3) ? imgArray[2] : imgArray[1],
                            context.getResources().getString(R.string.news_title_republica), "Andy Rubin",
                            "Thu, 11-11-2015",
                            "Thu, 11-11-2015",
                            context.getResources().getString(R.string.news_description_republica), newsUrl);
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

    public static void getTrendingNews(Context context, int newsType, String categoryId, String categoryName) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsObjs.add(new NewsObj(String.valueOf(newsType), categoryId,
                    newsType + categoryId + i,
                    "",
                    (i % 2 == 0) ? imgArray[0] : (i == 3 || i == 5) ? imgArray[2] : imgArray[1],
                    categoryName + " " + context.getResources().getString(R.string.news_title_nagarik),
                    "कल्पना पौडेल",
                    "आइतबार २ फागुन, २०७२",
                    "आइतबार २ फागुन, २०७२",
                    context.getResources().getString(R.string.news_description_nagarik), newsUrl));
        }

//        EventBus.post(newsObjs);

    }

}
