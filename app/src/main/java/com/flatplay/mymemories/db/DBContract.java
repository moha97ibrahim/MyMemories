package com.flatplay.mymemories.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract {
    private DBContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.flatplay.mymemories";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_PRODUCT = "events";

    public static final class event implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;


        public final static String TABLE_NAME = "event";


        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_EVENT_DATE = "event_date";

        public final static String COLUMN_EVENT_TITLE = "event_title";

        public final static String COLUMN_EVENT_SUBJECT = "event_subject";

        public final static String COLUMN_EVENT_BODY = "event_body";

        public final static String COLUMN_EVENT_TYPE = "event_type";

        public final static String COLUMN_EVENT_STATUS = "event_status";


    }
}
