package com.rocket.biometrix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SleepParent extends AppCompatActivity {

    private LinearLayout displayEntriesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //If the module is not enabled, perform no more setup and exit the activity
        if (CheckEnabledStatus() )
        {
            setContentView(R.layout.activity_sleep_parent);

            displayEntriesLayout = (LinearLayout) findViewById(R.id.sleepDisplayEntriesLinearLayout);
            UpdatePreviousEntries();
        }
        else
        {
            finish();
        }
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

    /**
     * If the sleep module is not enabled for the current user, ask if they would like to re-enable it
     */
    protected boolean CheckEnabledStatus()
    {
        boolean enabled = true;

        try
        {
            if (!LocalAccount.GetInstance().isSleepEnabled(getApplicationContext()))
            {
                enabled = false;
            }
        }
        catch(NullPointerException except)
        {
            //Since the default is enabled, if the user is not logged in set it to enabled for now
            enabled = true;
        }

        return enabled;
    }

    /**
     * Allows the disabling of the sleep module
     * @param v
     */
    public void onSleepDisableClick(View v)
    {
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Disable Sleep Module")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)

                //Sets the action for yes
                //Sets it so that if yes is clicked, the current account has the sleep module disabled
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try
                        {
                            LocalAccount.GetInstance().changeSleepStatus(getApplicationContext(), false);
                            finish();
                        }
                        catch (NullPointerException except)
                        {
                            Toast.makeText(getApplicationContext(), "You must be logged in", Toast.LENGTH_LONG);
                        }
                    }
                })
                .setNegativeButton("No", null)						//Do nothing on no
                .show();
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
