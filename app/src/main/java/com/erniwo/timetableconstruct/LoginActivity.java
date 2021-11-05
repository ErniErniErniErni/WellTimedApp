package com.erniwo.timetableconstruct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

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
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize the "SignIn"
        signUp = (TextView) findViewById(R.id.signUp);
        //set on click listener to the "Sign Up"
        signUp.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
            editTextPassword.setError("Minimun password length is 6 characters.");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility((View.VISIBLE));

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    onAuthSuccess(task.getResult().getUser());
                    startActivity((new Intent(LoginActivity.this, TimetableStudentActivity.class)));
                    Toast.makeText(LoginActivity.this, "",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this, "Failed to login.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {
        if (user != null) {
            ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(user.getUid()).child("role");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    if(Integer.parseInt(value) == 1) {
                        startActivity(new Intent(LoginActivity.this,TimetableStudentActivity.class));
                        Toast.makeText(LoginActivity.this,"Logged in successfully as a student.",Toast.LENGTH_LONG).show();
                        finish();
//                    }else(Integer.parseInt(value) == 2) {
//                        startActivity(new Intent(MainActivity.this, .class));
//                        Toast.makeText(MainActivity.this,"Logged in successfully as a teacher.",Toast.LENGTH_LONG).show();
//                        finish();
//                    }else {
//                        startActivity(new Intent(MainActivity.this, .class));
//                        Toast.makeText(MainActivity.this,"Logged in successfully as an admin.",Toast.LENGTH_LONG).show();
//                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}









