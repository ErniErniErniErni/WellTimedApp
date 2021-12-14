package com.erniwo.timetableconstruct.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.admin.AdminManageListOfClassesActivity;
import com.erniwo.timetableconstruct.admin.AdminManageListOfTeachersActivity;
import com.erniwo.timetableconstruct.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentTimetableActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nameOfUser;
    private ImageView logout;
    private Button tryButton;
    private String currentID;

    private String TAG = "StudentTimetableActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_timetable);

        nameOfUser = findViewById(R.id.name_of_user_student);
        logout = findViewById(R.id.logout_icon);
        logout.setOnClickListener(this);
        tryButton = (Button) findViewById(R.id.course11);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setCurrentID(snapshot.child(currentUserId).child("IDNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadStudentName();
        pullExistingClasses();

    } // onCreate

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


    private void pullExistingClasses() {
        //Pull timetable from backend
        String currentStudentID = getCurrentID();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Classes");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()) {
                    if(child.child("Student").child(getCurrentID()).exists()){
                        String classID = child.getKey();
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Classes")
                                .child(classID).child("Timetable");
                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                for(DataSnapshot child2 : snapshot2.getChildren()){

                                    String subject = child2.child("Subject").getValue().toString();
                                    String location = child2.child("Location").getValue().toString();
                                    String teacher = child2.child("Teacher").getValue().toString();
                                    String dayOfWeek = child2.child("DayOfWeek").getValue().toString();
                                    String period = child2.child("Period").getValue().toString();

                                    String textOnCourseCard = subject + "\n" + location + "\n" + teacher;
                                    tryButton.setText(textOnCourseCard);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LoadCardButton();//image?
    }

    private void LoadCardButton() {
        //Pull lecture info

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_icon:
                logout();
                break;
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Message.showMessage(getApplicationContext(),"You have logged out!");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void loadStudentName() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child(userID).child("name").getValue().toString();
                setCurrentID(snapshot.child(userID).child("IDNumber").getValue().toString());
                nameOfUser.setText( userName + "'s Timetable");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getCurrentID() {
        return currentID;
    }

    public void setCurrentID(String sID) {

        this.currentID = sID;
    }
}

