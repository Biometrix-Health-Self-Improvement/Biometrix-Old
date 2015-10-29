package com.rocket.biometrix;

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




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginSuccessful = false;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void okayButtonClick(View view)
    {
        String username = null;
        String password = null;

        EditText usernameEdit =  (EditText) findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) findViewById(R.id.passwordEditText);
        password = passwordEdit.getText().toString();

        //loginSuccessful = tryLogin(username, password);

        new DatabaseLoginConnect(this).execute(username, password);
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
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Login Failed =(", Toast.LENGTH_LONG).show();
        }

    }
}
