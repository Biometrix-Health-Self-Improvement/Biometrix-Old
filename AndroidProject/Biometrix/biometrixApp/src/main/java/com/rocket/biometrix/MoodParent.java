package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoodParent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        createEntryButtonOnClickListener();


        populatePastEntriesScrollview();

    }

    //Sets the create entry button to switch to the create entry activity
    void createEntryButtonOnClickListener(){
        Button createEntry = (Button) findViewById(R.id.createMoodEntryButton);
        createEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateEntryMood = new Intent(MoodParent.this, MoodEntry.class);
                startActivity(CreateEntryMood);
            }
        });
    }


    //in progress - don't mind me
    void populatePastEntriesScrollview(){

        LinearLayout entryList = (LinearLayout) this.findViewById(R.id.pastMoodEntries);


        View div;


        for (int i =0; i < 40; i++) {
            TextView pastEntry = new TextView(this);
            pastEntry.setText("in java addin' textviews 1");
            pastEntry.setTextSize(16);
            entryList.addView(pastEntry);

            div = new View(this);
            div.setBackgroundResource(R.color.colorBackgroundDark);
            div.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

            entryList.addView(div);
        }
    }


}
