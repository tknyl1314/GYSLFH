package com.titan.gyslfh.monitor;

import android.content.Context;

import com.hikvision.netsdk.RealPlayCallBack;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;

import org.MediaPlayer.PlayM4.Player;

/**
 * Created by whs on 2017/6/1
 * 视频监控
 */

public class MonitorViewModel extends BaseViewModel {
    private MonitorInterface mViewInterface;

    private  HikVisionUtil mHikVersionUtil;

    public MonitorViewModel(Context context, MonitorInterface monitorInterface, DataRepository dataRepository) {
        super();
        this.mContext=context;
        mDataRepository=dataRepository;
        mViewInterface=monitorInterface;
        mHikVersionUtil=new HikVisionUtil();


    }
    public void getDvrInfo(){

    }

    public void start() {

        boolean islogin= HikVisionUtil.loginDVR("222.85.147.92",8000,"admin","sfb12345");// 登录设备
        if(islogin){
            snackbarText.set("登陆成功");
            mHikVersionUtil.previewSingle(8, new RealPlayCallBack() {
                @Override
                public void fRealDataCallBack(int iRealHandle, int iDataType,
                                              byte[] pDataBuffer, int iDataSize) {
                  /*  MonitorActivity.this.processRealData(1, iDataType, pDataBuffer,
                            iDataSize, Player.STREAM_REALTIME);*/
                    mViewInterface.processRealData(1,iDataType,pDataBuffer,iDataSize,Player.STREAM_REALTIME);
                }
            });
        }

    }




}
