package com.example.administrator.app44drink.Drawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.app44drink.LoginIntro.SignUp1Activity;
import com.example.administrator.app44drink.R;

public class SettingActivity extends AppCompatActivity {
    ImageView drawerIv;
    TextView home, myTv, noticeTv, askTv, howTv, settingTv;
    TextView logoutTv, leaveTv, verTv, policyTv;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        home = (TextView)findViewById(R.id.home);
        myTv = (TextView)findViewById(R.id.myTv);
        noticeTv = (TextView)findViewById(R.id.noticeTv);
        askTv = (TextView)findViewById(R.id.askTv);
        howTv = (TextView)findViewById(R.id.howTv);
        settingTv = (TextView)findViewById(R.id.settingTv);
        logoutTv = (TextView)findViewById(R.id.logoutTv);
        leaveTv = (TextView)findViewById(R.id.leaveTv);
        verTv = (TextView)findViewById(R.id.verTv);
        policyTv = (TextView)findViewById(R.id.policyTv);

        drawerIv = (ImageView)findViewById(R.id.drawerIv);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        verTv.setText("APP 정보 : 버전 "+ version);

        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(SettingActivity.this);
                alt_bld.setTitle("로그아웃");
                alt_bld.setMessage("로그아웃 하시겠습니까?");
                alt_bld.setCancelable(false);
                alt_bld.setPositiveButton("로그아웃",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(SettingActivity.this, com.example.administrator.app44drink.LoginIntro.LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                alt_bld.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });

        leaveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(SettingActivity.this);
                alt_bld.setTitle("회원탈퇴");
                alt_bld.setMessage("회원탈퇴 하시겠습니까?");
                alt_bld.setCancelable(false);
                alt_bld.setPositiveButton("회원탈퇴",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(SettingActivity.this, com.example.administrator.app44drink.LoginIntro.LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                alt_bld.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, com.example.administrator.app44drink.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MyActivity.class);
                startActivity(intent);
                finish();

            }
        });

        noticeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        askTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AskActivity.class);
                startActivity(intent);
                finish();
            }
        });

        howTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, HowActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }
}
