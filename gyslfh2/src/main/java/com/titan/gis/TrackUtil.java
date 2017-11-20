package com.titan.gis;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.esri.arcgisruntime.geometry.Point;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Whs on 2016/12/12 0012
 */
public class TrackUtil {

    public final static String CoorType_GCJ02 = "gcj02";
    public final static String CoorType_BD09LL= "bd09ll";
    public final static String CoorType_BD09MC= "bd09";
    /***
     *61 ： GPS定位结果，GPS定位成功。
     *62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
     *63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
     *65 ： 定位缓存的结果。
     *66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
     *67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
     *68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
     *161： 网络定位结果，网络定位定位成功。
     *162： 请求串密文解析失败。
     *167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
     *502： key参数错误，请按照说明文档重新申请KEY。
     *505： key不存在或者非法，请按照说明文档重新申请KEY。
     *601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
     *602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
     *501～700：key验证失败，请按照说明文档重新申请KEY。
     */
    // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
    private static LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>();
    private static LinkedList<ArcgisLocationEntity> arcgisLocationList = new LinkedList<ArcgisLocationEntity>();

    public static float[] EARTH_WEIGHT = {0.1f,0.2f,0.4f,0.6f,0.8f}; // 推算计算权重_地球
    //public static float[] MOON_WEIGHT = {0.0167f,0.033f,0.067f,0.1f,0.133f};
    //public static float[] MARS_WEIGHT = {0.034f,0.068f,0.152f,0.228f,0.304f};

    public TrackUtil() {

    }

    /***
     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
     * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
     *
     * @return Bundle
     */
    public static Bundle Algorithm(BDLocation location) {
        Bundle locData = new Bundle();
        double curSpeed = 0;
        if (locationList.isEmpty() || locationList.size() < 2) {
            LocationEntity temp = new LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            locData.putInt("iscalculate", 0);
            locationList.add(temp);
        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
                score += curSpeed * EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
                                / 2);
                locData.putInt("iscalculate", 1);
            } else {
                locData.putInt("iscalculate", 0);
            }
            LocationEntity newLocation = new LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            locationList.add(newLocation);
        }
        return locData;
    }

    /**
     * @param point 定位点
     * @return
     */
    public static Bundle locationSmoothAlgorithm(Point point) {
        Bundle locData = new Bundle();
        double curSpeed = 0;
        if (arcgisLocationList.isEmpty() || arcgisLocationList.size() < 2) {
            ArcgisLocationEntity temp = new ArcgisLocationEntity();
            temp.point = point;
            temp.time = System.currentTimeMillis();
            locData.putInt("iscalculate", 0);
            arcgisLocationList.add(temp);
        } else {
            if (arcgisLocationList.size() > 5)
                arcgisLocationList.removeFirst();
            double score = 0;
            for (int i = 0; i < arcgisLocationList.size(); ++i) {
                LatLng lastPoint = new LatLng(arcgisLocationList.get(i).point.getX(),
                        arcgisLocationList.get(i).point.getY());
                LatLng curPoint = new LatLng(point.getX(), point.getY());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - arcgisLocationList.get(i).time) / 1000;
                score += curSpeed * EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                point=new Point((arcgisLocationList.get(arcgisLocationList.size() - 1).point.getX() + point.getX())/ 2,
                                (arcgisLocationList.get(arcgisLocationList.size() - 1).point.getY() + point.getY())/ 2);
                locData.putInt("iscalculate", 1);//计算结果
            } else {
                locData.putInt("iscalculate", 0);//非计算结果
            }
            ArcgisLocationEntity newLocation = new ArcgisLocationEntity();
            newLocation.point = point;
            newLocation.time = System.currentTimeMillis();
            arcgisLocationList.add(newLocation);
            locData.putSerializable("location",newLocation);
        }
        return locData;
    }

    /**
     * 封装定位结果和时间的实体类
     *
     * @author baidu
     *
     */
    public  static class LocationEntity {
       public BDLocation location;
        public long time;
    }

    /**
     * 封装Arcgis定位结果和时间的实体类
     */
    public  static class ArcgisLocationEntity implements Serializable {
        public Point point;
        public long time;
    }
}
