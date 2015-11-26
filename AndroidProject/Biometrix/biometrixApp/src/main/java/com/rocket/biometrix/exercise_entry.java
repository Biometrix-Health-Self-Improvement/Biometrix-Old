package com.rocket.biometrix;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class exercise_entry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner minuteSpinner;
    boolean toasted = false; //Used to display encouraging messages ONCE in minuteSpinner.

    public static TextView timeTV;
    public static TextView dateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //@Filling contents of Minutes: Slide down menu
        minuteSpinner = (Spinner) findViewById(R.id.ex_min_spinner);
        //Array adapter from exer_strings resource
        ArrayAdapter minSpin = ArrayAdapter.createFromResource(
                this, R.array.ex_min_array,android.R.layout.simple_spinner_item);

        minuteSpinner.setAdapter(minSpin);

        //Listener for selected minute taps and getting the tapped minutes as strings.
        minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (initializedAdapter != parentView.getAdapter()) {
                    initializedAdapter = parentView.getAdapter();
                    return;
                }

                String selected = parentView.getItemAtPosition(position).toString();

                if (selected.equals("5") || selected.equals("10")) {
                    if (!toasted) {
                        Toast.makeText(getApplicationContext(), "Keep it up :)", Toast.LENGTH_LONG).show();
                        toasted = true;
                    }
                } else {
                    if (!toasted) {
                        Toast.makeText(getApplicationContext(), "Nice!", Toast.LENGTH_LONG).show();
                        toasted = true;
                    }
                }

//                textQualification=selected;
//                SearchUpdated("Qualification");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // default to 5 min
            }
        });

        //@Corrections for 'next' ime button //I'm too stupid to figure out how to use.
//        TextView nextField = (TextView)currentField.focusSearch(View.FOCUS_RIGHT);
//        nextField.requestFocus();

        //Linking contexts likes non-null variables.
        timeTV = (TextView) findViewById(R.id.ex_tv_time);
        dateTV = (TextView) findViewById(R.id.ex_tv_date);

        //Slick calls to fill date and time textviews.
        DateTimePopulateTextView DTPOWAH = new DateTimePopulateTextView(exercise_entry.this, R.id.ex_tv_date, R.id.ex_tv_time);
        DTPOWAH.Populate();

    }//END onCreate()

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //To please the cruel mistress Android studio
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //To please the cruel mistress Android Studio...
        //O! Foul bits! My tongue will tell the anger of my heart,
        // or else my heart concealing it will break.
    }

    //Getters and Setters NEVER USED

    //Setter for time TextView
public void setTimeText (String time){
    timeTV = (TextView)findViewById(R.id.ex_tv_time);
    timeTV.setText(time);
}
    //Setter for date TextView
    public void setDateText (String date){
        TextView dateTV = (TextView)findViewById(R.id.ex_tv_date);
        dateTV.setText(date);
    }

    //Getter for time TextView
    public TextView getTimeText (){
        TextView timeTV = (TextView)findViewById(R.id.ex_tv_time);
        return timeTV;
    }
    //Getter for date TextView
    public TextView getDateText () {
        TextView dateTV = (TextView) findViewById(R.id.ex_tv_date);
        return dateTV;
    }

}
