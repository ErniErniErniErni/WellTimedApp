package com.erniwo.timetableconstruct.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.erniwo.timetableconstruct.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class TeacherTimetableActivity extends AppCompatActivity {

    private TextView nameOfUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDb;
    private String userKey;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_timetable);

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

        nameOfUser = (TextView) findViewById(R.id.NameOfUser);
//        nameOfUser.setText( currentUsername+ "'s Timetable");
        nameOfUser.setText( "Teacher's Timetable");

    }
}