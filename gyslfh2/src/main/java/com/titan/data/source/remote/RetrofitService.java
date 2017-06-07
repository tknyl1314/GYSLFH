package com.titan.data.source.remote;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by whs on 2017/2/17
 * Retrofit 接口
 */

public interface RetrofitService {
    //登陆
    @GET("/FireServices.asmx/Login")
    Observable<String> Checklogin(@Query("userName") String username, @Query("passWord") String password,@Query("ClientID") String clientid);

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
    //上传轨迹点
    @GET("/FireServices.asmx/AddMobileRealtime")
    Observable<String> upLoadTrackPoint(@Query("jsonText") String point);
    /*火情上报、接警信息录入*/
    @Headers("Cache-Control: application/json;charset=utf-8")
    @FormUrlEncoded
    @POST("/FireServices.asmx/AddReceiptAlarm")
    Observable<String> uploadAlarmInfo(@Field("jsonText") String json);
    /*火情待回警数*/
    @GET("/FireServices.asmx/getUndealAlarm")
    Observable<String> getUnDealAlarmCount(@Query("dqid") String dqid);

    /*获取火警详细信息*/
    @GET("/FireServices.asmx/getUnReturnAlarmRecords")
    Observable<String> getAlarmInfoDetail(@Query("receiptid") String alarmid);

    /*获取火警详细信息*/
    @GET("/FireServices.asmx/backAlarm")
    Observable<String> onBackAlarm(@Query("jsonText") String backalarm);



}
