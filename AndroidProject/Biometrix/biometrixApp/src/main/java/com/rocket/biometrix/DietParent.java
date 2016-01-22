package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DietParent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createEntryButtonOnClickListener();


    }

    void createEntryButtonOnClickListener(){
        Button createEntry = (Button) findViewById(R.id.createMoodEntryButton);
        createEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateEntryDiet = new Intent(DietParent.this, DietEntry.class);
                startActivity(CreateEntryDiet);
            }
        });
    }
}
