package com.rocket.biometrix.Common;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Activity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rocket.biometrix.ExerciseModule.ExerciseEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by JP on 11/25/2015.
 *A class that populates 2 TextViews with user-selected date and time.
 * How to use:
 * 1) Have two TextViews http://developer.android.com/reference/android/widget/TextView.html
 * @see ExerciseEntry
 * 2) Make an instance of this class inside your activity, pass it a view and two int ids
 * <pre>
 *DateTimeSelectorPopulateTextView DTPTV = new DateTimeSelectorPopulateTextView(YOUR_CLASS.this, R.id.YOUR_DATE_TEXTVIEW, R.id.YOUR_TIME_TEXTVIEW);
 * </pre>
 * 3)Now call the Populate method on the class instance you just made. (Good place to call is in the OnCreate() of your activity)
 * * <pre>
 *DTPTV.Populate();
 * </pre>
 */
public class DateTimeSelectorPopulateTextView {

    final public static String _dateFormat = "EEE, MM/dd/yyyy";
    final public  static String _timeFormat = "h:mm a";
    int dateID;
    int timeID;
    Activity activity;
    protected View view;
    public TextView textDate;
    public TextView textTime;


    //Fill up this constructor with the Context (probs ur activity), and the TextView IDs (R.id.YOUR_ID_HERE)
    public DateTimeSelectorPopulateTextView(Activity activity, View view, int dateID, int timeID){
        this.activity = activity;
        this.view = view;
        this.dateID = dateID;
        this.timeID = timeID;
        TextView textDate = (TextView) view.findViewById(dateID);
        TextView textTime = (TextView) view.findViewById(timeID);
    }

    //Seeds all TextVies and calls setDatetimeOnPage
    public void Populate(){
        setDatetimeOnPage();
        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        setDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));
        setTime(current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE));
    }

    //Sets the default date and time values on the activity and sets the onClickListener for the time and date views
    //dateID is TextView id you want to have filled.
    private void setDatetimeOnPage(){
        view.findViewById(dateID).setOnClickListener(new View.OnClickListener() {
            //ExerciseEntry.dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance(TimeZone.getDefault());
                int mYear = current.get(Calendar.YEAR);
                int mMonth = current.get(Calendar.MONTH);
                int mDay = current.get(Calendar.DAY_OF_MONTH);

                //creates the date picker and sets the listener to call setDate whenever the date is changed
                DatePickerDialog mDatePicker = new DatePickerDialog(DateTimeSelectorPopulateTextView.this.view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        setDate(year, month, day);
                    }
                }, mYear, mMonth, mDay); //mYear, mMonth, and mDay are default values to strt the date picker on, which are set to current
                mDatePicker.setTitle("Select Date");
                mDatePicker.show(); //shows the date picker that was just create
            }
        });
        //2nd click listener for time
        view.findViewById(timeID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar current = Calendar.getInstance(TimeZone.getDefault());
                int mHour = current.get(Calendar.HOUR_OF_DAY);
                int mMinute = current.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(DateTimeSelectorPopulateTextView.this.view.getContext(), new TimePickerDialog.OnTimeSetListener() {
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
        //TextView textDate = (TextView) findViewById(dateID);

        Calendar c = new GregorianCalendar(year, month, day);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_dateFormat).format(c.getTime());
            updateTVdate("Date: " + str); //update on seperate thread (not stack safe)
        }
    }
    private void setTime(int hour, int minute){
        //TextView textTime = (TextView)findViewById(timeID);

        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        Calendar c = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH),current.get(Calendar.DAY_OF_MONTH),
                hour, minute);
        if (!c.after(Calendar.getInstance(TimeZone.getDefault()))) {
            String str = new SimpleDateFormat(_timeFormat).format(c.getTime());
            updateTVtime("Time: " + str);
        }
    }

    //Updating parent activity must be done on separate thread
    public void updateTVdate(final String str1){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textDate = (TextView) view.findViewById(dateID);
                textDate.setText(str1);
            }
        });
    }
    //other method for time. The real challenge is making this more flexible to work with any number of buttons!
    public void updateTVtime(final String str1){
        //view.getContext().runOnUiThread(new Runnable() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textTime = (TextView) view.findViewById(timeID);
                textTime.setText(str1);
            }
        });
    }


}
