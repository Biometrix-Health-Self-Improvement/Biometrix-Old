package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GetDateTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_date_time);
    }

    public void onSetClick(View view)
    {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datepicker);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timepicker);
        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());

        SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = mSDF.format(calendar.getTime());

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = sdf.format(new Date(year-1900, month, day));

        //Toast.makeText(getApplicationContext(), formatedDate + ' ' + time, Toast.LENGTH_LONG).show();

        Intent dataPassback = new Intent();
        dataPassback.putExtra("date", formattedDate);
        dataPassback.putExtra("time", formattedTime);

        setResult(RESULT_OK, dataPassback);
        finish();
    }
}
