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


        //Done click event saves entered data to string array
        //Bundles string array for transport across activities
        //Saves entry to SQLlite DB using LocalStorageAccess
        //And, Adds this exercise to the 'plan' if it needs to be added
        //Lastly, it closes up the entry activity.
        Button ExerciseEntryDone = (Button) findViewById(R.id.ex_b_done);
        ExerciseEntryDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Filling a string that holds title
                String titleString = GetStringFromEditText(R.id.ex_title);

                //Filling date and time strings for bundle's string array
                String dateString = dateTV.getText().toString();
                String timeString = timeTV.getText().toString();

                //Cleaning date and time strings (I know what you're thinking, why not test the buffer to determine where the 1st number occurs to avoid using magic numbers? Not worth it is my opinion.)
                dateString = removeChars(dateString, 12); //Date:_Fri,__ = 12 characters.
                timeString = removeChars(timeString, 7); //Time:__ = 7 characters

                //Filling reps string
                String repsString = GetStringFromEditText(R.id.ex_et_reps);

                //Filling weight string from its editText found @content_exercise_entry.xml
                String weightString = GetStringFromEditText(R.id.ex_et_weight);

                //Filling notes string
                String notesString = GetStringFromEditText(R.id.ex_notes);

                //Make string array to hold all the strings extracted from the user's input on this entry activity
                String[] exerciseEntryData = {titleString,dateString,timeString,minSelected,typeSelected,notesString};

                //Put string array that has all the entries data points in it into a Bundle. This bundle is for future extensibility it is NOT for the parent class.
                Bundle exerciseEntryBundle = new Bundle();
                exerciseEntryBundle.putStringArray("exEntBundKey",exerciseEntryData);


                //TODO:Recieve intent extras from parent. Change the old String array to the newly filled string array; then put extra BACK IN and set result to OK w/ error checking.
                Bundle parentExtras = getIntent().getExtras();
                String[] usersEntryData = parentExtras.getStringArray("parentArray");


                //Here is where the parent bundle could be used (to auto-populate an entry for editing for example) For now I will only set the parent's string array equal to exerciseEntryData.
                usersEntryData = exerciseEntryData;

//                Intent backtoParent = getIntent();
//
//                String msg = backtoParent.getStringExtra("key");
//                if (msg.contentEquals("key")) {
//                    backtoParent.putExtra("widthInfo", s);
//                    setResult(RESULT_OK, backtoParent);
//                    finish();
//                }

                //TODO: SQLite calls to LocalStorageAccess which will have to be made more abstract first since it's hardcoded now.

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

    //Function given an <<EditText>> resource ID (R.id.ex_et_weight) will return its text contents as a string.
    //Soft error handling will just mess up the returned string if you gave a bad id, not crash the app.
    public String GetStringFromEditText(int id)
    {
        String endResult = "ERROR in GetStringFromEditText: Resource ID does not exist";
        //0 is always an invalid resource. And if a view can't be found by its ID, findViewById returns null
        //http://developer.android.com/reference/android/content/res/Resources.html#getIdentifier%28java.lang.String,%20java.lang.String,%20java.lang.String%29
        //http://developer.android.com/reference/android/app/Activity.html#findViewById%28int%29
        if (id != 0) {
            if (findViewById(id) != null)
            try {
                final EditText et = (EditText) findViewById(id);
                endResult = et.getText().toString();
            }//end try
            catch (IllegalArgumentException |  ClassCastException exceptionName) {
                endResult = "ERROR in GetStringFromEditText: try block";
            }
        }
        return endResult;
    }


    //String cleaner, just removes a number of characters from the front of the string.
    //http://docs.oracle.com/javase/7/docs/api/java/lang/StringBuffer.html#delete%28int,%20int%29
    private static String removeChars(String s, int num) {
        StringBuffer buf = new StringBuffer(s);
        int front = 0;
        //Simple error checking. To avoid exception below (this is just a wrapper function around String Buffer's delete() function)
        //StringIndexOutOfBoundsException - if start is negative, greater than length(), or greater than end.
       if (num < s.length()) {
           buf.delete(front,num-1);
       }
        return buf.toString();
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
