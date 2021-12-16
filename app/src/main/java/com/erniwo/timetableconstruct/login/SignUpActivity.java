package com.erniwo.timetableconstruct.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.erniwo.timetableconstruct.Admin;
import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.model.Admini;
import com.erniwo.timetableconstruct.model.Student;
import com.erniwo.timetableconstruct.model.Teacher;
import com.erniwo.timetableconstruct.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView header;
    private TextView signUpButton;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTestID;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    Student student;
    Teacher teacher;
    Admini admin;

    private String TAG = "SignUpActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.d(TAG, "onCreate");
        student = new Student();
        teacher = new Teacher();
        admin = new Admini();

        // init elements
        header = (TextView) findViewById(R.id.header);
        editTextName = (EditText) findViewById(R.id.signup_edit_text_name);
        editTextEmail = (EditText) findViewById(R.id.signup_edit_text_email);
        editTextPassword = (EditText) findViewById(R.id.signup_edit_text_password);
        editTestID = (EditText) findViewById(R.id.signup_edit_text_id);
        radioGroup = (RadioGroup) findViewById(R.id.signup_chooseRoleGroup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        // onClick action
        header.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

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
            case R.id.header:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.signUpButton:
                signUpNewUser();
                break;
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }

    private void signUpNewUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().toLowerCase(Locale.ROOT).trim();
        String password = editTextPassword.getText().toString().trim();
        String idnumber = editTestID.getText().toString().toUpperCase(Locale.ROOT).trim();
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if(name.isEmpty()) {
            editTextName.setError("Please enter your name!");
            editTextName.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editTextEmail.setError("Please enter your email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Please enter your password!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Please enter a password that is longer than 6 digits!");
            editTextPassword.requestFocus();
            return;
        }

        if(idnumber.isEmpty()) {
            editTestID.setError("Please enter your ID Number");
            editTestID.requestFocus();
            return;
        }

        if(idnumber.length() != 5) {
            editTestID.setError("Please enter a valid ID Number with 5 characters!");
            editTestID.requestFocus();
            return;
        }

        String type;

        if(checkedId == -1) {
            Message.showMessage(getApplicationContext(),"Please choose your role!!");
            return;
        }else if (checkedId == R.id.radiobutton_student){
            type = "student";
        }else if (checkedId == R.id.radiobutton_teacher){
            type = "teacher";
        }else if (checkedId == R.id.radiobutton_admin){
            type = "admin";
        }else {
            Message.showMessage(getApplicationContext(), "Something is wrong, please try again!");
            type = "0";
        }

        progressBar.setVisibility(View.VISIBLE);

        // check if an user already exists in the database (has registered before)
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    List<String> signInMethods = result.getSignInMethods();
                    if(signInMethods.isEmpty()) {
                        return;
                    }else {
                        editTextEmail.setError("This email is already registered, please login.");
                        editTextEmail.requestFocus();
                    }
                }else {
                    Log.e(TAG, "Error getting sign in methods for user", task.getException());
                }
            }
        });

        // create new user in the database
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            onAuthSuccess(task.getResult().getUser());

                            User user = new User(name, email, type, idnumber);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "User signed up successfully.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        switch (type) {
                                            case "student":
                                                student.setEmail(email);
                                                student.setName(name);
                                                student.setIdnumber(idnumber);
                                                student.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("Students")
                                                        .child(idnumber);
                                                studentsRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        studentsRef.setValue(student);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;
                                            case "teacher":
                                                teacher.setEmail(email);
                                                teacher.setName(name);
                                                teacher.setIdnumber(idnumber);
                                                teacher.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                DatabaseReference teachersRef = FirebaseDatabase.getInstance().getReference("Teachers")
                                                        .child(idnumber);
                                                teachersRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        teachersRef.setValue(teacher);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;
                                            case "admin":
                                                admin.setEmail(email);
                                                admin.setName(name);
                                                admin.setIdnumber(idnumber);
                                                admin.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference("Admins")
                                                        .child(idnumber);
                                                adminsRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        adminsRef.setValue(admin);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;
                                        }

                                        // log out to avoid automatically logging in immediately after signing up
                                        FirebaseAuth.getInstance().signOut();

                                        //redirect to login page
                                        startActivity((new Intent(SignUpActivity.this,LoginActivity.class)));
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to sign up. Try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Failed to sign up.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user){
//        writeNewUser();
    }
}// class






















