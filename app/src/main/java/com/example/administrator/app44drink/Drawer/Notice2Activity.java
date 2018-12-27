package com.example.administrator.app44drink.Drawer;

import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.app44drink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Notice2Activity extends AppCompatActivity {

    ImageView backIv, img1,img2,img3;
    TextView titleTv, contentTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice2);
        backIv = (ImageView) findViewById(R.id.backIv);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        titleTv = (TextView) findViewById(R.id.titleTv);
        contentTv = (TextView) findViewById(R.id.contentTv);

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
        String str = getResources().getString(R.string.url) + "newsContent.php";
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
            if (bool.equals("true")) {
                String title = "";
                String content = "";
                String img1url = "";
                String img2url = "";
                String img3url = "";
                try {
                    title = news.getString("title");
                    content = news.getString("content");
                    img1url = news.getString("img1");
                    img2url = news.getString("img2");
                    img3url = news.getString("img3");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                titleTv.setText(title);
                if(img1url.equals("")){
                    img1.setVisibility(View.GONE);
                }
                if(img2url.equals("")){
                    img2.setVisibility(View.GONE);
                }
                if(img3url.equals("")){
                    img3.setVisibility(View.GONE);
                }
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.ic_launcher);
                requestOptions.error(R.mipmap.ic_launcher);
                requestOptions.centerCrop();
                Glide
                        .with(Notice2Activity.this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(img1url)
                        .into(img1);
                Glide
                        .with(Notice2Activity.this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(img2url)
                        .into(img2);
                Glide
                        .with(Notice2Activity.this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(img2url)
                        .into(img2);
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