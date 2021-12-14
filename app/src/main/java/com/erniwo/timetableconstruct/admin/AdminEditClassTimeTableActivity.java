package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.admin.AdminManageListOfClassesActivity;
import com.erniwo.timetableconstruct.login.LoginActivity;
import com.erniwo.timetableconstruct.login.SignUpActivity;
import com.erniwo.timetableconstruct.model.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdminEditClassTimeTableActivity extends AppCompatActivity implements View.OnClickListener{

    private String currentClassID;
    private String currentClassName;

    private TextView saveButton;

    private EditText editTextSubject;
    private EditText editTextLocation;
    private EditText editTextDayOfWeek;
    private EditText editTextPeriod;
    private EditText editTextTeacher;

    Course course;

    private String TAG = "AdminEditClassTimeTableActivityLog";

    public String getCurrentClassName() {
        return currentClassName;
    }

    public String getCurrentClassID() {
        return currentClassID;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public void setCurrentClassID(String currentClassID) {
        this.currentClassID = currentClassID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_class_time_table);

        Log.d(TAG, "onCreate");
        course = new Course();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String currClassName = (String) bundle.get("ClickedClassName");
            String currClassID = (String) bundle.get("ClickedClassID");
            setCurrentClassName(currClassName);
            setCurrentClassID(currClassID);
            Log.d(TAG, getCurrentClassName());
            Log.d(TAG, getCurrentClassID());
        }

        editTextSubject = (EditText) findViewById(R.id.edit_class_ttb_edit_text_subject);
        editTextLocation = (EditText) findViewById(R.id.edit_class_ttb_edit_text_location);
        editTextDayOfWeek = (EditText) findViewById(R.id.edit_class_ttb_edit_text_day_of_week);
        editTextPeriod = (EditText) findViewById(R.id.edit_class_ttb_edit_text_period);
        editTextTeacher = (EditText) findViewById(R.id.edit_class_ttb_edit_text_teacher);

        saveButton = (TextView) findViewById(R.id.edit_class_ttb_save_button);
        saveButton.setOnClickListener(this);

//        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
//        classesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot child: snapshot.getChildren()) {
//
//                    String childNameValue = child.child("Name").getValue().toString().trim();
//
//                    if (childNameValue.equals(currentClassName)) {
//                        currentClassID = child.getKey();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    } // onCreate

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
            case R.id.edit_class_ttb_save_button:
                saveInfoAndLeave();
                break;
        }
    }

    private void saveInfoAndLeave() {
        String subject = editTextSubject.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String dayOfWeek = editTextDayOfWeek.getText().toString().trim();
        String period = editTextPeriod.getText().toString().trim();
        String teacher = editTextTeacher.getText().toString().trim();

        if(subject.isEmpty()) {
            editTextSubject.setError("Please enter a subject.");
            editTextSubject.requestFocus();
            return;
        }

        if(location.isEmpty()) {
            editTextLocation.setError("Please enter a course location.");
            editTextLocation.requestFocus();
            return;
        }

        if(dayOfWeek.isEmpty()) {
            editTextDayOfWeek.setError("Please enter a day of week.");
            editTextDayOfWeek.requestFocus();
            return;
        }

        if(period.isEmpty()) {
            editTextPeriod.setError("Please enter a period.");
            editTextPeriod.requestFocus();
            return;
        }

        if(teacher.isEmpty()) {
            editTextTeacher.setError("Please enter a teacher's name.");
            editTextTeacher.requestFocus();
            return;
        }

//        Course course = new Course(subject, location, dayOfWeek, period, teacher);
        String courseID = dayOfWeek + period;

        course.setSubject(subject);
        course.setLocation(location);
        course.setDayOfWeek(dayOfWeek);
        course.setPeriod(period);
        course.setTeacher(teacher);

        DatabaseReference courseIDRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(currentClassID).child("Timetable").child(courseID);
        courseIDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseIDRef.setValue(course);
                Message.showMessage(getApplicationContext(), "Course info saved successfully.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Message.showMessage(getApplicationContext(), "Failed to save course info.");
            }
        });
            Log.wtf(TAG,"wtff");
//        courseIDRef.child("Subject").setValue(subject);
//        courseIDRef.child("DayOfWeek").setValue(dayOfWeek);
//        courseIDRef.child("Period").setValue(period);
//        courseIDRef.child("Location").setValue(location);
//        courseIDRef.child("Teacher").setValue(teacher);
//        Message.showMessage(getApplicationContext(), "Course info saved successfully.");
        Intent intent = new Intent(getApplicationContext(), AdminManageClassTimetableActivity.class);
//        intent.putExtra("ClickedClassName", getCurrentClassName());
//        intent.putExtra("ClickedClassID", getCurrentClassID());
        startActivity(intent);
    }
}