package com.rocket.biometrix;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
}

/* The below is the code that I used to create a connection to the database. However, since this was
 added as a sub-class originally, I am leaving it commented out until we determine exactly how we
 want to do this.


//Some, might not be all, of the needed imported classes.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import android.os.AsyncTask;

//How the database connection task is called
new DatabaseLoginConnect().execute();

//Has to extend AsyncTask as AndroidStudio does not let you run a network
//connection in the main thread.
//The Void, Void, ResultSet are the return types of the 3 functions you can
//override in this class (the others are along the line of preExecute and postExecute?)
class DatabaseLoginConnect extends AsyncTask<Void, Void, ResultSet>
{

    //Required method, this is the main method for doing the asynchronous task
    @Override
    protected ResultSet doInBackground(Void... params)
    {
        try {
            //Gets the database driver from the jtds jar file in the same path
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            //The credentials of a test user made in this database. It only has read permissions to the server
            String username = "ReadOnlyTest";
            String password = "ReadTest";

            //The rather complex server string. It is
            //"driver://servername:port#/DATABASE;user=username;password=password;instance=InstanceType
            Connection DbConnection = DriverManager.getConnection("jdbc:jtds:sqlserver://rocketjpserver.cktljt0phf7d.us-west-2.rds.amazonaws.com:1433/Test;user=" + username + ";password=" + password + ";instance=SQLEXPRESS");

            Log.w("Connection", "open");

            //Creates an SQL statement object.
            Statement sqlStatement = DbConnection.createStatement();

            //Gets a result set from the executed query that is passed.
            ResultSet resultSet = sqlStatement.executeQuery(" select * from Test.dbo.TeamRocketsTargets");

            //Close the database connection
            DbConnection.close();


            return resultSet;
        } catch (Exception e)
        {
            Log.w("Error connection","" + e.getMessage());
            return null;
        }
    }
}
*/