package com.titan.gyslfh.main;

import com.esri.arcgisruntime.geometry.Point;

import java.util.List;

/**
 * Created by whs on 2017/4/23
 */

public interface IMain {


     /**
      * 1:火警上报
      * 2:接警录入
      */
     void onAlarm(int type);

     /**
      * 接警管理
      */
     void onAlarmInfo();

     /**
      * 回警
      */
     void onBackAlarm();

     /**
      * 定位
      * @param currentPoint
      */
     void onLocation(Point currentPoint);

     void  showToast(String info, int type);

     void showTrackLine(List<Point> pointList);

     /**
      * 标绘
      */
     void Plot(boolean isplot);

     /**
      * 开启导航
      */
     void  startNavigation(boolean isnav);

     /**
      * 测试方法
      */
     void test();


    //void removeTrackLine();
}
