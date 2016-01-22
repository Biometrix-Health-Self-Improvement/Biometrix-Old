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

        new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_CHECK,username, password);

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
                if ((Boolean)jsonObject.get("Verified") == true)
                {
                    Toast.makeText(getApplicationContext(), "Login Succeeded!", Toast.LENGTH_LONG).show();

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
