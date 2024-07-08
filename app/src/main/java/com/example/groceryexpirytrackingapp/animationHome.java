package com.example.groceryexpirytrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class animationHome extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ImageView imageView;
    TextView text1,text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_home);

        //Animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Find view by id
        imageView=findViewById(R.id.imageView2);
        text1=findViewById(R.id.textView);
        text2=findViewById(R.id.textView2);

        //set animation to view
        imageView.setAnimation(topAnim);
        text1.setAnimation(bottomAnim);
        text2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(animationHome.this, Login.class));
                finish();
            }
        },5000);

    }
}