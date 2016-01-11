package com.rocket.biometrix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

//TODO: Implement an export method that will call on some kind of FileAccess class that will save an encrypted binary to the users phone as like an offline save backup. SD CARD
/**
 * Created by "F4LL0N" on 1/10/2016.
 * SQL lite subclass
 * //http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html
 * Abstract base 'middle layer' class for interfacing with, reading, writing a local DB.
 */
public abstract class LocalStorageAccessREMIX extends SQLiteOpenHelper {

    //Name of database that won't change throughout all the implementations of this class.
    protected static final String DATABASE_NAME = "BiometrixLAS";
    protected static final int DATABASE_VERSION = 1;

    //TODO: Pull user login information from SharedPreference Class and hash it somehow or use the webservice to get a UserID for all the tables.

    public LocalStorageAccessREMIX(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public LocalStorageAccessREMIX(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
