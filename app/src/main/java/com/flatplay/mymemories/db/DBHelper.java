package com.flatplay.mymemories.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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


    public ArrayList<String> getEvent(String id) {
        ArrayList<String> arrayList1 = new ArrayList<>();
        SQLiteDatabase database;
        String query = "SELECT * FROM event WHERE " + DBContract.event._ID + "=" + id;
        database = getReadableDatabase();
        Cursor get = database.rawQuery(query, null);
        get.moveToFirst();
        while (get.isAfterLast() == false) {
            arrayList1.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_DATE)));
            arrayList1.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_TITLE)));
            arrayList1.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_SUBJECT)));
            arrayList1.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_BODY)));
            arrayList1.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_STATUS)));
            get.moveToNext();
        }
        return arrayList1;
    }


    public void updateEvent(ContentValues values, String[] id) {
        SQLiteDatabase database;
        database = getWritableDatabase();
        String where = DBContract.event._ID + " = ?";
        database.update("event", values, where, id);
    }

    public ArrayList<ArrayList<String>> getEventByType(String status) {
        ArrayList<ArrayList<String>> arrayList1 = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        SQLiteDatabase database;
        String query = "SELECT * FROM event WHERE " + DBContract.event.COLUMN_EVENT_STATUS + " = '" + status + "'";
//        String query = "SELECT * FROM event ";
        database = getReadableDatabase();
        Cursor get = database.rawQuery(query, null);
        get.moveToFirst();
        while (!get.isAfterLast()) {
            arrayList2.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_DATE)));
            arrayList2.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_TITLE)));
            arrayList2.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_SUBJECT)));
            arrayList2.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_BODY)));
            arrayList2.add(get.getString(get.getColumnIndex(DBContract.event.COLUMN_EVENT_STATUS)));
            arrayList1.add(arrayList2);
            arrayList2 = new ArrayList<>();
            get.moveToNext();
        }
        return arrayList1;
    }
}
