package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.erniwo.timetableconstruct.R;

public class AdminManageListOfClassesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    String[] classNameArray = {"Class 101", "Class 102", "Class 103",
            "Class 201", "Class 202", "Class 203", "Class 301",
            "Class 302", "Class 303", "Class 304", "Class 401",
            "Class 402", "Class 403", "Class 404", "Class 405",
    "Class 501", "Class 502", "Class 503", "Class 504",
    "Class 601", "Class 602"};
    private ListView listOfClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_classes);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, classNameArray);

        listOfClasses = (ListView) findViewById(R.id.ListOfClasses);
        listOfClasses.setAdapter(adapter);
        listOfClasses.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent in1 = new Intent(AdminManageListOfClassesActivity.this, AdminManageClassTimetableActivity.class);
        startActivity(in1);
    }
}