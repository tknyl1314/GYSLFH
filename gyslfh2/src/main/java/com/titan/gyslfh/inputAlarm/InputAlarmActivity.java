package com.titan.gyslfh.inputAlarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.esri.arcgisruntime.geometry.Point;
import com.titan.gyslfh.main.MainActivity;
import com.titan.model.Image;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.ActivityInputAlarmBinding;
import com.titan.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class InputAlarmActivity extends AppCompatActivity implements InputAlarmView {


    private Context mContext;
    private InputAlarmViewModel viewModel;
    private InputAlarm inputAlarm;
    private ActivityInputAlarmBinding binding;
    private List<Image> imgs = new ArrayList<Image>();
    private Point curpoint= null;
    private int imgMax = 9;
    /**需要动态获取的权限*/
    private String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_alarm);

        if (binding == null) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_input_alarm);
        }
        mContext = InputAlarmActivity.this;
        inputAlarm = new InputAlarm();
        binding.setInputAlarm(inputAlarm);
        viewModel = new InputAlarmViewModel(mContext, this, inputAlarm);

        initData();
        getRequeatPermission();
    }

    private void initView() {

    }

    private void initData() {
        curpoint = MainActivity.getCurrentPoint();
        binding.inputAlarmLon.setText(curpoint.getX()+"");
        binding.inputAlarmLat.setText(curpoint.getY()+"");


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inputAlarm_back:/**返回按钮*/
                this.finish();
                break;
            case R.id.input_submit:/**提交按钮*/
                viewModel.sendMesToServer();
                break;
            case R.id.inputAlarm_policeTime:/**出警时间*/
                viewModel.showTimePopuwindow(binding.inputAlarmPoliceTime, false);
                break;
            case R.id.inputAlarm_reportTime:/**上报时间*/
                viewModel.showTimePopuwindow(binding.inputAlarmReportTime, false);
                break;
            case R.id.inputAlarm_pic_view:/**图片选择*/
                toSelectPic();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到图片选择页面
     */
    private void toSelectPic() {
        if (imgs.size() > 3) {
            String tip = mContext.getResources().getString(R.string.picselect_tip);
            ToastUtil.setToast(mContext, tip);
        } else {
            RxGalleryFinal
                    .with(InputAlarmActivity.this)
                    .image()
                    .radio()
                    .maxSize(imgMax)
                    //.crop() //裁剪
                    .imageLoader(ImageLoaderType.FRESCO)
                    .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                        @Override
                        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                            Image img = new Image();
                            String path = imageRadioResultEvent.getResult().getOriginalPath();
                            img.setFJ_URL(path);
                            imgs.add(img);
                        }
                    })
                    .openGallery();
        }
    }

    @Override
    public void showInProgress() {
        binding.inputAlarmProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnProgress() {
        binding.inputAlarmProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    /**
     * 检查权限
     */
    private void getRequeatPermission() {
        // If an error is found, handle the failure to start.
        // Check permissions to see if failure may be due to lack of permissions.
        boolean permissionCheck1 = ContextCompat.checkSelfPermission(InputAlarmActivity.this, reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(InputAlarmActivity.this, reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23&&!(permissionCheck1 && permissionCheck2)) {
            // If permissions are not already granted, request permission from the user.
            int requestCode = 3;
            ActivityCompat.requestPermissions(InputAlarmActivity.this, reqPermissions, requestCode);
        }
    }
}
