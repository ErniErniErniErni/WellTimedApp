package com.erniwo.timetableconstruct.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.erniwo.timetableconstruct.Message;
import com.erniwo.timetableconstruct.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

        listOfClasses = findViewById(R.id.list_of_classes);
        addClassButton = findViewById(R.id.add_class);
        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addClassButtonIntent = new Intent(AdminManageListOfClassesActivity.this, AdminAddNewClassActivity.class);
                startActivity(addClassButtonIntent);
            }
        });

        ArrayList<String> classNameArray = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, classNameArray);

        listOfClasses.setAdapter(adapter);
//        MyListAdapter myListAdapter = new MyListAdapter(this,
//                R.layout.list_item, classNameArray);
//        MyListAdapter myListAdapter = new MyListAdapter(this,
//                R.layout.activity_listview, classNameArray);

//        listOfClasses.setAdapter(myListAdapter);
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
                Log.e(TAG, "classNameArray Updated");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } // onCreate

//    private class MyListAdapter extends ArrayAdapter<String> {
//        private int layout;
//        private MyListAdapter (Context context, int resource, List<String> objects) {
//            super(context, resource, objects);
//            layout = resource;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder mainViewHolder = null;
//            if(convertView == null) {
//                LayoutInflater inflater = LayoutInflater.from(getContext());
//                convertView = inflater.inflate(layout, parent, false);
//                ViewHolder viewHolder = new ViewHolder();
//                viewHolder.title = convertView.findViewById(R.id.list_item_text);
//                viewHolder.button = convertView.findViewById(R.id.list_delete_button);
//                viewHolder.button.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        deleteClass(getClickedClassID());
//                    }
//                });
//                convertView.setTag(viewHolder);
//            } else {
//                mainViewHolder = (ViewHolder) convertView.getTag();
//                mainViewHolder.title.setText(getItem(position));
//            }
//            return convertView;
//        }
//    }
//
//    public class ViewHolder {
//        TextView title;
//        Button button;
//    }

    private void deleteClass(String classID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Classes").child(classID);
        databaseReference.removeValue();
        Message.showMessage(getApplicationContext(), "Deleted!" + getClickedClassName());
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










