package com.example.administrator.app44drink.LoginIntro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.app44drink.R;

public class Intro2Activity extends AppCompatActivity {
    Button btn;
    TextView doTv1;
    TextView doTv2;
    TextView doTv3;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);
        btn = (Button) findViewById(R.id.btn);
        doTv1 = (TextView)findViewById(R.id.dotTv1);
        doTv2 = (TextView)findViewById(R.id.dotTv2);
        doTv3 = (TextView)findViewById(R.id.dotTv3);
        pager = (ViewPager) findViewById(R.id.pager);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
                int intro = prefs.getInt("first", 0);

                Intent intent = new Intent(Intro2Activity.this, LoginActivity.class);
                if(intro == 1) {
                    intent = new Intent(Intro2Activity.this, com.example.administrator.app44drink.MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));


    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            FragmentIntro f = new FragmentIntro();
            if(i == 0) {
                f.setImg(R.drawable.help_01);
            } else if(i == 1) {
                f.setImg(R.drawable.help_02);
            } else if(i == 2) {
                f.setImg(R.drawable.help_03);
            }

            return f;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
