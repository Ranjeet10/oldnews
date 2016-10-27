package com.bajratechnologies.nagariknews.controller.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.bajratechnologies.nagariknews.model.NewsObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronem on 4/1/16.
 */
public class SqliteDatabase {

    private String TAG = getClass().getSimpleName();
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
     * @param newsObj ==============================================================================================
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
        contentValues.put(DBConstant.NEWS_TOSHOW, 1);
        contentValues.put(DBConstant.NEWS_ISSAVED, newsObj.getIsSaved());

        db.insert(DBConstant.TABLE_NEWS, null, contentValues);
    }

    public void saveNewstoSaved(NewsObj newsObj) {
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
        contentValues.put(DBConstant.NEWS_TOSHOW, 1);
        contentValues.put(DBConstant.NEWS_ISSAVED, newsObj.getIsSaved());

        db.insert(DBConstant.TABLE_SAVED_NEWS, null, contentValues);
    }

    public void saveGallery(String newsType, String galleryType, String response) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstant.NEWS_TYPE, newsType);
        contentValues.put(DBConstant.GALLERY_TYPE, galleryType);
        contentValues.put(DBConstant.GALLERY_RESPONSE, response);

        db.insert(DBConstant.TABLE_GALLERY, null, contentValues);
    }

    public void saveEpaperPages(String newsType, String epaperId, String response) {
        ContentValues values = new ContentValues();
        values.put(DBConstant.NEWS_TYPE, newsType);
        values.put(DBConstant.EPAPER_ID, epaperId);
        values.put(DBConstant.EPAPER_PAGE_RESPONSE, response);

        db.insert(DBConstant.TABLE_EPAPER_PAGE, null, values);
    }

    /**
     * get {@link NewsObj} from the {@link SQLiteDatabase} if the following parameters matches
     *
     * @param newsType
     * @param newsCategoryId
     * @param newsId
     * @return ==============================================================================================
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

    public Boolean isNewsDetailPresent(String newsType, String newsCategoryId, String newsId) {
        Cursor cursor = db.query(true, DBConstant.TABLE_NEWS, null,
                DBConstant.NEWS_TYPE + "='" + newsType + "' AND " +
                        DBConstant.NEWS_CATEGORY_ID + "='" + newsCategoryId + "' AND " +
                        DBConstant.NEWS_ID + "='" + newsId + "'"
                , null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0 || cursor.getCount() > 0) {
            if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_DESCRIPTION)))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Boolean isEpaperPagePresent(String newsType, String epaperId) {
        Cursor cursor = db.query(true, DBConstant.TABLE_EPAPER_PAGE, null,
                DBConstant.NEWS_TYPE + "='" + newsType + "' AND " +
                        DBConstant.EPAPER_ID + " ='" + epaperId + "'",
                null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0)
            return true;
        return false;
    }

    /**
     * ==============================================================================================
     * get list of all the news matching the newsType
     *
     * @param newsType
     * @return ==============================================================================================
     */
    public List<NewsObj> getNewsList(String newsType, String categoryId, Boolean isFromFav) {
        ArrayList<NewsObj> list = new ArrayList<>();
        Cursor cursor;
        if (isFromFav) {
            cursor = db.query(true, DBConstant.TABLE_SAVED_NEWS, null,
                    DBConstant.NEWS_TYPE + "='" + newsType + "'", null, null, null, null, null);

        } else {
            cursor = db.query(true, DBConstant.TABLE_NEWS, null,
                    DBConstant.NEWS_TYPE + "='" + newsType + "' AND " + DBConstant.NEWS_CATEGORY_ID + "='" + categoryId + "'", null, null, null, null, null);
        }

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
                                cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_URL)),
                                cursor.getInt(cursor.getColumnIndex(DBConstant.NEWS_ISSAVED)))
                );
                cursor.moveToNext();
            }
            Log.i("Sqlite", list.size() + "");
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
                    cursor.getString(cursor.getColumnIndex(DBConstant.NEWS_URL)),
//                    cursor.getInt(cursor.getColumnIndex(DBConstant.NEWS_TOSHOW)),
                    cursor.getInt(cursor.getColumnIndex(DBConstant.NEWS_ISSAVED)));
            return newsObj;
        }
        return null;


    }

    public String getLocalGalleryResponse(String newsType, String galleryType) {
        Cursor cursor = db.query(true, DBConstant.TABLE_GALLERY, null, DBConstant.NEWS_TYPE + "='" + newsType +
                        "' AND " +
                        DBConstant.GALLERY_TYPE + "='" + galleryType + "'",
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(DBConstant.GALLERY_RESPONSE));
        }
        return null;
    }

    public String getEpaperPages(String newsType, String epaperId) {
        Cursor cursor = db.query(true, DBConstant.TABLE_EPAPER_PAGE, null,
                DBConstant.NEWS_TYPE + "='" + newsType + "' AND " +
                        DBConstant.EPAPER_ID + " ='" + epaperId + "'",
                null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0)
            return cursor.getString(cursor.getColumnIndex(DBConstant.EPAPER_PAGE_RESPONSE));
        return null;
    }

    /**
     * ==============================================================================================
     * methods to delete specific rows of the tables
     *
     * @param newsType
     * @param newsCategoryId
     * @param newsId         ==============================================================================================
     */
    public void deleteRowFromNews(String newsType, String newsCategoryId, String newsId) {
        db.delete(DBConstant.TABLE_NEWS,
                DBConstant.NEWS_TYPE + "=" + newsType + " AND " +
                        DBConstant.NEWS_CATEGORY_ID + "='" + newsCategoryId + "' AND " +
                        DBConstant.NEWS_ID + "='" + newsId + "'", null);
    }

    public void deleteRowFromSavedNews(String newsType, String newsId) {
        db.delete(DBConstant.TABLE_SAVED_NEWS,
                DBConstant.NEWS_TYPE + "=" + newsType + " AND " +
                        DBConstant.NEWS_ID + "='" + newsId + "'", null);
    }

    public void deleteLocalGallery(String newsType, String galleryType) {
        db.delete(DBConstant.TABLE_GALLERY,
                DBConstant.NEWS_TYPE + "='" + newsType + "' AND " +
                        DBConstant.GALLERY_TYPE + "='" + galleryType + "'", null);
    }

    public void deleteEpaperPages(String newsType, String epaperId) {
        db.delete(DBConstant.TABLE_EPAPER_PAGE,
                DBConstant.NEWS_TYPE + "='" + newsType + "' AND " +
                        DBConstant.EPAPER_ID + "='" + epaperId + "'", null);
    }

    /**
     * ==============================================================================================
     * methods to update the existing rows of the Tables
     *
     * @param newsObj ==============================================================================================
     */
    public void updateNewsDetail(NewsObj newsObj) {
        if (isNewsPresent(newsObj.getNewsType(), newsObj.getNewsCategoryId(), newsObj.getNewsId())) {
            ContentValues values = new ContentValues();
            values.put(DBConstant.NEWS_DESCRIPTION, newsObj.getDescription());
            values.put(DBConstant.NEWS_URL, newsObj.getNewsUrl());
            values.put(DBConstant.NEWS_ISSAVED, newsObj.getIsSaved());
            db.update(DBConstant.TABLE_NEWS, values,
                    DBConstant.NEWS_TYPE + " =? AND " +
                            DBConstant.NEWS_CATEGORY_ID + " =? AND " +
                            DBConstant.NEWS_ID + " =?",
                    new String[]{newsObj.getNewsType(), newsObj.getNewsCategoryId(), newsObj.getNewsId()})
            ;
        } else {
            saveNews(newsObj);
        }
    }

    /**
     * ==============================================================================================
     * methods to Truncate Table
     * ==============================================================================================
     */
    public void deleteAllNews() {
        db.execSQL(DBQueryStrings.DELETE_FROM + DBConstant.TABLE_NEWS);
    }
    public void deleteAllSavedNews() {
        db.execSQL(DBQueryStrings.DELETE_FROM + DBConstant.TABLE_SAVED_NEWS);
    }


    public void deleteAllLocalGallery() {
        db.execSQL(DBQueryStrings.DELETE_FROM + DBConstant.TABLE_GALLERY);
    }

    public void deleteAllEpaperPages() {
        db.execSQL(DBQueryStrings.DELETE_FROM + DBConstant.TABLE_EPAPER_PAGE);
    }

}
