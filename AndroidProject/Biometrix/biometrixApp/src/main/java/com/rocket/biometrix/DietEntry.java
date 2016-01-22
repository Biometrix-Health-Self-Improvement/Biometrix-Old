package com.rocket.biometrix;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DietEntry extends AppCompatActivity {
    final static String _dateFormat = "EEE, MM/dd/yyyy";
    final static String _timeFormat = "h:mm a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_create_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDatetimeOnPage();
        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        setDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));
        setTime(current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE));
    }

    //Sets the default date and time values on the activity and sets the onClickListener for the time and date views
    private void setDatetimeOnPage(){
        TextView textDate = (TextView)findViewById(R.id.DietStartDateTextView);
        TextView textTime = (TextView)findViewById(R.id.DietStartTimeTextView);

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance(TimeZone.getDefault());
                int mYear = current.get(Calendar.YEAR);
                int mMonth = current.get(Calendar.MONTH);
                int mDay = current.get(Calendar.DAY_OF_MONTH);

                //creates the date picker and sets the listener to call setDate whenever the date is changed
                DatePickerDialog mDatePicker = new DatePickerDialog(DietEntry.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog time = new TimePickerDialog(DietEntry.this, new TimePickerDialog.OnTimeSetListener() {
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
        TextView textDate = (TextView) findViewById(R.id.DietStartDateTextView);
        //TODO: Check to make sure date is not in the future, if it is, display error and do not change date

        Calendar c = new GregorianCalendar(year, month, day);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_dateFormat).format(c.getTime());
            textDate.setText("Date: " + str);
        }
    }
    private void setTime(int hour, int minute){
        TextView textTime = (TextView)findViewById(R.id.DietStartTimeTextView);

        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        Calendar c = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH),current.get(Calendar.DAY_OF_MONTH),
                hour, minute);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_timeFormat).format(c.getTime());
            textTime.setText("Time: " + str);
        }
    }
}
