package com.erniwo.timetableconstruct.admin;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import com.erniwo.timetableconstruct.R;

import org.junit.Test;

public class AdminMainActivityTest {

    @Test
    public void test_visibility_widgets_on_screen() {
        ActivityScenario activityScenario = ActivityScenario.launch(AdminMainActivity.class);

        Espresso.onView(withId(R.id.manage_class_timetable))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.manage_teacher_timetable))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.manage_students))
                .check(matches(isDisplayed()));

    }

}