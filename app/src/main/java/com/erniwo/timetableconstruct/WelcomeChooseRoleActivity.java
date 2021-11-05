package com.erniwo.timetableconstruct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeChooseRoleActivity extends AppCompatActivity implements View.OnClickListener{

    private Button studentLogin;
    private Button teacherLogin;
    private Button adminLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_choose_role);

        // initialize the "Student Login"
        studentLogin = (Button)findViewById(R.id.StudentLoginButton);
        //set on click listener to the "Student Login"
        studentLogin.setOnClickListener(this);

        teacherLogin = (Button)findViewById(R.id.StudentLoginButton);
        studentLogin.setOnClickListener(this);

        adminLogin = (Button)findViewById(R.id.AdminLoginButton);
        adminLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.StudentLoginButton:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.TeacherLoginButton:
//                startActivity(new Intent());
                break;
            case R.id.AdminLoginButton:
//                startActivity(new Intent());
                break;
        }
    }
}