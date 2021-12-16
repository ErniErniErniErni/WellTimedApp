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

public class AdminAddNewTeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTeacherName;
    private EditText editTeacherID;
    private Button addNewTeacherButton;


    private String TAG = "AdminAddNewTeacherActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_teacher);

        // init elements
        editTeacherName = findViewById(R.id.enter_edit_text_teacher_name);
        editTeacherID = findViewById(R.id.enter_edit_text_teacher_id);
        addNewTeacherButton = findViewById(R.id.add_new_teacher_button);


        // onClick action
        addNewTeacherButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_teacher_button:
                addNewTeacher();
                Intent intent = new Intent(AdminAddNewTeacherActivity.this,
                        AdminManageListOfTeachersActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void addNewTeacher() {
        String txt_Name = editTeacherName.getText().toString().trim();

        String txt_ID = editTeacherID.getText().toString().toUpperCase(Locale.ROOT).trim();
        if(txt_Name.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Teacher Name!");
        }else if(txt_ID.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Teacher ID!");
        }else{
            // save to firebase realtime database
            FirebaseDatabase.getInstance().getReference().child("Teachers")
                    .child(txt_ID).child("name").setValue(txt_Name);
            Message.showMessage(getApplicationContext(),"Added new teacher successfully!");
        }
    }
}