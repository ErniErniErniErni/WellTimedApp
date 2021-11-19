package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AdminEditClassTimeTableActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView saveButton;

    private EditText editTextSubject;
    private EditText editTextDayOfWeek;
    private EditText editTextLocation;
    private EditText editTextPeriod;
    private EditText editTextTeacher;

    private String currentClassID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_class_time_table);

        editTextSubject = (EditText) findViewById(R.id.editText_subject);
        editTextDayOfWeek = (EditText) findViewById(R.id.editText_day_of_week);
        editTextLocation = (EditText) findViewById(R.id.editText_location);
        editTextPeriod = (EditText) findViewById(R.id.editText_period);
        editTextTeacher = (EditText) findViewById(R.id.editText_teacher);

        saveButton = (TextView) findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        String currentClassName = AdminManageListOfClassesActivity.getClickedClassName();
        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
        classesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {


                    String childNameValue = child.child("Name").getValue().toString().trim();
//                    Message.showMessage(getApplicationContext(),childNameValue);


                    if (childNameValue.equals(currentClassName)) {
                        Message.showMessage(getApplicationContext(),"Yeahhhhhh");
                        currentClassID = child.getKey();
//                        Message.showMessage(getApplicationContext(),getClickedClassID());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                saveInfoAndLeave();
                break;
        }
    }

    private void saveInfoAndLeave() {
        String subject = editTextSubject.getText().toString().trim();
        String dayOfWeek = editTextDayOfWeek.getText().toString().trim();
        String period = editTextPeriod.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String teacher = editTextTeacher.getText().toString().trim();

        if(subject.isEmpty()) {
            editTextSubject.setError("Please enter subject.");
            editTextSubject.requestFocus();
            return;
        }
        if(dayOfWeek.isEmpty()) {
            editTextDayOfWeek.setError("Please enter day of week.");
            editTextDayOfWeek.requestFocus();
            return;
        }
        if(location.isEmpty()) {
            editTextLocation.setError("Please enter course location.");
            editTextLocation.requestFocus();
            return;
        }
        if(period.isEmpty()) {
            editTextPeriod.setError("Please enter period.");
            editTextPeriod.requestFocus();
            return;
        }
        if(teacher.isEmpty()) {
            editTextTeacher.setError("Please enter teacher's name.");
            editTextTeacher.requestFocus();
            return;
        }
        Course course = new Course(subject, dayOfWeek, period, location, teacher);
        String courseID = dayOfWeek + period;

        DatabaseReference courseIDRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(currentClassID).child("Timetable").child(courseID);
        courseIDRef.child("Subject").setValue(subject);
        courseIDRef.child("DayOfWeek").setValue(dayOfWeek);
        courseIDRef.child("Period").setValue(period);
        courseIDRef.child("Location").setValue(location);
        courseIDRef.child("Teacher").setValue(teacher);
        Message.showMessage(getApplicationContext(), "Course info saved successfully.");


    }
}