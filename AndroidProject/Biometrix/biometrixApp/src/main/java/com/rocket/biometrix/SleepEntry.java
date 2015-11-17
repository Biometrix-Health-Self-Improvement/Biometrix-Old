package com.rocket.biometrix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SleepEntry extends AppCompatActivity {

    private String sleepDate;
    private String sleepTime;

    private TextView endDateTextView;
    private SeekBar hourSeekBar;
    private SeekBar minuteSeekBar;

    public static final int SLEEP_GETDATE_INT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_entry);

        sleepDate = null;
        sleepTime = null;
        endDateTextView = null;
    }

    public void getSleepDateClick(View view)
    {
        Intent Launch_Mood = new Intent(SleepEntry.this, GetDateTime.class);
        startActivityForResult(Launch_Mood, SLEEP_GETDATE_INT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case (SLEEP_GETDATE_INT) :
            {
                if (resultCode == Activity.RESULT_OK) {
                    sleepDate = data.getStringExtra("date");
                    sleepTime = data.getStringExtra("time");

                    TextView startDateView = (TextView) findViewById(R.id.sleepStartTextView);

                    String sleepString = getResources().getString(R.string.sleep_start_base) + sleepTime + " on " + sleepDate;

                    startDateView.setText(sleepString);

                    UpdateEndDateTime();
                }
            }
        }
    }


    protected void UpdateEndDateTime()
    {
        if (endDateTextView == null)
        {
            endDateTextView = (TextView) findViewById(R.id.sleepEndTimeTextView);
        }
        if (hourSeekBar == null)
        {
            hourSeekBar = (SeekBar) findViewById(R.id.sleepHoursSeekBar);

            hourSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    UpdateEndDateTime();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }

        if (minuteSeekBar == null)
        {
            minuteSeekBar = (SeekBar) findViewById(R.id.sleepMinutesSeekBar);

            minuteSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser)
                {
                    UpdateEndDateTime();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }

        if (sleepTime != null)
        {
            String[] splitTime;
            splitTime = sleepTime.split(":");

            if (splitTime.length == 3)
            {
                Integer hours = Integer.parseInt(splitTime[0]);
                Integer minutes = Integer.parseInt(splitTime[1]);

                hours = hours + hourSeekBar.getProgress();
                minutes = minutes + minuteSeekBar.getProgress();

                if (minutes > 59)
                {
                    minutes = minutes - 60;
                    hours = hours + 1;
                }

                if (hours > 23)
                {
                    hours = hours - 24;
                }

                String hourString;

                if (hours < 10)
                {
                    hourString = "0" + hours.toString();
                }
                else
                {
                    hourString = hours.toString();
                }

                String minuteString;

                if (minutes < 10)
                {
                    minuteString = "0" + minutes.toString();
                }
                else
                {
                    minuteString = minutes.toString();
                }

                String endTime = hourString + ":" + minuteString + ":" + splitTime[2];

                String sleepEndString = getResources().getString(R.string.sleep_end_base) + endTime;
                endDateTextView.setText(sleepEndString);
            }
        }

    }

    public void onDoneClick(View view)
    {


        Intent dataPassback = new Intent();

        setResult(RESULT_OK, dataPassback);
        finish();
    }
}
