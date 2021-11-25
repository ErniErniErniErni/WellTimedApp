package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminAddNewClassActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addNewClassButton;
    private EditText editClassID;
    private EditText editGradeNum;
    private EditText editClassNum;
    private EditText editStudentNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_class);

        addNewClassButton = findViewById(R.id.add_new_class_button);
        addNewClassButton.setOnClickListener(this);
        editClassID = findViewById(R.id.edit_classId);
        editGradeNum = findViewById(R.id.edit_grade_num);
        editClassNum = findViewById(R.id.edit_class_num);
        editStudentNum = findViewById(R.id.edit_student_num);
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
        String txt_GradeNum = editGradeNum.getText().toString().trim();
        String txt_ClassNum = editClassNum.getText().toString().trim();
        String className = "Grade " + txt_GradeNum + " Class " + txt_ClassNum;
        String studentID = editStudentNum.getText().toString().trim();
        String[] split = studentID.split(",");
//        ArrayList<String> namesList = new ArrayList<>();
//       String strNameList = "";
//       for (int i = -1; i < namesList.size(); i++) {
//           strNameList += split[i] + ",";
//       }


       if(txt_ID.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Class ID!");
        }else if(txt_GradeNum.isEmpty()) {
            Message.showMessage(getApplicationContext(),"Please enter Grade Number!");
        }else if(txt_ClassNum.isEmpty()) {
            Message.showMessage(getApplicationContext(), "Please enter Class Number!");
        }else {
            FirebaseDatabase.getInstance().getReference().child("Classes").child(txt_ID).child("Name").setValue(className);//.push();
            DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(txt_ID).child("Student");
//            studentRef.setValue(strNameList);
            Message.showMessage(getApplicationContext(),"Added new class successfully!");
        }
    }
}