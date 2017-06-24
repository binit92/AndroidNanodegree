package com.mynanodegreeapps.bakingapp;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.anything;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.util.BakingActivityIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by inbkumar01 on 6/24/2017.
 */

@RunWith(AndroidJUnit4.class)
public class IdlingResourceBakingActivityTest {

    private BakingActivityIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<BakingActivity> mBakingActivityTestRule =
            new ActivityTestRule<>(BakingActivity.class);


    // Register any resource that need to be synchronized with Espresso before the test is run
    @Before
    public void registerIdlingResource(){
        BakingActivity activity = mBakingActivityTestRule.getActivity();
        idlingResource = new BakingActivityIdlingResource(activity);

        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void testToRunAfterSync(){
       // onData(anything()).inAdapterView(withId(R.id.recipeGrid)).atPosition(0).perform(click());
        onView(ViewMatchers.withId(R.id.recipeGrid)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    // Remember to unregister resource when not need to avoid malfunction
    @After
    public void unregisterIdlingResource(){
        if(idlingResource != null){
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

}