package com.titan.gyslfh.main;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import java.text.DecimalFormat;

/**
 * Created by Whs on 2016/12/1 0001
 */
public class MainViewModel extends BaseObservable  {
    //用户等级
    public final ObservableField<Integer> userlevel = new ObservableField<>();
    private Context mContext;
    private IMain mMain;

    /**经纬度格式化*/
    DecimalFormat locformat=new DecimalFormat(".000000");
    public MainViewModel(Context context, IMain mlogin) {
        this.mContext = context;
        this.mMain=mlogin;
    }
    /**
     * 一键报警
     */
    public void onAlarm() {
        mMain.onAlarm();

    }

    /**
     * 接警信息
     */
    public void onAlarmInfo()
    {
        mMain.onAlarmInfo();
    }

    /**
     * 上传轨迹点
     */
    /*private String uplaodTrackPoint(Point point) {
        String  uptime= DateUtil.dateFormat(new Date());
        //2363 :Xian_1980_3_Degree_GK_Zone_39
        if(point!=null){
            String lon = locformat.format(point.getX());
            String lat = locformat.format(point.getY());
            if (TitanApplication.IntetnetISVisible)
            {
                try {
                    // 上传轨迹到服务器
                    //boolean isuploc=webService.UPLonLat(MyApplication.SBH, lon,lat, uptime);
                    // 上传轨迹到本地数据库
                   *//* boolean isup = DataBaseHelper.UploadLocalDatebase(MyApplication.SBH,
                            lon, lat, uptime,"1");*//*
                    if(true){
                        return upPtSuccess;
                    }else {
                        return upPtError;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return  upPtError;
                }
            } else
            {
                try {
                    //boolean isadd=DataBaseHelper.UploadLocalDatebase(MyApplication.SBH, lon, lat, uptime,"0");
                    if(true){
                        return upPtSuccess;
                    }else {
                        return upPtError;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return upPtError;
                }

            }
        }else {
            return "获取当前轨迹点失败";
        }


    }*/




}
