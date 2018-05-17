package com.titan.gyslfh.upfireinfo;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.titan.gis.selectaddress.SelectAddressFragment;
import com.titan.gis.selectaddress.SelectAddressViewModel;
import com.titan.model.Image;
import com.titan.model.TitanLocation;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragUpalarmBinding;
import com.titan.util.SnackbarUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by whs on 2017/5/17
 * 火警上报／接警录入
 */

public class UpAlarmFragment extends Fragment implements IUpAlarm ,OnDateSetListener {
    private UpAlarmViewModel mViewModel;

    private FragUpalarmBinding mDataBinding;
    //上传进度
    private Dialog mProgresssDialog;
    //通知区县选择
    private Dialog mCountrysDiallg;
    //时间选择
    private TimePickerDialog mTimePickerDialog;
    //时间类型
    private int timetype=1;
    //adapter
    private  PhotoAdapter mAdapter;
    private Context mContext;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);



    /**
     ** 需要动态获取的权限
     */
    String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};

    public static UpAlarmFragment newInstance() {
        return new UpAlarmFragment();
    }

    public void setViewModel(UpAlarmViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.frag_upalarm, container, false);
        mDataBinding.setViewmodel(mViewModel);
        return mDataBinding.getRoot();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();

        setupSnackbar();

        intiRecyclerView();

        getRequeatPermission();


    }

    /**
     * 初始化图片
     */
    private void intiRecyclerView() {
       /* mDataBinding.advTest.setImageURI(Uri.fromFile(new File("/storage/C01A-21A7/DCIM/IMMQY/IMG_20170522153811.jpg")));
        List<Image> images=new ArrayList<>();
        Image image=new Image();
        image.setPath("/storage/C01A-21A7/DCIM/IMMQY/IMG_20170522153811.jpg");
        images.add(image);*/
         mAdapter = new PhotoAdapter(getActivity(),new ArrayList<Image>(0), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.rvImgs.setLayoutManager(linearLayoutManager);
        mDataBinding.rvImgs.setAdapter(mAdapter);
        //mDataBinding.rvImgs.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mDataBinding.rvImgs.addOnScrollListener(mController);

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
    public void upLoadAlarmInfo() {

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
                    .setType(Type.ALL)
                    .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
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

        if(mCountrysDiallg==null){
           mCountrysDiallg= new MaterialDialog.Builder(mContext)
                    .title(getActivity().getString(R.string.countrys))
                    .items((CharSequence[]) mContext.getResources().getStringArray(R.array.countrys))
                    .itemsIds(mContext.getResources().getIntArray(R.array.countrys_id))
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            //int[] dqids=getActivity().getResources().getIntArray(R.array.countrys_id);
                            //int dqid =dqids[which];
                           /* switch (type){

                                case 2:


                            }*/
                            mViewModel.noticeAreaIDs.clear();
                            Collections.addAll(mViewModel.noticeAreaIDs, which);

                            //mDataBinding.btnNoticeareaids.setText();
                            return true;
                        }
                    })
                   .onPositive(new MaterialDialog.SingleButtonCallback() {
                       @Override
                       public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                           dialog.dismiss();
                       }
                   })
                   .onNegative(new MaterialDialog.SingleButtonCallback() {
                       @Override
                       public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                           mViewModel.noticeAreaIDs.clear();
                           dialog.clearSelectedIndices();
                       }
                   })
                   .alwaysCallMultiChoiceCallback()
                   .positiveText("确定")
                   .negativeText("取消")
                   .build();

        }
        mCountrysDiallg.show();

    }



    @Override
    public void showProgress(boolean isshow) {
        if (mProgresssDialog == null) {
            mProgresssDialog = new MaterialDialog.Builder(mContext)
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

        new MaterialDialog.Builder(mContext)
                .title(getActivity().getString(R.string.origin))
                .items((CharSequence[]) getActivity().getResources().getStringArray(R.array.alarm_source))
                .cancelable(true)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("id", position+1);
                        map.put("name",text);
                        //map.put(1, (String) text);
                        mViewModel.orgin.set(map);
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
        new MaterialDialog.Builder(mContext)
                .title(getActivity().getString(R.string.status))
                .items((CharSequence[]) getActivity().getResources().getStringArray(R.array.firestatus))
                .cancelable(false)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mViewModel.status.set((String) text);
                    }
                })
                .build()
                .show();

    }

    /**
     * 选择地址
     */
    @Override
    public void showSelectAddress() {
        replaceFragment(mViewModel.titanloc.get());

    }

    @Override
    public void finish() {getActivity().finish();
    }

    /**
     * 界面跳转
     * @param location
     */
    private void replaceFragment(TitanLocation location) {
        FragmentManager manager =getFragmentManager();
        SelectAddressFragment selectAddressFragment =  SelectAddressFragment.newInstance(location);
        SelectAddressViewModel viewModel= new SelectAddressViewModel(getActivity(),selectAddressFragment);
        selectAddressFragment.setViewModel(viewModel);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, selectAddressFragment);
        transaction.addToBackStack(null);
        //设置过度动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void showIsfireDialog() {

    }

    @Override
    public void openAlarmInfoDetails(String id) {

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String time = getDateToString(millseconds);
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
     * 格式化时间
     * @param time
     * @return
     */
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);

    }
}
