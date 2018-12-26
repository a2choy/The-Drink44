package com.example.administrator.app44drink;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.administrator.app44drink.Drawer.AskActivity;
import com.example.administrator.app44drink.Drawer.HowActivity;
import com.example.administrator.app44drink.Drawer.MyActivity;
import com.example.administrator.app44drink.Drawer.NoticeActivity;
import com.example.administrator.app44drink.Drawer.SettingActivity;
import com.example.administrator.app44drink.LoginIntro.SignUp1Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ImageView searchIv, favIv, mapIv;
    LinearLayout layout;
    ListView mainLv;
    MyAdapter adapter;

    ViewPager pager;
    ImageView doTv1;
    ImageView doTv2;
    ImageView doTv3;

    DrawerLayout drawer;
    ImageView drawerIv;
    TextView home, myTv, noticeTv, askTv, howTv, settingTv;

    String id;
    String search = "";
    double lat, lon;
    int type = 1;

    PagerAdapter pagerAdapter;
    boolean adapterCreated = false;
    boolean mapMode = false;
    boolean nextPageAvailable = false;
    int currentPage = 0;
    boolean lastItemVisibleFlag = false;

    String[] per = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    ArrayList<CoffeeInfo> arr = new ArrayList<>();
    ArrayList<Event> arrEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchIv = (ImageView)findViewById(R.id.searchIv);
        mapIv = (ImageView)findViewById(R.id.mapIv);
        favIv = (ImageView)findViewById(R.id.favIv);
        layout = (LinearLayout)findViewById(R.id.layout);
        mainLv = (ListView)findViewById(R.id.mainLv);
        doTv1 = (ImageView)findViewById(R.id.dotTv1);
        doTv2 = (ImageView)findViewById(R.id.dotTv2);
        doTv3 = (ImageView)findViewById(R.id.dotTv3);
        pager = (ViewPager)findViewById(R.id.pager);

        drawerIv = (ImageView)findViewById(R.id.drawerIv);
        home = (TextView)findViewById(R.id.home);
        myTv = (TextView)findViewById(R.id.myTv);
        noticeTv = (TextView)findViewById(R.id.noticeTv);
        askTv = (TextView)findViewById(R.id.askTv);
        howTv = (TextView)findViewById(R.id.howTv);
        settingTv = (TextView)findViewById(R.id.settingTv);

        init();
        init2();

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });



        adapter = new MyAdapter(this);
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
        SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
        id = prefs.getString("id", "aaa");

        arr.clear();
        sendResponse();

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                setDot(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        favIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
                alt_bld.setTitle("정렬 방식");
                alt_bld.setMessage("정렬 방식");
                alt_bld.setCancelable(false);
                alt_bld.setPositiveButton("즐겨찾기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                type = 2;
                                currentPage = 0;
                                arr.clear();
                                sendResponse();
                            }
                        });
                alt_bld.setNegativeButton("보통",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                type = 1;
                                currentPage = 0;
                                arr.clear();
                                sendResponse();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
                return;
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        myTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        noticeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        askTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AskActivity.class);
                startActivity(intent);
                finish();
            }
        });

        howTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HowActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void sendResponse(){
        final String finalId = id;

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String str = getResources().getString(R.string.url) + "shopList.php";
        StringRequest myReq = new StringRequest(
                Request.Method.POST,
                str,
                shopListResponse,
                errorResponse) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",finalId);
                params.put("la", String.valueOf(lat));
                params.put("lo", String.valueOf(lon));
                params.put("type", String.valueOf(type));
                if(type == 1 || type == 2) {
                    params.put("search", "");
                } else {
                    params.put("search", search);
                }
                params.put("pagenum", String.valueOf(currentPage));
                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

    Response.Listener<String> shopListResponse = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            JSONObject shopList = null;
            try {
                shopList = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String bool = "";
            try {
                bool = shopList.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(bool.equals("false")){
                return;
            } else {
                JSONArray event = null;
                JSONArray shop = null;
                try {
                    event = shopList.getJSONArray("event");
                    shop = shopList.getJSONArray("list");
                    if(shopList.getString("addlist").equals("1")){
                        nextPageAvailable = true;
                    } else {
                        nextPageAvailable = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrEvent.clear();
                for (int i = 0; i < event.length(); i++) {
                    try {
                        String temp = event.getJSONObject(i).getString("img");
                        int tempNum = Integer.parseInt(event.getJSONObject(i).getString("num"));
                        arrEvent.add(new Event(tempNum,temp));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < shop.length(); i++) {
                    try {

                        JSONObject temp = shop.getJSONObject(i);
                        int tempNum = Integer.parseInt(temp.getString("num"));
                        String tempImg = temp.getString("img");
                        String tempTitle = temp.getString("title");
                        int tempBanner = Integer.parseInt(temp.getString("baner"));
                        int tempKm = Integer.parseInt(temp.getString("km"));
                        double tempLa = Double.parseDouble(temp.getString("la"));
                        double tempLo = Double.parseDouble(temp.getString("lo"));
                        int tempMyshop = Integer.parseInt(temp.getString("myshop"));
                        CoffeeInfo tempInfo = new CoffeeInfo(tempNum, tempImg, tempTitle, tempBanner, tempKm, tempLa,tempLo,tempMyshop);
                        arr.add(tempInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            adapter.notifyDataSetChanged();
            pagerAdapter.notifyDataSetChanged();

            if(!adapterCreated) {
                setDot(pager.getCurrentItem());
                adapterCreated = true;
            }

        }
    };
    Response.ErrorListener errorResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };


    private void init2() {
        GPSListener listener = new GPSListener();
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //            //    ActivityCompat#requestPermissions
            //            // here to request the missing permissions, and then overriding
            //            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //            //                                          int[] grantResults)
            //            // to handle the case where the user grants the permission. See the documentation
            //            // for ActivityCompat#requestPermissions for more details.
            //            //권한 없어서 못쓴다
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 5, listener);
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            //Log.d("heu", "222lat: " + location.getLatitude() + ",  lon: "+ location.getLongitude());
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
    }





    int position;
    private void setDot(int position){
        this.position = position;
        doTv1.setImageResource(R.drawable.circle_off);
        doTv2.setImageResource(R.drawable.circle_off);
        doTv3.setImageResource(R.drawable.circle_off);
        switch (position){
            case 0:
                doTv1.setImageResource(R.drawable.circle_on);
                break;

            case 1:
                doTv2.setImageResource(R.drawable.circle_on);
                break;

            case 2:
                doTv3.setImageResource(R.drawable.circle_on);
                break;
        }
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            FragmentMain f = new FragmentMain();
            f.setImg(arrEvent.get(i).url);
            return f;
        }

        @Override
        public int getCount() {
            return arrEvent.size();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, com.example.administrator.app44drink.DetailActivity.class);
        intent.putExtra("num", "" + arr.get(position).num);
        intent.putExtra("la", "" + lat);
        intent.putExtra("lo", "" + lon);
        startActivity(intent);

    }

    public class RowDataViewHolder {
        ImageView mainIv;
        ImageView bannerIv;
        TextView cafeTv;
        TextView distanceTv;
    }
    class MyAdapter extends ArrayAdapter {
        LayoutInflater lnf;

        public MyAdapter(Activity context) {
            super(context, R.layout.item,  arr);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return  arr.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return  arr.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            RowDataViewHolder viewHolder;
            if (convertView == null) {
                convertView = lnf.inflate(R.layout.item, parent, false);
                viewHolder = new RowDataViewHolder();
                viewHolder.mainIv = (ImageView)convertView.findViewById(R.id.mainIv);
                viewHolder.bannerIv = (ImageView)convertView.findViewById(R.id.bannerIv);
                viewHolder.cafeTv = (TextView) convertView.findViewById(R.id.cafeTv);
                viewHolder.distanceTv = (TextView)convertView.findViewById(R.id.distanceTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RowDataViewHolder) convertView.getTag();
            }

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.ic_launcher);
            requestOptions.error(R.mipmap.ic_launcher);
            requestOptions.centerCrop();
            Glide
                    .with(MainActivity.this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(arr.get(position).img)
                    .into(viewHolder.mainIv);


            if(arr.get(position).banner == 1){
                viewHolder.bannerIv.setVisibility(View.VISIBLE);
                viewHolder.bannerIv.setImageResource(R.drawable.best_icon);
            } else if(arr.get(position).banner == 2) {
                viewHolder.bannerIv.setVisibility(View.VISIBLE);
                viewHolder.bannerIv.setImageResource(R.drawable.new_icon);
            } else {
                viewHolder.bannerIv.setVisibility(View.GONE);
            }

            viewHolder.cafeTv.setText(arr.get(position).title);
            viewHolder.distanceTv.setText("" + arr.get(position).km);


            return convertView;
        }
    }
    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void init() {
        int pos = -1;
        for (int i = 0; i < per.length; i++) {
            int permissionChk = ContextCompat.checkSelfPermission(this, per[i]);
            if (permissionChk == PackageManager.PERMISSION_GRANTED) {
                //권한 있으므로 그냥 실행
                Log.d("heu", "이미 권한을 부여했음");
            } else {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, per[pos])) {
                //이전에 해당 권한을 거절한 이력이 있음
            }
            //런타임 권한이 없으므로 권한 요청
            String[] pers = {per[pos]};
            ActivityCompat.requestPermissions(this, pers, 2000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //승락
                    Log.d("heu", "승락");
                    init();
                } else {
                    //거절
                    Log.d("heu", "거절");
                }
            }
        }
    }
}
