package com.rocket.biometrix;

/**
 * Created by TJ on 10/28/2015.
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
            //Gets the database driver from the jtds jar file in the same path
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            //The credentials of a test user made in this database. It only has read permissions to the server
            String sqlUsername = "ReadOnlyTest";
            String sqlPassword = "ReadTest";

            //The rather complex server string. It is
            //"driver://servername:port#/DATABASE;user=username;password=password;instance=InstanceType
            Connection DbConnection = DriverManager.getConnection("jdbc:jtds:sqlserver://rocketjpserver.cktljt0phf7d.us-west-2.rds.amazonaws.com:1433/Test;user=" + sqlUsername + ";password=" + sqlPassword + ";instance=SQLEXPRESS");

            Log.w("Connection", "open");

            //Creates an SQL statement object.
            Statement sqlStatement = DbConnection.createStatement();

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

            DbConnection.close();

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