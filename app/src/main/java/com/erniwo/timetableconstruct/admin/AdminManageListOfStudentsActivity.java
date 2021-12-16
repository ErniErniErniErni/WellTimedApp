package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class AdminManageListOfStudentsActivity extends AppCompatActivity{

    private ListView listOfStudents;
    private Button addNewStudent;

    ArrayList<String> studentNameArray = new ArrayList<>();

    private String TAG = "AdminManageListOfStudentsActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_students);

        Log.d(TAG, "onCreate");

        // init elements
        listOfStudents = findViewById(R.id.list_of_students);
        addNewStudent = findViewById(R.id.add_new_student_button);

        // adapt to array
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, studentNameArray);
        listOfStudents.setAdapter(adapter);

        // onClick actions
        addNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(AdminManageListOfStudentsActivity.this, AdminAddNewStudentActivity.class);
                    startActivity(intent);
                }catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        });

        pullListOfStudentsFromDatabaseAndShow(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    private void pullListOfStudentsFromDatabaseAndShow(ArrayAdapter adapter) {
        DatabaseReference studentInfoRef = FirebaseDatabase.getInstance().getReference().child("Students");
        studentInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentNameArray.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String studentName = snapshot.child("name").getValue().toString();
                    String studentID = snapshot.getKey().trim();
                    studentNameArray.add(studentID + ": " + studentName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}