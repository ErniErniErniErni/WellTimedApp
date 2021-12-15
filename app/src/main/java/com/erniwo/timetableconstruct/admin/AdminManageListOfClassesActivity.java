package com.erniwo.timetableconstruct.admin;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminManageListOfClassesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listOfClasses;
    private Button addClassButton;
    private static String clickedClassName;
    private static String clickedClassID;

    private String TAG = "AdminManageListOfClassesActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_list_of_classes);

        // init elements
        listOfClasses = findViewById(R.id.list_of_classes);
        addClassButton = findViewById(R.id.add_class);

        // adapt to array
        ArrayList<String> classNameArray = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, classNameArray);
        listOfClasses.setAdapter(adapter);

        // onClick behaviours
        listOfClasses.setOnItemClickListener(this);
            // long click a class name to delete its info
        listOfClasses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item = position;

                new AlertDialog.Builder(AdminManageListOfClassesActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Delete Item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String clickClassName = ((TextView) view).getText().toString().trim();
                                setClickedClassName(clickClassName);

                                DatabaseReference classRef = FirebaseDatabase.getInstance().getReference()
                                        .child("Classes");
                                classRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child: snapshot.getChildren()) {
                                            String childNameValue = child.child("Name").getValue().toString().trim();
                                            if (childNameValue.equals(getClickedClassName())) {
                                                String matchingClassID = child.getKey().trim();
                                                setClickedClassID(matchingClassID);

                                                // delete from back end
                                                classRef.child(getClickedClassID()).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                // delete from front end
                                classNameArray.remove(which_item);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

        // show list of classes
        DatabaseReference classInfoRef = FirebaseDatabase.getInstance().getReference("Classes");//.child();
        classInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classNameArray.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    classNameArray.add(snapshot.child("name").getValue().toString());
                }
                adapter.notifyDataSetChanged();
                Log.i(TAG, "classNameArray Updated");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addClassButtonIntent = new Intent(AdminManageListOfClassesActivity.this, AdminAddNewClassActivity.class);
                startActivity(addClassButtonIntent);
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
        Log.d(TAG, "On Item Cicked()" + clickClassName);

        setClickedClassName(clickClassName);
        Log.d(TAG, "On Item Cicked()" + getClickedClassName());

        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
        classesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {

                    String childNameValue = child.child("name").getValue().toString().trim();
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
                        try {
                            startActivity(in1);
                        } catch (NullPointerException ex) {
                            Log.e(TAG, Log.getStackTraceString(null));
                        }
//                        startActivity(in1);
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










