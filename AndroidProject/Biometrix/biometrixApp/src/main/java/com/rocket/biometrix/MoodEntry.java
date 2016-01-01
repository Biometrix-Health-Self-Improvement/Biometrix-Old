package com.rocket.biometrix;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MoodEntry extends AppCompatActivity {
    final static String _dateFormat = "EEE, MM/dd/yyyy";
    final static String _timeFormat = "h:mm a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_creat_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDatetimeOnPage();
        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        setDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));
        setTime(current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE));
        setRatingBarListener();
    }

    //Sets the default date and time values on the activity and sets the onClickListener for the time and date views
    private void setDatetimeOnPage(){
        TextView textDate = (TextView)findViewById(R.id.moodCreateEntryDateSelect);
        TextView textTime = (TextView)findViewById(R.id.moodCreateEntryTimeSelect);

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance(TimeZone.getDefault());
                int mYear = current.get(Calendar.YEAR);
                int mMonth = current.get(Calendar.MONTH);
                int mDay = current.get(Calendar.DAY_OF_MONTH);

                //creates the date picker and sets the listener to call setDate whenever the date is changed
                DatePickerDialog mDatePicker = new DatePickerDialog(MoodEntry.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        setDate(year, month, day);
                    }
                }, mYear, mMonth, mDay); //mYear, mMonth, and mDay are default values to strt the date picker on, which are set to current
                mDatePicker.setTitle("Select Date");
                mDatePicker.show(); //shows the date picker that was just create
            }
        });

        textTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar current = Calendar.getInstance(TimeZone.getDefault());
                int mHour = current.get(Calendar.HOUR_OF_DAY);
                int mMinute = current.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(MoodEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timepicker, int hourOfDay, int minute) {
                        setTime(hourOfDay, minute);
                    }
                }, mHour, mMinute, false);
                time.setTitle("Select Time");
                time.show();
            }
        });
    }
    private void setDate(int year, int month, int day) {
        TextView textDate = (TextView) findViewById(R.id.moodCreateEntryDateSelect);
        //TODO: Check to make sure date is not in the future, if it is, display error and do not change date

        Calendar c = new GregorianCalendar(year, month, day);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_dateFormat).format(c.getTime());
            textDate.setText("Date: " + str);
        }
    }
    private void setTime(int hour, int minute){
        TextView textTime = (TextView)findViewById(R.id.moodCreateEntryTimeSelect);

        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        Calendar c = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH),current.get(Calendar.DAY_OF_MONTH),
                hour, minute);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_timeFormat).format(c.getTime());
            textTime.setText("Time: " + str);
        }
    }

    private void setRatingBarListener(){
        //Depression
        SeekBar rating = (SeekBar) findViewById(R.id.moodDepressedRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) findViewById(R.id.moodDepressedDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });

        //Elevated
        rating = (SeekBar) findViewById(R.id.moodElevatedRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) findViewById(R.id.moodElevatedDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });

        //Irritability
        rating = (SeekBar) findViewById(R.id.moodIrritabilityRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) findViewById(R.id.moodIrritabilityDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });

        //Anxiety
        rating = (SeekBar) findViewById(R.id.moodAnxietyRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) findViewById(R.id.moodAnxietyDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });
    }

    private void setRatingLabel(TextView desc, int prog){
        String str = null;
        switch (prog) { //get string based on rating
            case 0: //none
                str = getResources().getString(R.string.mood_rating_none);
                break;
            case 1: //mild
                str = getResources().getString(R.string.mood_rating_mild);
                break;
            case 2: //moderate
                str = getResources().getString(R.string.mood_rating_mod);
                break;
            case 3: //severe
                str = getResources().getString(R.string.mood_rating_sev);
                break;
            case 4: //very severe
                str = getResources().getString(R.string.mood_rating_vsev);
                break;
        }
        desc.setText(str);
    }
}