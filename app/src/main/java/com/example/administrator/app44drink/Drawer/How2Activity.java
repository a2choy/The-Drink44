package com.example.administrator.app44drink.Drawer;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.example.administrator.app44drink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class How2Activity extends AppCompatActivity {

    ImageView backIv;
    TextView titleTv, contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how2);

        backIv = (ImageView)findViewById(R.id.backIv);
        titleTv = (TextView)findViewById(R.id.titleTv);
        contentTv = (TextView)findViewById(R.id.contentTv);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
        final String id = prefs.getString("id", "1234");
        final String num = getIntent().getStringExtra("num");

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String str = getResources().getString(R.string.url) + "guideContent.php";
        StringRequest myReq = new StringRequest(
                Request.Method.POST,
                str,
                noticeResponse,
                errorResponse) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("num", num);
                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);

    }
    Response.Listener<String> noticeResponse = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            JSONObject news = null;
            try {
                news = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String bool = "";
            try {
                bool = news.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(bool.equals("true")){
                String title = "";
                String content = "";
                try {
                    title = news.getString("title");
                    content = news.getString("content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                titleTv.setText(title);
                contentTv.setText(content);
            }
        }
    };

    Response.ErrorListener errorResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };
}
