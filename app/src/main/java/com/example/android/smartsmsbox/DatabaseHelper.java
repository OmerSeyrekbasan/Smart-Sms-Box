package com.example.android.smartsmsbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "smsInbox.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Database.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + Database.FeedEntry.TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertSMS(SMS sms) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Database.FeedEntry.COLUMN_BODY, sms.getBody());
        values.put(Database.FeedEntry.COLUMN_SENDER, sms.getSender());
        values.put(Database.FeedEntry.COLUMN_READ, sms.isRead());
        values.put(Database.FeedEntry.COLUMN_CATEGORY, sms.getCategory());
        values.put(Database.FeedEntry.COLUMN_TYPE, sms.getType());
        values.put(Database.FeedEntry.COLUMN_TIME, sms.getTime());
        values.put(Database.FeedEntry.COLUMN_LATITUDE, sms.getLatitude());
        values.put(Database.FeedEntry.COLUMN_LONGITUDE, sms.getLongitude());

        // insert row
        long id = db.insert(Database.FeedEntry.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

//    public Note getNote(long id) {
//        // get readable database as we are not inserting anything
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(Note.TABLE_NAME,
//                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},
//                Note.COLUMN_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // prepare note object
//        Note note = new Note(
//                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
//                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
//                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));
//
//        // close the db connection
//        cursor.close();
//
//        return note;
//    }

    public ArrayList<SMS> getAllSMS() {
        ArrayList<SMS> smss = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Database.FeedEntry.TABLE_NAME + " ORDER BY " +
                Database.FeedEntry.COLUMN_TIME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SMS sms = new SMS();
                sms.setId(cursor.getInt(cursor.getColumnIndex(Database.FeedEntry._ID)));
                sms.setBody(cursor.getString(cursor.getColumnIndex(Database.FeedEntry.COLUMN_BODY)));
                sms.setTime(cursor.getString(cursor.getColumnIndex(Database.FeedEntry.COLUMN_TIME)));
                sms.setSender(cursor.getString(cursor.getColumnIndex(Database.FeedEntry.COLUMN_SENDER)));
                sms.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Database.FeedEntry.COLUMN_LATITUDE))));
                sms.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Database.FeedEntry.COLUMN_LONGITUDE))));
                sms.setRead(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Database.FeedEntry.COLUMN_READ))));
                sms.setType((cursor.getInt(cursor.getColumnIndex(Database.FeedEntry.COLUMN_TYPE))));
                sms.setCategory((cursor.getInt(cursor.getColumnIndex(Database.FeedEntry.COLUMN_CATEGORY))));

                smss.add(sms);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return smss;
    }

//    public void deleteNote(Note note) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(note.getId())});
//        db.close();
//    }

    public int updateNote(SMS sms) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Database.FeedEntry.COLUMN_READ, sms.isRead());

        // updating row
        return db.update(Database.FeedEntry.TABLE_NAME, values, Database.FeedEntry._ID + " = ?",
                new String[]{String.valueOf(sms.getId())});
    }


}
