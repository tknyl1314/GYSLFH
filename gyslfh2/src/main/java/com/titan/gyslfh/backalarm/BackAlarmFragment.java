package com.titan.gyslfh.backalarm;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.titan.gyslfh.alarminfo.AlarmInfoModel;
import com.titan.gyslfh.upfireinfo.PhotoAdapter;
import com.titan.model.Image;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragBackalarmBinding;
import com.titan.util.DateUtil;
import com.titan.util.SnackbarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by whs on 2017/5/17
 * 回警界面
 */

public class BackAlarmFragment extends Fragment implements BackAlarmInterface ,OnDateSetListener {
    private BackAlarmViewModel mViewModel;

    private FragBackalarmBinding mDataBinding;
    //上传进度
    private Dialog mProgresssDialog;
    //是否火情dialog
    private  Dialog mIsfireDialog;
    //时间选择
    private TimePickerDialog mTimePickerDialog;
    //接警来源
    private Dialog mAlarmOriginDialog;
    //时间类型
    private int timetype=1;
    //adapter
    private  PhotoAdapter mAdapter;



    private static final String ARG_PARAM1 = "param1";

    private String alarmid;

    private AlarmInfoModel.AlarmInfo mAlarmInfo;
    /**
     ** 需要动态获取的权限
     */
    String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};

    public static BackAlarmFragment newInstance(AlarmInfoModel.AlarmInfo alarmInfo) {
        BackAlarmFragment fragment=new BackAlarmFragment();
        Bundle bundle=new Bundle();
        //bundle.putString(ARG_PARAM1,alarmid);
        bundle.putSerializable(ARG_PARAM1,  alarmInfo);
        fragment.setArguments(bundle);
        return  fragment;
    }

    public void setViewModel(BackAlarmViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //alarmid = getArguments().getString(ARG_PARAM1);
            mAlarmInfo = (AlarmInfoModel.AlarmInfo) getArguments().get(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.frag_backalarm, container, false);
        mDataBinding.setViewmodel(mViewModel);
        return mDataBinding.getRoot();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSnackbar();

        intiRecyclerView();

        getRequeatPermission();


    }
    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(mAlarmInfo);
    }

    /**
     * 初始化图片
     */
    private void intiRecyclerView() {
         mAdapter = new PhotoAdapter(getActivity(),new ArrayList<Image>(0),  this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.rvImgs.setLayoutManager(linearLayoutManager);
        mDataBinding.rvImgs.setAdapter(mAdapter);

    }

    private void setupSnackbar() {
        mViewModel.snackbarText.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        SnackbarUtils.showSnackbar(getView(), mViewModel.snackbarText.get());

                    }
                });
    }

    /**
     * 检查权限
     */
    private void getRequeatPermission() {
        // If an error is found, handle the failure to start.
        // Check permissions to see if failure may be due to lack of permissions.
        boolean permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !(permissionCheck1 && permissionCheck2)) {
            // If permissions are not already granted, request permission from the user.
            int requestCode = 3;
            requestPermissions(reqPermissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void showDateDialog(int timetype) {
        this.timetype=timetype;
        if (mTimePickerDialog == null) {
            long fiveYears = 5L * 365 * 1000 * 60 * 60 * 24L;

            long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
            mTimePickerDialog = new TimePickerDialog.Builder()
                    .setCallBack(this)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("时间选择")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("时")
                    .setMinuteText("分")
                    .setThemeColor(getResources().getColor(R.color.colorPrimary))
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis()-fiveYears)
                    .setMaxMillseconds(System.currentTimeMillis()+tenYears)
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setType(Type.ALL)
                    .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                    .setWheelItemTextSize(12)
                    .build();

        }
        mTimePickerDialog.show(getFragmentManager(),"timepicker");


    }

    /**
     * 1:所属地区
     * 2:通知区县
     * 通知区县选择
     */
    @Override
    public void showCountrySelectDialog(final int type) {


    }



    @Override
    public void showProgress(boolean isshow) {
        if (mProgresssDialog == null) {
            mProgresssDialog = new MaterialDialog.Builder(getActivity())
                    //.title(getActivity().getString(R.string.title))
                    .content(getActivity().getString(R.string.content_progress))
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }
        if (isshow) {
            mProgresssDialog.show();
        } else {
            mProgresssDialog.dismiss();
        }

    }

    /**
     * 显示图片
     */
    @Override
    public void showImage() {
        mAdapter.refresh();
        //mAdapter.notifyDataSetChanged();

    }

    @Override
    public void showImageDetail(int position) {

        mViewModel.imgs.remove(position);
        mAdapter.notifyDataSetChanged();
        //mViewModel.snackbarText.set(position+"");
    }


    /**
     * 接警来源
     */
    @Override
    public void showOriginDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(getActivity().getString(R.string.origin))
                .items((CharSequence[]) getActivity().getResources().getStringArray(R.array.alarm_source))
                .cancelable(false)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mViewModel.alarmsouce.set(""+position);
                    }
                })
                .build()
                .show();
    }

    /**
     * 火情状态
     */
    @Override
    public void showStatusDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(getActivity().getString(R.string.status))
                .items((CharSequence[]) getActivity().getResources().getStringArray(R.array.firestatus))
                .cancelable(false)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Map<Integer,String> map=new HashMap<>();
                        map.put(0, position+"");
                        map.put(1, (String) text);
                        mViewModel.firestatus.set(map);
                    }
                })
                .build()
                .show();

    }

    @Override
    public void showIsfireDialog() {

        new MaterialDialog.Builder(getActivity())
                .cancelable(false)
                .title(getActivity().getString(R.string.alarminfo_isfire))
                .items((CharSequence[]) getActivity().getResources().getStringArray(R.array.alarm_isfire))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Map<Integer,String> map=new HashMap<>();
                        map.put(0, position+"");
                        map.put(1, (String) text);
                        mViewModel.isfire.set(map);
                    }
                })
                .build()
                .show();

    }

    @Override
    public void openAlarmInfoDetails(String id) {

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String time = DateUtil.dateFormat(new Date(millseconds));
        switch (timetype){
            //
            case 1:
                mViewModel.reportTime.set(time);
                break;
            //
            case 2:
                mViewModel.policeTime.set(time);
                break;
        }


    }


    /**
     * 火情类型
     */
    @Override
    public void showAlarmType() {
        new MaterialDialog.Builder(getActivity())
                .title(getActivity().getString(R.string.firetype))
                .items((CharSequence[]) getActivity().getResources().getStringArray(R.array.firetype))
                .cancelable(false)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Map<Integer,String> map=new HashMap<>();
                        map.put(0, position+"");
                        map.put(1, (String) text);
                        mViewModel.firetype.set(map);
                    }
                })
                .build()
                .show();

    }
}
