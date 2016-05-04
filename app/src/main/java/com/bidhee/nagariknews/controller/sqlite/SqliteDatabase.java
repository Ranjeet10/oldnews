package com.bidhee.nagariknews.controller.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bidhee.nagariknews.model.NewsObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronem on 4/1/16.
 */
public class SqliteDatabase {
    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public SqliteDatabase(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }


    /**
     * opening the database
     *
     * @return this
     */
    public SqliteDatabase open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * closing the database
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * {@link #saveNews(NewsObj)}
     * method to save the news with the following parameters
     *
     * @param newsObj
     */
    public void saveNews(NewsObj newsObj) {
        //creating the ContentValue object to hold the single row
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstant.NEWS_TYPE, newsObj.getNewsType());
        contentValues.put(DBConstant.NEWS_CATEGORY_ID, newsObj.getNewsCategoryId());
        contentValues.put(DBConstant.NEWS_ID, newsObj.getNewsId());
        contentValues.put(DBConstant.NEWS_CATEGORY_NAME, newsObj.getNewsCategoryName());
        contentValues.put(DBConstant.NEWS_TITLE, newsObj.getTitle());
        contentValues.put(DBConstant.NEWS_INTRO, newsObj.getIntroText());
        contentValues.put(DBConstant.NEWS_DESCRIPTION, newsObj.getDescription());
        contentValues.put(DBConstant.NEWS_URL, newsObj.getNewsUrl());
        contentValues.put(DBConstant.NEWS_DATE, newsObj.getDate());
        contentValues.put(DBConstant.NEWS_IMAGE, newsObj.getImg());
        contentValues.put(DBConstant.NEWS_REPORTED_BY, newsObj.getReportedBy());

        db.insert(DBConstant.TABLE_NEWS, null, contentValues);
    }

    /**
     * get {@link NewsObj} from the {@link SQLiteDatabase} if the following parameters matches
     *
     * @param newsType
     * @param newsCategoryId
     * @param newsId
     * @return
     */
    public Boolean isNewsPresent(String newsType, String newsCategoryId, String newsId) {
        Cursor cursor = db.query(true, DBConstant.TABLE_NEWS, null,
                DBConstant.NEWS_TYPE + "='" + newsType + "' AND " +
                        DBConstant.NEWS_CATEGORY_ID + "='" + newsCategoryId + "' AND " +
                        DBConstant.NEWS_ID + "='" + newsId + "'"
                , null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0 || cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * get list of all the news matching the newsType
     *
     * @param newsType
     * @return
     */
    public List<NewsObj> getNewsList(String newsType) {
        ArrayList<NewsObj> list = new ArrayList<>();
        Cursor cursor = db.query(true, DBConstant.TABLE_NEWS, null,
                DBConstant.NEWS_TYPE + "='" + newsType + "'", null, null, null, null, null);

        try {
            int rowCOunt = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < rowCOunt; i++) {
                list.add(new NewsObj(cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_TYPE)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_CATEGORY_ID)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_ID)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_CATEGORY_NAME)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_IMAGE)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_TITLE)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_REPORTED_BY)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_DATE)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_INTRO)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_DESCRIPTION)),
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_URL)))
                );
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    public NewsObj getNewsObj(String newsType, String newsCategoryId, String newsId) {
        NewsObj newsObj;
        Cursor cursor = db.query(true, DBConstant.TABLE_NEWS, null,
                DBConstant.NEWS_TYPE + "='" + newsType + "' AND " +
                        DBConstant.NEWS_CATEGORY_ID + "='" + newsCategoryId + "' AND " +
                        DBConstant.NEWS_ID + "='" + newsId + "'"
                , null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            newsObj = new NewsObj(cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_ID)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_CATEGORY_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_REPORTED_BY)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_DATE)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_INTRO)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_URL)));
            return newsObj;
        }
        return null;


    }

    public void deleteRowFromNews(String newsType, String newsCategoryId, String newsId) {
        db.delete(DBConstant.TABLE_NEWS,
                DBConstant.NEWS_TYPE + "=" + newsType + " AND " +
                        DBConstant.NEWS_CATEGORY_ID + "='" + newsCategoryId + "' AND " +
                        DBConstant.NEWS_ID + "='" + newsId + "'", null);
    }

//    public void updateNewsDetail(String newsType, String newsCategoryId, String newsId, String newsDescription) {
//        db.rawQuery("UPDATE "+DBConstant.TABLE_NEWS+" SET "+DBConstant.NEWS_DESCRIPTION)
//    }

    public void deleteAllNews() {
        db.execSQL(DBQueryStrings.DELETE_FROM + DBConstant.TABLE_NEWS);
    }

}
