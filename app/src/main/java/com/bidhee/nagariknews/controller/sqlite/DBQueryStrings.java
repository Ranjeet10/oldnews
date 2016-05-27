package com.bidhee.nagariknews.controller.sqlite;

/**
 * Created by ronem on 4/1/16.
 */
public class DBQueryStrings {
    static String integer = " integer,";
    static String text = " text,";
    static String open_brac = " ( ";
    static String close_brac = " ) ";
    static String create_table = "CREATE TABLE ";
    static String drop_table = "DROP TABLE IF EXISTS ";

    //DELETE string
    public static String DELETE_FROM = "DELETE FROM ";

    /**
     * raw string for creating table
     * {@link DBConstant  #TABLE_NEWS}
     */


    public static final String CREATE_TABLE_NEWS = create_table +
            DBConstant.TABLE_NEWS +
            open_brac +
            DBConstant.NEWS_TYPE + text +
            DBConstant.NEWS_CATEGORY_ID + text +
            DBConstant.NEWS_ID + text +
            DBConstant.NEWS_CATEGORY_NAME + text +
            DBConstant.NEWS_TITLE + text +
            DBConstant.NEWS_INTRO + text +
            DBConstant.NEWS_DESCRIPTION + text +
            DBConstant.NEWS_URL + text +
            DBConstant.NEWS_DATE + text +
            DBConstant.NEWS_IMAGE + text +
            DBConstant.NEWS_REPORTED_BY + text +
            DBConstant.NEWS_TOSHOW + integer +
            DBConstant.NEWS_ISSAVED + " integer" +
            close_brac;

    public static final String CREATE_TABLE_SAVED_NEWS = create_table +
            DBConstant.TABLE_SAVED_NEWS +
            open_brac +
            DBConstant.NEWS_TYPE + text +
            DBConstant.NEWS_CATEGORY_ID + text +
            DBConstant.NEWS_ID + text +
            DBConstant.NEWS_CATEGORY_NAME + text +
            DBConstant.NEWS_TITLE + text +
            DBConstant.NEWS_INTRO + text +
            DBConstant.NEWS_DESCRIPTION + text +
            DBConstant.NEWS_URL + text +
            DBConstant.NEWS_DATE + text +
            DBConstant.NEWS_IMAGE + text +
            DBConstant.NEWS_REPORTED_BY + text +
            DBConstant.NEWS_TOSHOW + integer +
            DBConstant.NEWS_ISSAVED + " integer" +
            close_brac;

    public static final String CREATE_TABLE_GALLERY = create_table +
            DBConstant.TABLE_GALLERY +
            open_brac +
            DBConstant.NEWS_TYPE + text +
            DBConstant.GALLERY_TYPE + text +
            DBConstant.GALLERY_RESPONSE + " text" +
            close_brac;

    public static final String CREATE_TABLE_EPAPER_PAGE = create_table +
            DBConstant.TABLE_EPAPER_PAGE +
            open_brac +
            DBConstant.NEWS_TYPE + text +
            DBConstant.EPAPER_ID + text +
            DBConstant.EPAPER_PAGE_RESPONSE + " text" +
            close_brac;
}
