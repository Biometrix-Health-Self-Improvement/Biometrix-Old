package com.rocket.biometrix;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ExerciseParent extends AppCompatActivity
                            implements EditCalendar.OnFragmentInteractionListener{

    static final int ADD_ENTRY_REQUEST = 1;
    String[] usersEntryData = null; //will be filled up by ExerciseEntry on its finish();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Fragment for Calendar
        EditCalendar eCalendar = new EditCalendar();

        FragmentManager manager = getSupportFragmentManager();

       FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.ex_parent_frame, eCalendar, "ExCalendar");
        transaction.commit();






        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button addNewEntry = (Button) findViewById(R.id.exNewEntry);
        addNewEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start new intent
                Intent LaunchNewEntry = new Intent(ExerciseParent.this, ExerciseEntry.class);

                //put extra data for ExerciseEntry to use
                //http://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data
                //http://www.mybringback.com/android-sdk/12204/onactivityresult-android-tutorial/
                //Bundling the empty usersEntryData to be filled. This seems redundant NOW but later, it will make life easier.
                Bundle usrEntD = new Bundle();
                usrEntD.putStringArray("parentArray", usersEntryData);

                //key is a reference to the data im putting in the intent. Which is the bundle created above.
                LaunchNewEntry.putExtra("key", usrEntD);

                //Way to check if user actually put in an entry or accidentally tapped it.
                startActivityForResult(LaunchNewEntry, 1);

            }
        }); //end addNewEntry on click listener


    } //end OnCreate of ExerciseParent

    //Automated class for this garbage.
    //TODO: https://github.com/beplaya/Wagon convince group to be truly open source warriors

    //Automatically called
    //http://developer.android.com/reference/android/app/Activity.html
    //Why is JP doing this? For future extensibility, this will have to be done to edit past entries. (to autopopulate entry with original data)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras().containsKey("childKey")) {
                    usersEntryData = data.getBundleExtra("childKey").getStringArray("childKey");
                    //Lil bling bling to let user know they successfully saved an entry.
                    Toast.makeText(getApplicationContext(), "Added Entry: " + usersEntryData[0], Toast.LENGTH_SHORT).show();
                }
            }
        }// end request code check
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
