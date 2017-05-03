package com.titan.gyslfh.main;

/**
 * Created by whs on 2017/4/23
 */

public interface IMain {

     /**
      * 跳转
      */
     void  onNext();

     void onAlarm();

     void onAlarmInfo();

     void  showToast(String info, int type);
}
