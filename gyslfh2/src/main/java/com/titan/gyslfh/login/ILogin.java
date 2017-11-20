package com.titan.gyslfh.login;

/**
 * Created by whs on 2017/4/23
 * 登陆界面接口
 */

public interface ILogin {
     /**
      * 跳转
      */
     void  onNext();
     void  showProgress();
     void  stopProgress();
     void  showToast(String info,int type);

}
