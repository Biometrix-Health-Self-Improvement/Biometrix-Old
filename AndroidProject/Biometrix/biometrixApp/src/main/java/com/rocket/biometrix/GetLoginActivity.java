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

public class GetLoginActivity extends AppCompatActivity implements AsyncResponse {

    private Boolean loginSuccessful;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = null;
        password = null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginSuccessful = false;
    }


    public void okayButtonClick(View view)
    {
        EditText usernameEdit =  (EditText) findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) findViewById(R.id.passwordEditText);
        password = passwordEdit.getText().toString();

        //loginSuccessful = tryLogin(username, password);

        new DatabaseLoginConnect(this).execute(DatabaseConnectionTypes.LOGIN_CHECK,username, password);

    }

    public void cancelButtonClick(View view)
    {
        this.onBackPressed();
    }

    @Override
    public void processFinish(Object result)
    {
        try
        {
            loginSuccessful = (Boolean) result;
        }
        catch (Exception e)
        {
            loginSuccessful = false;
        }

        if (loginSuccessful)
        {
            Toast.makeText(getApplicationContext(), "Login Succeeded", Toast.LENGTH_LONG).show();

            //Create's an "intent" to passback user information with keys username and password.
            Intent dataPassback = new Intent();
            dataPassback.putExtra("username", username);
            dataPassback.putExtra("password", password);

            setResult(RESULT_OK, dataPassback);
            finish();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Login Failed =(", Toast.LENGTH_LONG).show();
        }

    }
}
