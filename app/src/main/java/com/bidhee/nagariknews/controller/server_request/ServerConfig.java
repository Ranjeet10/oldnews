package com.bidhee.nagariknews.controller.server_request;

import com.bidhee.nagariknews.BuildConfig;

/**
 * Created by ronem on 4/20/16.
 */
public class ServerConfig {


    //    private static String API_DIR = "api/news/";
//    private static String LIST_PRE_PAGE = "list?_format=json&page=";
//    private static String CATEGORY_ID = "&category_id=";
    public static String NAGARIK_VIDEO_CHANNEL_ID = "UCxxx4M3jP9HcKLHJ0dFLe7g&maxResults";
    public static String MYREPUBLICA_VIDEO_CHANNEL_ID = "UCkGUD1LtYhxNyY-XoE2wICQ";

    public static String getCategoryListUrl(int newsType) {
        String baseUrl = "";
        String media = "";
        switch (newsType) {
            case 0:
            case 1:
                media = "myrepublica";
                baseUrl = BuildConfig.BASE_URL_REPUBLICA;
                break;
            case 2:
                media = "nagarik";
                baseUrl = BuildConfig.BASE_URL_NAGARIK;
                break;
            case 3:
                media = "sukrabar";
                baseUrl = BuildConfig.BASE_URL_SUKRABAR;
                break;

        }
        String url = baseUrl + "/api/auth/categories?media=" + media;
        return url;
    }

    public static String getCategoryListSaveurl(int newsType) {
        String media = "";
        String baseUrl = "";
        switch (newsType) {
            case 0:
            case 1:
                media = "myrepublica";
                baseUrl = BuildConfig.BASE_URL_REPUBLICA;

                break;
            case 2:
                media = "nagarik";
                baseUrl = BuildConfig.BASE_URL_NAGARIK;
                break;
            case 3:
                media = "sukrabar";
                baseUrl = BuildConfig.BASE_URL_SUKRABAR;
                break;

        }
        String url = baseUrl + "/api/auth/change-categories?media=" + media;
        return url;
    }

    public static String getNewsTitleUrl(String baseUrl, int pageIndex, String cat_id) {
        String url = baseUrl + "/api/news/list?_format=json&page=" + pageIndex + "&category_id=" + cat_id;
        return url;
    }

    public static String getNewsDetailUrl(String baseUrl, String newsId) {
        String url = baseUrl + "/api/news/" + newsId + "/detail?_format=json";
        return url;
    }

    public static String getLatestBreakingNewsUrl(String baseUrl) {
        String url = baseUrl + "/api/news/latest-breaking?_format=json";
        return url;
    }

    public static String getMeroRuchiUrl(String baseUrl) {
        String url = baseUrl + "/api/auth/interested-news?media=nagarik";

        return url;
    }

    public static String getYoutubeChannelLinkUrl(String channelId, int count) {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=" + channelId + "=" + count + "&key=AIzaSyBvuEzoL3_rdCuOs7wpvVxLlpIa8kVPdcs";
        return url;
    }

    public static String getGalleryUrl(String baseUrl, int galleryType) {

        String url = baseUrl + "/api/news/list/photo?_format=json&page=1";
        return url;
    }


    public static String AUTH_URL = "http://consumers.bidheegroup.com/api/consumer/oauth";
    public static String REGISTER_URL = "http://consumers.bidheegroup.com/api/consumer/register";
    public static String LOGIN_URL = "http://consumers.bidheegroup.com/api/consumer/login";

}
