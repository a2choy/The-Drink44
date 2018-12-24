package com.example.administrator.app44drink.LoginIntro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.app44drink.R;

public class IntroActivity extends AppCompatActivity {

    ImageView logoTv;
    RelativeLayout layout;
    int intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        logoTv = (ImageView)findViewById(R.id.logoIv);
        layout = (RelativeLayout)findViewById(R.id.layout);

        Animation anim = AnimationUtils.loadAnimation(IntroActivity.this,R.anim.logo);
        layout.startAnimation(anim);
        SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
        intro = prefs.getInt("first", 0);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(0);

                Intent intent = new Intent(IntroActivity.this, Intro2Activity.class);
                if(intro == 1) {
                    intent = new Intent(IntroActivity.this, com.example.administrator.app44drink.MainActivity.class);
                }
                startActivity(intent);

                finish();
            }
        });

        handler.sendEmptyMessageDelayed(0,4000);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeMessages(0);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);



            Intent intent = new Intent(IntroActivity.this, Intro2Activity.class);
            if(intro == 1) {
                intent = new Intent(IntroActivity.this, com.example.administrator.app44drink.MainActivity.class);
            }
            startActivity(intent);

            finish();
        }
    };
}
