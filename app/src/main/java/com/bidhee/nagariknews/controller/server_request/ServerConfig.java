package com.bidhee.nagariknews.controller.server_request;

import com.bidhee.nagariknews.BuildConfig;

/**
 * Created by ronem on 4/20/16.
 */
public class ServerConfig {


//    private static String API_DIR = "api/news/";
//    private static String LIST_PRE_PAGE = "list?_format=json&page=";
//    private static String CATEGORY_ID = "&category_id=";

    public static String getNewsTitleUrl(String baseUrl, int pageIndex, String cat_id) {
        String url = baseUrl + "api/news/list?_format=json&page=" + pageIndex + "&category_id=" + cat_id;
        return url;
    }

    public static String getNewsDetailUrl(String baseUrl, String newsId) {
        String url = baseUrl + "api/news/" + newsId + "/detail?_format=json";
        return url;
    }

    public static String getLatestBreakingNewsUrl(String baseUrl) {
        String url = baseUrl + "api/news/latest-breaking?_format=json";
        return url;
    }

    public static String getYoutubeChannelLinkUrl() {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCxxx4M3jP9HcKLHJ0dFLe7g&maxResults=50&key=AIzaSyBvuEzoL3_rdCuOs7wpvVxLlpIa8kVPdcs";
        return url;
    }

    public static String REGISTER_URL = "http://consumers.bidheegroup.com/api/consumer/register";
}
