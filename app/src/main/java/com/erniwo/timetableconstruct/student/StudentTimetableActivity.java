package com.erniwo.timetableconstruct.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
    private String currentStudentID;

    private FrameLayout mFrameLayout;
    private ImageButton mAddImgBtn;
    private LinearLayout headerClassNumLl;
    private static float sCellWidthPx;//CourseCardWidth
    private static float sCellHeightPx;//CourseCardHeight

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_timetable);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setCurrentStudentID(snapshot.child(currentUserId).child("studentID").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        nameOfUser = findViewById(R.id.name_of_user_student);
        logout = findViewById(R.id.logout_icon);
        logout.setOnClickListener(this);

        tryButton = (Button) findViewById(R.id.course11);

        loadStudentName();
        pullExistingClasses();




        //mAddImgBtn = findViewById(R.id.img_btn_add);
//        mFrameLayout = findViewById(R.id.fl_timetable);
//        headerClassNumLl = findViewById(R.id.ll_header_class_num);
//        float headerClassNumWidth = getResources().getDimension(R.dimen.);
//        initAddBtn();
//        initFrameLayout();
    }


    private void pullExistingClasses() {
        //Pull timetable from backend
        String currentStudentID = getCurrentStudentID();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Classes");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()) {
                    if(child.child("Student").child(getCurrentStudentID()).exists()){
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
                setCurrentStudentID(snapshot.child(userID).child("studentID").getValue().toString());
                nameOfUser.setText( userName + "'s Timetable");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void setTableCellDimens(float headerWidth) {
//        //Gets the screen width used to set the width of the course view
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int displayWidth = displayMetrics.widthPixels;
//        int displayHeight = displayMetrics.heightPixels;
//
//        Resources resources = getResources();
//        int toolbarHeight = 40;
//        int headerWeekHeight = 40;
//
//        //Course View width
//        sCellWidthPx = (displayWidth - headerWidth) / 7.0f;
//
//        sCellHeightPx = Math.max(sCellWidthPx,
//                (displayHeight - toolbarHeight - headerWeekHeight) / (float) 8);
//    }

    @SuppressLint("ClickableViewAccessibility")
    private void initFrameLayout() {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mFrameLayout.getLayoutParams();
        //CourseCardHeight
        layoutParams.height = (int) sCellHeightPx * 8;
        //CourseCardWidth
        layoutParams.width = (int) sCellWidthPx * 7;

        mAddImgBtn.getLayoutParams().height = (int) sCellHeightPx;

        mFrameLayout.performClick();
        mFrameLayout.setOnTouchListener((view, motionEvent) -> {
            int event = motionEvent.getAction();
            if (event == MotionEvent.ACTION_UP) {
                if (mAddImgBtn.getVisibility() == View.VISIBLE) {
                    mAddImgBtn.setVisibility(View.GONE);
                } else {
                    int x = (int) (motionEvent.getX() / sCellWidthPx);
                    int y = (int) (motionEvent.getY() / sCellHeightPx);
                    x = (int) (x * sCellWidthPx);
                    y = (int) (y * sCellHeightPx);
                    setAddImgBtn(x, y);
                }
            }
            return true;
        });
    }

//    private void initAddBtn() {
//        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
//        layoutParams.width = (int) sCellWidthPx;
//        layoutParams.height = (int) sCellHeightPx;
//    }

    private void setAddImgBtn(int left, int top) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        mAddImgBtn.setVisibility(View.VISIBLE);
    }


    public String getCurrentStudentID() {
        return currentStudentID;
    }

    public void setCurrentStudentID(String sID) {

        this.currentStudentID = sID;
    }
}

