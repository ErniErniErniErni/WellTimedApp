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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        clickedClassName = parent.getItemAtPosition(position).toString();
        clickedClassName = ((TextView) view).getText().toString().trim();

        modifyClickedClassID();

        Intent in1 = new Intent(AdminManageListOfClassesActivity.this, AdminManageClassTimetableActivity.class);

        startActivity(in1);

        //for testing
//        Message.showMessage(getApplicationContext(),getClickedClassName());
//        Message.showMessage(getApplicationContext(),getClickedClassID());
    }

    public static String getClickedClassName() {
        return clickedClassName;
    }

    public static String getClickedClassID() {
        return clickedClassID;
    }

    public void setClickedClassID(String clickedClassID) {
        this.clickedClassID = clickedClassID;
    }

    public void modifyClickedClassID() {
        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
        classesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {


                    String childNameValue = child.child("Name").getValue().toString().trim();
//                    Message.showMessage(getApplicationContext(),childNameValue);


                    if (childNameValue.equals(getClickedClassName())) {
//                        Message.showMessage(getApplicationContext(),"Yeahhhhhh");
                        setClickedClassID(child.getKey());
//                        Message.showMessage(getApplicationContext(),getClickedClassID());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}










