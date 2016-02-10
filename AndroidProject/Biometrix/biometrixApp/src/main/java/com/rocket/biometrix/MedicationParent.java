package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class MedicationParent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_parent);

        //Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar2);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createEntryButtonOnClickListener();

    }

    void createEntryButtonOnClickListener(){
        Button createEntry = (Button) findViewById(R.id.NewMedicationEntryButton);
        createEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateEntryMedication = new Intent(MedicationParent.this, MedicationEntry.class);
                startActivity(CreateEntryMedication);
            }
        });
    }
}
