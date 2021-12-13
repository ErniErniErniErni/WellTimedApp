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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AdminAddNewClassActivity extends AppCompatActivity implements View.OnClickListener {


//    private EditText editClassID;
//    private EditText editGradeNum;
//    private EditText editClassNum;
//    private EditText editStudentNum;

    private TextView addStudentsArrowDown;
    ArrayList<Integer> studentList = new ArrayList<>();
    ArrayList<String> studentListList = new ArrayList<>();
    static String[] studentArray = new String[1000];
    private boolean[] selectedStudent;

    private TextView selectGradeNumArrowDown;
    ArrayList<Integer> gradeList = new ArrayList<>();
    static String[] gradeArray = new String[]{"Grade 1", "Grade 2", "Grade 3", "Grade 4",
            "Grade 5", "Grade 6", "Grade 7","Grade 8",
            "Grade 9", "Grade 10", "Grade 11", "Grade 12"};
    private boolean[] selectedGrade;

    private TextView selectClassNumArrowDown;
    ArrayList<Integer> classList = new ArrayList<>();
    static String[] classArray = new String[]{"Class 1", "Class 2", "Class 3", "Class 4",
    "Class 5", "Class 6", "Class 7", "Class 8",
    "Class 9", "Class 10", "Class 11", "Class 12"};
    private boolean[] selectedClass;

    private Button addNewClassButton;


//    AlertDialog.Builder builder = new AlertDialog.Builder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_class);

//        editClassID = findViewById(R.id.edit_classId);
//        editGradeNum = findViewById(R.id.edit_grade_num);
//        editClassNum = findViewById(R.id.edit_class_num);
        addStudentsArrowDown = findViewById(R.id.select_student_num);
        addStudentsArrowDown.setOnClickListener(this);
        selectGradeNumArrowDown = findViewById(R.id.select_grade_num);
        selectGradeNumArrowDown.setOnClickListener(this);
        selectClassNumArrowDown = findViewById(R.id.select_class_num);
        selectClassNumArrowDown.setOnClickListener(this);
        addNewClassButton = findViewById(R.id.add_new_class_button);
        addNewClassButton.setOnClickListener(this);

        }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_class_button:
                addNewClass();
                break;
            case R.id.select_student_num:
                multipleCheckStudents();
                break;
        }
    }
    public void addNewClass() {
//        String txt_ID = editClassID.getText().toString().trim();
//        String txt_GradeNum = editGradeNum.getText().toString().trim();
//        String txt_ClassNum = editClassNum.getText().toString().trim();
//        String className = "Grade " + txt_GradeNum + " Class " + txt_ClassNum;
//
//        if(txt_ID.isEmpty()) {
////            Message.showMessage(getApplicationContext(),"Please enter Class ID!");
//             editClassID.setError("Please enter Class ID!");
//             editClassID.requestFocus();
//             return;
//        }
//
//        if(txt_GradeNum.isEmpty()) {
////            Message.showMessage(getApplicationContext(),"Please enter Grade Number!");
//            editGradeNum.setError("Please enter Grade Number!");
//            editGradeNum.requestFocus();
//            return;
//        }
//
//        if(txt_ClassNum.isEmpty()) {
////          Message.showMessage(getApplicationContext(), "Please enter Class Number!");
//             editClassNum.setError("Please enter Class Number!");
//             editClassID.requestFocus();
//             return;
//        }


//        FirebaseDatabase.getInstance().getReference().child("Classes").child(txt_ID).child("Name").setValue(className);//.push();
//        String stringListOfStudentID = addStudentsArrowDown.getText().toString();
//        List<String> stringListOfStudentIDList = Arrays.asList(stringListOfStudentID.split(","));
//        for (String id: stringListOfStudentIDList ){
//            System.out.println(id);
//            FirebaseDatabase.getInstance().getReference().child("Classes").child(txt_ID).child("Student").child(id).setValue("");
//        }
//        //.push();
//        Message.showMessage(getApplicationContext(),"Added new class successfully!");

    }


    public void multipleCheckStudents() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, studentListList);
        DatabaseReference classInfoRef = FirebaseDatabase.getInstance().getReference().child("Users");
        classInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                studentListList.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    String userType = child.child("type").getValue().toString();
                    if (userType.equals("student")) {
                        String studentID = child.child("IDNumber").getValue().toString();
                        studentListList.add(studentID);
                        System.out.println(studentListList.size());
                    }
                }
                adapter.notifyDataSetChanged();
                if (!studentListList.isEmpty()) {
                    Message.showMessage(getApplicationContext(), "Listlist not Empty!");
//                    return;
                }

                studentArray = new String[studentListList.size()];

                for (int q = 0; q < studentListList.size(); q++) {
                    studentArray[q] = studentListList.get(q);
                }
                if (studentArray == null) {
                    Message.showMessage(getApplicationContext(), "Array Null!");
                    return;
                }

                int studentArrayLength = studentArray.length;
                selectedStudent = new boolean[studentArrayLength];

                //Init alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        AdminAddNewClassActivity.this
                );
                //Set title
                builder.setTitle("Please Select from the Following Student IDs:");
                //Set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(studentArray, selectedStudent, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        //Check condition
                        if (isChecked) {
                            //When checkbox selected
                            //Add position in student list
                            studentList.add(which);
                            //Sort student list
                            Collections.sort(studentList);
                        } else {
                            //When checkbox unselected
                            //Remove position from list
                            studentList.remove(which);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Init string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        //Use for loop
                        for (int j = 0; j < studentList.size(); j++) {
                            //Concat array value
                            stringBuilder.append(studentArray[studentList.get(j)]);
                            //Check condition
                            if (j != studentList.size() - 1) {
                                //When j value not equal to student list size -1
                                //Add comma
                                stringBuilder.append(",");
                            }
                        }
                        //Set text on text view
                        addStudentsArrowDown.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismiss Dialog
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Use for loop
                        for (int i = 0; i < selectedStudent.length; i++) {
                            //Remove all selected
                            selectedStudent[i] = false;
                            //Clear student list
                            studentList.clear();
                            //Clear text view value
                            addStudentsArrowDown.setText("");
                        }
                    }
                });
                Message.showMessage(getApplicationContext(), "Almost");
//                return;
                //Show dialog
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

}// class