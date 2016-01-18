package com.rocket.biometrix;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExerciseEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static TextView timeTV; //Used by the DateTimePopulateTextView in the onCreate event
    public static TextView dateTV;

    String minSelected; //string to save minutes exercised spinner result
    String typeSelected; //string to save type of exercise selected in the radio 'bubble' buttons

    Spinner minuteSpinner;
    boolean toasted = false; //Used to display encouraging messages ONCE in minuteSpinner.
    //To avoid 'hard coded' strings...These are implemented in the minuteSpinners listener in the onCreate event
    String lowestSpinnerValueThreshold = "5"; //5 minutes
    String lowSpinnerValueThreshold = "10"; //10 minutes (idea is to encourage user to exercise more but still celebrate their 'baby' gains)
    String lowSpinnerMessage = "Keep it up :)"; //The encouraging message
    String highSpinnerMessage = "Nice!"; //The BEST message users strive for

    String [] exerciseEntryData = {}; //String array that will store all user entered data, used in bundles and SQLite insert


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
                this, R.array.ex_min_array, android.R.layout.simple_spinner_item);

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
                minSelected = parentView.getItemAtPosition(position).toString();

                if (minSelected.equals(lowestSpinnerValueThreshold) || minSelected.equals(lowSpinnerValueThreshold)) {
                    if (!toasted) {
                        Toast.makeText(getApplicationContext(), lowSpinnerMessage, Toast.LENGTH_LONG).show();
                        toasted = true;
                    }
                } else {
                    if (!toasted) {
                        Toast.makeText(getApplicationContext(), highSpinnerMessage, Toast.LENGTH_LONG).show();
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
                        typeSelected = ETa1.getText().toString();
                        break;

                    case R.id.ex_rb_b:
                        final RadioButton ETa2 = (RadioButton) findViewById(R.id.ex_rb_b);
                        typeSelected = ETa2.getText().toString();
                        break;

                    case R.id.ex_rb_c:
                        final RadioButton ETa3 = (RadioButton) findViewById(R.id.ex_rb_c);
                        typeSelected = ETa3.getText().toString();
                        break;

                    case R.id.ex_rb_d:
                        final RadioButton ETa4 = (RadioButton) findViewById(R.id.ex_rb_d);
                        typeSelected = ETa4.getText().toString();
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


        /**Done click event saves entered data to string array
        *Bundles string array for transport across activities
         * Receives bundle from parent, changes the data inside and sends it back to parent with error checking
        *Saves entry to SQLlite DB using LocalStorageAccess
        *And, Adds this exercise to the 'plan' if it needs to be added
        *Lastly, it closes up the entry activity with finish() which will activate the onActivityResult() in ExerciseParent.
         * */
        Button ExerciseEntryDone = (Button) findViewById(R.id.ex_b_done);
        ExerciseEntryDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Filling a string that holds title
                String titleString = ModuleStringHelper.GetStringFromEditText(R.id.ex_title, ExerciseEntry.this);

                //Filling date and time strings for bundle's string array
                String dateString = dateTV.getText().toString();
                String timeString = timeTV.getText().toString();

                //Cleaning date and time strings with helper class
                dateString = ModuleStringHelper.fixDate(dateString);
                timeString = ModuleStringHelper.fixTime(timeString);

                //Filling reps/laps string
                String repsString = ModuleStringHelper.GetStringFromEditText(R.id.ex_et_reps, ExerciseEntry.this);

                //Filling weight/intensity string from its editText found @content_exercise_entry.xml
                String weightString = ModuleStringHelper.GetStringFromEditText(R.id.ex_et_weight, ExerciseEntry.this);

                //Filling notes string
                String notesString = ModuleStringHelper.GetStringFromEditText(R.id.ex_notes, ExerciseEntry.this);

                //Make string array to hold all the strings extracted from the user's input on this entry activity
                //{TITLE, TYPE, MINUTES, REPS, LAPS, WEIGHT, INTY, NOTES, DATE, TIME}; //No distinction between reps and laps, weight and intensity.
                exerciseEntryData = new String[] {titleString, typeSelected, minSelected, repsString, repsString, weightString, weightString, notesString, dateString, timeString};

                //https://developer.android.com/reference/android/os/Bundle.html
                //Put string array that has all the entries data points in it into a Bundle. This bundle is for future extensibility it is NOT for the parent class.
                Bundle exerciseEntryBundle = new Bundle();
                exerciseEntryBundle.putStringArray("exEntBundKey", exerciseEntryData);


                //Getting PARENT bundle.
                Bundle parentExtras = getIntent().getExtras();
                String[] usersEntryData = parentExtras.getStringArray("parentArray"); //Don't worry this is supposed to be redundant for extendability.
                //Here is where the parent bundle could be used (to auto-populate an entry for editing for example) For now I will only set the parent's string array equal to exerciseEntryData.
                usersEntryData = exerciseEntryData;

                //Get PARENT intent for setResult()
                Intent backtoParent = getIntent();

                //Another bundle specifically for giving data and results back to the PARENT
                Bundle backtoParentBund = new Bundle();
                backtoParentBund.putStringArray("childKey", usersEntryData);


                backtoParent.putExtra("childKey", backtoParentBund);
                //HERE is where extensive error checking could be done on the user's entry (IE if it is all blank don't save) but for now... if the key matched say it went OK
                setResult(RESULT_OK, backtoParent);


                Context context = ExerciseEntry.this;
                //Pull keys from LSA Exercise
                LocalStorageAccessExercise dbEx = new LocalStorageAccessExercise(context);

                //You don't have to keep strings in the same order across classes, I just did to make the code easier.
                //{TITLE, TYPE, MINUTES, REPS, LAPS, WEIGHT, INTY, NOTES, DATE, TIME};
                String[] columnNames = dbEx.getColumns();

                //Making sure I have data for each column (even if null or empty, note that this is NOT required, you can insert columns individually if you wish.) @see putNull
                if (columnNames.length == exerciseEntryData.length) {
                    ContentValues rowToBeInserted = new ContentValues();
                    int dataIndex = 0;
                    for (String column : columnNames) {
                        //Insert column name ripped from LSA child class, and the user's entry data we gathered above
                        rowToBeInserted.put(column, exerciseEntryData[dataIndex]);
                        dataIndex++;
                    }
                    //Call insert method
                    dbEx.insertFromContentValues(rowToBeInserted);
                }
                //TODO: CODE BROKEN HERE
               // dbEx.selectALLasStrings(dbEx.getTableName());
                //TODO: No such table "Exercise"
                Cursor result = dbEx.getAllDepts();
                result.moveToFirst();
                do {
                    result.getString(0);
                } while (result.moveToNext());
                result.close();
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

}
