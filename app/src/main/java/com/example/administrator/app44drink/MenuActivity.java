package com.example.administrator.app44drink;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    ListView mainLv1, mainLv2;

    ArrayList<MenuInfo> arr1 = new ArrayList<>();
    ArrayList<MenuInfo> arr2 = new ArrayList<>();
    MyAdapter1 adapter1;
    MyAdapter2 adapter2;

    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mainLv1 = (ListView)findViewById(R.id.mainLv1);
        mainLv2 = (ListView)findViewById(R.id.mainLv2);

        ArrayList<MenuInfo> temp = (ArrayList<MenuInfo>)getIntent().getSerializableExtra("menu");

        findViewById(R.id.backIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        for(int i = 0; i < temp.size(); i++){
            if(flag){
                arr1.add(temp.get(i));
            } else {
                arr2.add(temp.get(i));
            }
            flag = !flag;
        }
        if(arr1.size() > arr2.size()){
            arr2.add(new MenuInfo("",-1));
        }
        adapter1 = new MyAdapter1(this);
        adapter2 = new MyAdapter2(this);
        mainLv1.setAdapter(adapter1);
        mainLv2.setAdapter(adapter2);

    }
    public class RowDataViewHolder {
        TextView menuTv;
        TextView priceTv;
    }
    class MyAdapter1 extends ArrayAdapter {
        LayoutInflater lnf;

        public MyAdapter1(Activity context) {
            super(context, R.layout.item,  arr1);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return  arr1.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return  arr1.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            RowDataViewHolder viewHolder;
            if (convertView == null) {
                convertView = lnf.inflate(R.layout.menu_item, parent, false);
                viewHolder = new RowDataViewHolder();
                viewHolder.menuTv = (TextView) convertView.findViewById(R.id.menuTv);
                viewHolder.priceTv = (TextView)convertView.findViewById(R.id.priceTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RowDataViewHolder) convertView.getTag();
            }

            viewHolder.menuTv.setText(arr1.get(position).name);
            if(arr1.get(position).price < 0){
                viewHolder.priceTv.setText("");
            } else {
                viewHolder.priceTv.setText(arr1.get(position).price + "원");
            }

            return convertView;
        }
    }
    class MyAdapter2 extends ArrayAdapter {
        LayoutInflater lnf;

        public MyAdapter2(Activity context) {
            super(context, R.layout.item,  arr2);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return  arr2.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return  arr2.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            RowDataViewHolder viewHolder;
            if (convertView == null) {
                convertView = lnf.inflate(R.layout.menu_item, parent, false);
                viewHolder = new RowDataViewHolder();
                viewHolder.menuTv = (TextView) convertView.findViewById(R.id.menuTv);
                viewHolder.priceTv = (TextView)convertView.findViewById(R.id.priceTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RowDataViewHolder) convertView.getTag();
            }

            viewHolder.menuTv.setText(arr2.get(position).name);
            if(arr2.get(position).price < 0){
                viewHolder.priceTv.setText("");
            } else {
                viewHolder.priceTv.setText(arr2.get(position).price + "원");
            }


            return convertView;
        }
    }
}
