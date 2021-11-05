package com.erniwo.timetableconstruct;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdminManageListOfClassesActivity extends AppCompatActivity {

    String[] classNameArray = {"Class 101", "Class 102", "Class 103", "Class 201", "Class 202", "Class 203", "Class 301"};
    private ListView listOfClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_classes);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, classNameArray);

        listOfClasses = (ListView) findViewById(R.id.ListOfClasses);
        listOfClasses.setAdapter(adapter);
    }
}