package com.rocket.biometrix;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SleepEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_entry);
    }


    public void SelectDateTime(View v) {
        final int viewID = v.getId();
        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.datetimeset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datepicker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timepicker);
                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm:ss");
                String time = mSDF.format(calendar.getTime());

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                String formatedDate = sdf.format(new Date(year-1900, month, day));

                if (viewID == R.id.sleepInitialDateButton)
                {
                    TextView initialSleepText = (TextView) findViewById(R.id.sleepInitialDateTextView);
                    initialSleepText.setText(formatedDate + ' ' + time);
                }
                else if (viewID == R.id.sleepEndDateButton)
                {
                    TextView endSleepText = (TextView) findViewById(R.id.sleepEndDateTextView);
                    endSleepText.setText(formatedDate + ' ' + time);
                }

                //Toast.makeText(getApplicationContext(), formatedDate + ' ' + time, Toast.LENGTH_LONG).show();

                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}
