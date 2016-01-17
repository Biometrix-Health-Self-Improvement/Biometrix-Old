package com.rocket.biometrix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

//TODO: Use this for final test adb uninstall <yourpackagename>

//TODO: Implement an export method that will call on some kind of FileAccess class that will save an encrypted binary to the users phone as like an offline save backup. SD CARD
/**
 * Created by "F4LL0N" on 1/10/2016.
 * SQL lite subclass
 * //http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html
 * Abstract base 'middle layer' class for interfacing with, reading, writing a local DB.
 * RENAME TO SQLiteAdapterBase when finished.
 */
public abstract class LocalStorageAccessREMIX extends SQLiteOpenHelper {

    //Name of database that won't change throughout all the implementations of this class.
    protected static final String DATABASE_NAME = "BiometrixLAS";
    /*
    * Please increment by 1 each time major changes are made in the database, document your change here
     * Version 1 on 1/08/16
    */
    protected static final int DATABASE_VERSION = 1;

    //TODO: Pull user login information from SharedPreference Class and hash it somehow or use the webservice to get a UserID for all the tables.

    public LocalStorageAccessREMIX(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //SQLiteOpenHelper default Ctor
    public LocalStorageAccessREMIX(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    //Called when app is installed
    @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(createTable());
            }
        catch(SQLException e){
            e.printStackTrace();
            //TODO: Error handling, validation...
        }

    }
    //When database version has changed, call the child module implementation of updating the database.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    //For now call onUpgradeAlter. I know this breaks OO principles, but seems like a good solution since the modules tables can be so different, and the whole db has to be updated at once.
       boolean oldVersionDetected = onUpgradeAlter(db, oldVersion, newVersion);
    }

    //Way to insert values into a table
    //Child class (a module's implementation will fill a ContentValues and call this function after double checking the keys=column names)
    protected long safeInsert(String tablename, String nullColumn, ContentValues columnsAndValues){

        SQLiteDatabase db = this.getWritableDatabase();
         long rowNumberInserted = -1; //-1 if fail

        db.beginTransaction();

        try {
            rowNumberInserted = db.insertOrThrow(tablename, nullColumn, columnsAndValues);
            db.setTransactionSuccessful();
            db.close(); //TODO: close necessary?
        } catch(SQLException e) {

            e.printStackTrace();

        } finally {
            db.endTransaction(); //rollback is automatic
        }

        return rowNumberInserted;
    }

   //Get int database version for testing in the onUpgrade methods
    protected int getDBVersion(){
        return DATABASE_VERSION;
    }


    //TODO: Define functions for accessing database here. Probably just going to return Strings, however CalendarView() likes em

    //A module's table create sql statement.
    protected abstract String createTable();

    //Returns string array of private string variables representing Columns in child module class
    protected abstract String[] getColumns();

    //Version safe Alter table SQL called in onUpgrade, eventually might return some kind of error checking information...
    //Returns true if oldVersion was detected
    protected abstract boolean onUpgradeAlter(SQLiteDatabase db, int oldVersion, int newVersion);
}
