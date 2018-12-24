package com.example.administrator.app44drink.Drawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.app44drink.CoffeeInfo;
import com.example.administrator.app44drink.Event;
import com.example.administrator.app44drink.LoginIntro.SignUp1Activity;
import com.example.administrator.app44drink.MainActivity;
import com.example.administrator.app44drink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AskActivity extends AppCompatActivity {

    EditText nameEt, emailEt, contentEt;
    ImageView submitBt;
    DrawerLayout drawer;
    ImageView drawerIv;
    TextView home, myTv, noticeTv, askTv, howTv, settingTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        nameEt = (EditText)findViewById(R.id.nameEt);
        emailEt = (EditText)findViewById(R.id.emailEt);
        contentEt = (EditText)findViewById(R.id.contentEt);
        submitBt = (ImageView)findViewById(R.id.submitBt);

        drawerIv = (ImageView)findViewById(R.id.drawerIv);
        home = (TextView)findViewById(R.id.home);
        myTv = (TextView)findViewById(R.id.myTv);
        noticeTv = (TextView)findViewById(R.id.noticeTv);
        askTv = (TextView)findViewById(R.id.askTv);
        howTv = (TextView)findViewById(R.id.howTv);
        settingTv = (TextView)findViewById(R.id.settingTv);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = nameEt.getText().toString().trim();
                final String email = emailEt.getText().toString().trim();
                final String content = contentEt.getText().toString().trim();

                RequestQueue stringRequest = Volley.newRequestQueue(AskActivity.this);
                String str = getResources().getString(R.string.url) + "callContentIn.php";
                StringRequest myReq = new StringRequest(
                        Request.Method.POST,
                        str,
                        askResponse,
                        errorResponse) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id",id);
                        params.put("content",content);
                        params.put("email",email);
                        return params;
                    }
                };
                myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                stringRequest.add(myReq);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AskActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AskActivity.this, MyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        noticeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AskActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        askTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        howTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AskActivity.this, HowActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AskActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    Response.Listener<String> askResponse = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(AskActivity.this);
            alt_bld.setTitle("성공");
            alt_bld.setMessage("등록되었습니다");
            alt_bld.setCancelable(false);
            alt_bld.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = alt_bld.create();
            alert.show();
            nameEt.setText("");
            emailEt.setText("");
            contentEt.setText("");
        }
    };
    Response.ErrorListener errorResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

}
