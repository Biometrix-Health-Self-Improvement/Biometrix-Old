package com.rocket.biometrix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SleepParent extends AppCompatActivity {

    Button new_entry_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_parent);

        new_entry_button = (Button) findViewById(R.id.NewSleepEntryButton);
    }

    public void onNewSleepEntryClick(View v) {
        Intent sleepEntry = new Intent(this, SleepEntry.class);
        startActivity(sleepEntry);
    }
}
