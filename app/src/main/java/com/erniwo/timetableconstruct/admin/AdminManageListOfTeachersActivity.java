package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminManageListOfTeachersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    
    private ListView listOfTeachers;
    private Button addNewTeacher;

    private static String clickedTeacherName;
    private static String clickedTeacherID;
    ArrayList<String> teacherNameArray = new ArrayList<>();

    private String TAG = "AdminmanageListOfTeachersActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_teachers);
        
        // init elements
        listOfTeachers = findViewById(R.id.list_of_teachers);
        addNewTeacher = (Button) findViewById(R.id.add_new_teacher); 
        
        // adapt to array
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, teacherNameArray);
        listOfTeachers.setAdapter(adapter);


        // onClick behaviours
        listOfTeachers.setOnItemClickListener(this);

            // long click a teacher to delete info
        listOfTeachers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item = position;

                new AlertDialog.Builder(AdminManageListOfTeachersActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Delete Item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String clickTeacherName = ((TextView) view).getText().toString().trim();
                                setClickedTeacherName(clickTeacherName);

                                DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference()
                                        .child("Teachers");
                                teacherRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child: snapshot.getChildren()) {
                                            String childNameValue = child.child("name").getValue().toString().trim();
                                            if (childNameValue.equals(getClickedTeacherName())) {
                                                String matchingTeacherID = child.getKey().trim();
                                                setClickedTeacherID(matchingTeacherID);

                                                // delete from back end
                                                teacherRef.child(getClickedTeacherID()).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                // delete from front end
                                teacherNameArray.remove(which_item);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

        addNewTeacher.setOnClickListener(this);

        // show list of teachers
        pullListOfTeachersFromDatabaseAndShow(adapter);
//        DatabaseReference teacherInfoRef = FirebaseDatabase.getInstance().getReference().child("Teachers");
//        teacherInfoRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                teacherNameArray.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String teacherName = snapshot.child("name").getValue().toString();
//                    String teacherID = snapshot.getKey().trim();
//                    teacherNameArray.add(teacherID);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    } // onCreate

    private void pullListOfTeachersFromDatabaseAndShow( ArrayAdapter adapter) {
        DatabaseReference teacherInfoRef = FirebaseDatabase.getInstance().getReference().child("Teachers");
        teacherInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherNameArray.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String teacherName = snapshot.child("name").getValue().toString();
                    String teacherID = snapshot.getKey().trim();
                    teacherNameArray.add(teacherName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent in1 = new Intent(AdminManageListOfTeachersActivity.this,
                AdminManageClassTimetableActivity.class);
        startActivity(in1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_teacher:
                Intent intent = new Intent(AdminManageListOfTeachersActivity.this,
                        AdminAddNewTeacherActivity.class);
                startActivity(intent);
                break;
        }

    }

    public static String getClickedTeacherName() {
        return clickedTeacherName;
    }

    public static String getClickedTeacherID() {
        return clickedTeacherID;
    }

    public static void setClickedTeacherName(String clickedTeacherName) {
        AdminManageListOfTeachersActivity.clickedTeacherName = clickedTeacherName;
    }

    public static void setClickedTeacherID(String clickedTeacherID) {
        AdminManageListOfTeachersActivity.clickedTeacherID = clickedTeacherID;
    }
}