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
    //private static final String USER_ID = "not implemented yet, will be in shared preferences";
    private static final String MODE = "Mode"; //String to for mode which is not implemented yet. Think: Belly fat reduction mode - probably needs laps, won't need weight. Yada yada YODA
    //Columns
    private static final String TITLE = "Title"; //Title will help co-determine the module mode e.g. Simple mode (yay I walked to the fridge), Gainz mode (weight and reps etc.)
    private static final String TYPE = "Type"; //light, cardio, etc.
    private static final String MINUTES = "Minutes"; //minutes exercised
    private static final String REPS = "Reps"; //Reps or laps, data significance determined by module mode WHICH IS NOT IMPLEMENTED YET
    private static final String LAPS = "Laps";
    private static final String WEIGHT = "Weight";
    private static final String INTY = "Intensity";
    private static final String NOTES = "Notes";
    private static final String DATE = "DateEx";
    private static final String TIME = "TimeEx";

    // All the columns above, see getColumns() below
    private static final String[] columns = {TITLE, TYPE, MINUTES, REPS, LAPS, WEIGHT, INTY, NOTES, DATE, TIME};

    //Later, we'll hopefully get to a shared preferences class that stores BMI and weight information.

    public LocalStorageAccessExercise(Context context) {
        super(context);
    }


    //onCreate in parent will call this.
    @Override
    protected String createTable() {
        //Some SQL
        String createTableSQL = "CREATE TABLE " + TABLE_NAME +
                "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " VARCHAR(255), " +
                TYPE + " VARCHAR(140), " +
                MINUTES + " TINYINT, " +
                REPS + " TINYINT, " +
                LAPS + " TINYINT, " +
                WEIGHT + " SMALLINT, " +
                INTY + " TINYINT, " +
                NOTES + " VARCHAR(255), " +
                DATE + " DATE, " +
                TIME + "VARCHAR(50)" +
                ");";

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

    //TODO: document function
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

        //WHERE THE MAGIC HAPPENS
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
