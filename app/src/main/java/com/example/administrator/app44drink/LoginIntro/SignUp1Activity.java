package com.example.administrator.app44drink.LoginIntro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SignUp1Activity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{
    EditText idEt, pass1Et, pass2Et;
    Button check1Bt, signUpBt;
    boolean check1 = false;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);
        idEt = (EditText)findViewById(R.id.idEt);
        pass1Et = (EditText)findViewById(R.id.pass1Et);
        pass2Et = (EditText)findViewById(R.id.pass2Et);
        check1Bt = (Button)findViewById(R.id.check1Bt);
        signUpBt = (Button)findViewById(R.id.signUpBt);

        idEt.addTextChangedListener(textWatcher);

        check1Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idEt.getText().toString().trim();
                SignUp1Activity.this.id = id;
                RequestQueue stringRequest = Volley.newRequestQueue(SignUp1Activity.this);
                String str = getResources().getString(R.string.url) + "checkMember.php";
                StringRequest myReq = new StringRequest(
                        Request.Method.POST,
                        str,
                        SignUp1Activity.this,
                        SignUp1Activity.this){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", id);
                        params.put("type", "1");

                        return params;
                    }
                };
                myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                stringRequest.add(myReq);
            }
        });


        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = pass1Et.getText().toString().trim();
                String pass2 = pass2Et.getText().toString().trim();

                if(!pass1.equals(pass2)){
                    pass1Et.setText("");
                    pass2Et.setText("");
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(SignUp1Activity.this);
                    alt_bld.setTitle("패스워드 확인");
                    alt_bld.setMessage("패스워드가 일치해야 합니다");
                    alt_bld.setCancelable(false);
                    alt_bld.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    alert.show();
                    return;
                }
                if(!check1){
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(SignUp1Activity.this);
                    alt_bld.setTitle("아이디 확인");
                    alt_bld.setMessage("아이디를 확인해야 합니다");
                    alt_bld.setCancelable(false);
                    alt_bld.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    alert.show();
                    return;
                }
                if(pass1.length() < 6){
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(SignUp1Activity.this);
                    alt_bld.setTitle("패스워드 확인");
                    alt_bld.setMessage("패스워드가 6자 이상이여야 합니다");
                    alt_bld.setCancelable(false);
                    alt_bld.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    alert.show();
                    return;
                }

                String shaPass = testSHA256(pass1);
                Log.d("heu", shaPass);

                Intent intent = new Intent(SignUp1Activity.this, com.example.administrator.app44drink.LoginIntro.SignUpActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });


    }

    public String testSHA256(String str){
        String SHA = "";
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
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
            check1 = false;
        }
    };

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(String s) {
      /*  try {
            s = URLDecoder.decode(URLEncoder.encode(s,"iso8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        JSONObject memChk = null;
        try {
            memChk = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bool = "";
        try {
            bool = memChk.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(SignUp1Activity.this);
        alt_bld.setTitle("아이디 확인");
        if(bool.equals("true")) {

            alt_bld.setMessage("사용이 가능 합니다.\n사용 하사겠습니까?");
            alt_bld.setCancelable(false);
            alt_bld.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            check1 = true;
                        }
                    });
            alt_bld.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            idEt.setText("");
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
