package com.rocket.biometrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class HomeScreen extends AppCompatActivity {
    private static Button mood_button;

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
    }
}






