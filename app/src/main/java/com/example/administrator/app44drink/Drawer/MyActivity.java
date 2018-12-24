package com.example.administrator.app44drink.Drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.app44drink.MainActivity;
import com.example.administrator.app44drink.R;

public class MyActivity extends AppCompatActivity {

    TextView nameTv, nickTv;
    ImageView drawerIv;
    TextView home, myTv, noticeTv, askTv, howTv, settingTv;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        nameTv = (TextView)findViewById(R.id.nameTv);
        nickTv = (TextView)findViewById(R.id.nickTv);

        home = (TextView)findViewById(R.id.home);
        myTv = (TextView)findViewById(R.id.myTv);
        noticeTv = (TextView)findViewById(R.id.noticeTv);
        askTv = (TextView)findViewById(R.id.askTv);
        howTv = (TextView)findViewById(R.id.howTv);
        settingTv = (TextView)findViewById(R.id.settingTv);

        drawerIv = (ImageView)findViewById(R.id.drawerIv);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
        String name = prefs.getString("name", "홍 길 동");
        String nick = prefs.getString("nickname", "길동 아범");

        nameTv.setText("이름: " + name);
        nickTv.setText("닉네임: " + nick);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, com.example.administrator.app44drink.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        noticeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        askTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, AskActivity.class);
                startActivity(intent);
                finish();
            }
        });

        howTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, HowActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
