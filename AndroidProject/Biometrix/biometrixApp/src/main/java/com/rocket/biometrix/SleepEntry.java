package com.rocket.biometrix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class SleepEntry extends AppCompatActivity {

    private TextView endDateTextView; //Reference for the end date textview so it only is grabbed once
    private SeekBar hourSeekBar;      //Reference for the hour seek bar so it only is grabbed once
    private SeekBar minuteSeekBar;    //Reference for the minute seek bar so it only is grabbed once

    private TextView startDateTextView; //Reference so it only is grabbed once
    private TextView startTimeTextView; //''
    private TextView sleptTimeTextView; //''

    private SeekBar qualitySeekBar;     //''
    private TextView qualityNumberTextView; //''

    private Spinner generalHealthSpinner;

    private TextView noteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_entry);

        //Moves this to another function to clean up onCreate
        GetViewReferences();

        //Sets a default of 7 hours as this should be fairly average.
        hourSeekBar.setProgress(7);
        minuteSeekBar.setProgress(0);

        //Moves listener setups to another function to avoid clutter
        SetupListeners();

        qualitySeekBar.setMax(SleepData.maxQuality);

        UpdateEndTimes();
    }


    /**
     * Obtains references to the views for this activity and stores them in class variables
     */
    protected void GetViewReferences()
    {
        endDateTextView = (TextView) findViewById(R.id.sleepEndTimeTextView);
        hourSeekBar = (SeekBar) findViewById(R.id.sleepHoursSeekBar);
        minuteSeekBar = (SeekBar) findViewById(R.id.sleepMinutesSeekBar);
        startDateTextView = (TextView) findViewById(R.id.sleepStartDateTextView);
        startTimeTextView = (TextView) findViewById(R.id.sleepStartTimeTextView);
        sleptTimeTextView = (TextView) findViewById(R.id.sleepTimeSleptTextView);
        qualitySeekBar = (SeekBar) findViewById(R.id.sleepQualitySeekBar);
        qualityNumberTextView = (TextView) findViewById(R.id.sleepQualityNumberTextView);
        generalHealthSpinner = (Spinner) findViewById(R.id.sleepGeneralHealthSpinner);
        noteTextView = (TextView) findViewById(R.id.sleepNotesEditText);
    }

    /**
     * Sets up the listeners for the view objects
     */
    protected void SetupListeners()
    {
        //Sets up the date and time fields to work with the activity that grabs them.
        DateTimePopulateTextView timeSelectSetup = new DateTimePopulateTextView(SleepEntry.this, R.id.sleepStartDateTextView, R.id.sleepStartTimeTextView);
        timeSelectSetup.Populate();

        //Sets up the listeners so the seek bars call updates when changed.
        hourSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UpdateEndTimes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Call updates when minute seekbar is changed.
        minuteSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UpdateEndTimes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Updates the text box to be the same as the seekbar
        qualitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //Ensures that a quality too low cannot be given
                if(progress < SleepData.minQuality)
                {
                    qualitySeekBar.setProgress(SleepData.minQuality);
                }

                qualityNumberTextView.setText(Integer.toString(qualitySeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Set's progress to a default of half for the quality of sleep.
        qualitySeekBar.setProgress(5);

        //Makes sure the times are updated if the original date is updated.
        startDateTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdateEndTimes();
            }
        });

        //Makes sure the times are updated if the original time is updated.
        startTimeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdateEndTimes();
            }
        });

        //Following code was adapted from JP's exercise entry code..
        //Array adapter from sleep string resources
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sleep_gen_health_array,android.R.layout.simple_spinner_item);

        generalHealthSpinner.setAdapter(spinnerAdapter);

        //Listener for selected minute taps and getting the tapped minutes as strings.
        generalHealthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            protected Adapter initializedAdapter = null;

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if (initializedAdapter != parentView.getAdapter())
                {
                    initializedAdapter = parentView.getAdapter();
                    return;
                }

                String selected = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // default to 5 min
            }
        });
    }

    /**
     * Updates the times to match the addition of the current time and entered time, as well as updates
     * the time spent.
     */
    public void UpdateEndTimes()
    {
        //Grabs initial time and date
        String dateText = startDateTextView.getText().toString();
        String timeText = startTimeTextView.getText().toString();

        //Grabs seekbar input
        Integer enteredHours = hourSeekBar.getProgress();
        Integer enteredMinutes = minuteSeekBar.getProgress();

        //Builds the display string for the time slept
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getResources().getString(R.string.sleep_time_slept_base));
        stringBuilder.append(enteredHours.toString());
        stringBuilder.append(":");

        if (enteredMinutes < 10)
        {
            stringBuilder.append("0");
        }
        stringBuilder.append(enteredMinutes.toString());
        sleptTimeTextView.setText(stringBuilder);

        //Determines AM or PM
        boolean pmTime = timeText.contains("PM");

        //Pulls out the month, day, and year information of the string into an array
        dateText = dateText.substring(dateText.indexOf(",") + 1);
        String[] splitDate;
        splitDate = dateText.split("/");

        //Pulls out the hour and minute information
        timeText = timeText.substring(timeText.indexOf(":") + 2);
        String[] splitTime;
        splitTime = timeText.split(" ")[0].split(":");

        //Does not continue of there were an incorrect number of elements parsed.
        if ((splitDate.length == 3) && (splitTime.length == 2))
        {
            //Parses date
            Integer month = Integer.parseInt(splitDate[0].trim());
            Integer day = Integer.parseInt(splitDate[1].trim());
            Integer year = Integer.parseInt(splitDate[2].trim());

            //Parses time
            Integer hour = Integer.parseInt(splitTime[0].trim());
            Integer minute = Integer.parseInt(splitTime[1].trim());

            //12 extra hours for PM selected
            if (pmTime)
            {
                hour = hour + 12;
            }

            //Creates a calendar and then adds time to it based on the sliders
            Calendar endCalendar = new GregorianCalendar(year, month, day, hour, minute);
            endCalendar.add(Calendar.HOUR, enteredHours);
            endCalendar.add(Calendar.MINUTE, enteredMinutes);

            //Creates a format for date and time together
            SimpleDateFormat endTimeFormat = new SimpleDateFormat(DateTimePopulateTextView._timeFormat + " " + DateTimePopulateTextView._dateFormat);


            String sleepEndString = getResources().getString(R.string.sleep_end_time_base) + endTimeFormat.format(endCalendar.getTime());
            endDateTextView.setText(sleepEndString);
        }
    }

    /**
     * Stores the information that was gathered if it is valid and then closes the activity.
     * @param view The button that made the call to exit the activity
     */
    public void onDoneClick(View view)
    {
        LocalStorageAccess fileAccess = new LocalStorageAccess(this, null, null, 1);

        String dateText = startDateTextView.getText().toString();
        String timeText = startTimeTextView.getText().toString();
        String duration = sleptTimeTextView.getText().toString();

        dateText = dateText.substring(dateText.indexOf(",") + 1).trim();
        timeText = timeText.substring(timeText.indexOf(":") + 2).trim();
        duration = duration.substring(duration.indexOf(":") + 2).trim();

        int quality = qualitySeekBar.getProgress();

        String notes = noteTextView.getText().toString();
        String status = generalHealthSpinner.getSelectedItem().toString();


        SleepData sleepData = new SleepData(dateText + " " + timeText, duration, quality, status, notes);

        fileAccess.AddSleepEntry(sleepData);

        finish();
    }
}
