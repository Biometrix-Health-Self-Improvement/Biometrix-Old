package com.rocket.biometrix.SleepModule;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rocket.biometrix.Common.DateTimeSelectorPopulateTextView;
import com.rocket.biometrix.Database.LocalStorageAccessSleep;
import com.rocket.biometrix.NavigationDrawerActivity;
import com.rocket.biometrix.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SleepEntry.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SleepEntry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepEntry extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



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


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SleepEntry() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SleepEntry.
     */
    public static SleepEntry newInstance(String param1, String param2) {
        SleepEntry fragment = new SleepEntry();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try{
            NavigationDrawerActivity nav = (NavigationDrawerActivity) getActivity();
            //Change the title of the action bar to reflect the current fragment
            nav.setActionBarTitleFromFragment(R.string.action_bar_title_sleep_parent);
            //set activities active fragment to this one
            nav.activeFragment = this;
        } catch (Exception e){}


    }

    protected void GetViewReferences(View v)
    {
        endDateTextView = (TextView) v.findViewById(R.id.sleepEndTimeTextView);
        hourSeekBar = (SeekBar) v.findViewById(R.id.sleepHoursSeekBar);
        minuteSeekBar = (SeekBar) v.findViewById(R.id.sleepMinutesSeekBar);
        startDateTextView = (TextView) v.findViewById(R.id.sleepStartDateTextView);
        startTimeTextView = (TextView) v.findViewById(R.id.sleepStartTimeTextView);
        sleptTimeTextView = (TextView) v.findViewById(R.id.sleepTimeSleptTextView);
        qualitySeekBar = (SeekBar) v.findViewById(R.id.sleepQualitySeekBar);
        qualityNumberTextView = (TextView) v.findViewById(R.id.sleepQualityNumberTextView);
        generalHealthSpinner = (Spinner) v.findViewById(R.id.sleepGeneralHealthSpinner);
        noteTextView = (TextView) v.findViewById(R.id.sleepNotesEditText);
    }


    /**
     * Sets up the listeners for the view objects
     */
    protected void SetupListeners(View v)
    {
        //Sets up the date and time fields to work with the activity that grabs them.
        DateTimeSelectorPopulateTextView timeSelectSetup = new DateTimeSelectorPopulateTextView(getActivity(),v, R.id.sleepStartDateTextView, R.id.sleepStartTimeTextView);
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
                getActivity(), R.array.sleep_gen_health_array,android.R.layout.simple_spinner_item);

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sleep_entry, container, false);


        //Moves this to another function to clean up onCreate
        GetViewReferences(v);

        //Sets a default of 7 hours as this should be fairly average.
        hourSeekBar.setProgress(7);
        minuteSeekBar.setProgress(0);

        //Moves listener setups to another function to avoid clutter
        SetupListeners(v);

        qualitySeekBar.setMax(SleepData.maxQuality);

        UpdateEndTimes();

        return v;
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
            SimpleDateFormat endTimeFormat = new SimpleDateFormat(DateTimeSelectorPopulateTextView._timeFormat + " " + DateTimeSelectorPopulateTextView._dateFormat);


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
        LocalStorageAccessSleep fileAccess = new LocalStorageAccessSleep(view.getContext(), null, null, 1);

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

       // finish();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
