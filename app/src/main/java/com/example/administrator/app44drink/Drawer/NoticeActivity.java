package com.example.administrator.app44drink.Drawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.app44drink.LoginIntro.SignUpActivity;
import com.example.administrator.app44drink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mainLv;
    ImageView drawerIv;
    TextView home, myTv, noticeTv, askTv, howTv, settingTv;
    DrawerLayout drawer;

    boolean nextPageAvailable = false;
    int currentPage = 0;
    boolean lastItemVisibleFlag = false;

    ArrayAdapter adapter;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<String> num = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        mainLv = (ListView) findViewById(R.id.mainLv);

        home = (TextView) findViewById(R.id.home);
        myTv = (TextView) findViewById(R.id.myTv);
        noticeTv = (TextView) findViewById(R.id.noticeTv);
        askTv = (TextView) findViewById(R.id.askTv);
        howTv = (TextView) findViewById(R.id.howTv);
        settingTv = (TextView) findViewById(R.id.settingTv);

        drawerIv = (ImageView) findViewById(R.id.drawerIv);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        sendResponse();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arr) ;
        mainLv.setAdapter(adapter);
        mainLv.setOnItemClickListener(this);
        mainLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && nextPageAvailable) {
                    currentPage++;
                    sendResponse();
                }
            }

        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, com.example.administrator.app44drink.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, MyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        noticeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        askTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, AskActivity.class);
                startActivity(intent);
                finish();
            }
        });

        howTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, HowActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void sendResponse(){
        SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
        final String id = prefs.getString("id", "1234");

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String str = getResources().getString(R.string.url) + "newsList.php";
        StringRequest myReq = new StringRequest(
                Request.Method.POST,
                str,
                noticeResponse,
                errorResponse) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("lastnum", "" + currentPage);
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
                String addList = "";
                JSONArray list = null;
                try {
                    addList = news.getString("addlist");
                    list = news.getJSONArray("list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                nextPageAvailable = addList.equals("1") ? true : false;
                for (int i = 0; i < list.length(); i++) {
                    String temp = "";
                    try {
                        if(list.getJSONObject(i).getString("type").equals("1")){
                            temp += "[공지] ";
                        }
                        temp += list.getJSONObject(i).getString("title");
                        arr.add(temp);
                        String tempNum = list.getJSONObject(i).getString("num");
                        num.add(tempNum);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    };

    Response.ErrorListener errorResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, com.example.administrator.app44drink.Drawer.Notice2Activity.class);
        intent.putExtra("num", num.get(i));
        startActivity(intent);

    }
}
