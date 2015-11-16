package com.rocket.biometrix;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MoodCreatEntry extends AppCompatActivity {
    final static String _dateFormat = "EEE, MM/dd/yyyy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_creat_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        setDefaultDatetimeOnPage();
    }

    private void setDefaultDatetimeOnPage(){
        TextView textDate = (TextView)findViewById(R.id.moodCreateEntryDateSelect);

        setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance();
                int mYear = current.get(Calendar.YEAR);
                int mMonth = current.get(Calendar.MONTH);
                int mDay = current.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(MoodCreatEntry.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        setDate(year, month, day);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }
    private void setDate(int year, int month, int day){
        TextView textDate = (TextView)findViewById(R.id.moodCreateEntryDateSelect);
        //TODO: Check to make sure date is not in the future, if it is, display error and do not change date
        Calendar c = new GregorianCalendar(year, month, day);
        String str = new SimpleDateFormat(_dateFormat).format(c.getTime());
        textDate.setText("Date: " + str);
    }
}
