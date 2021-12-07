package com.erniwo.timetableconstruct.login;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;


// this import is important
// not this one: import static android.support.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.click;
import com.erniwo.timetableconstruct.R;

import org.junit.Rule;
import org.junit.Test;

// Test activity navigation
public class ForgotPasswordActivityTest {
//    @Rule
//    ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);

    @Test
    public void test_navigation_ForgotPasswordActivity() {
        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);

        Espresso.onView(withId(R.id.forgotPassword)).perform(click());
        Espresso.onView(withId(R.id.layout_forgot_password)).check(matches(isDisplayed()));
//        pressBack();
//        Espresso.onView(withId(R.id.layout_login)).check(matches(isDisplayed()));

    }

    @Test
    public void test_navigation_backPress_toLoginActivity() {
        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);
//        ActivityScenario activityScenario = ActivityScenario.launch(ForgotPasswordActivity.class);


        Espresso.onView(withId(R.id.forgotPassword)).perform(click());
        Espresso.onView(withId(R.id.layout_forgot_password)).check(matches(isDisplayed()));

        // non-display keyboard
//        pressBack();

        // back to loginActivity
        pressBack();
        Espresso.onView(withId(R.id.layout_login)).check(matches(isDisplayed()));

        // one more pressBack to kill the app


    }
}