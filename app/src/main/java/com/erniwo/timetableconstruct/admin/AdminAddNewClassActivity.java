package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AdminAddNewClassActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addNewClassButton;
    private EditText editClassID;
    private EditText editGradeNum;
    private EditText editClassNum;
//    private EditText editStudentNum;
    private TextView addStudentsArrowDown;
    private boolean[] selectedStudent;
    ArrayList<Integer> studentList = new ArrayList<>();
    ArrayList<String> studentListList = new ArrayList<>();
    String[] studentArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_class);

        addNewClassButton = findViewById(R.id.add_new_class_button);
        addNewClassButton.setOnClickListener(this);
        editClassID = findViewById(R.id.edit_classId);
        editGradeNum = findViewById(R.id.edit_grade_num);
        editClassNum = findViewById(R.id.edit_class_num);
        addStudentsArrowDown = findViewById(R.id.checkbox_textview);
//        selectedStudent = new boolean[studentArray.length];

        @SuppressLint("ResourceType") ArrayAdapter adapter = new ArrayAdapter<String>(this,R.id.checkbox_textview,studentListList);



//        DatabaseReference classInfoRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        classInfoRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                studentListList.clear();
//
//                for(DataSnapshot child: snapshot.getChildren()) {
//                    String userType = child.child("type").getValue().toString();
//                    if(userType.equals("1")) {
//                        String studentID = child.child("studentID").getValue().toString();
//                        studentListList.add(studentID);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        for (int q = 0 ; q < studentListList.size(); q ++) {
//            studentArray[q] = studentListList.get(q);
//        }
//
//
//        addStudentsArrowDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Init alert dialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        getApplicationContext()
//                );
//                //Set title
//                builder.setTitle("Select Students");
//                //Set dialog non cancelable
//                builder.setCancelable(false);
//
//                builder.setMultiChoiceItems(studentArray, selectedStudent, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        //Check condition
//                        if(isChecked) {
//                            //When checkbox selected
//                            //Add position in student list
//                            studentList.add(which);
//                            //Sort student list
//                            Collections.sort(studentList);
//                        }else{
//                            //When checkbox unselected
//                            //Remove position from list
//                            studentList.remove(which);
//                        }
//                    }
//                });
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Init string builder
//                        StringBuilder stringBuilder = new StringBuilder();
//                        //Use for loop
//                        for (int j = 0; j < studentList.size(); j++) {
//                            //Concat array value
//                            stringBuilder.append(studentArray[studentList.get(j)]);
//                            //Check condition
//                            if(j != studentList.size()-1) {
//                                //When j value not equal to student list size -1
//                                //Add comma
//                                stringBuilder.append(",");
//                            }
//                        }
//                        //Set text on text view
//                        addStudentsArrowDown.setText(stringBuilder.toString());
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Dismiss Dialog
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Use for loop
//                        for (int i = 0; i < selectedStudent.length; i++) {
//                            //Remove all selected
//                            selectedStudent[i] = false;
//                            //Clear student list
//                            studentList.clear();
//                            //Clear text view value
//                            addStudentsArrowDown.setText("");
//                        }
//                    }
//                });
//                //Show dialog
//                builder.show();
//            }
//        });

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
//        String studentID = editStudentNum.getText().toString().trim();
//        String[] split = studentID.split(",");
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
//            DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(txt_ID).child("Student");
//            studentRef.setValue(strNameList);
            Message.showMessage(getApplicationContext(),"Added new class successfully!");
        }
    }
}