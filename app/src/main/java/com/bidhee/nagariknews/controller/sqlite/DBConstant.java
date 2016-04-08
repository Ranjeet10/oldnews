package com.bidhee.nagariknews.controller.sqlite;

/**
 * CLASS DBConstant
 * <p> contains the variables used for the SQLite Database And Methods</p>
 * Created by ronem on 4/1/16.
 *
 * @version 1.0
 */
public class DBConstant {
    /**
     * Databse name and database version
     */
    public static final String DATABASE_NAME = "nagarikdb";
    public static final int DATABASE_VERSION = 1;

    /**
     * Database Table name
     */
    public static final String TABLE_NEWS = "news";

    /**
     * columns for the table news
     */
    public static final String NEWS_TYPE = "news_type";
    public static final String NEWS_CATEGORY_ID = "news_cat_id";
    public static final String NEWS_ID = "news_id";
    public static final String NEWS_CATEGORY_NAME = "news_category_name";
    public static final String NEWS_TITLE = "news_title";
    public static final String NEWS_DESCRIPTION = "news_description";
    public static final String NEWS_URL = "news_url";
    public static final String NEWS_DATE = "news_date";
    public static final String NEWS_IMAGE = "news_image";
    public static final String NEWS_REPORTED_BY = "news_reported_by";


}
