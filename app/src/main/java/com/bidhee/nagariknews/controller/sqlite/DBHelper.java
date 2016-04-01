package com.bidhee.nagariknews.controller.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ronem on 4/1/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static String TAG = "database_tag";

    DBHelper(Context context) {
        super(context, DBConstant.DATABASE_NAME, null, DBConstant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBQueryStrings.CREATE_TABLE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion
                + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL(DBQueryStrings.drop_table + DBConstant.TABLE_NEWS);
        onCreate(db);
    }
}
