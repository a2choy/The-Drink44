package com.example.administrator.app44drink.LoginIntro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener {

    ImageView backBt;
    EditText nameEt, nickEt, emailEt, phoneEt;
    Button policyBt, tosBt, signUpBt, check2Bt;
    boolean check2 = false;
    CheckBox policyCb;
    String type;
    String id;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        backBt = (ImageView)findViewById(R.id.backIv);
        nameEt = (EditText)findViewById(R.id.nameEt);
        nickEt = (EditText)findViewById(R.id.nickEt);
        emailEt = (EditText)findViewById(R.id.emailEt);
        phoneEt = (EditText)findViewById(R.id.phoneEt);
        check2Bt = (Button)findViewById(R.id.check2Bt);
        policyBt = (Button)findViewById(R.id.policyBt);
        tosBt = (Button)findViewById(R.id.tosBt);
        signUpBt = (Button)findViewById(R.id.signUpBt);
        policyCb = (CheckBox)findViewById(R.id.policyCb);

        signUpBt.setOnClickListener(this);

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        type = String.valueOf(getIntent().getIntExtra("type", 1));
        id = getIntent().getStringExtra("id");

        nickEt.addTextChangedListener(textWatcher);

        check2Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nick = nickEt.getText().toString().trim();

                RequestQueue stringRequest = Volley.newRequestQueue(SignUpActivity.this);
                String str = getResources().getString(R.string.url) + "checkNickname.php";
                StringRequest myReq = new StringRequest(
                        Request.Method.POST,
                        str,
                        SignUpActivity.this,
                        SignUpActivity.this){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nickname", nick);

                        return params;
                    }
                };
                myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                stringRequest.add(myReq);
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable edit) {
            // Text가 바뀌고 동작할 코드
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Text가 바뀌기 전 동작할 코드
        }

        //
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            check2 = false;
        }
    };

    @Override
    public void onClick(View v) {

        Log.d("heu", "hello");
        final String name = nameEt.getText().toString().trim();
        this.name = nameEt.getText().toString().trim();
        final String nick = nickEt.getText().toString().trim();
        final String email = emailEt.getText().toString().trim();
        final String phone = phoneEt.getText().toString().trim();



        if(name.length() > 0 && nick.length() > 0 && email.length() > 0 && phone.length() > 0 && check2 && policyCb.isChecked()) {
            RequestQueue stringRequest = Volley.newRequestQueue(this);
            String str = getResources().getString(R.string.url) + "joinMember.php";
            StringRequest myReq = new StringRequest(
                    Request.Method.POST,
                    str,
                    joinMemberResponse,
                    this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", id);
                    params.put("type", type);
                    params.put("memname", name);
                    params.put("memnickname", nick);
                    params.put("pushtoken", "abcddffdfdfg");
                    params.put("device", Build.MODEL);
                    params.put("os", "aos");
                    params.put("osv", String.valueOf(Build.VERSION.SDK_INT));
                    return params;
                }
            };
            myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
            stringRequest.add(myReq);
        } else {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(SignUpActivity.this);
            alt_bld.setTitle("회원가입");
            alt_bld.setMessage("모두 적어야합니다");
            alt_bld.setCancelable(false);
            alt_bld.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = alt_bld.create();
            alert.show();
        }

    }

    Response.Listener<String> joinMemberResponse = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            JSONObject memChk = null;
            try {
                memChk = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String bool = "";
            String uniqueId = "";
            String nickname = "";
            try {
                bool = memChk.getString("result");
                uniqueId = memChk.getString("id");
                nickname = memChk.getString("nickname");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(SignUpActivity.this);
            alt_bld.setTitle("회원가입");
            if(bool.equals("true")) {
                alt_bld.setMessage("가입 성공");
                alt_bld.setCancelable(false);
                final String finalUniqueId = uniqueId;
                final String finalNickname = nickname;
                alt_bld.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("first", 1);
                                editor.putString("id", finalUniqueId);
                                editor.putString("name", name);
                                editor.putString("nickname", finalNickname);
                                editor.commit();
                                Intent intent = new Intent(SignUpActivity.this, com.example.administrator.app44drink.MainActivity.class);
                                startActivity(intent);
                            }
                        });
            } else {
                alt_bld.setMessage("가입 실패");
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
        Log.d("heu","error");
    }

    @Override
    public void onResponse(String s) {
        /*try {
            s = URLDecoder.decode(URLEncoder.encode(s,"iso8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        JSONObject nickChk = null;
        try {
            nickChk = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bool = "";
        try {
            bool = nickChk.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(SignUpActivity.this);
        alt_bld.setTitle("아이디 확인");
        if(bool.equals("true")) {

            alt_bld.setMessage("사용이 가능 합니다.\n사용 하사겠습니까?");
            alt_bld.setCancelable(false);
            alt_bld.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            check2 = true;
                        }
                    });
            alt_bld.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            nickEt.setText("");
                        }
                    });

        } else {
            alt_bld.setMessage("사용이 불가");
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
}
