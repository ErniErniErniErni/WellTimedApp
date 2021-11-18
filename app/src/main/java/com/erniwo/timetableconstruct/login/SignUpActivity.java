package com.erniwo.timetableconstruct.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView header;
    private TextView signUpButton;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        header = (TextView) findViewById(R.id.header);
        header.setOnClickListener(this);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        radioGroup = (RadioGroup) findViewById(R.id.chooseRoleGroup);

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
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if(name.isEmpty()) {
            editTextName.setError("Please imput your name!");
            editTextName.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editTextEmail.setError("Please imput your email!");
            editTextEmail.requestFocus();
            return;
        }

//        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            editTextEmail.setError("Please imput valid email!");
//            editTextEmail.requestFocus();
//            return;
//        }

        if(password.isEmpty()) {
            editTextPassword.setError("Please imput your password!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Please imput a password that is longer than 6 digits!");
            editTextPassword.requestFocus();
            return;
        }

//        if(radioGroup.getCheckedRadioButtonId() == -1) {
//            Message.showMessage(getApplicationContext(),"Please choose your role!!");
////            Toast.makeText(SignUpActivity.this, "Please choose your role!!", Toast.LENGTH_LONG).show();
//            return;
//        }
        int type;

        if(checkedId == -1) {
            Message.showMessage(getApplicationContext(),"Please choose your role!!");
//            type = 0;
            return;
        }else if (checkedId == R.id.radiobutton_student){
            type = 1;
        }else if (checkedId == R.id.radiobutton_teacher){
            type = 2;
        }else if (checkedId == R.id.radiobutton_admin){
            type = 3;
        }else {
            Message.showMessage(getApplicationContext(), "Something is wrong, please try again!");
            type = 0;
        }

        progressBar.setVisibility(View.VISIBLE);

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
//                        Message.showMessage(getApplicationContext(),"This email exists, please login");
//                    if(signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)){
//                        // User can sign in with email/password
//                    } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
//                        // User can sign in with email/link
                    }
                }else {
                    Log.e(TAG, "Error getting sign in methods for user", task.getException());
                }
            }
        });

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());

                            User user = new User(name, email, type);

                            //The authentication stuff is added to the build.gradle before this step
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "User signed up successfully.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        //redirect to login page
                                        startActivity((new Intent(SignUpActivity.this,LoginActivity.class)));
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to sign up. Try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(SignUpActivity.this, "Failed to sign up.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user){
//        writeNewUser();
    }
}






















