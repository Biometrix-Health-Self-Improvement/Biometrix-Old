package com.rocket.biometrix;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Troy Riblett, troy.riblett@oit.edu on 10/28/2016
 * DatabaseConnect
 * Connects to the Biometrix Webserver to access the relational database
 *
 */
public class DatabaseConnect extends AsyncTask<String, Void, Void>
{
    private AsyncResponse delegate = null;

    //Creates a delegate to the function that will catch the return result
    DatabaseConnect(AsyncResponse receiver)
    {
        super();
        delegate = receiver;
    }

    //The string object that will be passed to the return result
    private String returnResult;


    @Override
    /**
     * Initializes the returnResult to an empty string
     */
    protected void onPreExecute()
    {
        returnResult = "";
    }

    //Required method, this is the main method for doing the asynchronous task
    @Override
    /**
     * Passed three string variables that correspond to the operation, username,
     * and password respectively
     */
    protected Void doInBackground(String... params)
    {
        //Creates the object for the string that will be returned
        StringBuilder returnStringBuilder = new StringBuilder();

        //This string contains the information for which database operation to perform
        //It must have a match in the Db_operation.php file on the server
        String db_operation = "";
        if (params[0].equals(DatabaseConnectionTypes.LOGIN_CHECK) )
        {
            db_operation = "Login";
        }
        else if (params[0].equals(DatabaseConnectionTypes.LOGIN_CREATE) )
        {
            db_operation = "Add";
        }
        else if(params[0].equals(DatabaseConnectionTypes.LOGIN_DELETE) )
        {
            db_operation = "Delete";
        }

        //The http url connection that will be used to talk to the webserver
        HttpURLConnection urlConnection = null;
        try
        {
            //The stream to pass data to the webserver
            DataOutputStream webOutput;

            //The stream to read responses from the server
            DataInputStream webInput;
            URL url = new URL("http://54.201.41.185/Db_operation.php");

            //Opens the connection to the webserver
            urlConnection = (HttpURLConnection) url.openConnection();

            //Miscellaneous settings for the connection
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Host", "54.201.41.185");

            //Sets the type of content passed to be json.. I think
            urlConnection.setRequestProperty("Content-Type", "application/json");


            //Creates the JSONObject to pass to the webserver. Username and Password
            //Must be filled out as well as operation
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("Username", params[1]);
            jsonParam.put("Password", params[2]);
            jsonParam.put("Operation", db_operation);

            //Send POST output
            webOutput = new DataOutputStream(urlConnection.getOutputStream() );
            webOutput.write((URLEncoder.encode(jsonParam.toString(), "UTF-8")).getBytes());
            webOutput.flush();
            webOutput.close();

            //
            urlConnection.connect();

            //Ensures that the webserver was actually able to be accessed
            int HttpResult = urlConnection.getResponseCode();

            if(HttpResult ==HttpURLConnection.HTTP_OK)
            {
                //Reads in all of the input from the stream to the string builder
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;

                while ((line = bufferedReader.readLine()) != null)
                {
                    returnStringBuilder.append(line + "\n");
                }

                bufferedReader.close();

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        }
        catch (java.io.IOException except)
        {
            returnResult = DatabaseConnectionTypes.CONNECTION_FAIL;
        }
        catch (org.json.JSONException jsonExcept)
        {
            returnResult = "Problems parsing output to server";
        }
        finally
        {
            //Attempts to disconnect the server. If it fails, the connection was null anyway
            try
            {
                urlConnection.disconnect();
            }
            catch (java.lang.NullPointerException except)
            {
                urlConnection = null;
            }
        }

        returnResult = returnStringBuilder.toString();

        return null;
    }


  /*  protected String GetConnectionURL(String dbConnectString)
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
    }*/

    @Override
    protected void onPostExecute(Void aVoid)
    {
        //Calls back to the other module with the resulting string
        delegate.processFinish(returnResult);
    }

}