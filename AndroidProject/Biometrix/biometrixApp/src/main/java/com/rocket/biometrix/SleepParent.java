package com.rocket.biometrix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class SleepParent extends AppCompatActivity {

    private LinearLayout displayEntriesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_parent);

        displayEntriesLayout = (LinearLayout)findViewById(R.id.sleepDisplayEntriesLinearLayout);
        UpdatePreviousEntries();
    }

    /**
     * Starts the child activity to create a sleep entry.
     * @param v The view that called this, should be the button
     */
    public void onNewSleepEntryClick(View v)
    {
        Intent sleepEntry = new Intent(this, SleepEntry.class);
        startActivity(sleepEntry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdatePreviousEntries();
    }


    /**
     * Updates the scroll view with the information contained in the database for sleep.
     */
    private void UpdatePreviousEntries()
    {
        LocalStorageAccess fileAccess = new LocalStorageAccess(this, null, null, 1);

        List<SleepData> sleepDataLinkedlist = fileAccess.GetSleepEntries();

        displayEntriesLayout.removeAllViews();

        for (SleepData data : sleepDataLinkedlist)
        {
            TextView textView = new TextView(this);

            //Creates the string that will be displayed.
            StringBuilder dispString = new StringBuilder();
            dispString.append(data.getStartTime());
            dispString.append(" for ");
            dispString.append(data.getDuration());
            dispString.append(". Quality: ");
            dispString.append(data.getSleepQuality());

            textView.setText(dispString);
            displayEntriesLayout.addView(textView);
        }
    }
}
