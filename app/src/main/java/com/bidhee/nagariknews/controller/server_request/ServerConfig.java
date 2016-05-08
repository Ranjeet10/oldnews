package com.bidhee.nagariknews.controller.server_request;

import com.bidhee.nagariknews.BuildConfig;

/**
 * Created by ronem on 4/20/16.
 */
public class ServerConfig {

    public static String NAGARIK_VIDEO_CHANNEL_ID = "UCxxx4M3jP9HcKLHJ0dFLe7g&maxResults";

    public static String getCategoryListUrl(int newsType) {
        String[] bm = getBaseUrlAndMedia(newsType);
        String url = bm[0] + "/api/auth/categories?media=" + bm[1];
        return url;
    }

    public static String getCategoryListSaveurl(int newsType) {
        String[] bm = getBaseUrlAndMedia(newsType);
        String url = bm[0] + "/api/auth/change-categories?media=" + bm[1];
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

    public static String getMeroRuchiUrl(int newsType) {
        String[] bm = getBaseUrlAndMedia(newsType);
        String url = bm[0] + "/api/auth/interested-news?media=" + bm[1];
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

    private static String[] getBaseUrlAndMedia(int newsType) {
        String[] bm = new String[2];
        switch (newsType) {
            case 0:
            case 1:
                bm[0] = BuildConfig.BASE_URL_REPUBLICA;
                bm[1] = "myrepublica";
                break;
            case 2:
                bm[0] = BuildConfig.BASE_URL_NAGARIK;
                bm[1] = "nagarik";
                break;
            case 3:
                bm[0] = BuildConfig.BASE_URL_SUKRABAR;
                bm[1] = "sukrabar";
                break;
        }
        return bm;
    }

}
