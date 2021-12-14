package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button viewClassTimetable;
    private Button viewTeacherTimetable;
    private ImageView logout;

    private String TAG = "AdminMainActivityLog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        viewClassTimetable = findViewById(R.id.manage_class_timetable);
        viewClassTimetable.setOnClickListener(this);

        viewTeacherTimetable = findViewById(R.id.manage_teacher_timetable);
        viewTeacherTimetable.setOnClickListener(this);

        logout = findViewById(R.id.logout_icon);
        logout.setOnClickListener(this);
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
            case R.id.manage_class_timetable:
                startActivity(new Intent(this, AdminManageListOfClassesActivity.class));
                break;
            case R.id.manage_teacher_timetable:
                startActivity(new Intent(this, AdminManageListOfTeachersActivity.class));
                break;
            case R.id.logout_icon:
                logout();
                break;
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Message.showMessage(getApplicationContext(),"You have logged out!");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }


}