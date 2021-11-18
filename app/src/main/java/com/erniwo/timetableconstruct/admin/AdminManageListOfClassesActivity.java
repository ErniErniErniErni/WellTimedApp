package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AdminManageListOfClassesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

//    String[] classNameArray = {"Class 101", "Class 102", "Class 103",
//            "Class 201", "Class 202", "Class 203", "Class 301",
//            "Class 302", "Class 303", "Class 304", "Class 401",
//            "Class 402", "Class 403", "Class 404", "Class 405",
//    "Class 501", "Class 502", "Class 503", "Class 504",
//    "Class 601", "Class 602"};
    private ListView listOfClasses;
    private Button addClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_classes);

        listOfClasses = findViewById(R.id.list_of_classes);
        addClass = findViewById(R.id.add_class);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(AdminManageListOfClassesActivity.this, AdminAddNewClassActivity.class);
                startActivity(in2);
            }
        });

        ArrayList<String> classNameArray = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, classNameArray);

        listOfClasses.setAdapter(adapter);
        listOfClasses.setOnItemClickListener(this);

        DatabaseReference classInfoRef = FirebaseDatabase.getInstance().getReference("Classes");//.child();

        classInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classNameArray.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    classNameArray.add(snapshot.child("Name").getValue().toString());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent in1 = new Intent(AdminManageListOfClassesActivity.this, AdminManageClassTimetableActivity.class);
        startActivity(in1);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.add_class:
////                addClass();
//                startActivity(new Intent(this,AdminManageLOCAddClassActivity.class));
//                break;
//        }
//    }

//    private void addClass() {
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("C0106", "Grade 1 Class 6");
//        //only called when logged in
//        FirebaseDatabase.getInstance().getReference().child("Classes").updateChildren(map);
//    }
}