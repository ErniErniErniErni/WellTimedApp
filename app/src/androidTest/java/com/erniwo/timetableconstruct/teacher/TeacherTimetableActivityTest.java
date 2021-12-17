package com.erniwo.timetableconstruct.teacher;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.student.StudentTimetableActivity;

import org.junit.Test;

public class TeacherTimetableActivityTest {

    @Test
    public void test_visibility_widgets_on_screen() {
        ActivityScenario activityScenario = ActivityScenario.launch(TeacherTimetableActivity.class);

        Espresso.onView(withId(R.id.title_bar))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.name_of_teacher_header))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.well_timed_text))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.logout_icon))
                .check(matches(isDisplayed()));
    }

}