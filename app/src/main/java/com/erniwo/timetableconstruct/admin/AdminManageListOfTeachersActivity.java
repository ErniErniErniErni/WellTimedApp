package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.erniwo.timetableconstruct.R;

public class AdminManageListOfTeachersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    String[] teacherNameArray = {"Mrs Jane S.", "Miss Anna Smith", "Miss Xia Li", "Miss Batu Bao",
            "Mr Jian Wang", "Mr John Smith", "Miss Mary W.", "Miss Karen Lee",
    "Mr Zhang", "Mrs Johnson", "Mr Peter Smith", "Mrs Ann Lee"};
    private ListView listOfTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_teachers);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, teacherNameArray);

        listOfTeachers = (ListView) findViewById(R.id.ListOfTeachers);
        listOfTeachers.setAdapter(adapter);
        listOfTeachers.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent in1 = new Intent(AdminManageListOfTeachersActivity.this, AdminManageClassTimetableActivity.class);
        startActivity(in1);
    }

}