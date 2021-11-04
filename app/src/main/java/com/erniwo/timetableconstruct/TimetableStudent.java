package com.erniwo.timetableconstruct;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TimetableStudent extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    private ImageButton mAddImgBtn;
    private LinearLayout headerClassNumLl;

    private static float sCellWidthPx;//CourseCardWidth
    private static float sCellHeightPx;//CourseCardHeight

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_student);

        mAddImgBtn = findViewById(R.id.img_btn_add);
        mFrameLayout = findViewById(R.id.fl_timetable);
        headerClassNumLl = findViewById(R.id.ll_header_class_num);

//        float headerClassNumWidth = getResources().getDimension(R.dimen.);

        initAddBtn();

        initFrameLayout();

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

    private void initAddBtn() {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        layoutParams.width = (int) sCellWidthPx;
        layoutParams.height = (int) sCellHeightPx;
    }

    private void setAddImgBtn(int left, int top) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        mAddImgBtn.setVisibility(View.VISIBLE);
    }

}