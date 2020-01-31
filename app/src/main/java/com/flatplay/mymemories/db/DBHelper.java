package com.flatplay.mymemories.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "event.db";

    private static int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //event table
        String SQL_CREATE_MOVIECREATING_TABLE = "CREATE TABLE " + DBContract.event.TABLE_NAME + "("
                + DBContract.event._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBContract.event.COLUMN_EVENT_DATE + " TEXT,"
                + DBContract.event.COLUMN_EVENT_TITLE + " TEXT,"
                + DBContract.event.COLUMN_EVENT_SUBJECT + " TEXT,"
                + DBContract.event.COLUMN_EVENT_BODY + " TEXT,"
                + DBContract.event.COLUMN_EVENT_TYPE + " TEXT,"
                + DBContract.event.COLUMN_EVENT_STATUS + " TEXT);";


        db.execSQL(SQL_CREATE_MOVIECREATING_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
