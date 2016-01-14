package com.rocket.biometrix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by JP on 1/10/2016.
 * Will rename when finished
 */
public class LocalStorageAccessExercise extends LocalStorageAccessREMIX {

    //TODO: Could columns be a private inner class?
    //Strings that represent table and column names in the database for Exercise X
    private static final String TABLE_NAME = "Exercise";
    private static final String UID = "Exercise_id"; //ID used for primary key
    private static final String MODE = "Mode"; //String to for mode which is not implemented yet. Think: Belly fat reduction mode - probably needs laps, won't need weight. Yada yada YODA
    //Columns
    private static final String TITLE = "Title"; //Title will help co-determine the module mode e.g. Simple mode (yay I walked to the fridge), Gainz mode (weight and reps etc.)
    private static final String TYPE = "Type"; //light, cardio, etc.
    private static final String MINUTES = "Minutes"; //minutes exercised
    private static final String REPS = "Reps"; //Reps or laps, data significance determined by module mode WHICH IS NOT IMPLEMENTED YET
    private static final String LAPS = "Laps";
    private static final String WEIGHT = "Weight";
    private static final String INT = "Intensity";
    private static final String NOTES = "Notes";

    // All the columns above, see getColumns() below
    private static final String[] columns = {TITLE, TYPE, MINUTES, REPS, LAPS, WEIGHT, INT, NOTES};

    //Later, we'll hopefully get to a shared preferences class that stores BMI and weight information.

    public LocalStorageAccessExercise(Context context) {
        super(context);
    }


    //TODO: How avoid RAW SQL on table create? IS there a predefined merge in SQLiteDatabaseHelper?
    //TODO: Convince group to use: https://github.com/greenrobot/greenDAO
    @Override
    protected String createTable() {
        //Some SQL
        String createTableSQL = "CREATE TABLE " + TABLE_NAME +
                "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " VARCHAR(255));"; //TODO: FINISH THIS

        return createTableSQL;
    }

    //Update table on user upgrade
    protected boolean onUpgradeAlter(SQLiteDatabase db, int oldVersion, int newVersion) {
        boolean versionDetected = true;

        //In future, will need to test version to upgrade properly.
        if (oldVersion < 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db); //Drop and recreate
        }
//        else if (oldVersion < 2) {
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//            onCreate(db); //Drop and recreate
//        }
        else {
            versionDetected = false;
        }

        return versionDetected;
    }

    //Prints all column names and returns a string array with them in it.
    public String[] getColumns() {
        for (String s : columns) {
            System.out.println(s);
            Log.d("column: ", s);
        }
        return columns;
    }

    //TODO: enforce that children have an insert method???
    //TODO: Should I check keys BEFORE insert? or test the content AFTER exception throw?
    //Make a method that calls this with ur custom dadta points bro.
    // protected long safeInsert(String tablename, String nullColumn, ContentValues columnsAndValues){
    //TODO: use iterator to fill CV from string array and/or check the keys against the private strings above.
    //check if key exists, if she do, insert it into the CV all in one itty
    public void insertFromContentValues(ContentValues cv) {

        //Real ContentValues that will be passed to the base class' insert method.
        ContentValues dataToBeInserted = new ContentValues();

        //This is inefficient for more than 100 columns, but we've got a glorified file system so we'll be fine.
        for (String columnName : columns) {
            if (cv.containsKey(columnName)) {
                //if the key pulled out of the parameter cv is equal to any string inside columns:
                dataToBeInserted.put(columnName,cv.getAsString(columnName));
            }//TODO: else block with Log() information
        }

        //WHERE THE MAGIC HAPPENS TODO: Understand implicit casts in sqlite, everything works as 100% as strings, but unsure about DATETIME
        safeInsert(TABLE_NAME, columns[1], dataToBeInserted );


//        //ANOTHER WAY
//            //Get Set of ContentValues keys and values.
//            Set<Map.Entry<String, Object>> cvSet = cv.valueSet();
//            Iterator itr = cvSet.iterator(); //Make an iterator on that Set.
//
//            Log.d("insertFromContentValues", "ContentValue Length :: " + cv.size());
//
//            while(itr.hasNext())
//            {
//                //move iterator :::: test for off by one
//                Map.Entry me = (Map.Entry)itr.next();
//                String key = me.getKey().toString(); //cv's key (column name)
//                Object value =  me.getValue(); //cv's value (data)
//
//                Log.d("insertFromContentValues", "Key:"+key+", values:"+(String)(value == null?null:value.toString()));
//
//                //Fill up other cv here.
//
//            }//end itr
        }//end insert


}
