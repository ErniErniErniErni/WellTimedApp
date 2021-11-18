package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddNewClassActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addNewClassButton;
    private EditText editClassID;
    private EditText editClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_class);

        addNewClassButton = findViewById(R.id.add_new_class_button);
        addNewClassButton.setOnClickListener(this);
        editClassID = findViewById(R.id.edit_classId);
        editClassName = findViewById(R.id.edit_className);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_class_button:
                addNewClass();
                break;
        }
    }

   public void addNewClass() {
        String txt_ID = editClassID.getText().toString().trim();
        String txt_Name = editClassName.getText().toString().trim();
        if(txt_ID.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Class ID!");
        }else if(txt_Name.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Class Name!");
        }else {
            FirebaseDatabase.getInstance().getReference().child("Classes").child(txt_ID).child("Name").setValue(txt_Name);//.push();
            Message.showMessage(getApplicationContext(),"Added new class successfully!");
        }
    }
}