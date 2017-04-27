package com.titan.gyslfh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Point;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.titan.model.Image;
import com.titan.model.UploadInfo;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.ActivityUpalarmBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class UpAlarmActivity extends AppCompatActivity {
    Context mContext;
    ActivityUpalarmBinding binding;
    //WebService webservice=null;
    UploadInfo uploadinfo=null;
    ProgressDialog mprogress=null;
    SimpleDraweeView draweeView1=null;
    SimpleDraweeView draweeView2=null;
    SimpleDraweeView draweeView3=null;

    List<Image> imgs=new ArrayList<Image>();
    List<Image> copy_imgs=new ArrayList<Image>();
    int i=0;
    /**需要动态获取的权限*/
    String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};
    Spinner sp_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_up_alarm);
        initFresco();
        mContext=this;
        //webservice=new WebService(mContext);

        binding=DataBindingUtil.setContentView(this,  R.layout.activity_upalarm);
        uploadinfo=new UploadInfo();
        uploadinfo.setTYPE("1");
        binding.setUploadinfo(uploadinfo);
        sp_type= (Spinner) findViewById(R.id.sp_uptype);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //森林火灾
                        uploadinfo.setTYPE("1");
                        break;
                    case 1:
                        //病虫害
                        uploadinfo.setTYPE("2");
                        break;
                    case 2:
                        //盗砍滥伐
                        uploadinfo.setTYPE("3");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getRequeatPermission();

    }

    /**
     * 检查权限
     */
    private void getRequeatPermission() {
        // If an error is found, handle the failure to start.
        // Check permissions to see if failure may be due to lack of permissions.
        boolean permissionCheck1 = ContextCompat.checkSelfPermission(UpAlarmActivity.this, reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(UpAlarmActivity.this, reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23&&!(permissionCheck1 && permissionCheck2)) {
            // If permissions are not already granted, request permission from the user.
            int requestCode = 3;
            ActivityCompat.requestPermissions(UpAlarmActivity.this, reqPermissions, requestCode);
        }/* else {
            // Report other unknown failure types to the user - for example, location services may not
            // be enabled on the device.
                    *//*String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());*//*
          *//*  String message="获取读取SD卡权限失败";
            Toast.makeText(UpAlarmActivity.this, message, Toast.LENGTH_LONG).show();*//*

            // Update UI to reflect that the location display did not actually start
            //mSpinner.setSelection(0, true);


        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     *
     */
    private void initFresco() {
        Fresco.initialize(this);
    }
    /**
     *
     * 上报问题
     * @param view
     */
    public void onAlarm(View view) {
        Point curpt=MainActivity.getCurrentPoint();
        try {
            uploadinfo.setLAT(curpt.getY()+"");
            uploadinfo.setLON(curpt.getX()+"");
        }catch (Exception e){
            Toast.makeText(mContext,"获取坐标失败",Toast.LENGTH_SHORT).show();
            return;
        }

        EditText et_describe= (EditText) findViewById(R.id.et_describe);
        EditText et_remark= (EditText) findViewById(R.id.et_remark);
        uploadinfo.setSBH(TitanApplication.SBH);
        uploadinfo.setDESCRIBE(et_describe.getText().toString().trim());
        uploadinfo.setREMARK(et_remark.getText().toString().trim());
        //uploadinfo.setSBH(MyApplication.SBH);
        upLoadTask uptask=new upLoadTask(uploadinfo);
        uptask.execute();
        //Toast.makeText(mContext,"上报中……",Toast.LENGTH_SHORT).show();
    }
    /**
     *  打开相册
     */
    public void openGallery(View view) {
        if(imgs.size()>=3){
            Toast.makeText(mContext,"最多只能上传3张图片",Toast.LENGTH_SHORT).show();
        }else {
            RxGalleryFinal
                    .with(UpAlarmActivity.this)
                    .image()
                    .radio()
                    .maxSize(3)
                    //.crop() //裁剪
                    .imageLoader(ImageLoaderType.FRESCO)
                    .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                        @Override
                        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                            i++;
                            final Image img=new Image();
                            String path=imageRadioResultEvent.getResult().getOriginalPath();
                            img.setFJ_URL(imageRadioResultEvent.getResult().getOriginalPath());
                            imgs.add(img);
                            uploadinfo.setFJINFO(imgs);
                            SimpleDraweeView sd=new SimpleDraweeView(mContext);

                            switch (i){
                                case 1:
                                  draweeView1 = (SimpleDraweeView) findViewById(R.id.sdv_imgview1);
                                    draweeView1.setImageURI(Uri.fromFile(new File(img.getFJ_URL())));
                                    draweeView1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent=new Intent(UpAlarmActivity.this,DeleteImageActivity.class);
                                            Bundle bundle=new Bundle();
                                            String str=img.getFJ_URL();
                                            bundle.putString("img_name",str);
                                            bundle.putInt("img_location",1);
                                            intent.putExtras(bundle);
                                            startActivityForResult(intent,0);
                                        }
                                    });
                                    break;
                                case 2:
                                  draweeView2= (SimpleDraweeView) findViewById(R.id.sdv_imgview2);
                                    draweeView2.setImageURI(Uri.fromFile(new File(img.getFJ_URL())));
                                    draweeView2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent=new Intent(UpAlarmActivity.this,DeleteImageActivity.class);
                                            Bundle bundle=new Bundle();
                                            String str=img.getFJ_URL();
                                            bundle.putString("img_name",str);
                                            bundle.putInt("img_location",2);
                                            intent.putExtras(bundle);
                                            startActivityForResult(intent,0);
                                        }
                                    });
                                    break;
                                case 3:
                                  draweeView3= (SimpleDraweeView) findViewById(R.id.sdv_imgview3);
                                    draweeView3.setImageURI(Uri.fromFile(new File(img.getFJ_URL())));
                                    draweeView3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent=new Intent(UpAlarmActivity.this,DeleteImageActivity.class);
                                            Bundle bundle=new Bundle();
                                            String str=img.getFJ_URL();
                                            bundle.putString("img_name",str);
                                            bundle.putInt("img_location",3);
                                            intent.putExtras(bundle);
                                            startActivityForResult(intent,0);
                                        }
                                    });
                                    break;
                            }
                        }
                    })
                    .openGallery();
        }


    }

    /**
     * 删除图片后返回页面图片重新排列展示
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==1)
        {
            Bundle bundle = data.getExtras();
            String isDelete=null;
            isDelete = bundle.getString("isDelete");
            int img_location = bundle.getInt("img_location");
           if(isDelete!=null)
           {
               /**
                * 根据你点中的图片进行删除
                */
             if(isDelete.equals("是"))
             {

                 if(img_location==1)
                 {
                     imgs.remove(0);
                 }else if(img_location==2)
                 {
                     imgs.remove(1);
                 }else
                 {
                     imgs.remove(2);
                 }

                 /**
                  * 将删除后的图片排序展示，
                  */
                 if(imgs.size()==0)
                 {

                     if(draweeView1!=null)
                     {
                         draweeView1.setImageURI("");
                     }
                     if(draweeView2!=null)
                     {
                         draweeView2.setImageURI("");
                     }
                     if(draweeView3!=null)
                     {
                         draweeView3.setImageURI("");
                     }

                     i=0;
                 }else if(imgs.size()==1)
                 {
                     draweeView1.setImageURI(Uri.fromFile(new File(imgs.get(0).getFJ_URL())));
                     if(draweeView2!=null)
                     {
                         draweeView2.setImageURI("");
                     }
                     if(draweeView3!=null)
                     {
                         draweeView3.setImageURI("");
                     }
                     i=1;
                 }else if(imgs.size()==2)
                 {
                     draweeView1.setImageURI(Uri.fromFile(new File(imgs.get(0).getFJ_URL())));
                     draweeView2.setImageURI(Uri.fromFile(new File(imgs.get(1).getFJ_URL())));
                     if(draweeView3!=null)
                     {
                         draweeView3.setImageURI("");
                     }
                     i=2;
                 }else
                 {
                     draweeView1.setImageURI(Uri.fromFile(new File(imgs.get(0).getFJ_URL())));
                     draweeView2.setImageURI(Uri.fromFile(new File(imgs.get(1).getFJ_URL())));
                     draweeView3.setImageURI(Uri.fromFile(new File(imgs.get(2).getFJ_URL())));

                 }

                 Toast.makeText(UpAlarmActivity.this,"图片删除成功",Toast.LENGTH_SHORT).show();
             }
         }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *  上报task
     */
    class upLoadTask extends AsyncTask<Void,Void,Boolean>
    {
        UploadInfo muploadinfo;

        public upLoadTask(UploadInfo uploadinfo) {
            this.muploadinfo=uploadinfo;

        }

        @Override
        protected Boolean  doInBackground(Void... voids) {
            //List<Image> list=muploadinfo.getFJINFO();

            if(muploadinfo.getFJINFO()!=null&&muploadinfo.getFJINFO().size()>0){
                for(int i=0;i<muploadinfo.getFJINFO().size();i++)
                {
                    try {
                        FileInputStream fis = new FileInputStream(muploadinfo.getFJINFO().get(i).getFJ_URL());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[5024];
                        int count = 0;
                        while ((count = fis.read(buffer)) >= 0) {
                            baos.write(buffer, 0, count);
                        }
                        String  uploadBuffer = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);//进行Base64编码
                        muploadinfo.getFJINFO().get(i).setFJ_URL(uploadBuffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show();
                        //return false;
                    }
                }
            }

            //return  webservice.upLoadInfo(muploadinfo);
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress = new ProgressDialog(mContext);
            //mprogress.setTitle("正在提交");
            mprogress.setMessage("正在提交,请稍等...");
            mprogress.setCancelable(false);
            mprogress.show();

        }


        @Override
        protected void onPostExecute(Boolean  success) {
            super.onPostExecute(success);
            mprogress.dismiss();
            //Toast.makeText(mContext,success,Toast.LENGTH_SHORT).show();
            if(success)
            {
                mprogress.dismiss();
                Toast.makeText(mContext,"上传成功",Toast.LENGTH_SHORT).show();
                UpAlarmActivity.this.finish();

            }else {
                mprogress.dismiss();
                Toast.makeText(mContext,"上传失败",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
