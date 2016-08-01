package com.bajratechnologies.nagariknews.controller.server_request;

import com.bajratechnologies.nagariknews.BuildConfig;

/**
 * Created by ronem on 4/20/16.
 */
public class ServerConfig {

    public static String NAGARIK_VIDEO_CHANNEL_ID = "UCxxx4M3jP9HcKLHJ0dFLe7g&maxResults";

    //i found it
    public static String playlisturl = "https://www.googleapis.com/youtube/v3/search?part=id%2C+snippet&channelId=UCxxx4M3jP9HcKLHJ0dFLe7g&maxResults=50&order=date&type=video%2C+playlist&key=AIzaSyBvuEzoL3_rdCuOs7wpvVxLlpIa8kVPdcs";

    public static String numberOfPlayList = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=UCxxx4M3jP9HcKLHJ0dFLe7g&key=AIzaSyBvuEzoL3_rdCuOs7wpvVxLlpIa8kVPdcs";
    public static String playlistviedeos = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=20&key=AIzaSyBvuEzoL3_rdCuOs7wpvVxLlpIa8kVPdcs&playlistId=PLaRFwT957MFvo17MscoVGTDMc9_J85zWB";

    //with video count
    public static String withvieoCount = "http://gdata.youtube.com/feeds/api/playlists/PLaRFwT957MFvo17MscoVGTDMc9_J85zWB?v=2&alt=jsonc&start-index=0";

    //single video view count

//    https://www.googleapis.com/youtube/v3/videos?part=statistics&id=dc5lTQWorVw&key=AIzaSyBvuEzoL3_rdCuOs7wpvVxLlpIa8kVPdcs

    public static String getCategoryListUrl(int newsType) {
        String[] bm = getBaseUrlAndMedia(newsType);
        String url = bm[0] + "/api/auth/categories";
        return url;
    }

    public static String getCategoryListSaveurl(int newsType) {
        String[] bm = getBaseUrlAndMedia(newsType);
        String url = bm[0] + "/api/auth/change-categories";
        return url;
    }

    public static String getNewsTitleUrl(String baseUrl, int pageIndex, String cat_id) {
        String url = baseUrl + "/api/news/list?_format=json&page=" + pageIndex + "&category_id=" + cat_id;
        return url;
    }

    public static String getSaveNewsTitleUrl(String baseurl) {
        String url = baseurl + "/api/auth/saved-news";
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

    public static String getYoutubeChannelLinkUrl(String channelId, int count, String apikey) {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=" + channelId + "=" + count + "&key=" + apikey;
        return url;
    }

    public static String getVideoInfoUrl(String videoId, String apikey) {
        String url = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=" + videoId + "&key=" + apikey;
        return url;
    }

    public static String getGalleryUrl(String baseUrl, String galleryType) {

        String url = baseUrl + "/api/news/list/" + galleryType + "?_format=json&page=1";
        return url;
    }

    public static String getEpaperListUrl(String media) {
        String url = BuildConfig.EPAPER_BASE_URL + "/api/news/list?_format=json&media=" + media;
        return url;
    }

    public static String getEpaperPages(int id) {
        String url = BuildConfig.EPAPER_BASE_URL + "/api/news/" + id + "/detail?_format=json";
        return url;
    }


//    public static String AUTH_URL = "http://consumers.bidheegroup.com/api/consumer/oauth";
//    public static String REGISTER_URL = "http://consumers.bidheegroup.com/api/consumer/register";
//    public static String LOGIN_URL = "http://consumers.bidheegroup.com/api/consumer/login";
//    public static String SAVE_NEWS_URL = "http://consumers.bidheegroup.com/api/consumer/news/save";

    public static String AUTH_URL = BuildConfig.AUTH_BASE_URL + "/api/consumer/oauth";
    public static String REGISTER_URL = BuildConfig.AUTH_BASE_URL + "/api/consumer/register";
    public static String LOGIN_URL = BuildConfig.AUTH_BASE_URL + "/api/consumer/login";
    public static String SAVE_NEWS_URL = BuildConfig.AUTH_BASE_URL + "/api/consumer/news/save";

    public static String getUnsaveNewsUrl(String id, String media) {
        String url = BuildConfig.AUTH_BASE_URL + "/api/consumer/news/" + id + "/delete?media=" + media;
        return url;
    }

    public static String GCM_RREGISTRATION_URL = "http://consumers.bidheegroup.com/api/consumer/save-device-information";

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
