package com.erniwo.timetableconstruct.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class TeacherTimetableActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView nameOfUser;
    private ImageView logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDb;
    private String userKey;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_timetable);

        logout = findViewById(R.id.logout_icon);
        logout.setOnClickListener(this);
        nameOfUser = findViewById(R.id.name_of_user_teacher);

        loadTeacherName();


//        firebaseAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance();
//        mDb = mDatabase.getReference();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        userKey = user.getUid();
//
//        mDb.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                currentUsername = snapshot.child("type").getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



    }

    private void loadTeacherName() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child(userID).child("name").getValue().toString();
                nameOfUser.setText( userName + "'s Timetable");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_icon:
                logout();
                break;
        }
    }

//    public String getTeacherName(){
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userID = user.getUid();
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String userName = snapshot.child(userID).child("name").getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return userName;
//        String userID = user.getUid();
////        ref.child(userID).get
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getValue() == userID) {
//                    String currentUserName = snapshot.child("name").toString();
//                    return currentUserName;
//                }
//                adapter
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Message.showMessage(getApplicationContext(),"You have logged out!");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}