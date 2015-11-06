package com.rocket.biometrix;

/**
 * Created by TJ on 10/28/2015.
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.os.AsyncTask;
import android.util.Log;


/**
 *
 */
public class DatabaseLoginConnect extends AsyncTask<String, Void, Void>
{
    private AsyncResponse delegate = null;

    DatabaseLoginConnect(AsyncResponse receiver)
    {
        super();
        delegate = receiver;
    }

    private Object returnResult;

    @Override
    protected void onPreExecute()
    {
        returnResult = false;
    }

    //Required method, this is the main method for doing the asynchronous task
    @Override
    protected Void doInBackground(String... params)
    {
        String connectionUrl = GetConnectionURL(params[0]);

        if (connectionUrl != null) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

                Connection dbConnection = DriverManager.getConnection(connectionUrl);

                Log.w("Connection", "open");

                if (params[0].equals(DatabaseConnectionTypes.LOGIN_CHECK) )
                {
                    CheckLoginExists(params[1], params[2], dbConnection);
                }
                else if (params[0].equals(DatabaseConnectionTypes.LOGIN_CREATE) )
                {
                    CreateNewLogin(params[1], params[2], dbConnection);
                }

                dbConnection.close();

            } catch (Exception e) {
                Log.w("Error connection", "" + e.getMessage());
            }
        }
        return null;
    }


    protected String GetConnectionURL(String dbConnectString)
    {
        String dbName = "Biometrix";
        String server = "rocketjpserver.cktljt0phf7d.us-west-2.rds.amazonaws.com:1433";

        String userName = null;
        String password = null;

        if (dbConnectString.equals(DatabaseConnectionTypes.LOGIN_CHECK) )
        {
            userName = "ReadOnlyTest";
            password = "ReadTest";
        }
        else if (dbConnectString.equals(DatabaseConnectionTypes.LOGIN_CREATE) )
        {
            userName = "LoginCreator";
            password = "RocketMaker1!";
        }

        String full_connection = null;

        if (userName != null && password != null)
        {
            full_connection = "jdbc:jtds:sqlserver://"+server+";" +"databaseName="+dbName+";user="+userName+";password="+password;
        }


        return full_connection;
    }

    protected void CheckLoginExists(String username, String password, Connection dbConnection)
    {
        try {
            //Creates an SQL statement object.
            Statement sqlStatement = dbConnection.createStatement();

            String queryString = "select * from Biometrix.dbo.LoginTable";

            queryString = queryString + " where username = '" + username + "' and password = '" + password + "'";

            ResultSet resultSet = sqlStatement.executeQuery(queryString);

            if (resultSet.next()) {
                //If there is a record, then login succeeded
                String valueReturned = resultSet.getString(1);

                returnResult = (valueReturned != null);
            } else {
                returnResult = false;
            }

            sqlStatement.close();
            resultSet.close();
        }
        catch (Exception except)
        {
            returnResult = false;
        }
    }

    protected void CreateNewLogin(String username, String password, Connection dbConnection)
    {
        try {
            //Creates an SQL statement object.
            Statement sqlStatement = dbConnection.createStatement();

            //e.g. Insert Biometrix.dbo.LoginTable (Username, Password) Values ('Bob', 'Password1');
            String queryString1 = "Insert Biometrix.dbo.LoginTable (Username, Password) Values ('";

            String queryString = queryString1 + username +"', '" + password + "')";

            int rowsEffected = sqlStatement.executeUpdate(queryString);

            if (rowsEffected == 1)
            {
                returnResult = true;
            }
            else
            {
                returnResult = false;
            }

            sqlStatement.close();
        }
        catch (Exception except)
        {
            returnResult = false;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        delegate.processFinish(returnResult);
    }

}