package com.titan.data.source.remote;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by whs on 2017/2/17
 * Retrofit 接口
 */

public interface RetrofitService {
    //登陆
    @GET("/FireServices.asmx/Login")
    Observable<String> Checklogin(@Query("userName") String username, @Query("passWord") String password);
    //火警录入
    @GET("/FireServices.asmx/alarmEntring")
    Observable<String> uploadFireInfo(@Query("date") String date, @Query("action") String action);
    //火警上报
    @GET("/FireServices.asmx/alarmUpLoad")
    Observable<String> uploadFireAlarm(@Query("date") String date, @Query("action") String action);
    //获取地区信息
    @GET("/FireServices.asmx/getAreaInfo")
    Observable<String> getAreaInfo(@Query("date") String date, @Query("action") String action);
    //获取火警信息
    @GET("/FireServices.asmx/getAlarmInfo")
    Observable<String> getAlarmInfo(@Query("date") String date, @Query("action") String action);
}
