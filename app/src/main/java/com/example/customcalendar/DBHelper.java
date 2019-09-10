package com.example.customcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "EVENTS_DB.db";
    public static final int DB_VERSION = 1;
    public static final String EVENT_TABLE_NAME = "eventstable";
    public static final String EVENT = "event";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    private static final String CREATE_EVENT_TABLE = "create table " + EVENT_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT + " TEXT, " + DESCRIPTION + " TEXT, " + DATE + " TEXT, " + MONTH + " TEXT, "
            + YEAR + " TEXT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS eventstable";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void SaveEvent(String event, String description, String date, String month, String year, SQLiteDatabase database) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT, event);
        cv.put(DESCRIPTION, description);
        cv.put(DATE, date);
        cv.put(MONTH, month);
        cv.put(YEAR, year);
        database.insert(EVENT_TABLE_NAME, null, cv);
    }

    public Cursor readEvent(String date, SQLiteDatabase database) {
        String[] projections = {EVENT, DESCRIPTION, DATE, MONTH, YEAR};
        String selection = DATE + "=?";
        String[] selectionArgs = {date};
        return database.query(EVENT_TABLE_NAME, projections, selection, selectionArgs, null, null, null);
    }

    public Cursor readEventPerMonth(String month, String year, SQLiteDatabase database) {
        String[] projections = {EVENT, DESCRIPTION, DATE, MONTH, YEAR};
        String selection = MONTH + "=? and " +YEAR + "=?";
        String[] selectionArgs = {month,year};
        return database.query(EVENT_TABLE_NAME, projections, selection, selectionArgs, null, null, null);
    }
}
