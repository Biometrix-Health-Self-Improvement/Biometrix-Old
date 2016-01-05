package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExerciseEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner minuteSpinner;
    boolean toasted = false; //Used to display encouraging messages ONCE in minuteSpinner.

    public static TextView timeTV; //Used by the DateTimePopulateTextView in the onCreate event
    public static TextView dateTV;

    String min_selected; //string to save minutes exercised spinner result
    String type_selected; //string to save type of exercise selected in the radio 'bubble' buttons


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
                //Set string
                min_selected = parentView.getItemAtPosition(position).toString();

                if (min_selected.equals("5") || min_selected.equals("10")) {
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

        //Group of exercise types in the right corner, like "cardio"
        RadioGroup rg = (RadioGroup) findViewById(R.id.ex_radioGroup);
        //When a bubble is poked, update a string to match the bubble poked.
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ex_rb_a:
                        //Extract CharSequences from UI and convert them to string array
                        final RadioButton ETa1 = (RadioButton) findViewById(R.id.ex_rb_a);
                        type_selected = ETa1.getText().toString();
                        break;

                    case R.id.ex_rb_b:
                        final RadioButton ETa2 = (RadioButton) findViewById(R.id.ex_rb_b);
                        type_selected = ETa2.getText().toString();
                        break;

                    case R.id.ex_rb_c:
                        final RadioButton ETa3 = (RadioButton) findViewById(R.id.ex_rb_c);
                        type_selected = ETa3.getText().toString();
                        break;

                    case R.id.ex_rb_d:
                        final RadioButton ETa4 = (RadioButton) findViewById(R.id.ex_rb_d);
                        type_selected = ETa4.getText().toString();
                        break;

                }


            }
        });

        //Linking contexts likes non-null variables.
        timeTV = (TextView) findViewById(R.id.ex_tv_time);
        dateTV = (TextView) findViewById(R.id.ex_tv_date);

        //Slick calls to fill date and time textviews.
        DateTimePopulateTextView DTPOWAH = new DateTimePopulateTextView(ExerciseEntry.this, R.id.ex_tv_date, R.id.ex_tv_time);
        DTPOWAH.Populate(); //Change the text


        //Done click event saves entered data to string array
        //Bundles string array for transport across activities
        //Saves entry to SQLlite DB using LocalStorageAccess
        //And, Adds this exercise to the 'plan' if it needs to be added
        //Lastly, it closes up the entry activity.
        Button addNewEntry = (Button) findViewById(R.id.ex_b_done);
        addNewEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                //Kill this thread, User will still have exercise main page open.
                finish();
            }
        }); //end addNewEntry on click listener

    }//END onCreate()

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //To please the cruel mistress Android studio
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //To please the cruel mistress Android Studio...
    }

    //Getters and Setters NOT USED CURRENTLY

    //Setter for time TextView
//public void setTimeText (String time){
//    timeTV = (TextView)findViewById(R.id.ex_tv_time);
//    timeTV.setText(time);
//}
//    //Setter for date TextView
//    public void setDateText (String date){
//        TextView dateTV = (TextView)findViewById(R.id.ex_tv_date);
//        dateTV.setText(date);
//    }
//
//    //Getter for time TextView
//    public TextView getTimeText (){
//        TextView timeTV = (TextView)findViewById(R.id.ex_tv_time);
//        return timeTV;
//    }
//    //Getter for date TextView
//    public TextView getDateText () {
//        TextView dateTV = (TextView) findViewById(R.id.ex_tv_date);
//        return dateTV;
//    }

}
