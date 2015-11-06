package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class HomeScreen extends AppCompatActivity {
    private static Button mood_button;
    private static Button exercise_button; //keeping code style the same

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        OnClickButtonListener();
    }

    public void OnClickButtonListener(){
        mood_button = (Button)findViewById(R.id.mood_button);
        mood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(".InitialActivityWithPulloutMenu");
                startActivity(intent);
            }
        });

        //@EXERCISE BUTTON
        exercise_button = (Button)findViewById(R.id.exercise_button);
        exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Launch_ExerciseParent = new Intent(HomeScreen.this, ExerciseParent.class);
                startActivity(Launch_ExerciseParent);
                //finish(); //keep Home running
            }});

    }//end OnClickButtonListener()


}






