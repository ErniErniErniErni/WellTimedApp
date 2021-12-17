package com.erniwo.timetableconstruct.admin;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.login.SignUpActivity;

import org.junit.Test;

public class AdminManageListOfClassesActivityTest {

    @Test
    public void test_visibility_widgets_on_screen() {
        ActivityScenario activityScenario = ActivityScenario.launch(AdminManageListOfClassesActivity.class);

        Espresso.onView(withId(R.id.add_class))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.above_item_list))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.list_of_classes))
                .check(matches(isDisplayed()));

    }

}