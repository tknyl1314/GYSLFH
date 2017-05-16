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
    @GET("/FireServices.asmx/getAlarmRecords")
    Observable<String> getAlarmInfo(@Query("strWhere") String strWhere, @Query("dqid") String dqid,@Query("pageIndex") String pagenum,@Query("pageCount") String count);
    //获取未回警信息
    @GET("/FireServices.asmx/getUnDealAlaramRecords")
    Observable<String> getUnDealAlarmInfo(@Query("strWhere") String strWhere, @Query("dqid") String dqid,@Query("pageIndex") String pagenum,@Query("pageCount") String count);
    @GET("/FireServices.asmx/upLoadTrackPoint")
    Observable<String> upLoadTrackPoint(@Query("pt") String point);
    /*接警信息录入*/
    @GET("/FireServices.asmx/AddReceiptAlarm")
    Observable<String> sendInputAlarmInfo(@Query("jsonText") String json);
}
