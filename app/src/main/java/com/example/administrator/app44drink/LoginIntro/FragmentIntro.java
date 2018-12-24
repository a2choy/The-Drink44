package com.example.administrator.app44drink.LoginIntro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.app44drink.R;

public class FragmentIntro extends Fragment {

    ImageView mainIv;
    int img;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_intro, container, false);

        mainIv = (ImageView)view.findViewById(R.id.mainIv);

        mainIv.setImageResource(img);
        return view;
    }
    public void setImg(int img){
        this.img = img;
    }
}
