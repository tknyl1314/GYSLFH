package com.titan.gis.selectaddress;

import android.content.Context;
import android.databinding.ObservableField;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.titan.BaseViewModel;
import com.titan.gis.PositionUtil;
import com.titan.model.TitanLocation;

/**
 * Created by whs on 2017/6/1
 * 选择地址
 */

public class SelectAddressViewModel extends BaseViewModel implements BaiduMap.OnMapStatusChangeListener {

    private SelectAddressInterface mViewInterface;

    public ObservableField<TitanLocation> titanloc = new ObservableField<>();
    //百度POI查询
    private PoiSearch mPoiSearch;
    //搜索半径
    private int radius = 100;
    //搜索分页编号
    private int loadIndex = 0;


    public SelectAddressViewModel(Context context, SelectAddressInterface mViewInterface) {
        this.mViewInterface = mViewInterface;
        this.mContext = context;

    }


    /**
     * 监听地图状态变化事件
     *
     * @param mapStatus
     */
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(final MapStatus mapStatus) {

        bdGeoCoder(mapStatus.target.longitude, mapStatus.target.latitude);
        /*mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    //详情检索失败
                    // result.error请参考SearchResult.ERRORNO
                    snackbarText.set("获取位置失败");
                    return;
                }
                String address=poiResult.getAllAddr().get(0).toString();
                TitanLocation loc=PositionUtil.bd09_To_Gcj02(mapStatus.target.longitude,mapStatus.target.latitude);
                titanloc.get().setAddress(address);

                *//*titanloc.get().setLat(mapStatus.target.latitude);
                titanloc.get().setLon(mapStatus.target.longitude);*//*

                //检索成功
                snackbarText.set("当前位于:"+poiResult.getAllAddr().get(0)+"附近");

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        PoiNearbySearchOption poiNearbySearchOption=new PoiNearbySearchOption();
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().sortType(PoiSortType.distance_from_near_to_far).location(mapStatus.target)
                .radius(radius).pageNum(loadIndex).keyword(null);
        poiNearbySearchOption.location(mapStatus.target);
        mPoiSearch.searchNearby(nearbySearchOption);*/
        //mViewInterface.mapChange(mapStatus);

    }

    /**
     * 地理编码
     *
     * @param lon
     * @param lat
     */
    public void bdGeoCoder(final double lon, final double lat) {
        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();
        //
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    snackbarText.set("获取位置信息失败");
                }
                TitanLocation loc = PositionUtil.bd09_To_Gcj02(lon, lat);
                titanloc.set(loc);
                if (result != null) {
                    titanloc.get().setAddress(result.getAddress());
                    snackbarText.set("当前位于" + result.getAddress() + "附近");
                }


            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);

        LatLng latLng = new LatLng(lat, lon);
        //
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        // 释放地理编码检索实例
        //geoCoder.destroy();
    }
}