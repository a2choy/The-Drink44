package com.example.administrator.app44drink.LoginIntro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.app44drink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NormalLoginActivity extends AppCompatActivity implements Response.ErrorListener {

    EditText idEt, pass1Et;
    Button loginBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_login);
        idEt = (EditText)findViewById(R.id.idEt);
        pass1Et = (EditText)findViewById(R.id.pass1Et);
        loginBt = (Button)findViewById(R.id.loginBt);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String id = idEt.getText().toString().trim();
                RequestQueue stringRequest = Volley.newRequestQueue(NormalLoginActivity.this);
                String str = getResources().getString(R.string.url) + "loginMember.php";
                StringRequest myReq = new StringRequest(
                        Request.Method.POST,
                        str,
                        loginResponse,
                        NormalLoginActivity.this) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", id);
                        params.put("type", "1");
                        params.put("pushtoken", "abcddffdfdfg");
                        params.put("device", Build.MODEL);
                        params.put("os", "aos");
                        params.put("osv", String.valueOf(Build.VERSION.SDK_INT));
                        return params;
                    }
                };
                myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                stringRequest.add(myReq);
            }
        });



    }
    Response.Listener<String> loginResponse = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            JSONObject login = null;
            try {
                login = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String bool = "";

            try {
                bool = login.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(NormalLoginActivity.this);
            alt_bld.setTitle("로그인");
            if(bool.equals("true")) {
                String uniqueId = "";
                String nickname = "";
                String name = "";
                try {
                    uniqueId = login.getString("id");
                    nickname = login.getString("nickname");
                    name = login.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alt_bld.setMessage("성공");
                alt_bld.setCancelable(false);
                final String finalUniqueId = uniqueId;
                final String finalNickname = nickname;
                final String finalName = name;
                alt_bld.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("first", 1);
                                editor.putString("id", finalUniqueId);
                                editor.putString("name", finalName);
                                editor.putString("nickname", finalNickname);
                                editor.commit();
                                Intent intent = new Intent(NormalLoginActivity.this, com.example.administrator.app44drink.MainActivity.class);
                                startActivity(intent);
                            }
                        });
            } else {
                alt_bld.setMessage("실패");
                alt_bld.setCancelable(false);
                alt_bld.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
            }
            AlertDialog alert = alt_bld.create();
            alert.show();
        }
    };

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }
}
