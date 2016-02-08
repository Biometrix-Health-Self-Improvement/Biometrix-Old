package com.rocket.biometrix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The activity to retrieve a user's login information (username and password) and verify it against
 * the database. It implements AsyncResponse to allow a response from an activity that is called
 * asynchronously, OnConnectionFailedListener to allow a message to be displayed if Google Services
 * cannot be contacted, and
 */
public class GetLoginActivity extends AppCompatActivity implements AsyncResponse
{
    //One string for each of the possible fields
    private String returnResult;
    private String username;
    private String password;
    private String email;



    @Override
    /**
     * Initializes the starting state for the activity. Also, prepares google account validation
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        //Sets the content for the page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = null;
        password = null;
        returnResult = "";

        //Since the action bar has the possibility of throwing nullpointer exception, catch it and
        //do nothing with it
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(java.lang.NullPointerException except)
        {
            //Do nothing
        }

    }


    /**
     * Calls the database to check the login for the user
     * @param view
     */
    public void okayButtonClick(View view)
    {
        EditText usernameEdit =  (EditText) findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) findViewById(R.id.passwordEditText);
        password = passwordEdit.getText().toString();

        if (username.equals("") || password.equals("") )
        {
            Toast.makeText(getApplicationContext(), "Username or password is blank", Toast.LENGTH_LONG).show();
        }
        else
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_CHECK,username, password);
        }
    }

    /**
     * Calls the database to check the login for the user
     * @param view
     */
    public void resetButtonClick(View view)
    {
        EditText usernameEdit =  (EditText) findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText emailEdit = (EditText) findViewById(R.id.loginEnterEmailEditText);
        email = emailEdit.getText().toString();

        if (username.equals("") || email.equals("") )
        {
            Toast.makeText(getApplicationContext(), "Username or email is blank, both are required to identify you", Toast.LENGTH_LONG).show();
        }
        else
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_RESET, username, email);
        }

    }

    public void cancelButtonClick(View view)
    {
        this.onBackPressed();
    }

    @Override
    /**
     * Retrieves the results of the call to the webserver
     */
    public void processFinish(String result)
    {
        returnResult = result;

        JSONObject jsonObject;

        //Tries to parse the returned result as a json object.
        try
        {
            jsonObject = new JSONObject(returnResult);
        }
        catch (JSONException jsonExcept)
        {
            jsonObject = null;
        }

        //If the return could not be parsed, then it was not a successful login
        if (jsonObject == null)
        {
            Toast.makeText(getApplicationContext(), returnResult, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                //If the operation succeeded
                if ((Boolean)jsonObject.get("Verified") )
                {
                    //If the json object passes back a token then it was a login
                    if ( jsonObject.has("Token"))
                    {
                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();

                        //Logs the user in with their login token.
                        LocalAccount.Login(username, jsonObject.getString("Token"));

                        /*//Create's an "intent" to passback user information with keys username and password.
                        Intent dataPassback = new Intent();
                        dataPassback.putExtra("username", username);
                        dataPassback.putExtra("password", password);*/

                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                    //Assume it was a password reset
                    {
                        Toast.makeText(getApplicationContext(), "Check your email (and your spam folder) for your reset link", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException jsonExcept)
            {
                Toast.makeText(getApplicationContext(), "Something went wrong with the server's return", Toast.LENGTH_LONG).show();
            }
        }

    }
}
