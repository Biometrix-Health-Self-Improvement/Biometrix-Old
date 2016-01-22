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

public class CreateLoginActivity extends AppCompatActivity implements AsyncResponse {

    private String returnResult;

    private String username;
    private String password;
    private String confirmedPassword;

    @Override
    /**
     * Initilizes various portions of the activity
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = null;
        password = null;
        confirmedPassword = null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        returnResult = "";
    }


    /**
     * Makes a request for the webserver to create a new user if the username and passwords match
     * @param view
     */
    public void okayButtonClick(View view)
    {
        //Gets username and passwords from the edit Text boxes
        EditText usernameEdit =  (EditText) findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) findViewById(R.id.passwordEditText);
        password = passwordEdit.getText().toString();

        EditText passwordConfirmEdit = (EditText) findViewById(R.id.confirmPasswordEditText);
        confirmedPassword = passwordConfirmEdit.getText().toString();

        //Calls the database connection if the passwords match
        if (password.equals(confirmedPassword))
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_CREATE,username, password);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelButtonClick(View view)
    {
        this.onBackPressed();
    }

    @Override
    /**
     * Retrieves the results of calling the webserver.
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

        //If the return could not be parsed, then it was not a successful addition
        if (jsonObject == null)
        {
            Toast.makeText(getApplicationContext(), returnResult, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                if ((Boolean)jsonObject.get("Verified") == true) {
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

