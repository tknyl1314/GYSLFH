package com.titan.data.source.remote;

import com.titan.model.FireRiskModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by whs on 2017/5/24
 */

public interface MeteorologyService {
    //获取气象数据
    @GET("/WeatherDataService/Service/Thematic")
    Observable<FireRiskModel> getMeteorology(@Query("Date") String date, @Query("DataType") String datatype, @Query("Hour") String huor, @Query("ThematicType") String type);
}
