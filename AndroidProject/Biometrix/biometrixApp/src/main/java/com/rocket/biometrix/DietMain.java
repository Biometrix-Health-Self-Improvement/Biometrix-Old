package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DietMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createEntryButtonOnClickListener();
    }

    void createEntryButtonOnClickListener(){
        Button createEntry = (Button) findViewById(R.id.createEntryButton);
        createEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateEntryDiet = new Intent(DietMain.this, DietCreateEntry.class);
                startActivity(CreateEntryDiet);
            }
        });
    }
}
