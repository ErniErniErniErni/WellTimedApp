package com.erniwo.timetableconstruct.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminEditClassTimeTableActivity extends AppCompatActivity implements View.OnClickListener{

    private String currentClassID;
    private String currentClassName;

    private Button saveButton;
    private EditText editTextSubject;
    private EditText editTextLocation;
    private AutoCompleteTextView editTextDayOfWeek;
    private AutoCompleteTextView editTextPeriod;
    private AutoCompleteTextView editTextTeacherID;

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

        // init elements
        editTextSubject = (EditText) findViewById(R.id.edit_class_ttb_edit_text_subject);
        editTextLocation = (EditText) findViewById(R.id.edit_class_ttb_edit_text_location);
        editTextDayOfWeek = (AutoCompleteTextView) findViewById(R.id.edit_class_ttb_edit_text_day_of_week);
        editTextPeriod = (AutoCompleteTextView) findViewById(R.id.edit_class_ttb_edit_text_period);
        editTextTeacherID = (AutoCompleteTextView) findViewById(R.id.edit_class_ttb_edit_text_teacher);
        saveButton = (Button) findViewById(R.id.edit_add_new_class_button);
        saveButton.setOnClickListener(this);

        // adapt array to select Day of Week
        String[] listOfDaysOfWeek = getResources().getStringArray(R.array.select_day_of_week);
        ArrayAdapter arrayAdapterDaysOfWeek = new ArrayAdapter(getApplicationContext(),
                R.layout.dropdown_item,listOfDaysOfWeek);
        editTextDayOfWeek.setAdapter(arrayAdapterDaysOfWeek);

        // adapt array to select Period of Day
        String[] listOfPeriods = getResources().getStringArray(R.array.select_period_of_day);
        ArrayAdapter arrayAdapterPeriod = new ArrayAdapter(getApplicationContext(),
                R.layout.dropdown_item,listOfPeriods);
        editTextPeriod.setAdapter(arrayAdapterPeriod);

        // adapt array to select Teacher
        ArrayList<String> teacherIDArray = new ArrayList<>();
//        ArrayList<String> teacherNameArray = new ArrayList<>();
        ArrayAdapter arrayAdapterTeachersID = new ArrayAdapter<String>(this,
                R.layout.activity_listview, teacherIDArray);
        editTextTeacherID.setAdapter(arrayAdapterTeachersID);

        // pull all teacher's ID from database and add them to ArrayList
        DatabaseReference classInfoRef = FirebaseDatabase.getInstance().getReference().child("Teachers");
        classInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                teacherIDArray.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                        try {
                            String teacherName = child.getKey().trim();
                            teacherIDArray.add(teacherName);
                        } catch (NullPointerException e) {
                            Log.e(TAG, "idnumber Null");
                        }
                }
                arrayAdapterTeachersID.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            case R.id.edit_add_new_class_button:
                saveInfoAndLeave();
                break;
        }
    }

    private void saveInfoAndLeave() {
        String subject = editTextSubject.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String dayOfWeek = editTextDayOfWeek.getText().toString().trim();
        String period = editTextPeriod.getText().toString().trim();
        String idnumber = editTextTeacherID.getText().toString().trim();

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

        if(idnumber.isEmpty()) {
            editTextTeacherID.setError("Please enter a teacher's name.");
            editTextTeacherID.requestFocus();
            return;
        }

        String lessonID = period + dayOfWeek;

        course.setSubject(subject);
        course.setLocation(location);
        course.setDayOfWeek(dayOfWeek);
        course.setPeriod(period);
        course.setidnumber(idnumber);
//        course.set

        // sync newly added lesson information to current class
        DatabaseReference courseIDRef = FirebaseDatabase.getInstance().getReference()
                .child("Classes").child(currentClassID).child("timetable").child(lessonID);
        courseIDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseIDRef.setValue(course);
                Message.showMessage(getApplicationContext(), "Lesson info saved successfully.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Message.showMessage(getApplicationContext(), "Failed to save course info.");
            }
        });

        // sync newly added lesson information to selected teacher
        DatabaseReference teacherIDRef = FirebaseDatabase.getInstance().getReference()
                .child("Teachers").child(idnumber).child("timetable").child(lessonID);
        teacherIDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherIDRef.setValue(course);
                Message.showMessage(getApplicationContext(), "Lesson info saved successfully.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Message.showMessage(getApplicationContext(), "Failed to save course info.");
            }
        });

        Intent intent = new Intent(getApplicationContext(), AdminManageClassTimetableActivity.class);
//        intent.putExtra("ClickedClassName", getCurrentClassName());
//        intent.putExtra("ClickedClassID", getCurrentClassID());
        startActivity(intent);
    }
}