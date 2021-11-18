package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddNewTeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addNewTeacherButton;
    private EditText editTeacherID;
    private EditText editTeacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_teacher);

        addNewTeacherButton = findViewById(R.id.add_new_teacher_button);
        addNewTeacherButton.setOnClickListener(this);
        editTeacherID = findViewById(R.id.edit_teacherId);
        editTeacherName = findViewById(R.id.edit_teacherName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_teacher_button:
                addNewTeacher();
                break;
        }
    }

    private void addNewTeacher() {
        String txt_ID = editTeacherID.getText().toString().trim();
        String txt_Name = editTeacherName.getText().toString().trim();
        if(txt_ID.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Teacher ID!");
        }else if(txt_Name.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Teacher Name!");
        }else {
            FirebaseDatabase.getInstance().getReference().child("Teachers").child(txt_ID).setValue(txt_Name);//.push();
            Message.showMessage(getApplicationContext(),"Added new teacher successfully!");
        }
    }
}