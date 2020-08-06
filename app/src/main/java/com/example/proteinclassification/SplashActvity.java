package com.example.proteinclassification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActvity extends AppCompatActivity {
    private ImageView logo;
    private static int splashTimeout=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo=(ImageView)findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActvity.this,welcomscreen.class);
                startActivity(i);
                finish();
            }
        },splashTimeout);

        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        logo.startAnimation(myanim);

    }

}
