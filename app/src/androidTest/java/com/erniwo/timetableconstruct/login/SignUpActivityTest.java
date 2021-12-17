package com.erniwo.timetableconstruct.login;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import com.erniwo.timetableconstruct.R;

import org.junit.Test;

public class SignUpActivityTest {

    @Test
    public void test_visibility_widgets_on_screen() {
        ActivityScenario activityScenario = ActivityScenario.launch(SignUpActivity.class);

        Espresso.onView(withId(R.id.header))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signup_name))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signup_email))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signup_id))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signup_id_helper_text))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signup_password))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signup_chooseRoleGroup))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signUpButton))
                .check(matches(isDisplayed()));

    }

}