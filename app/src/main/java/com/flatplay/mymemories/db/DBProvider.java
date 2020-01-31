package com.flatplay.mymemories.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBProvider extends ContentProvider {
    public static final String LOG_TAG = DBProvider.class.getSimpleName();

    private static final int event = 1000;

    private static final int MOVIE_ID  = 1001;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(DBContract.CONTENT_AUTHORITY, DBContract.PATH_PRODUCT, event);
        mUriMatcher.addURI(DBContract.CONTENT_AUTHORITY, DBContract.PATH_PRODUCT + "/#", MOVIE_ID);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = mUriMatcher.match(uri);

        switch (match) {
            case event:
                cursor = db.query(DBContract.event.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MOVIE_ID:
                selection = DBContract.event._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = db.query(DBContract.event.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query Unknown Uri " + getType(uri));
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case event:
                return DBContract.event.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return DBContract.event.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri + " with match" + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case event:
                return insertData(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertData(Uri uri, ContentValues values) {

        String name = values.getAsString(DBContract.event.COLUMN_EVENT_TITLE);

        if (name == null) {
            throw new IllegalArgumentException("event requires name");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowsInserted = db.insert(DBContract.event.TABLE_NAME, null, values);

        if (rowsInserted == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, rowsInserted);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case event:
                rowsDeleted = db.delete(DBContract.event.TABLE_NAME,null,null);
                break;
            case MOVIE_ID:
                selection = DBContract.event._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                rowsDeleted = db.delete(DBContract.event.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion Error for Uri:" + uri);
        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);

        switch (match){
            case event:
                return updateBill(uri, contentValues, selection, selectionArgs);
            case MOVIE_ID:
                selection = DBContract.event._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                return updateBill(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for" + uri);
        }
    }


    private int updateBill(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsUpdated = db.update(DBContract.event.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}
