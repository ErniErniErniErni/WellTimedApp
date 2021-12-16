package com.erniwo.timetableconstruct.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.admin.AdminMainActivity;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.student.StudentTimetableActivity;
import com.erniwo.timetableconstruct.teacher.TeacherTimetableActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView signUp;
    private TextView forgotPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private View studentButton;

    private String TAG = "LoginActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate");

        // init elements
        editTextEmail = findViewById(R.id.login_edit_text_email);
        editTextPassword = findViewById(R.id.login_edit_text_password);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);
        radioGroup = findViewById(R.id.chooseRoleGroup);
        login = findViewById(R.id.loginButton);

        // onClick action
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.loginButton:
                try {
                    userLogin();
                }catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                break;
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if(email.isEmpty()) {
            editTextEmail.setError("Please input Email.");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Please input password.");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Minimum password length is 6 characters.");
            editTextPassword.requestFocus();
            return;
        }

        if(checkedId == -1) {
            Message.showMessage(getApplicationContext(),"Please choose your role!");
            return;
        }

        progressBar.setVisibility((View.VISIBLE));

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    onAuthSuccess(task.getResult().getUser());
                }else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Message.showMessage(LoginActivity.this,"Failed to Authenticate.");
                    progressBar.setVisibility((View.INVISIBLE));
                }
            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String RegisteredUserID = currentUser.getUid();


        DatabaseReference aDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
        aDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int checkedId = radioGroup.getCheckedRadioButtonId();
                String type;

                if (checkedId == R.id.radiobutton_student){
                    type = "student";
                }else if (checkedId == R.id.radiobutton_teacher){
                    type = "teacher";
                }else if (checkedId == R.id.radiobutton_admin){
                    type = "admin";
                }else {
                    Message.showMessage(getApplicationContext(), "Something is wrong, please try again!");
                    type = "0";
                }

                String userType = dataSnapshot.child("type").getValue().toString();

                if(userType.equals(type)) {
                    if (userType.equals("student")) {
                        startActivity(new Intent(LoginActivity.this, StudentTimetableActivity.class));
                        Toast.makeText(LoginActivity.this, "Logged in successfully as a student.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility((View.INVISIBLE));
                        finish();
                    } else if (userType.equals("teacher")) {
                        startActivity(new Intent(LoginActivity.this, TeacherTimetableActivity.class));
                        Toast.makeText(LoginActivity.this, "Logged in successfully as a teacher.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility((View.INVISIBLE));
                        finish();
                    } else if (userType.equals("admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                        Toast.makeText(LoginActivity.this, "Logged in successfully as an admin.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility((View.INVISIBLE));
                        finish();
                    }
                }else{
                    progressBar.setVisibility((View.INVISIBLE));
                    Message.showMessage(getApplicationContext(),"Please choose a correct role!");
                    return;
                }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            onAuthSuccessAutomatic(currentUser);
            Log.d(TAG, "AutoLogin");
        }else {
            Log.d(TAG, "No Current User");
            return;
        }
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

    private void onAuthSuccessAutomatic(FirebaseUser user) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String RegisteredUserID = currentUser.getUid();

        DatabaseReference aDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);
        aDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userType = dataSnapshot.child("type").getValue().toString();

                if (userType.equals("student")) {
                    startActivity(new Intent(LoginActivity.this, StudentTimetableActivity.class));
                    Toast.makeText(LoginActivity.this, "Logged in successfully as a student.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility((View.INVISIBLE));
                    finish();
                } else if (userType.equals("teacher")) {
                    startActivity(new Intent(LoginActivity.this, TeacherTimetableActivity.class));
                    Toast.makeText(LoginActivity.this, "Logged in successfully as a teacher.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility((View.INVISIBLE));
                    finish();
                } else if (userType.equals("admin")) {
                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    Toast.makeText(LoginActivity.this, "Logged in successfully as an admin.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility((View.INVISIBLE));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}










