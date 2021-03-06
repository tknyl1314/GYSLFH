package com.titan.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.otitan.gyslfh.R;
import com.otitan.gyslfh.activity.UpFireActivity;

import java.io.File;

/**
 * Created by titan on 2016/12/18.
 */

public class DeleteImageActivity extends AppCompatActivity {
    ImageView img_isBack; //返回按钮
    ImageView img_isDelete;//删除按钮
    ImageView img_show;
    Toolbar toolbar;
    boolean isDelete=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedetial);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        img_isBack= (ImageView) findViewById(R.id.forest_isBack);
        img_isDelete= (ImageView) findViewById(R.id.forest_isDelete);
        img_show= (ImageView) findViewById(R.id.delete_img);
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        String img_name = bundle.getString("img_name");
        final int img_location = bundle.getInt("img_location");
        if(img_name!=null)
        {
            img_show.setImageURI(Uri.fromFile(new File(img_name)));
        }else
        {
            Toast.makeText(DeleteImageActivity.this,"没有选中图片", Toast.LENGTH_SHORT).show();
        }

        //图片删除点击事件
        img_isDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDelete=true;
                Intent intent=new Intent(DeleteImageActivity.this, UpFireActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBoolean("isDelete",isDelete);
                bundle.putInt("img_location",img_location);
                intent.putExtras(bundle);
                setResult(1,intent);
                finish();
            }
        });
        //页面返回按钮
        img_isBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(DeleteImageActivity.this,UpFireActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isDelete",isDelete);
                    bundle.putInt("img_location",img_location);
                    intent.putExtras(bundle);
                    setResult(1,intent);
                    finish();

            }
        });

    }


}
