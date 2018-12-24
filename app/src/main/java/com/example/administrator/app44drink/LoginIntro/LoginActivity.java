package com.example.administrator.app44drink.LoginIntro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.app44drink.R;

public class LoginActivity extends AppCompatActivity {

    ImageView signBt, kakaoBt, loginBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signBt = (ImageView)findViewById(R.id.signBt);
        kakaoBt = (ImageView)findViewById(R.id.kakaoBt);
        loginBt = (ImageView)findViewById(R.id.loginBt);

        signBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp1Activity.class);
                startActivity(intent);
            }
        });

        kakaoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, KakaoLoginActivity.class);
                startActivity(intent);
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, NormalLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
