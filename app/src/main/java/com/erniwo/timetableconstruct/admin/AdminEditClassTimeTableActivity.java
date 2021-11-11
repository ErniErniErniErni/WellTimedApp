package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.erniwo.timetableconstruct.R;

public class AdminEditClassTimeTableActivity extends AppCompatActivity {

    public static final String EXTRA_COURSE_INDEX = "course_index";
    public static final String EXTRA_Day_OF_WEEK = "day_of_week";
    public static final String EXTRA_CLASS_START = "class_start";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_class_time_table);
    }
}