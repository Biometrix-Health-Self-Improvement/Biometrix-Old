package com.rocket.biometrix.Database;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;


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
    public DatabaseConnect(AsyncResponse receiver)
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
     * Passed string variables that correspond to different variables depending on the operation
     * which is always the first paramaters
     */
    protected Void doInBackground(String... params) {
        //Creates the object for the string that will be returned
        StringBuilder returnStringBuilder = new StringBuilder();

        //This string contains the information for which database operation to perform
        //It must have a match in the Db_operation.php file on the server
        String db_operation = "";

        //Creates the JSONObject to pass to the webserver. Since Username and password requirements
        //Differ per operation
        JSONObject jsonParam = new JSONObject();
        try
        {

            switch (params[0])
            {
                //Login check requires username and password
                case DatabaseConnectionTypes.LOGIN_CHECK:
                    jsonParam.put("Username", params[1]);
                    jsonParam.put("Password", params[2]);
                    db_operation = "Login";
                    break;
                //Creating login requires username, password, and email
                case DatabaseConnectionTypes.LOGIN_CREATE:
                    db_operation = "Add";
                    jsonParam.put("Username", params[1]);
                    jsonParam.put("Password", params[2]);
                    jsonParam.put("Email", params[3]);
                    break;
                //Deleting login requires username, password, and email
                case DatabaseConnectionTypes.LOGIN_DELETE:
                    db_operation = "Delete";
                    jsonParam.put("Username", params[1]);
                    jsonParam.put("Password", params[2]);
                    jsonParam.put("Email", params[3]);
                    break;
                //Resetting password requires username and email field
                case  DatabaseConnectionTypes.LOGIN_RESET:
                    db_operation = "Reset";
                    jsonParam.put("Username", params[1]);
                    jsonParam.put("Email", params[2]);
                    break;
                case DatabaseConnectionTypes.GOOGLE_TOKEN:
                    db_operation = "GoogleLogin";
                    jsonParam.put("GoogleToken", params[1]);
                    break;
                default:
                    db_operation = "";
                    break;
            }

            jsonParam.put("Operation", db_operation);
        }
        catch (org.json.JSONException jsonExcept)
        {
            returnResult = "Problems parsing output to server";
        }


        //The http url connection that will be used to talk to the webserver
        HttpsURLConnection urlConnection = null;
        try
        {
            //The stream to pass data to the webserver
            DataOutputStream webOutput;

            //The stream to read responses from the server
            DataInputStream webInput;
            URL url = new URL("https://www.biometrixapp.com/do_operation.php");

            //Opens the connection to the webserver
            urlConnection = (HttpsURLConnection) url.openConnection();

            //Miscellaneous settings for the connection
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setUseCaches(false);

            //Apparently this breaks https connections, but is needed for http ones.
            /*urlConnection.setRequestProperty("Host", "biometrixapp.com");*/

            //Sets the type of content passed to be json
            urlConnection.setRequestProperty("Content-Type", "application/json");

            //Send POST output
            webOutput = new DataOutputStream(urlConnection.getOutputStream() );
            webOutput.write((URLEncoder.encode(jsonParam.toString(), "UTF-8")).getBytes());
            webOutput.flush();
            webOutput.close();

            //Starts the actual HTTPS connection
            urlConnection.connect();

            //Ensures that the webserver was actually able to be accessed
            int HttpsResult = urlConnection.getResponseCode();

            if(HttpsResult == HttpsURLConnection.HTTP_OK)
            {
                //Reads in all of the input from the stream to the string builder
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));

                String line = null;

                line = bufferedReader.readLine();

                if (line != null)
                {
                    returnStringBuilder.append(line);

                    while ((line = bufferedReader.readLine()) != null)
                    {
                        returnStringBuilder.append("\n" + line);
                    }

                    bufferedReader.close();
                }

            }
            else
            {
                //Debug info
                InputStream inputStream = urlConnection.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder debugInfo = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    debugInfo.append(line);
                }
                reader.close();

                inputStream.close();

                returnResult = debugInfo + DatabaseConnectionTypes.CONNECTION_FAIL;
            }
        }
        catch (java.io.IOException except)
        {
            returnResult = DatabaseConnectionTypes.CONNECTION_FAIL;
        }
        finally
        {
            //Attempts to disconnect the server. If it fails, the connection was null anyway
            try
            {
                urlConnection.disconnect();
            }
            catch (NullPointerException except)
            {
                urlConnection = null;
            }
        }

        returnResult = returnStringBuilder.toString();

        return null;
    }




    @Override
    protected void onPostExecute(Void aVoid)
    {
        //Calls back to the other module with the resulting string
        delegate.processFinish(returnResult);
    }

}