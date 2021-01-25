package com.example.placementapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.placementapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView img = findViewById(R.id.indiralogo);
        ImageView img2 = findViewById(R.id.welcome);
        ImageView img3 = findViewById(R.id.collegelogo);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_slide_down_animation);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_slide_up_animation);
        Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_fade_animation);
        img.startAnimation(anim1);
        img2.startAnimation(anim2);
        img3.startAnimation(anim3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }, 2000);
    }
}