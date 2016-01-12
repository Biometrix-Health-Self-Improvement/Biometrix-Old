package com.rocket.biometrix;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by Alder on 11/30/2015.
 * SQL lite subclass
 */
public class LocalStorageAccess extends SQLiteOpenHelper {
//http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html
    //TODO: How can this class be restructured to make adding entries easier? Editing entries easier? Pulling entries out for display easier? How can backing this up to webserver be made easier?

    //TODO: Can local dbs be detected and checked against some kind of key?
    //Schema
    private static final String LOCAL_DB_NAME = "BiometrixLocal";
    private static final int LOCAL_DB_VERSION = 1;
    private static final String UID = "_id";

    //TODO: Is there a better way to do this?
    //TODO: Think I'll make each module implement its own table through localstorageaccess with a nice constructor. I mean EXTEND this class.
    //Sleep table and columns
    private static final String TABLE_SLEEP = "Sleep";
    public static final String SLEEP_COLUMN_DATE = "StartDate";
    public static final String SLEEP_COLUMN_DURATION = "Duration";
    public static final String SLEEP_COLUMN_QUALITY = "Quality";
    public static final String SLEEP_COLUMN_NOTES = "Notes";
    public static final String SLEEP_COLUMN_HEALTH = "Health";

    //Exercise Add Entry Table strings

    //Extended from SQLiteOpenHelper
    public LocalStorageAccess(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version)
    {
        //Calls constructor of SQLiteOpenHelper
        super(context, LOCAL_DB_NAME, factory, LOCAL_DB_VERSION);
    }

    //TODO: Add if not exists error checking.
    //TODO: make abstract; NO RAW SQL
    // DICTIONARY with keys that link to SQL commands maybe? //Saved Preferences?
    //
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
        //TODO: FIX RAW SQL
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP);
        onCreate(db);
    }

    /**
     * Creates an SQL entry for the passed in sleep data
     * @param sleepData The data to be stored.
     */
    //TODO: Somehow avoid all these horrible getters (StringDictionary w/ column keys? & data type keys w/ cast switch?)
    //TODO: TRY HASHMAP
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


    //TODO: Create abstract way to fill the database. Some ideas: method that is a switch on a parameter that calls methods like the one below.
    //TODO: Another idea is to mimic a bundle and test string keys instead of data...
    //TODO: Another idea is some kind of observer pattern to call the right methods whenever an entry signal has been emitted by an activity.
    //TODO: Right now, I don't see any way out of hardcoding all of this down to the individual data points in SPECIFIC module entries :(
    /**
     * Returns the top row from the database sleep table
     * @return Returns a sleepdata object with the information from the database
     */
    public SleepData GetTopSleepEntry()
    {
        //Select Top 1 * From Sleep Order By StartDate
        String query = "Select * FROM " + TABLE_SLEEP + " Order By " + SLEEP_COLUMN_DATE;

        SQLiteDatabase db = this.getWritableDatabase();

        //TODO: Call up the clean up crew
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
