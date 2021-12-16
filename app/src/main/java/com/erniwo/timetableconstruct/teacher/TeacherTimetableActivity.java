package com.erniwo.timetableconstruct.teacher;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherTimetableActivity extends AppCompatActivity implements View.OnClickListener{

    private String currentTeacherName;
    private String currentTeacherID;
    private TextView nameOfTeacher;
    private FrameLayout frameLayoutLessonSection;
    private TextView[] mClassNumHeaders = null;
    private ImageView logout;
    ArrayList<String> lessonKeyList = new ArrayList<String>();

    private String TAG = "TeacherTimetableActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_timetable);

        Log.d(TAG, "onCreate");

        // init elements
        nameOfTeacher = findViewById(R.id.name_of_teacher_header);
        frameLayoutLessonSection = (FrameLayout) findViewById(R.id.frame_layout_lesson_section);
        logout = findViewById(R.id.logout_icon);

        // onCLick actions
        logout.setOnClickListener(this);

        loadTeacherName();
//        loadClassOfCurrentTeacher();

    } // onCreate

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        try{
            pullExistingLessonsFromDatabaseAndInitLessonsOnTimetable();
        }catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }

    private void pullExistingLessonsFromDatabaseAndInitLessonsOnTimetable() {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Context context = getApplicationContext();

        TableLayout tableLayout = new TableLayout(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tableLayout.setLayoutParams(lp);
        tableLayout.setStretchAllColumns(true);

        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
//                        rowLp.bottomMargin = 2;
        rowLp.weight = 1;
        cellLp.topMargin = 4;
        cellLp.leftMargin = 6;
        cellLp.weight = 1;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (int r = 0; r < 9; ++r) {

                    TableRow row = new TableRow(context);
                    row.setBaselineAligned(false);

                    for (int c = 0; c < 7; ++c) {

                        Button btn = (Button) layoutInflater.inflate(R.layout.item_lesson_card, null);
                        btn.setPadding(0,0,0,0);
                        btn.setIncludeFontPadding(false);
                        String currentLessonKey = String.valueOf(r + 1) + String.valueOf(c + 1);

                        String lessonInfoToBeDisplayedOnLessonCard;
                        Map<String,String> lessonInfoToBeDisplayedOnLessonCardMap = new HashMap<>();

                        // iterate all classes' info saved in firebase database to get the current class's timetable
                        for (DataSnapshot classIdChild: snapshot.child("Classes").getChildren()) {
                            try {
                                for (DataSnapshot classTimetableChild: classIdChild.child("timetable").getChildren()) {

                                    String teacherId = classTimetableChild.child("idnumber").getValue().toString().trim();

                                    if(getCurrentTeacherID().equals(teacherId)) {
                                        String subject = classTimetableChild.child("subject").getValue().toString().trim();
                                        String location = classTimetableChild.child("location").getValue().toString().trim();
                                        String classId = classIdChild.getKey();
                                        String lessonKey = classTimetableChild.getKey().trim();

                                        Log.d(TAG, "Current lesson key: " + lessonKey);
                                        lessonKeyList.add(lessonKey);

                                        lessonInfoToBeDisplayedOnLessonCard = subject + "\n\n" + location + "\n\n" + classId;
                                        lessonInfoToBeDisplayedOnLessonCardMap.put(lessonKey, lessonInfoToBeDisplayedOnLessonCard);
                                    }
                                }
                            }catch (Exception e){
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } // classIdChild

                        if (lessonInfoToBeDisplayedOnLessonCardMap.containsKey(currentLessonKey)) {
                            String lessonInfo = lessonInfoToBeDisplayedOnLessonCardMap.get(currentLessonKey);
                            Log.d(TAG, "textOnLessonButton"+ lessonInfo);
                            btn.setText(lessonInfo);
                            Log.d(TAG, "Button text set");
                            btn.setVisibility(VISIBLE);
                            Log.d(TAG, "Button set to VISIBLE");
                            if(row.getParent() != null) {
                                ((ViewGroup)row.getParent()).removeView(row);
                            }
                            row.addView(btn, cellLp);
                            Log.d(TAG, "Added cell to row, visible");

                        } else {

                            btn.setText(currentLessonKey);
                            btn.setVisibility(INVISIBLE);
                            Log.d(TAG, "Button set to INVISIBLE");
                            if(row.getParent() != null) {
                                ((ViewGroup)row.getParent()).removeView(row);
                            }
                            row.addView(btn, cellLp);
                            Log.d(TAG, "Added cell to row");

                        }

                    }
                    tableLayout.addView(row, rowLp);
                    Log.d(TAG, "Added row to table");
                }
                frameLayoutLessonSection.addView(tableLayout);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            } // onDataChange

        }); // currentClassTtbRef.addValueEventListener

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

    private void loadTeacherName() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()) {
                    Log.d(TAG, "child: " + child);
                    if(userID.equals(child.getKey())) {
                        setCurrentTeacherName(child.child("name").getValue().toString().trim());
                        setCurrentTeacherID(child.child("idnumber").getValue().toString().trim());
                        nameOfTeacher.setText(getCurrentTeacherName() + "'s Timetable");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    private void loadClassOfCurrentTeacher() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        Log.d(TAG, "ref value: "+ ref.child("Classes").getKey());
//        ref.child("Classes").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot classIdChild: snapshot.getChildren()) {
//
//                    Log.d(TAG, "classIdChild value: "+ classIdChild.getValue().toString());
//                    try {
//                        Log.d(TAG, "classIdChildTeacher value: "+ classIdChild.child("student").getValue().toString());
//
//                        for(DataSnapshot classInfoChild: classIdChild.child("student").getChildren()) {
//                            Log.d(TAG, "classInfoChild value: "+ classInfoChild.getKey());
//                            if (getCurrentTeacherID().equals(classInfoChild.getKey())) {
////                                setCurrentClassIdOfStudent(classIdChild.getKey());
////                                setCurrentClassNameOfStudent(classIdChild.child("name").getValue().toString().trim());
//                            }
//                        }
//                    }catch (Exception e){
//                        Log.e(TAG, Log.getStackTraceString(e));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }



    public String getCurrentTeacherName() {
        return currentTeacherName;
    }

    public String getCurrentTeacherID() {
        return currentTeacherID;
    }

    public void setCurrentTeacherName(String currentTeacherName) {
        this.currentTeacherName = currentTeacherName;
    }

    public void setCurrentTeacherID(String currentTeacherID) {
        this.currentTeacherID = currentTeacherID;
    }

//    public void setCurrentClassNameOfStudent(String currentClassNameOfStudent) {
//        this.currentClassNameOfStudent = currentClassNameOfStudent;
//    }
//
//    public void setCurrentClassIdOfStudent(String currentClassIdOfStudent) {
//        this.currentClassIdOfStudent = currentClassIdOfStudent;
//    }
//
//    public String getCurrentClassNameOfStudent() {
//        return currentClassNameOfStudent;
//    }
//
//    public String getCurrentClassIdOfStudent() {
//        return currentClassIdOfStudent;
//    }
}