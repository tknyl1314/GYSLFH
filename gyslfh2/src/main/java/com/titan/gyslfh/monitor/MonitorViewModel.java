package com.titan.gyslfh.monitor;

import android.content.Context;
import android.databinding.ObservableField;

import com.google.gson.Gson;
import com.hikvision.netsdk.RealPlayCallBack;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.remote.RemotDataSource;

import org.MediaPlayer.PlayM4.Player;

/**
 * Created by whs on 2017/6/1
 * 视频监控
 */

public class MonitorViewModel extends BaseViewModel {


    //可见光配置
    public ObservableField<DvrInfoModel> MonitorConfig=new ObservableField<>();

    //红外配置
    public ObservableField<DvrInfoModel> InfraredConfig=new ObservableField<>();
    //防盗配置
    public ObservableField<DvrInfoModel> AntitheftConfig=new ObservableField<>();
    //预览镜头
    private int mindex=1;

    private MonitorInterface mViewInterface;

    public HikVisionUtil getmHikVersionUtil() {
        return mHikVersionUtil;
    }

    public void setmHikVersionUtil(HikVisionUtil mHikVersionUtil) {
        this.mHikVersionUtil = mHikVersionUtil;
    }

    private  HikVisionUtil mHikVersionUtil;

    private String mmonitorinfo;

    public MonitorViewModel(Context context, MonitorInterface monitorInterface, DataRepository dataRepository,String monitorinfo) {
        super();
        this.mContext=context;
        mDataRepository=dataRepository;
        mViewInterface=monitorInterface;
        mmonitorinfo=monitorinfo;
        //mHikVersionUtil=new HikVisionUtil();


    }
    public void getDvrInfo(){
       mDataRepository.getDvrInfo(mmonitorinfo, new RemotDataSource.getCallback() {
            @Override
            public void onFailure(String info) {
                snackbarText.set(info);

            }

            @Override
            public void onSuccess(String data) {
                try {

                    MonitorModel1 monitorModel1 = new Gson().fromJson(data,MonitorModel1.class);
                    if(monitorModel1.getMonitorConfig()!=null&&monitorModel1.getMonitorConfig().size()>0){
                        MonitorConfig.set(monitorModel1.getMonitorConfig().get(0));
                    }
                    if(monitorModel1.getInfraredConfig()!=null&& monitorModel1.getInfraredConfig().size()>0){
                        InfraredConfig.set(monitorModel1.getInfraredConfig().get(0));

                    }
                    if(monitorModel1.getAntitheftConfig()!=null&& monitorModel1.getAntitheftConfig().size()>0){
                        AntitheftConfig.set(monitorModel1.getAntitheftConfig().get(0));
                    }

                    //判断设备状态 0:不在线 1:在线
                    if(MonitorConfig==null){
                        snackbarText.set("当前设备不在线");
                        //onPreview(MonitorConfig.get());

                    }else {
                       onPreview(MonitorConfig.get());
                    }

                } catch (Exception e) {
                    snackbarText.set("硬盘录像机数据解析异常"+e);
                }

            }
        });

    }



    public void start() {
        getDvrInfo();

    }

    /**
     * 预览
     */
    public void onPreview(DvrInfoModel dvrInfoModel){
        //硬盘录像IP
        final String dvrip=dvrInfoModel.getEXTRANET().split(":")[0];
        //端口
        final int dvrport= Integer.parseInt(dvrInfoModel.getMOBILEPORT());
        //用户名
        final String username=dvrInfoModel.getUSERNAME();
        //密码
        final String psw=dvrInfoModel.getPASSWORD();
        //通道
        final String td=dvrInfoModel.getTD();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean islogin= mHikVersionUtil.loginDVR(dvrip,dvrport,username,psw);// 登录设备
                if(islogin){
                    snackbarText.set("登陆成功");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHikVersionUtil.previewSingle(Integer.parseInt(td), new RealPlayCallBack() {
                                @Override
                                public void fRealDataCallBack(int iRealHandle, int iDataType,
                                                              byte[] pDataBuffer, int iDataSize) {
                        /*  MonitorActivity.this.processRealData(1, iDataType, pDataBuffer,
                            iDataSize, Player.STREAM_REALTIME);*/
                                    mViewInterface.processRealData(1,iDataType,pDataBuffer,iDataSize,Player.STREAM_REALTIME);
                                }
                            });
                        }
                    }).start();
                }
                else {
                    snackbarText.set("登陆失败");

                }
            }
        }).start();


    }

    /**
     *
     * @param index
     */
    public void startPreview(int index){
        if(index==mindex){
            return;
        }else {
            mindex=index;
            try {
                switch (mindex){
                    case 1:
                        if(MonitorConfig.get()!=null){
                            onPreview(MonitorConfig.get());
                        }else {
                            snackbarText.set("当前设备不在线");
                        }
                    case 2:
                        if(InfraredConfig.get()!=null){
                            onPreview(InfraredConfig.get());
                        }else {
                            snackbarText.set("当前设备不在线");
                        }
                    case 3:
                        if(AntitheftConfig.get()!=null){
                            onPreview(AntitheftConfig.get());
                        }else {
                            snackbarText.set("当前设备不在线");
                        }
                        break;
                }

            } catch (Exception e) {
                snackbarText.set("预览异常"+e.toString());
            }


        }
    }






}
