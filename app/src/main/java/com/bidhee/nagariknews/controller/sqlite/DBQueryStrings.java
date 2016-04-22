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
            DBConstant.NEWS_REPORTED_BY + " text" +
            close_brac;
}
