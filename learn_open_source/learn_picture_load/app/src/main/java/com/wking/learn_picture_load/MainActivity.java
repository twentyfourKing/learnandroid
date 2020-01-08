package com.wking.learn_picture_load;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

public class MainActivity extends AppCompatActivity {
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImg = (ImageView)findViewById(R.id.img_1);

        init();
    }

    private void init(){
        Glide.with(mImg).load("");
        GlideBuilder
    }
}
