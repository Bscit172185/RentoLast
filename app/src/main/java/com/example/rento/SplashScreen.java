package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.annotation.Annotation;


public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    ImageView img;

    @Override
    protected void onStart() {
        super.onStart();
        Animation anim= AnimationUtils.loadAnimation(SplashScreen.this,R.anim.move);
        img.startAnimation(anim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img=findViewById(R.id.imageView15);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user=auth.getCurrentUser();
                if(user==null){
                    Intent iHome= new Intent(SplashScreen.this,Login.class);
                    startActivity(iHome);
                    finish();
                }
                else {
                    Intent iHome= new Intent(SplashScreen.this,MainActivity.class);
                    Bundle b= ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this).toBundle();
                    startActivity(iHome,b);

                    finish();
                }

            }
        }, 2500);
    }
}