package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminManageListOfTeachersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

//    String[] teacherNameArray = {"Mrs Jane S.", "Miss Anna Smith", "Miss Xia Li", "Miss Batu Bao",
//            "Mr Jian Wang", "Mr John Smith", "Miss Mary W.", "Miss Karen Lee",
//    "Mr Zhang", "Mrs Johnson", "Mr Peter Smith", "Mrs Ann Lee"};
    private ListView listOfTeachers;
    private Button addNewTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_teachers);

        listOfTeachers = findViewById(R.id.ListOfTeachers);
        addNewTeacher = findViewById(R.id.add_teacher);
        addNewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(AdminManageListOfTeachersActivity.this, AdminAddNewTeacherActivity.class);
                startActivity(in2);
            }
        });

        ArrayList<String> teacherNameArray = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, teacherNameArray);

        listOfTeachers.setAdapter(adapter);
        listOfTeachers.setOnItemClickListener(this);

        DatabaseReference teacherInfoRef = FirebaseDatabase.getInstance().getReference().child("Teachers");
        teacherInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherNameArray.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    teacherNameArray.add(snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent in1 = new Intent(AdminManageListOfTeachersActivity.this, AdminManageClassTimetableActivity.class);
        startActivity(in1);
    }

}