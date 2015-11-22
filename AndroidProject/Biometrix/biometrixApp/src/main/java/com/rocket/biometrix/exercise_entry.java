package com.rocket.biometrix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class exercise_entry extends AppCompatActivity {

    Spinner minuteSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_entry);
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


        //@Filling contents of Minutes: Slide down menu
        minuteSpinner = (Spinner) findViewById(R.id.ex_min_spinner);

        ArrayAdapter minSpin = ArrayAdapter.createFromResource(
                this, R.array.ex_min_array,android.R.layout.simple_spinner_item);

        minuteSpinner.setAdapter(minSpin);

    }//END onCreate()

}
