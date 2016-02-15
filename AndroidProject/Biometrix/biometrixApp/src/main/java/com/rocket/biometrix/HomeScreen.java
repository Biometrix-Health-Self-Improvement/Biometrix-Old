package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class HomeScreen extends AppCompatActivity
{
    private static Button mood_button;
    private static Button diet_button;
    private static Button medications_button;
    private static Button exercise_button;
    private static Button sleep_button;
    private static Button analytics_button;
    private static Button login_button;
    private static Button createLogin_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        OnClickButtonListener();
    }

    public void OnClickButtonListener(){
        //@MOOD BUTTON
        mood_button = (Button)findViewById(R.id.mood_button);
        mood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_Mood = new Intent(HomeScreen.this, MoodParent.class);
                startActivity(Launch_Mood);
            }
        });

        //@DIET BUTTON
        diet_button = (Button)findViewById(R.id.diet_button);
        diet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_Diet = new Intent(HomeScreen.this, DietParent.class);
                startActivity(Launch_Diet);
            }
        });

        //@MEDICATIONS BUTTON
        medications_button = (Button)findViewById(R.id.medications_button);
        medications_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_Medications = new Intent(HomeScreen.this, HomeScreen.class);
                startActivity(Launch_Medications);
            }
        });

        //@EXERCISE BUTTON
        exercise_button = (Button)findViewById(R.id.exercise_button);
        exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_ExerciseParent = new Intent(HomeScreen.this, ExerciseParent.class);
                startActivity(Launch_ExerciseParent);
            }
        });

        //@SLEEP BUTTON
        sleep_button = (Button)findViewById(R.id.sleep_button);
        sleep_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_Sleep = new Intent(HomeScreen.this, SleepParent.class);
                startActivity(Launch_Sleep);
            }
        });

        //@ANALYTICS BUTTON
        analytics_button = (Button)findViewById(R.id.analytics_button);
        analytics_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_Analytics = new Intent(HomeScreen.this, HomeScreen.class);
                startActivity(Launch_Analytics);
            }
        });

        //@LOGIN BUTTON
        login_button = (Button)findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_Login = new Intent(HomeScreen.this, GetLoginActivity.class);
                startActivity(Launch_Login);
            }
        });

        //@CREATELOGIN BUTTON
        createLogin_button = (Button)findViewById(R.id.createLogin_button);
        createLogin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_CreateLogin = new Intent(HomeScreen.this, CreateLoginActivity.class);
                startActivity(Launch_CreateLogin);
            }
        });
    }


}






