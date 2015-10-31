package com.rocket.biometrix;

/**
 * Created by TJ on 10/28/2015.
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


//The Void, Void, ResultSet are the return types of the 3 functions you can
//override in this class (the others are along the line of preExecute and postExecute?)
public class DatabaseLoginConnect extends AsyncTask<String, Void, Void>
{
    private AsyncResponse delegate = null;

    DatabaseLoginConnect(AsyncResponse receiver)
    {
        super();
        delegate = receiver;
    }

    private Boolean loginSuccess;

    @Override
    protected void onPreExecute()
    {
        loginSuccess = false;
    }

    //Required method, this is the main method for doing the asynchronous task
    @Override
    protected Void doInBackground(String... params)
    {
        try {
            String dbName = "Test";
            String server = "rocketjpserver.cktljt0phf7d.us-west-2.rds.amazonaws.com:1433";
            String userName = "ReadOnlyTest";
            String password = "ReadTest";

            String connectionUrl = "jdbc:jtds:sqlserver://"+server+";" +"databaseName="+dbName+";user="+userName+";password="+password;
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            Connection dbConnection = DriverManager.getConnection(connectionUrl);

            Log.w("Connection", "open");

            //Creates an SQL statement object.
            Statement sqlStatement = dbConnection.createStatement();

            //Gets a result set from the executed query that is passed.
            String queryString = "select * from Biometrix.dbo.LoginTable";

            queryString = queryString + " where username = '" + params[0] + "' and password = '" + params[1] + "'";

            ResultSet resultSet = sqlStatement.executeQuery(queryString);

            if (resultSet.next())
            {
                //If there is a record, then login succeeded
                String valueReturned = resultSet.getString(1);

                loginSuccess = (valueReturned != null);

                //Close the database connection


            }
            else
            {
                loginSuccess = false;
            }

            dbConnection.close();

        } catch (Exception e)
        {
            Log.w("Error connection","" + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        delegate.processFinish(loginSuccess);
    }

}