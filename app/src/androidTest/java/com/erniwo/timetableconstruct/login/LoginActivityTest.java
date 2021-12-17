package com.erniwo.timetableconstruct.login;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.Espresso.*;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
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
public class LoginActivityTest {


    @Test
    public void test_navigation_LoginActivity_to_SignUpActivity() {
        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);

        Espresso.onView(withId(R.id.signUp)).perform(click());
        Espresso.onView(withId(R.id.layout_sign_up)).check(matches(isDisplayed()));
        pressBack();
        Espresso.onView(withId(R.id.layout_login)).check(matches(isDisplayed()));

    }

    @Test
    public void test_navigation_LoginActivity_to_ForgotPasswordAcitvity() {
        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);

        Espresso.onView(withId(R.id.forgotPassword)).perform(click());
        Espresso.onView(withId(R.id.layout_forgot_password)).check(matches(isDisplayed()));
        pressBack();
        Espresso.onView(withId(R.id.layout_login)).check(matches(isDisplayed()));

    }

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

    @Test
    public void test_textMatch_isheaderDisplayed() {
        ActivityScenario activityScenario = ActivityScenario.launch(LoginActivity.class);

        Espresso.onView(withId(R.id.header))
                .check(matches(withText(R.string.welcome_title)));
        Espresso.onView(withId(R.id.forgotPassword))
                .check(matches(withText("Forgot Password?")));
    }
}