package com.otitan.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by whs on 2017/2/17
 * Retrofit 接口
 */

public interface RetrofitService {
    /*@Headers({
            "Content-Type: text/xml; charset=utf-8",
            "SOAPAction: http://tempuri.org/GetNewsList"
    })
    @POST("AppWebService.asmx")
    Observable<Boolean> UserLogin(@Query("date") String date, @Query("action") String action);*/
    //登陆验证
    @GET("/FireService/WebService1.asmx/CheckLogin")
    Observable<String> checkLogin(@Query("username") String username,@Query("password")String password);
    //GET /AppWebService.asmx/GetNewsList?date=string&action=string HTTP/1.1
    @GET("/AppWebService.asmx/GetNewsList")
    Observable<String> getNews(@Query("date") String date, @Query("action") String action);
    //获取新闻内容
    @GET("/AppWebService.asmx/GetNewsInfo")
    Observable<String> getNewsInfo(@Query("id") String id);
    //获取初始化新闻
    @GET("/AppWebService.asmx/InitNews")
    Observable<String> getInitNews(@Query("count") String count);
    //获取新闻图片
    @GET("/{url}")
    Observable<ResponseBody> getImg(@Path("url") String url);
    //初始化法律信息
    @GET("/AppWebService.asmx/InitLaws")
    Observable<String> getinitlaw(@Query("count") String count);
    //获取法律信息列表
    @GET("/AppWebService.asmx/GetLawsList")
    Observable<String> getLaws(@Query("date") String date, @Query("action") String action);
    //获取法律内容
    @GET("/AppWebService.asmx/GetLawsInfo")
    Observable<String> getLawsInfo(@Query("id") String id);
}
