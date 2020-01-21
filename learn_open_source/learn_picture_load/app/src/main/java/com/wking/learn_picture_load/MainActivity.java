package com.wking.learn_picture_load;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

public class MainActivity extends AppCompatActivity {
    private ImageView mImg;
    String url = "https://www.wanandroid.com/resources/image/pc/default_project_img.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImg = (ImageView) findViewById(R.id.img_1);

        init();
    }

    private void init() {
//        Glide.with(this).load(url).into(mImg);
//        GlideApp.with(this).load(url).into(mImg);
//        Glide.with(this).asBitmap().load(url).into(mImg);
        Glide.with(this).load(url).into(mImg);
        BaseRequestOptions options = new RequestOptions().error().centerCrop().placeholder()
        Glide.with(this).load(url).apply(new Base)
    }
}
