package com.erniwo.timetableconstruct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.erniwo.timetableconstruct.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView signUp;
    private TextView forgotPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:
                startActivity(new Intent(this, SignUp.class));
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
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

        //varify valid email
//        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            editTextEmail.setError("Please enter a valid email.");
//            editTextEmail.requestFocus();
//            return;
//        }

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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //check if the email address has been verified or not
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//                    if(user.isEmailVerified()) {
                        //redirect to user profile
                        startActivity((new Intent(MainActivity.this, TimetableStudent.class)));
//                    }else {
//                        user.sendEmailVerification();
//                        Toast.makeText(MainActivity.this, "Check your email to verify the account", Toast.LENGTH_LONG).show();
//                    }
                }else {
                    Toast.makeText(MainActivity.this, "Failed to login.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}









