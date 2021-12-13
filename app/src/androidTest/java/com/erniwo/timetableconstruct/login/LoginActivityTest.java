package com.erniwo.timetableconstruct.login;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import android.os.Bundle;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.erniwo.timetableconstruct.R;

import org.junit.Test;

// Espresso
// The test goes alphabetically
public class LoginActivityTest {


//    @Test
//    public void isActivityInView() {
//
//        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);
//        Espresso.onView(withId(R.id.layout_login))
//                .check(matches(isDisplayed()));
//    }

    @Test
    public void test_visibility_widgets_on_screen() {
        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);

        Espresso.onView(withId(R.id.header))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.login_email))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.login_password))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.forgotPassword))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

//    @Test
//    public void test_textMatch_isheaderDisplayed() {
//        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);
//
//        Espresso.onView(withId(R.id.header))
//                .check(matches(withText(R.string.welcome_title)));
//    }
//    @Test
//    public void onCreate() {
//    }
//
//    @Test
//    public void onClick() {
//    }
//
//    @Test
//    public void checkButton() {
//    }
//
//    @Test
//    public void onStart() {
//    }
}