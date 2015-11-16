package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ExerciseParent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Button addNewEntry = (Button) findViewById(R.id.exNewEntry);
        addNewEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start new intent (Parent activity start with capital, child don't have capital)
                Intent LaunchNewEntry = new Intent(ExerciseParent.this, exercise_EntryFS.class);
                //start new activity
                startActivity(LaunchNewEntry);
                //finish();
            }
        }); //end addNewEntry on click listener

    } //end OnCreate of ExerciseParent
}
