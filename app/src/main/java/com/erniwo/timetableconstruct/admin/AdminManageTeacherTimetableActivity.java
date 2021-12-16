package com.erniwo.timetableconstruct.admin;

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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminManageTeacherTimetableActivity extends AppCompatActivity {

    private String currentTeacherName;
    private String currentTeacherID;
    private TextView nameOfTeacher;
    private TextView addButton;
    private FrameLayout frameLayoutLessonSection;
    private TextView[] mClassNumHeaders = null;
    private LinearLayout headerClassNumLl;
    ArrayList<String> lessonKeyList = new ArrayList<String>();

    private String TAG = "AdminManageTeacherTimeTableActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_teacher_timetable);

        Log.d(TAG, "onCreate");

        // get value from last page (AdminManageListOfTeachers.class)
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String currTeacherName = (String) bundle.get("ClickedTeacherName");
            String currTeacherID = (String) bundle.get("ClickedTeacherID");
            setCurrentTeacherName(currTeacherName);
            setCurrentTeacherID(currTeacherID);
        }

        // init elements
        headerClassNumLl = findViewById(R.id.ll_header_class_num);
        nameOfTeacher = (TextView) findViewById(R.id.name_of_teacher) ;
        addButton = (TextView) findViewById(R.id.add_button);
        frameLayoutLessonSection = (FrameLayout) findViewById(R.id.frame_layout_lesson_section);

        nameOfTeacher.setText("Timetable of " + getCurrentTeacherName());
        addButton.setVisibility(INVISIBLE);
    }// onCreate

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

        pullExistingLessonsFromDatabaseAndInitLessonsOnTimetable();

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

    private void pullExistingLessonsFromDatabaseAndInitLessonsOnTimetable() {

        String teacherID = getCurrentTeacherID();

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

        DatabaseReference teachersRef = FirebaseDatabase.getInstance().getReference().child("Teachers");
        teachersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (int r = 0; r < 9; ++r) {

                    TableRow row = new TableRow(context);

                    for (int c = 0; c < 7; ++c) {

                        Button btn = (Button) layoutInflater.inflate(R.layout.item_lesson_card, null);

                        String currentLessonKey = String.valueOf(r + 1) + String.valueOf(c + 1);

                        btn.setTextSize(9);

                        String lessonInfoToBeDisplayedOnLessonCard;
                        Map<String,String> lessonInfoToBeDisplayedOnLessonCardMap = new HashMap<>();

                        // iterate all teacher's info saved in firebase database to get current teacher's timetable
                        for (DataSnapshot teacherIdChild: snapshot.getChildren()) {
                            try {
                                if (teacherID.equals(teacherIdChild.getKey())) {
                                    String teacherName = teacherIdChild.child("name").getValue().toString().trim();
                                    Log.d(TAG, "Current teacher Name: " + teacherName);

                                    for (DataSnapshot timetableChild : teacherIdChild.child("timetable").getChildren()) {

                                        String subject = timetableChild.child("subject").getValue().toString().trim();
                                        String location = timetableChild.child("location").getValue().toString().trim();
                                        String lessonKey = timetableChild.getKey().trim();

                                        lessonInfoToBeDisplayedOnLessonCard = subject + "\n\n" + location;
                                        lessonKeyList.add(lessonKey);
                                        lessonInfoToBeDisplayedOnLessonCardMap.put(lessonKey, lessonInfoToBeDisplayedOnLessonCard);

                                    } // timetableChild
                                }
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }

                        } // teacherIdChild

                        if (lessonInfoToBeDisplayedOnLessonCardMap.containsKey(currentLessonKey)) {
                            String lessonInfo = lessonInfoToBeDisplayedOnLessonCardMap.get(currentLessonKey);
                            Log.d(TAG, "textOnLessonButton"+ lessonInfo);
                            btn.setText(lessonInfo);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AdminManageTeacherTimetableActivity.this,
                                            AdminLessonDetailsActivity.class);
                                    startActivity(intent);
                                }
                            });
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

        }); // currentTeacherTtbRef.addValueEventListener

    } // pullExistingLessonsFromDatabaseAndInitLessonsOnTimetable


    public String getCurrentTeacherName() {
        return currentTeacherName;
    }

    public void setCurrentTeacherName(String currentTeacherName) {
        this.currentTeacherName = currentTeacherName;
    }
    public String getCurrentTeacherID() {
        return currentTeacherID;
    }

    public void setCurrentTeacherID(String currentTeacherID) {
        this.currentTeacherID = currentTeacherID;
    }

}