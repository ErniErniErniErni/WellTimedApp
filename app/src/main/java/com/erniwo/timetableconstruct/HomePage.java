package com.erniwo.timetableconstruct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.erniwo.timetableconstruct.login.LoginActivity;

public class HomePage extends AppCompatActivity{

    private TextView appName;
    private TextView appDescription;
    private TextView poweredBy;

    private Animation sideSlideInAnim;
    private Animation bottomSlideInAnim;

    private static int HOME_PAGE_TIMER = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // initialization/ hooks
        appName = (TextView) findViewById(R.id.app_name);
        appDescription = (TextView) findViewById(R.id.app_description);
        poweredBy = (TextView) findViewById(R.id.powered_by);

        sideSlideInAnim = (Animation) AnimationUtils.loadAnimation(this, R.anim.side_slide_in_anim);
        bottomSlideInAnim = (Animation) AnimationUtils.loadAnimation(this, R.anim.bottom_slide_in_anim);

        // assign animations on textview
        appName.setAnimation(sideSlideInAnim);
        appDescription.setAnimation(sideSlideInAnim);
        poweredBy.setAnimation(bottomSlideInAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        },HOME_PAGE_TIMER);
    }
}