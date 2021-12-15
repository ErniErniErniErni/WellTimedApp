package com.erniwo.timetableconstruct.admin;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//public class AdminManageClassTimetableActivity extends AppCompatActivity implements View.OnClickListener {
public class AdminManageClassTimetableActivity extends AppCompatActivity {


    private String currentClassName;
    private String currentClassID;
    private TextView nameOfClass;
    private TextView addButton;
    private FrameLayout frameLayoutLessonSection;
    private TextView[] mClassNumHeaders = null;
    private LinearLayout headerClassNumLl;
    ArrayList<String> lessonKeyList = new ArrayList<String>();

    private String TAG = "AdminManageClassTimeTableActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_class_timetable);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String currClassName = (String) bundle.get("ClickedClassName");
            String currClassID = (String) bundle.get("ClickedClassID");
            setCurrentClassName(currClassName);
            setCurrentClassID(currClassID);
        }

        // init elements
        headerClassNumLl = findViewById(R.id.ll_header_class_num);
        nameOfClass = (TextView) findViewById(R.id.name_of_class) ;
        addButton = (TextView) findViewById(R.id.add_button);
        frameLayoutLessonSection = (FrameLayout) findViewById(R.id.frame_layout_lesson_section);

        pullExistingLessonsFromDatabaseAndInitLessonsOnTimetable2();

        nameOfClass.setText("Timetable of " + getCurrentClassName());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminManageClassTimetableActivity.this, AdminEditClassTimeTableActivity.class);
                intent.putExtra("ClickedClassName", getCurrentClassName());
                intent.putExtra("ClickedClassID", getCurrentClassID());
                startActivity(intent);
            }
        });

    } // onCreate

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
//        pullExistingClasses();
//        setLessonsOnTimetable();
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

            String classID = getCurrentClassID();
            DatabaseReference refClasses = FirebaseDatabase.getInstance().getReference("Classes")
                    .child(classID)
                    .child("timetable");
            refClasses.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                    for (DataSnapshot child2 : snapshot2.getChildren()) {


                        String subject = child2.child("subject").getValue().toString();
                        String location = child2.child("location").getValue().toString();
                        // String period = child2.child("Period").getValue().toString();
                        // String dayOfWeek = child2.child("DayOfWeek").getValue().toString();
                        String teacherid = child2.child("idnumber").getValue().toString();
                        String lessonKey = child2.getKey().trim();
                        Log.d(TAG, "Current lesson key: " + lessonKey);

                        lessonKeyList.add(lessonKey);
                        Log.d(TAG, "Added new lesson to List");

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
                        cellLp.topMargin = 4;
                        cellLp.leftMargin = 6;
//                        cellLp.bottomMargin = 2;
//                        cellLp.rightMargin = 2;

                        for (int r = 0; r < 9; ++r) {

                            TableRow row = new TableRow(context);

                            for (int c = 0; c < 7; ++c) {

                                String currentLessonKey = String.valueOf(r + 1) + String.valueOf(c + 1);
                                Button inflatedButton = (Button) layoutInflater.inflate(R.layout.item_lesson_card, null);
                                inflatedButton.setPadding(0, 0, 0, 0);
                                if (lessonKeyList.contains(currentLessonKey)) {

                                    inflatedButton.setText("bb");
                                    inflatedButton.setVisibility(VISIBLE);
                                    Log.d(TAG, "Button set to VISIBLE");
                                    row.addView(inflatedButton, cellLp);
                                    Log.d(TAG, "Added cell to row, visible");


                                } else {
                                    inflatedButton.setText("");
                                    inflatedButton.setVisibility(VISIBLE);
                                    Log.d(TAG, "Button set to INVISIBLE");
                                    row.addView(inflatedButton, cellLp);
                                    Log.d(TAG, "Added cell to row");

                                }
                            }
                            tableLayout.addView(row, rowLp);
                            Log.d(TAG, "Added row to table");
                        }
                        frameLayoutLessonSection.addView(tableLayout);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void pullExistingLessonsFromDatabaseAndInitLessonsOnTimetable2() {

        String classID = getCurrentClassID();

//        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference().child("Classes");
//            DatabaseReference currentClassTtbRef = classesRef.child(classID).child("timetable");
//            currentClassTtbRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot child: snapshot.getChildren()) {
//                        try {
//                        String subject = child.child("subject").getValue().toString().trim();
//                        String location = child.child("location").getValue().toString().trim();
//                        String period = child.child("period").getValue().toString();
//                        String dayOfWeek = child.child("dayOfWeek").getValue().toString();
//                        String teacherid = child.child("idnumber").getValue().toString().trim();
//                        String lessonKey = child.getKey().trim();
//                        Log.d(TAG, "Current lesson key: " + lessonKey);
//                        String textOnLessonCard = subject + "\n" + location + "\n" + teacherid;
//                        }catch (Exception e){
//                            Log.e(TAG, Log.getStackTraceString(e));
//                        }






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
        cellLp.topMargin = 4;
        cellLp.leftMargin = 6;







        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference().child("Classes");
        DatabaseReference currentClassTtbRef = classesRef.child(classID).child("timetable");
        currentClassTtbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {






                for (int r = 0; r < 9; ++r) {

                    TableRow row = new TableRow(context);

                    for (int c = 0; c < 7; ++c) {

                        Button btn = (Button) layoutInflater.inflate(R.layout.item_lesson_card, null);

                        String currentLessonKey = String.valueOf(r + 1) + String.valueOf(c + 1);

                        btn.setTextSize(50);

                        String textOnLessonCard;
                        for (DataSnapshot child: snapshot.getChildren()) {
                            try {
                                String subject = child.child("subject").getValue().toString().trim();
                                String location = child.child("location").getValue().toString().trim();
                                String period = child.child("period").getValue().toString();
                                String dayOfWeek = child.child("dayOfWeek").getValue().toString();
                                String teacherid = child.child("idnumber").getValue().toString().trim();
                                String lessonKey = child.getKey().trim();
                                Log.d(TAG, "Current lesson key: " + lessonKey);
                                textOnLessonCard = subject + "\n" + location + "\n" + teacherid;
                                lessonKeyList.add(lessonKey);

                            }catch (Exception e){
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        }




                        if (lessonKeyList.contains(currentLessonKey)) {

                            btn.setText(currentLessonKey);
                            btn.setVisibility(VISIBLE);
                            Log.d(TAG, "Button set to VISIBLE");
                            row.addView(btn, cellLp);
                            Log.d(TAG, "Added cell to row, visible");


                        } else {

                            btn.setText(currentLessonKey);
                            btn.setVisibility(INVISIBLE);
                            Log.d(TAG, "Button set to INVISIBLE");
                            row.addView(btn, cellLp);
                            Log.d(TAG, "Added cell to row");

                        }

//                        if (lesson) {
//                            row.addView(btn, cellLp);
//                            btn.setVisibility(INVISIBLE);
//                            Log.d(TAG, "Button set to INVISIBLE");
//                        }else {
//                            row.addView(btn, cellLp);
//                            Log.d(TAG, "Button set to VISIBLE");
//                        }
                    }
                    tableLayout.addView(row, rowLp);
                    Log.d(TAG, "Added row to table");
                }
                frameLayoutLessonSection.addView(tableLayout);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







//                 }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//                 get text to display on lesson card



//                inflatedButton.setPadding(0, 0, 0, 0);
//
//                DatabaseReference refClasses = FirebaseDatabase.getInstance().getReference("Classes")
//                        .child(classID)
//                        .child("timetable");
//                refClasses.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
//                        for (DataSnapshot child2 : snapshot2.getChildren()) {
//
//
//                        String subject = child2.child("subject").getValue().toString();
//                        String location = child2.child("location").getValue().toString();
//                        // String period = child2.child("Period").getValue().toString();
//                        // String dayOfWeek = child2.child("DayOfWeek").getValue().toString();
//                        String teacherid = child2.child("idnumber").getValue().toString();
//                        String lessonKey = child2.getKey().trim();
//                        Log.d(TAG, "Current lesson key: " + lessonKey);
//
//                        lessonKeyList.add(lessonKey);
//                        Log.d(TAG, "Added new lesson to List");
//
////                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                    Context context = getApplicationContext();
////
////                    TableLayout tableLayout = new TableLayout(context);
////                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
////                            ViewGroup.LayoutParams.MATCH_PARENT,
////                            ViewGroup.LayoutParams.MATCH_PARENT);
////                    tableLayout.setLayoutParams(lp);
////                    tableLayout.setStretchAllColumns(true);
////
////                    TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
////                            ViewGroup.LayoutParams.MATCH_PARENT,
////                            ViewGroup.LayoutParams.MATCH_PARENT,
////                            1.0f);
////                    TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
////                            ViewGroup.LayoutParams.MATCH_PARENT,
////                            ViewGroup.LayoutParams.MATCH_PARENT,
////                            1.0f);
//////                        rowLp.bottomMargin = 2;
////                    cellLp.topMargin = 4;
////                    cellLp.leftMargin = 6;
//////                        cellLp.bottomMargin = 2;
//////                        cellLp.rightMargin = 2;
//
////                    for (int r = 0; r < 9; ++r) {
////
////                        TableRow row = new TableRow(context);
////
////                        for (int c = 0; c < 7; ++c) {
////
////                            String currentLessonKey = String.valueOf(r + 1) + String.valueOf(c + 1);
////                            Button inflatedButton = (Button) layoutInflater.inflate(R.layout.item_lesson_card, null);
////                            inflatedButton.setPadding(0, 0, 0, 0);
//                        if (lessonKeyList.contains(currentLessonKey)) {
//
//                            inflatedButton.setText("bb");
//                            inflatedButton.setVisibility(VISIBLE);
//                            Log.d(TAG, "Button set to VISIBLE");
//                            row.addView(inflatedButton, cellLp);
//                            Log.d(TAG, "Added cell to row, visible");
//
//
//                        } else {
//                            inflatedButton.setText("");
//                            inflatedButton.setVisibility(VISIBLE);
//                            Log.d(TAG, "Button set to INVISIBLE");
//                            row.addView(inflatedButton, cellLp);
//                            Log.d(TAG, "Added cell to row");
//
//                        }
////                        }
////                        tableLayout.addView(row, rowLp);
////                        Log.d(TAG, "Added row to table");
////                    }
////                    frameLayoutLessonSection.addView(tableLayout);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

    }



//    private String getLessonInfoToShowOnLessonCard(String teacherid, String subject, String location) {
//        DatabaseReference refTeachers = FirebaseDatabase.getInstance()
//                .getReference("Teachers");
//        String textOnCourseCard = subject + "\n" + location;
//        refTeachers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String teacherName = snapshot.child(teacherid).child("name")
//                        .getValue().toString().trim();
//                String textOnCourseCard = subject + "\n" + location + "\n" + teacherName;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return textOnCourseCard;
//    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }
    public String getCurrentClassID() {
        return currentClassID;
    }

    public void setCurrentClassID(String currentClassID) {
        this.currentClassID = currentClassID;
    }
}