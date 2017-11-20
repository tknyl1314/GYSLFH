package com.titan.gyslfh.videoroom;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.titan.BaseViewModel;


/**
 * Created by Whs on 2016/12/1
 * 视频会议
 */
public class VideoChartViewModel extends BaseViewModel{

    //开启麦克风
    public ObservableBoolean isAudioEnable=new ObservableBoolean();
    //roomid
    public ObservableField<String> roomid=new ObservableField<>("");

    private VideoChart mVideoChart;



    public VideoChartViewModel(Context context, VideoChart videoChart) {
        this.mContext = context;
        this.mVideoChart=videoChart;
    }

    public void switchAudio(){
        isAudioEnable.set(!isAudioEnable.get());
        mVideoChart.switchAudio(isAudioEnable.get());
        if(isAudioEnable.get()){
            snackbarText.set("麦克风已开启");
        }else {
            snackbarText.set("麦克风已关闭");
        }
    }

    public void leaveRoom(){
        mVideoChart.leaveRoom();
    }

    public void switchCamera(){
        mVideoChart.switchCamera();
    }




}
