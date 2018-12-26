package com.example.administrator.app44drink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ImageView backIv, listIv, favIv, locIv;
    TextView titleTv, distanceTv, descriptionTv;
    ViewPager pager;
    ImageView dotTv1, dotTv2, dotTv3, dotTv4, dotTv5;

    ArrayList<String> arrImg = new ArrayList<>();
    ArrayList<MenuInfo> arrMenu = new ArrayList<>();
    CoffeeInfo coffee;
    String id;
    String content = null;
    String time = null;
    boolean adapterCreated = false;
    boolean favBool = false;

    MapFragment map;
    android.app.FragmentManager fm;
    boolean mapBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        backIv = (ImageView) findViewById(R.id.backIv);
        listIv = (ImageView) findViewById(R.id.listIv);
        favIv = (ImageView) findViewById(R.id.favIv);
        locIv = (ImageView) findViewById(R.id.locIv);
        dotTv1 = (ImageView) findViewById(R.id.dotTv1);
        dotTv2 = (ImageView) findViewById(R.id.dotTv2);
        dotTv3 = (ImageView) findViewById(R.id.dotTv3);
        dotTv4 = (ImageView) findViewById(R.id.dotTv4);
        dotTv5 = (ImageView) findViewById(R.id.dotTv5);
        titleTv = (TextView) findViewById(R.id.titleTv);
        descriptionTv = (TextView) findViewById(R.id.descriptionTv);
        distanceTv = (TextView) findViewById(R.id.distanceTv);
        pager = (ViewPager) findViewById(R.id.pager);

        fm = getFragmentManager();
        map = (MapFragment) fm.findFragmentById(R.id.map);

        map.getView().setVisibility(View.GONE);

        sendResponse();

        locIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coffee != null) {
                    mapBool = !mapBool;
                    if (mapBool) {
                        map.getView().setVisibility(View.VISIBLE);
                        map.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                double lat = coffee.la;
                                double lng = coffee.lo;
                                LatLng loc = new LatLng(lat, lng);

                                MarkerOptions option = new MarkerOptions();
                                option.position(loc);
                                option.title(coffee.title);

                                option.draggable(true);
                                googleMap.addMarker(option);
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                            }
                        });
                    } else {
                        map.getView().setVisibility(View.GONE);
                    }
                }
            }
        });


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

        listIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterCreated) {
                    Intent intent = new Intent(DetailActivity.this, com.example.administrator.app44drink.MenuActivity.class);
                    intent.putExtra("menu", arrMenu);
                    startActivity(intent);
                }
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        favIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterCreated) {
                    favBool = !favBool;
                    if (favBool) {
                        favIv.setImageResource(R.drawable.top_bookmark_on);
                    } else {
                        favIv.setImageResource(R.drawable.top_bookmark_off);
                    }
                    RequestQueue stringRequest = Volley.newRequestQueue(DetailActivity.this);
                    String str = getResources().getString(R.string.url) + "clickMy.php";
                    StringRequest myReq = new StringRequest(
                            Request.Method.POST,
                            str,
                            clickResponse,
                            errorResponse) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", id);
                            params.put("num", coffee.num + "");
                            if (favBool) {
                                params.put("type", "1");
                            } else {
                                params.put("type", "0");
                            }
                            return params;
                        }
                    };
                    myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                    stringRequest.add(myReq);
                }
            }
        });

    }

    private void sendResponse() {
        SharedPreferences prefs = getSharedPreferences("Intro", MODE_PRIVATE);
        id = prefs.getString("id", "aaa");

        final String num = getIntent().getStringExtra("num");
        final String la = getIntent().getStringExtra("la");
        final String lo = getIntent().getStringExtra("lo");

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String str = getResources().getString(R.string.url) + "shopContent.php";
        StringRequest myReq = new StringRequest(
                Request.Method.POST,
                str,
                shopListResponse,
                errorResponse) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("la", la);
                params.put("lo", lo);
                params.put("num", num);
                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

    Response.Listener<String> clickResponse = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {

        }
    };

    Response.Listener<String> shopListResponse = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            JSONObject shop = null;
            try {
                shop = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String bool = "";
            try {
                bool = shop.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (bool.equals("false")) {
                return;
            } else {

                JSONArray img = null;
                JSONArray menuList = null;
                try {
                    img = shop.getJSONArray("img");
                    menuList = shop.getJSONArray("list");

                    int tempNum = Integer.parseInt(shop.getString("num"));
                    String tempImg = "";
                    String tempTitle = shop.getString("title");
                    int tempBanner = Integer.parseInt(shop.getString("baner"));
                    int tempKm = Integer.parseInt(shop.getString("km"));
                    double tempLa = Double.parseDouble(shop.getString("la"));
                    double tempLo = Double.parseDouble(shop.getString("lo"));
                    int tempMyshop = Integer.parseInt(shop.getString("myshop"));
                    content = shop.getString("content");
                    time = shop.getString("time");
                    coffee = new CoffeeInfo(tempNum, tempImg, tempTitle, tempBanner, tempKm, tempLa, tempLo, tempMyshop);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrImg.clear();
                for (int i = 0; i < img.length(); i++) {
                    try {
                        String temp = img.getJSONObject(i).getString("img");
                        arrImg.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < menuList.length(); i++) {
                    try {

                        JSONObject temp = menuList.getJSONObject(i);
                        String name = temp.getString("name");
                        int price = Integer.parseInt(temp.getString("price"));
                        MenuInfo tempInfo = new MenuInfo(name, price);
                        arrMenu.add(tempInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!adapterCreated) {

                titleTv.setText(coffee.title);
                descriptionTv.setText(content);
                distanceTv.setText("" + coffee.km + "km");
                Log.d("heu", arrImg.size() + "");
                pager.setAdapter(new DetailActivity.PagerAdapter(getSupportFragmentManager()));
                setDot(pager.getCurrentItem());
                adapterCreated = true;
                dotTv1.setVisibility(View.VISIBLE);
                dotTv2.setVisibility(View.VISIBLE);
                dotTv3.setVisibility(View.VISIBLE);
                dotTv4.setVisibility(View.VISIBLE);
                dotTv5.setVisibility(View.VISIBLE);
                if (arrImg.size() == 1) {
                    dotTv1.setVisibility(View.GONE);
                    dotTv2.setVisibility(View.GONE);
                    dotTv3.setVisibility(View.GONE);
                    dotTv4.setVisibility(View.GONE);
                    dotTv5.setVisibility(View.GONE);
                } else if (arrImg.size() == 2) {
                    dotTv3.setVisibility(View.GONE);
                    dotTv4.setVisibility(View.GONE);
                    dotTv5.setVisibility(View.GONE);
                } else if (arrImg.size() == 3) {
                    dotTv4.setVisibility(View.GONE);
                    dotTv5.setVisibility(View.GONE);
                } else if (arrImg.size() == 4) {
                    dotTv5.setVisibility(View.GONE);
                }
            }

        }
    };
    Response.ErrorListener errorResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    int position;

    private void setDot(int position) {
        this.position = position;
        dotTv1.setImageResource(R.drawable.circle_off);
        dotTv2.setImageResource(R.drawable.circle_off);
        dotTv3.setImageResource(R.drawable.circle_off);
        dotTv4.setImageResource(R.drawable.circle_off);
        dotTv5.setImageResource(R.drawable.circle_off);
        switch (position) {
            case 0:
                dotTv1.setImageResource(R.drawable.circle_on);
                break;
            case 1:
                dotTv2.setImageResource(R.drawable.circle_on);
                break;
            case 2:
                dotTv3.setImageResource(R.drawable.circle_on);
                break;
            case 3:
                dotTv4.setImageResource(R.drawable.circle_on);
                break;
            case 4:
                dotTv5.setImageResource(R.drawable.circle_on);
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
            f.setImg(arrImg.get(i));
            return f;
        }

        @Override
        public int getCount() {
            return arrImg.size();
        }
    }

}
