package com.rocket.biometrix;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by JP on 11/25/2015.
 *
 *
 */
public class DateTimePopulateTextView extends AppCompatActivity {

    final static String _dateFormat = "EEE, MM/dd/yyyy";
    final static String _timeFormat = "h:mm a";
    int dateID;
    int timeID;
    Context ParentContext;

    //Fill up this constructor with the Context (probs ur activity), and the TextView IDs (R.id.YOUR_ID_HERE)
    DateTimePopulateTextView(Context ParentContext, int dateID, int timeID){
        this.ParentContext = ParentContext;
        this.dateID = dateID;
        this.timeID = timeID;
    }

//    setDatetimeOnPage();
//    Calendar current = Calendar.getInstance(TimeZone.getDefault());
//    setDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));
//    setTime(current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE));


    //Sets the default date and time values on the activity and sets the onClickListener for the time and date views
    //dateID is TextView id you want to have filled.
    private void setDatetimeOnPage(){
        TextView textDate = (TextView)findViewById(dateID);
        TextView textTime = (TextView)findViewById(timeID);

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance(TimeZone.getDefault());
                int mYear = current.get(Calendar.YEAR);
                int mMonth = current.get(Calendar.MONTH);
                int mDay = current.get(Calendar.DAY_OF_MONTH);

                //creates the date picker and sets the listener to call setDate whenever the date is changed
                DatePickerDialog mDatePicker = new DatePickerDialog(ParentContext, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog time = new TimePickerDialog(ParentContext, new TimePickerDialog.OnTimeSetListener() {
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
        TextView textDate = (TextView) findViewById(dateID);

        Calendar c = new GregorianCalendar(year, month, day);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_dateFormat).format(c.getTime());
            textDate.setText("Date: " + str);
        }
    }
    private void setTime(int hour, int minute){
        TextView textTime = (TextView)findViewById(timeID);

        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        Calendar c = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH),current.get(Calendar.DAY_OF_MONTH),
                hour, minute);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_timeFormat).format(c.getTime());
            textTime.setText("Time: " + str);
        }
    }


}
