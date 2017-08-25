package com.titan.gyslfh.main;

import com.esri.arcgisruntime.geometry.Point;

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

     void showTrackLine(Point point);

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

     /**
      * 三维场景
      */
     void openScene();

     /**
      * 图层控制
      */
     void showLayerControl();

     /**
      * 轨迹跟踪
      */
     void initLocationListener();

     /**
      * 关闭轨迹
      */
     void closeTrackLine();

    //void removeTrackLine();
}
