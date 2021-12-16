package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class AdminAddNewStudentActivity extends AppCompatActivity {

    private EditText editStudentName;
    private EditText editStudentID;
    private Button addNewStudentButton;

    private String TAG = "AdminAddNewStudentActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_student);

        Log.d(TAG, "onCreate");

        // init elements
        editStudentName = findViewById(R.id.enter_edit_text_student_name);
        editStudentID = findViewById(R.id.enter_edit_text_student_id);
        addNewStudentButton = findViewById(R.id.add_new_student_button);


        // onClick action
        addNewStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewStudent();
                    Intent intent = new Intent(AdminAddNewStudentActivity.this,
                            AdminManageListOfStudentsActivity.class);
                    startActivity(intent);
                }catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        });

    }

    private void addNewStudent() {
        String txt_Name = editStudentName.getText().toString().trim();

        String txt_ID = editStudentID.getText().toString().toUpperCase(Locale.ROOT).trim();
        if(txt_Name.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Student Name!");
        }else if(txt_ID.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Student ID!");
        }else{

            // save to firebase realtime database
            FirebaseDatabase.getInstance().getReference().child("Students")
                    .child(txt_ID).child("name").setValue(txt_Name);
            Message.showMessage(getApplicationContext(),"Added new student successfully!");
        }
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
}