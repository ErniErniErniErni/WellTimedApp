package com.erniwo.timetableconstruct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button viewClassTimetable;
    private Button viewTeacherTimetable;
    private Button exportTimetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        viewClassTimetable = (Button) findViewById(R.id.ManageClassTimetable);
        viewClassTimetable.setOnClickListener(this);

        viewTeacherTimetable = (Button) findViewById(R.id.ManageTeacherTimetable);
        viewTeacherTimetable.setOnClickListener(this);

        exportTimetable = (Button) findViewById(R.id.ExportTimetable);
        exportTimetable.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ManageClassTimetable:
                startActivity(new Intent(this, AdminManageListOfClassesActivity.class));
                break;
            case R.id.ManageTeacherTimetable:
//                startActivity(new Intent(this, .class));
                break;
            case R.id.ExportTimetable:
                break;
        }
    }
}