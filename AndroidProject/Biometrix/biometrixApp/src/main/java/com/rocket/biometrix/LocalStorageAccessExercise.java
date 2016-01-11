package com.rocket.biometrix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by JP on 1/10/2016.
 */
public class LocalStorageAccessExercise extends LocalStorageAccessREMIX {

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

    //Later, we'll hopefully get to a shared preferences class that stores BMI and weight information.

    public LocalStorageAccessExercise(Context context) {
        super(context);
    }


    //TODO: How avoid RAW SQL on table create? IS there a predefined merge in SQLiteDatabaseHelper?
    //TODO: Convince group to use: https://github.com/greenrobot/greenDAO
    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                   "(" + UID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " VARCHAR(255));" );
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }





}
