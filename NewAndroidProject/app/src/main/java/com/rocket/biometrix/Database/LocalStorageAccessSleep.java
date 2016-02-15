package com.rocket.biometrix.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rocket.biometrix.SleepModule.SleepData;

import java.util.LinkedList;
import java.util.List;

public class LocalStorageAccessSleep extends SQLiteOpenHelper {
//http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html

    //Schema
    private static final String LOCAL_DB_NAME = "BiometrixLocal";
    private static final int LOCAL_DB_VERSION = 1;
    private static final String UID = "_id";


    //Sleep table and columns
    private static final String TABLE_SLEEP = "Sleep";
    public static final String SLEEP_COLUMN_DATE = "StartDate";
    public static final String SLEEP_COLUMN_DURATION = "Duration";
    public static final String SLEEP_COLUMN_QUALITY = "Quality";
    public static final String SLEEP_COLUMN_NOTES = "Notes";
    public static final String SLEEP_COLUMN_HEALTH = "Health";

    //Exercise Add Entry Table strings

    //Extended from SQLiteOpenHelper
    public LocalStorageAccessSleep(Context context, String name,
                                   SQLiteDatabase.CursorFactory factory, int version)
    {
        //Calls constructor of SQLiteOpenHelper
        super(context, LOCAL_DB_NAME, factory, LOCAL_DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creates the SQL string to make the SLEEP table
        String CREATE_SLEEP_TABLE = "CREATE TABLE " +
                TABLE_SLEEP + " ( " + SLEEP_COLUMN_DATE + " datetime Not Null," + SLEEP_COLUMN_DURATION
                + " time Not null, " + SLEEP_COLUMN_QUALITY + " int Not Null," +
                SLEEP_COLUMN_NOTES + " varchar(300), " + SLEEP_COLUMN_HEALTH + " varchar(20) " + ");";
        db.execSQL(CREATE_SLEEP_TABLE);
    }

    //Extended from SQLiteOpenHelper
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP);
        onCreate(db);
    }

    /**
     * Creates an SQL entry for the passed in sleep data
     * @param sleepData The data to be stored.
     */

    public void AddSleepEntry(SleepData sleepData)
    {
        ContentValues values = new ContentValues();
        values.put(SLEEP_COLUMN_DATE, sleepData.getStartTime());
        values.put(SLEEP_COLUMN_DURATION, sleepData.getDuration());
        values.put(SLEEP_COLUMN_HEALTH, sleepData.getHealthStatus());
        values.put(SLEEP_COLUMN_QUALITY, sleepData.getSleepQuality());
        values.put(SLEEP_COLUMN_NOTES, sleepData.getNotes());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SLEEP, null, values);
        db.close();
    }


    /**
     * Returns the top row from the database sleep table
     * @return Returns a sleepdata object with the information from the database
     */
    public SleepData GetTopSleepEntry()
    {
        //Select Top 1 * From Sleep Order By StartDate
        String query = "Select * FROM " + TABLE_SLEEP + " Order By " + SLEEP_COLUMN_DATE;

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery(query, null);

        String date;
        String duration;
        int quality;
        String status;
        String notes;

        SleepData data = null;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            date = cursor.getString(0);
            duration = cursor.getString(1);
            quality = cursor.getInt(2);
            notes = cursor.getString(3);
            status = cursor.getString(4);


            data = new SleepData(date, duration, quality, status, notes);
        }

        cursor.close();

        db.close();
        return data;
    }

    /**
     * Returns the rows from the sleep data table
     * @return Returns a list of sleepdata objects with the information from the database
     */
    public List<SleepData> GetSleepEntries()
    {
        String query = "Select " + SLEEP_COLUMN_DATE + ", " + SLEEP_COLUMN_DURATION + ", " +
                SLEEP_COLUMN_QUALITY + ", " + SLEEP_COLUMN_HEALTH + ", " + SLEEP_COLUMN_NOTES +
                " FROM " + TABLE_SLEEP + " Order By " + SLEEP_COLUMN_DATE;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String date;
        String duration;
        int quality;
        String status;
        String notes;

        List<SleepData> sleepDataList = new LinkedList<SleepData>();
        SleepData data = null;

        //If there is a valid entry move to it
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {
                date = cursor.getString(0);
                duration = cursor.getString(1);
                quality = cursor.getInt(2);
                status = cursor.getString(3);
                notes = cursor.getString(4);

                data = new SleepData(date, duration, quality, status, notes);
                sleepDataList.add(data);

                cursor.moveToNext();
            }
        }

        cursor.close();

        db.close();

        return sleepDataList;
    }
}
