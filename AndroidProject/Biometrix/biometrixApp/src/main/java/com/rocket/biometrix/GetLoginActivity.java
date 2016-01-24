package com.rocket.biometrix;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class GetLoginActivity extends AppCompatActivity implements AsyncResponse {

    private String returnResult;

    private String username;
    private String password;
    private String email;

    @Override
    /**
     * Initializes various things for the activity
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = null;
        password = null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        returnResult = "";
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
            Toast.makeText(getApplicationContext(), "Username or password is blank", Toast.LENGTH_LONG);
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
                    //If the json object passes back an email address, that means that it was a reset, not a login
                    if ( jsonObject.get("EmailAddress").equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "User created!", Toast.LENGTH_LONG).show();

                        //Create's an "intent" to passback user information with keys username and password.
                        Intent dataPassback = new Intent();
                        dataPassback.putExtra("username", username);
                        dataPassback.putExtra("password", password);

                        setResult(RESULT_OK, dataPassback);
                        finish();
                    }
                    else
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
