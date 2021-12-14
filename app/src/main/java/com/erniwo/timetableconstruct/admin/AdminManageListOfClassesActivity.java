package com.erniwo.timetableconstruct.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AdminManageListOfClassesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listOfClasses;
    private Button addClass;
    private static String clickedClassName;
    private static String clickedClassID;

    private String TAG = "AdminManageListOfClassesActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_classes);

        listOfClasses = findViewById(R.id.list_of_classes);
        addClass = findViewById(R.id.add_class);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(getApplicationContext(), AdminAddNewClassActivity.class);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clickClassName = ((TextView) view).getText().toString().trim();
        Log.d(TAG, clickClassName); // this step is logged

        setClickedClassName(clickClassName);
        Log.d(TAG, getClickedClassName()); // this step is logged

        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
        classesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {

                    String childNameValue = child.child("Name").getValue().toString().trim();
                    Log.d(TAG, childNameValue);
                    if (childNameValue.equals(getClickedClassName())) {
                        String classID = child.getKey().trim();
                        Log.d(TAG, "childNameValue = " + classID);
                        setClickedClassID(classID);
                        Log.d(TAG, "clickedCLassID = " + getClickedClassID());
                        Intent in1 = new Intent(AdminManageListOfClassesActivity.this, AdminManageClassTimetableActivity.class);
                        String clickedClassName = getClickedClassName();
                        String clickedClassID = getClickedClassID();
                        Log.d(TAG, getClickedClassID());
                        in1.putExtra("ClickedClassName", clickedClassName);
                        in1.putExtra("ClickedClassID", clickedClassID);
                        startActivity(in1);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getClickedClassName() {
        return clickedClassName;
    }

    public void setClickedClassName(String clickedClassName) {
        this.clickedClassName = clickedClassName;
    }

    public String getClickedClassID() {
        return clickedClassID;
    }

    public void setClickedClassID(String clickedClassID) {
        this.clickedClassID = clickedClassID;
    }
}










