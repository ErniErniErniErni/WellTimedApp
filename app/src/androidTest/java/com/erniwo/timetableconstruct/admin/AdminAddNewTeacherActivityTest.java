package com.erniwo.timetableconstruct.admin;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import com.erniwo.timetableconstruct.R;

import org.junit.Test;

public class AdminAddNewTeacherActivityTest {

    @Test
    public void test_visibility_widgets_on_screen() {

        ActivityScenario activityScenario = ActivityScenario.launch(AdminAddNewTeacherActivity.class);

        Espresso.onView(withId(R.id.enter_teacher_name))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.enter_teacher_ID))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.add_new_teacher_id_helper_text))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.add_new_teacher_button))
                .check(matches(isDisplayed()));

    }

}