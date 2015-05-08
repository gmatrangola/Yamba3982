package com.newcircle.yamba;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by geoff on 5/8/15.
 */
public class StatusActivityTest extends ActivityInstrumentationTestCase2<StatusActivity> {

    public StatusActivityTest() {
        super(StatusActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // initialize the instance under test
        getActivity();
    }

    public void testCharacterCounter() {
        int maxChars = 140;
        String testString = "Test status update";

        onView(withId(R.id.status_text_count)).check(matches(withText(String.valueOf(maxChars))));

        onView(withId(R.id.status_text)).perform(typeText(testString));

        int count = maxChars - testString.length();

        onView(withId(R.id.status_text_count)).check(matches(withText(String.valueOf(count))));
    }

    public void testZeroCondition() {
        String testString = "This status update is exactly 140 characters."
                + " Are you impressed that I was able to make this match"
                + " without extra chars? I worked really hard";

        onView(withId(R.id.status_text)).perform(typeText(testString));
        onView(withId(R.id.status_text_count)).check(matches(withText(String.valueOf("0"))));

        onView(withId(R.id.status_button)).check(matches(isEnabled()));
    }

    public void testCounterOverflow() {
        String testString = "This status update is exactly 140 characters."
                + " Are you impressed that I was able to make this match"
                + " without extra chars? I worked really hard xxxxxxxxxx";

        onView(withId(R.id.status_text)).perform(typeText(testString));
        onView(withId(R.id.status_button)).check(matches(not(isEnabled())));

        onView(withId(R.id.status_text)).perform(clearText());

        onView(withId(R.id.status_button)).check(matches(isEnabled()));
    }
}
