package com.rocket.biometrix;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by JP on 25/11/2015. Created since a lot of modules need time picker
 * Dialog box seems to work better with the time picker
 * http://developer.android.com/guide/topics/ui/controls/pickers.html
 * FYI it is horribly painful to try and force the old style of
 * time picker, wasted 3 hours of my life trying. Just use the new one. This one.
 *
 * How to use:
 * put the method below in you activity, call it from a button
   public void showTimePickerDialog(View v) {
 DialogFragment newFragment = new TimePickerFragment();
 newFragment.show(getFragmentManager(), "tag");
 }
 */

public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

