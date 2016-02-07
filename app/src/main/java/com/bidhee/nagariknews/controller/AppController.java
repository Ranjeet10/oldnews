package com.bidhee.nagariknews.controller;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import nagarikschema.DaoMaster;
import nagarikschema.DaoSession;

/**
 * Created by ronem on 2/4/16.
 */
public class AppController extends Application {

    public DaoSession daoSession;
    private String DATABASE = "nagarikdb";

    @Override
    public void onCreate() {
        super.onCreate();
        setUpDataBase();
    }

    private void setUpDataBase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), DATABASE, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
