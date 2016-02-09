package com.rocket.biometrix;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by JP on 1/22/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HelloWorldEspressoTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<ExerciseParent> mActivityRule = new ActivityTestRule<>(
            ExerciseParent.class);

    @Test
            public void addnewexists(){
        onView(withId(R.id.exNewEntry)).check(matches(withText("+ Add Exercise")));


    }

//    @Before
//    public void initValidString() {
//        // Specify a valid string.
//        mStringToBetyped = "Espresso";
//    }

//    @Test
//    public void changeText_sameActivity() {
//        // Type text and then press the button.
//        onView(withHint(R.id.ex_notes))
//                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
//
//        // Check that the text was changed.
//        onView(withId(R.id.ex_notes))
//                .check(matches(withText(mStringToBetyped)));
    }
