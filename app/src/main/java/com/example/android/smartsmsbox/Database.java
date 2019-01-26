package com.example.android.smartsmsbox;

import android.provider.BaseColumns;

public final class Database {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_SENDER + " TEXT," +
                        FeedEntry.COLUMN_BODY + " TEXT," +
                        FeedEntry.COLUMN_READ + " INTEGER," +
                        FeedEntry.COLUMN_TYPE + " INTEGER," +
                        FeedEntry.COLUMN_CATEGORY + " INTEGER," +
                        FeedEntry.COLUMN_TIME + " TEXT," +
                        FeedEntry.COLUMN_LATITUDE + " TEXT," +
                        FeedEntry.COLUMN_LONGITUDE + " TEXT)";
        private Database() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "sms";
        public static final String COLUMN_SENDER = "sender";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_READ= "read";
        public static final String COLUMN_CATEGORY= "category";
        public static final String COLUMN_TYPE= "type";
        public static final String COLUMN_LATITUDE= "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
    }



}
