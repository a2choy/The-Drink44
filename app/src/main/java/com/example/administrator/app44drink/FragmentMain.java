package com.example.administrator.app44drink;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.app44drink.R;

public class FragmentMain extends Fragment {

    ImageView mainIv;
    String img;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_intro, container, false);

        mainIv = (ImageView)view.findViewById(R.id.mainIv);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);
        requestOptions.centerCrop();
        Glide
                .with(getActivity())
                .setDefaultRequestOptions(requestOptions)
                .load(img)
                .into(mainIv);

        return view;
    }
    public void setImg(String img){
        this.img = img;
    }

}
